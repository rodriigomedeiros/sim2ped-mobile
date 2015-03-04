package br.uern.ges.med.sim2ped.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.managers.SendDataTask;

/**
 * Created by Rodrigo on 03/09/2014 (16:22).
 *
 * Package: br.uern.ges.med.sim2ped.fragments
 */
public class SendStatisticsFragment extends Fragment implements
        SendDataTask.SendHistoryTaskListener, View.OnClickListener {

    private boolean isTaskRunning = false;
    private Context context;
    private TextView textViewProgress;
    private String progress = "";
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isTaskRunning) {
            progressBar.setVisibility(View.VISIBLE);
            textViewProgress.setText(progress);
        } else if(progress.length() > 0){
            textViewProgress.setText(progress);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView;
        rootView = inflater.inflate(R.layout.layout_synchronize, container, false);
        context = rootView.getContext();

        Button buttonSync = (Button) rootView.findViewById(R.id.btn_sync);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSync);

        textViewProgress = (TextView) rootView.findViewById(R.id.textViewTeste);
//        CheckBox checkHistory = (CheckBox) rootView.findViewById(R.id.checkHistory);

        buttonSync.setOnClickListener(this);
  /*      checkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckBox) view).setChecked(true);
                Toast.makeText(context, R.string.form_msg_error_impossible_uncheck_history, Toast.LENGTH_LONG).show();
            }
        }); */

        return rootView;
    }

    @Override
    public void onSendHistoryStarted() {
        isTaskRunning = true;
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSendHistoryFinished(String result) {
        if(isTaskRunning) {
            progressBar.setVisibility(View.GONE);
            textViewProgress.setText(result);
        }

        isTaskRunning = false;
    }

    @Override
    public void progressSendHistoryUpdate(String progress) {
        if(isTaskRunning) {
            this.progress = progress;
            textViewProgress.setText(progress);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if(isTaskRunning) {
            progressBar.setVisibility(View.GONE);
            textViewProgress.setText(this.progress);
        }
    }

    @Override
    public void onClick(View view) {
        if (!isTaskRunning) {
            MainActivity mainActivity = (MainActivity) getActivity();
            SendDataTask sendDataTask = new SendDataTask(mainActivity, this, context);
            sendDataTask.execute();
        }
    }
}
