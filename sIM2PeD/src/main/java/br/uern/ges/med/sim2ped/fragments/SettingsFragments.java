package br.uern.ges.med.sim2ped.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.compats.PreferenceCompatFragment;
import br.uern.ges.med.sim2ped.managers.WriteStatisticsFileTask;


public class SettingsFragments extends PreferenceCompatFragment implements
        WriteStatisticsFileTask.WriteStatisticsFileTaskListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private WriteStatisticsFileTask.WriteStatisticsFileTaskListener writeStatisticsFileTaskListener;
    private boolean isTaskRunning = false;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        writeStatisticsFileTaskListener = this;

        addPreferencesFromResource(R.xml.preferences);

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        context = getActivity().getWindow().getContext();

        Preference buttonLogout = findPreference("buttonLogout");
        buttonLogout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                Toast.makeText(context,
                        context.getText(R.string.msg_is_required_keep_up_logged), Toast.LENGTH_LONG).show();

                /*
                Preferences preferences = new Preferences(context);
                preferences.setKeepLogged(false);
                code for what you want it to do
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                */

                return true;
            }
        });

        Preference buttonBackup = findPreference("buttonBackup");
        buttonBackup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {

                if (!isTaskRunning) {
                    WriteStatisticsFileTask writeStatisticsFileTask =
                            new WriteStatisticsFileTask(writeStatisticsFileTaskListener, context);
                    writeStatisticsFileTask.execute();
                }

                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isTaskRunning) {
            progressDialog = ProgressDialog.show(
                    context,
                    context.getString(R.string.msg_generating_backup),
                    context.getString(R.string.msg_please_wait),
                    true);
            progressDialog.setCancelable(false);
        } 
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {   }

    @Override
    public void onWriteStarted() {
        isTaskRunning = true;
        progressDialog = ProgressDialog.show(
                context,
                context.getString(R.string.msg_generating_backup),
                context.getString(R.string.msg_please_wait),
                true);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onWriteFinished() {
        if(isTaskRunning) {
            progressDialog.dismiss();
        }

        isTaskRunning = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if(isTaskRunning) {
            progressDialog.dismiss();
        }
    }
}