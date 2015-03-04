package br.uern.ges.med.sim2ped.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 15/10/2014 (10:44).
 *
 *
 * Package: br.uern.ges.med.sim2ped.gcm
 */
public class GCMHelper {

    private static final String PROPERTY_REG_ID = "registration_id";

    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static boolean checkPlayServices(Activity activity, int requestCode) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, requestCode).show();

            } else {
                Toast.makeText(activity, activity.getText(R.string.msg_device_not_suported), Toast.LENGTH_LONG).show();
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public static String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.trim().length() == 0) {
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private static void storeRegistrationId(Context context, String regId) {

        final SharedPreferences prefs = getGCMPreferences(context);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static void registerInBackground(final Context ctx, final GoogleCloudMessaging gcm, final RegisterListener registerListener) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute(){
                registerListener.onStartRegisterGCM();
            }

            @Override
            protected String doInBackground(Void... params) {

                String regId = null;
                try {
                    regId = gcm.register(Constant.GCM_SENDER_ID);

                    storeRegistrationId(ctx, regId);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                return regId;
            }

            @Override
            protected void onPostExecute(String reg) {
                super.onPostExecute(reg);
                registerListener.onGCMRegisterComplete(reg);
            }

        }.execute();

    }

    private static SharedPreferences getGCMPreferences(Context context) {

        return context.getSharedPreferences("GDE", Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        }
        catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public interface RegisterListener {
        public void onGCMRegisterComplete(String regId);
        public void onStartRegisterGCM();
    }
}
