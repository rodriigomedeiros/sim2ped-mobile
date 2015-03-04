package br.uern.ges.med.sim2ped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import br.uern.ges.med.sim2ped.gcm.GCMHelper;
import br.uern.ges.med.sim2ped.managers.DownloaderDataTask;
import br.uern.ges.med.sim2ped.managers.LoginTask;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.ConnectionDetector;
import br.uern.ges.med.sim2ped.utils.Constant;
import br.uern.ges.med.sim2ped.utils.Utility;

import static br.uern.ges.med.sim2ped.managers.LoginTask.LoginTaskListener;

/**
 * Created by Rodrigo on 24/09/2014 (21:42).
 * <p/>
 * Package: br.uern.ges.med.sim2ped
 */
public class LoginActivity extends Activity implements GCMHelper.RegisterListener, LoginTaskListener, DownloaderDataTask.DownloaderListener, View.OnClickListener{

    private EditText editTextUser, editTextPassword;
    private CheckBox checkKeepLogged;
    private TextView textViewStatus;
    private boolean isTaskRunning = false;
    private String progress = "";
    private ProgressBar progressBar;
    private Preferences preferences;
    private Context context;
    private boolean validLogin;
    private static final int REQUEST_CODE_GOOGLEPLAY = 0;
    private String registrationGMCToken = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        context = getApplicationContext();
        preferences = new Preferences(context);

        if(preferences.getKeepLogged()){
            Intent intObj = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intObj);
            finish();
        }

        //Name Text control
        editTextUser = (EditText) findViewById(R.id.editText_user);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        checkKeepLogged = (CheckBox) findViewById(R.id.checkKeepLogged);
        textViewStatus = (TextView) findViewById(R.id.textView_status);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        if (GCMHelper.checkPlayServices(this, REQUEST_CODE_GOOGLEPLAY)) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            registrationGMCToken = GCMHelper.getRegistrationId(this);

            if (registrationGMCToken.trim().length() == 0) {
                GCMHelper.registerInBackground(this, gcm, this);
            }

        } else {
            Log.i(Constant.TAG_LOG, "Google Play no installed.");
        }

        //Button to trigger web service invocation
        Button b = (Button) findViewById(R.id.btn_login);
        //Button Click Listener
        b.setOnClickListener(this);

        checkKeepLogged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckBox) view).setChecked(true);
                Toast.makeText(context, context.getText(R.string.msg_is_required_keep_up_logged),
                        Toast.LENGTH_LONG).show();
            }
        });

        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isTaskRunning) {
            //progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
            progressBar.setVisibility(View.VISIBLE);
            textViewStatus.setText(progress);
        } else if(progress.length() > 0){
            textViewStatus.setText(progress);
        }
    }

    @Override
    public void onLoginStarted() {
        isTaskRunning = true;
        progressBar.setVisibility(View.VISIBLE);

        Log.i(Constant.TAG_LOG, "Login STARTED");
    }

    @Override
    public void onDownloaderStarted() {
        isTaskRunning = true;
        progressBar.setVisibility(View.VISIBLE);

        Log.i(Constant.TAG_LOG, "Downloader STARTED");
    }

    @Override
    public void onLoginFinished(String result) {
        if(isTaskRunning) {
            //progressDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            textViewStatus.setText(result);
        }
        isTaskRunning = false;

        if(validLogin) {
            long userCode = Long.parseLong(editTextUser.getText().toString());
            preferences.setUserCodeLogged(userCode); // set user code logged in app

            DownloaderDataTask downloaderDataTask = new DownloaderDataTask(userCode, this, context);
            downloaderDataTask.execute();
        }
    }

    @Override
    public void onDownloaderFinished(String result) {
        if(isTaskRunning) {
            //progressDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            textViewStatus.setText(result);
        }
        isTaskRunning = false;

        if(validLogin) {
            if (checkKeepLogged.isChecked())
                preferences.setKeepLogged(true);

            Intent intObj = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intObj);
            finish();
        }
    }

    @Override
    public void progressLoginUpdate(String progress) {
        if (isTaskRunning) {
            this.progress = progress;
            textViewStatus.setText(progress);
        }
    }

    @Override
    public void progressDownloaderUpdate(String progress) {
        if (isTaskRunning) {
            this.progress = progress;
            textViewStatus.setText(progress);
        }
    }

    @Override
    public void onClick(View view) {
        textViewStatus.setVisibility(View.VISIBLE);
        ConnectionDetector connectionDetector = new ConnectionDetector(this);

        if(connectionDetector.isConnectingToInternet()) {
            String user = editTextUser.getText().toString();
            String password = editTextPassword.getText().toString();

            // Check if of the device is registered in GCM
            if(Utility.isNotNull(registrationGMCToken)) {
                //Check if text controls are not empty
                if (Utility.isNotNull(user)) {
                    if (Utility.isNotNull(password)) {

                        LoginTask loginTask = new LoginTask(this, getApplicationContext());
                        loginTask.setUser(user);
                        loginTask.setPassword(password);
                        loginTask.setGCMRegister(registrationGMCToken);
                        loginTask.execute();
                    }
                    //If Password text control is empty
                    else {
                        textViewStatus.setText(context.
                                getText(R.string.form_msg_error_type_the_password));
                    }
                    //If Username text control is empty
                } else {
                    textViewStatus.setText(context.
                            getText(R.string.form_msg_error_type_the_code_user));
                }
            } else {
                textViewStatus.setText(context.getText(R.string.msg_device_note_registered_gcm));
            }
        } else {
            textViewStatus.setText(context.getText(R.string.error_no_connection_with_network));
        }
    }

    @Override
    public void validCredentials(boolean check) {
        validLogin = check;
    }

    @Override
    public void onGCMRegisterComplete(String regId) {
        registrationGMCToken = regId;
        Log.i(Constant.TAG_LOG, "GCM Register: "+registrationGMCToken);
    }

    @Override
    public void onStartRegisterGCM() {
        Log.i(Constant.TAG_LOG, "Start GCM Register");
    }
}