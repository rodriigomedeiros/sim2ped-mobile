package br.uern.ges.med.sim2ped.managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.restclient.MEDRestClient;
import br.uern.ges.med.sim2ped.utils.Constant;


/**
 * Created by Rodrigo on 25/09/2014 (16:30).
 * <p/>
 * Package: br.uern.ges.med.sim2ped.managers
 */
public class LoginTask extends AsyncTask<String, String, Void> {

    private final LoginTaskListener loginTaskListener;
    private final Context context;
    private String user = "", password = "", loginResult = "", gcmToken = "";

    public LoginTask(LoginTaskListener loginTaskListener, Context context){
        this.context = context;
        this.loginTaskListener = loginTaskListener;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setGCMRegister(String token){
        this.gcmToken = token;
    }

    @Override
    protected void onPreExecute(){
        loginTaskListener.onLoginStarted();
    }

    protected void onProgressUpdate(String... text){
        loginTaskListener.progressLoginUpdate(text[0]);
        Log.i(Constant.TAG_LOG, "LoginTask is: " + text[0] + ".");
    }

    @Override
    protected Void doInBackground(String... strings) {
        publishProgress(context.getString(R.string.msg_verify_credentials_login));

        RequestParams params = new RequestParams();
        params.put(Constant.PARAM_WS_VERIFY_LOGIN_USER_CODE, user);
        params.put(Constant.PARAM_WS_VERIFY_LOGIN_USER_PASSWORD, password);
        params.put(Constant.PARAM_WS_GCM_TOKEN, gcmToken);

        MEDRestClient.post(context, Constant.WEBSERVICE_RELATIVE_URL_LOGIN, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d(Constant.TAG_LOG, "onSuccess");

                try {
                    if(response.getBoolean(Constant.JSON_MESSAGE_VALID_LOGIN)){
                        loginResult = context.getString(R.string.msg_login_successful);
                        loginTaskListener.validCredentials(true);
                    } else {
                        loginResult = context.getString(R.string.msg_login_failed);
                        loginTaskListener.validCredentials(false);
                    }
                } catch (JSONException ignored) { }

            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                Log.d(Constant.TAG_LOG, "onFailure");
                Log.e(Constant.TAG_LOG, "RESPONSE: " + errorResponse.toString());
                throwable.printStackTrace();

                publishProgress(context.getString(R.string.error_unexpected_error));
                Log.d(Constant.TAG_LOG, context.getString(R.string.error_unexpected_error));

                loginTaskListener.validCredentials(false);
            }


            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                Log.d(Constant.TAG_LOG, "onFailure");
                Log.e(Constant.TAG_LOG, "RESPONSE: " + errorResponse.toString());
                throwable.printStackTrace();

                publishProgress(context.getString(R.string.error_unexpected_error));
                Log.d(Constant.TAG_LOG, context.getString(R.string.error_unexpected_error));

                loginTaskListener.validCredentials(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    publishProgress(context.getString(R.string.error_404));
                    Log.d(Constant.TAG_LOG, context.getString(R.string.error_404));
                } else if (statusCode == 500) {
                    publishProgress(context.getString(R.string.error_500));
                    Log.d(Constant.TAG_LOG, context.getString(R.string.error_500));
                } else {
                    publishProgress(context.getString(R.string.error_unexpected_error));
                    Log.d(Constant.TAG_LOG, context.getString(R.string.error_unexpected_error));
                    throwable.printStackTrace();
                }

                Log.e(Constant.TAG_LOG, "RESPONSE: " + responseString);
                loginTaskListener.validCredentials(false);
            }

            @Override
            public void onFinish() {
                Log.d(Constant.TAG_LOG, "onFinish");
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        loginTaskListener.onLoginFinished(loginResult);
    }

    public interface LoginTaskListener {
        public void validCredentials(boolean check);
        void onLoginStarted();
        void onLoginFinished(String result);
        void progressLoginUpdate(String progress);
    }
}
