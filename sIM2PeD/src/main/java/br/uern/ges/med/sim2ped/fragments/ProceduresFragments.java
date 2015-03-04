package br.uern.ges.med.sim2ped.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.model.ContextModel;
import br.uern.ges.med.sim2ped.model.RoutineModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;

/**
 * Created by Rodrigo on 01/10/2014 (14:18).
 *
 * Package: br.uern.ges.med.sim2ped.fragments
 */
public class ProceduresFragments extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView;
        Context context = container.getContext();

        rootView = inflater.inflate(R.layout.layout_procedures, container, false);

        // update the actionbar to show the up carat/affordance
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        Preferences preferences = new Preferences(context);

        Button button = (Button) rootView.findViewById(R.id.btn_procedures_performeds);
        TextView context_name = (TextView) rootView.findViewById(R.id.text_item_list_procedure_contextName);
        TextView question_hour = (TextView) rootView.findViewById(R.id.text_item_list_procedure_hourQuestion);

        RoutineModel routineModel = new RoutineModel(context);
        ContextModel contextModel = new ContextModel(context);

        Routine routine = routineModel.getActualRoutine(preferences.getUserCodeLogged());
        br.uern.ges.med.sim2ped.beans.Context context1 = contextModel.getContext(routine.getContextId());

        context_name.setText(context1.getName());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(routine.getTime());

        String weekDay, minute, hour;
        weekDay = new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime());
        minute = new SimpleDateFormat("mm", Locale.getDefault()).format(cal.getTime());
        hour = new SimpleDateFormat("HH", Locale.getDefault()).format(cal.getTime());

        String h;
        h = context.getString(R.string.format_date_hour_next_procedure, weekDay, hour, minute);

        question_hour.setText(h);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating a fragment object
                Fragment fragment = new ProceduresPerformedsFragments();

                // Getting reference to the FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.frame_container, fragment);

                // Transition of fragments
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                MainActivity activity = (MainActivity) getActivity();
                activity.getMDrawerToggle().setDrawerIndicatorEnabled(false);
                ft.addToBackStack(null);

                // Committing the transaction
                ft.commit();
            }
        });

       return rootView;
    }
}
