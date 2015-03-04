package br.uern.ges.med.sim2ped.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.Care;
import br.uern.ges.med.sim2ped.beans.HistoryResponse;
import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.model.CaresModel;
import br.uern.ges.med.sim2ped.model.HistoryResponsesModel;
import br.uern.ges.med.sim2ped.model.RoutineModel;
import br.uern.ges.med.sim2ped.model.TipsModel;
import br.uern.ges.med.sim2ped.utils.Constant;

public class AlertCareFragments extends Fragment{

    private long careId = 0, routineId = 0;
    private HistoryResponse historyResponse = new HistoryResponse();
    HistoryResponsesModel historyResponsesModel;

    @SuppressWarnings("deprecation")
    @Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View rootView;
        setHasOptionsMenu(true);

        Context context = container.getContext();

		rootView = inflater.inflate(R.layout.layout_alert_care, container, false);

        Bundle bundle = getArguments();

		try {
            careId = Long.parseLong(bundle.getString(Constant.BUNDLE_CARE_ID));
            routineId = Long.parseLong(bundle.getString(Constant.BUNDLE_ROUTINE_ID));

			Log.i(Constant.TAG_LOG, "CARE ID CLICKED IN NOTIFICATION: " + careId);
		} catch (Exception ignored){ }

        CaresModel caresModel = new CaresModel(context);
        TipsModel tipsModel = new TipsModel(context);
        RoutineModel routineModel = new RoutineModel(context);
        historyResponsesModel = new HistoryResponsesModel(context);

        Care care = caresModel.get(careId);
        Tip tip = tipsModel.getBasedInCare(careId);
        Routine routine = routineModel.getRoutine(routineId);
        Calendar calendar = Calendar.getInstance();

        historyResponse = new HistoryResponse();

        historyResponse.setCareId(careId);
        historyResponse.setRoutineId(routineId);
        historyResponse.setContextId(routine.getContextId());
        //historyResponse.setResponseHour(calendar.getTimeInMillis());
        //historyResponse.setResponse("");

        /*
        long idHistory = historyResponsesModel.insert(historyResponse);

        if(idHistory > 0)
            historyResponse.setId(idHistory);
        else
            historyResponse.setId(0);
            */

        TextView textCareAlertCareDetails = (TextView)
                rootView.findViewById(R.id.textCareAlertCareDetails);
        TextView textTipInAlertCaresDetail = (TextView)
                rootView.findViewById(R.id.textTipInAlertCaresDetail);
        LinearLayout layoutTextTipInAlertCaresDetails = (LinearLayout)
                rootView.findViewById(R.id.layoutTextTipInAlertCaresDetails);

        textCareAlertCareDetails.setText(care.getTitleCare());
        textTipInAlertCaresDetail.setText(tip.getText());

        int background = context.getResources().getIdentifier("bg_tips_" + tip.getColor(),
                "drawable", context.getPackageName());

        if (Build.VERSION.SDK_INT >= 16) {
            layoutTextTipInAlertCaresDetails.setBackground(context.getResources().getDrawable(background));
        } else {
            layoutTextTipInAlertCaresDetails.setBackgroundDrawable(context.getResources().getDrawable(background));
        }

		// Change ActionBar
		final MainActivity activity = (MainActivity) getActivity();
		final ActionBar actionBar = activity.getSupportActionBar();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View customActionBar = getLayoutInflater(savedInstanceState).inflate(R.layout.actionbar_yes_no,
				new LinearLayout(container.getContext()), true);

		actionBar.setCustomView(customActionBar, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		View yesActionView = customActionBar.findViewById(R.id.action_yes);
        View noActionView = customActionBar.findViewById(R.id.action_no);

		yesActionView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Toast.makeText(container.getContext(),
                        container.getContext().getString(R.string.msg_care_cogratulations),
                        Toast.LENGTH_LONG).show();

                Vibrator v2 = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

                // Start without a delay
                // Vibrate for 100 milliseconds
                // Sleep for 1000 milliseconds
                long[] pattern = {10, 100, 100, 50, 100, 25};

                // The '0' here means to repeat indefinitely
                // '-1' would play the vibration once
                v2.vibrate(pattern, -1);

				// To change body of implemented methods use File | Settings | File Templates.
				actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
                actionBar.setDisplayHomeAsUpEnabled(true);

                Calendar calendar = Calendar.getInstance();

                historyResponse.setResponseHour(calendar.getTimeInMillis());
                historyResponse.setResponse("Y");

                historyResponsesModel.insert(historyResponse);
			}
		});

        noActionView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(),
                        container.getContext().getString(R.string.msg_care_sad),
                        Toast.LENGTH_LONG).show();

                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

                // Start without a delay
                // Vibrate for 100 milliseconds
                // Sleep for 1000 milliseconds
                long[] pattern = {10, 100, 500, 200, 500, 300};

                // The '0' here means to repeat indefinitely
                // '-1' would play the vibration once
                v.vibrate(pattern, -1);

                // To change body of implemented methods use File | Settings | File Templates.
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);

                actionBar.setDisplayHomeAsUpEnabled(true);
                Calendar calendar = Calendar.getInstance();
                historyResponse.setResponseHour(calendar.getTimeInMillis());
                historyResponse.setResponse("N");

                historyResponsesModel.insert(historyResponse);
            }
        });

		return rootView;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // Creating a fragment object
                Fragment fragment = new ProceduresFragments();

                // Getting reference to the FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.frame_container, fragment);

                // Transition of fragments
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                MainActivity activity = (MainActivity) getActivity();
                activity.getMDrawerToggle().setDrawerIndicatorEnabled(true);
               //ft.addToBackStack(null);

                // Committing the transaction
                ft.commit();


                // called when the up affordance/carat in actionbar is pressed
               // getActivity().onBackPressed();
                //return true;
            break;
        }

        return super.onOptionsItemSelected(item);
    }
}