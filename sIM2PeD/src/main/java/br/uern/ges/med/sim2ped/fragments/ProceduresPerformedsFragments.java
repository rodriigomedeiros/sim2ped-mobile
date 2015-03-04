package br.uern.ges.med.sim2ped.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.adapter.ProceduresAdapter;
import br.uern.ges.med.sim2ped.beans.HistoryResponse;
import br.uern.ges.med.sim2ped.beans.Procedure;
import br.uern.ges.med.sim2ped.model.CaresModel;
import br.uern.ges.med.sim2ped.model.ContextModel;
import br.uern.ges.med.sim2ped.model.HistoryResponsesModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.CalendarUtils;

/**
 * Created by Rodrigo on 01/10/2014 (14:18).
 *
 * Package: br.uern.ges.med.sim2ped.fragments
 */
public class ProceduresPerformedsFragments extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView;
        setHasOptionsMenu(true);

        Context context = container.getContext();

        // update the actionbar to show the up carat/affordance
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootView = inflater.inflate(R.layout.layout_procedures_performed, container, false);
        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.list_procedures);

        ArrayList<Procedure> procedures = new ArrayList<Procedure>();
        ArrayList<HistoryResponse> historyResponses;

        Preferences preferences = new Preferences(context);

        HistoryResponsesModel historyResponsesModel = new HistoryResponsesModel(context);

        historyResponses = historyResponsesModel.
                getHistoryResponsesAfter(preferences.getUserCodeLogged(),
                        CalendarUtils.getHours24HoursAfter().getTimeInMillis());

        if(historyResponses.size() > 0){
            listView.setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.alert_no_history).setVisibility(View.VISIBLE);
        }

        for(HistoryResponse historyResponse : historyResponses){
            Procedure procedure = new Procedure();
            ContextModel contextModel = new ContextModel(context);
            CaresModel caresModel = new CaresModel(context);
            Calendar cal = Calendar.getInstance();

            procedure.setContextName(contextModel.getContext(historyResponse.getContextId()).getName());
            procedure.setTitleCare(caresModel.get(historyResponse.getCareId()).getTitleCare());
            cal.setTimeInMillis(historyResponse.getResponseHour());

            String h;
            h = context.getString(R.string.format_date_hour_procedure,
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()),
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE));
            procedure.setHourResponse(h);

            String response;
            if(historyResponse.getResponse().equals("Y"))
                response = context.getString(R.string.format_yes_procedure);
            else
                response = context.getString(R.string.format_no_procedure);

            procedure.setResponse(response);

            procedures.add(procedure);
        }




        ProceduresAdapter adapter = new ProceduresAdapter(context, procedures);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // called when the up affordance/carat in actionbar is pressed
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
