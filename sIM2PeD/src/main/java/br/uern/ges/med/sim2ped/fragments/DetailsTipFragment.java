package br.uern.ges.med.sim2ped.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.adapter.ListCareAdapter;
import br.uern.ges.med.sim2ped.beans.Care;
import br.uern.ges.med.sim2ped.beans.StatisticalReadingTips;
import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.model.CaresModel;
import br.uern.ges.med.sim2ped.model.StatisticsModel;
import br.uern.ges.med.sim2ped.model.TipsModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.Constant;

@SuppressLint("NewApi")
public class DetailsTipFragment extends Fragment {

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView;
		setHasOptionsMenu(true);

        Context context = container.getContext();

        Bundle bundle = getArguments();

		// update the actionbar to show the up carat/affordance
		MainActivity activity = (MainActivity) getActivity();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		rootView = inflater.inflate(R.layout.layout_detail_tip, container, false);

		long index = Long.parseLong(bundle.getString(Constant.BUNDLE_TIP_ID));

        long statisticId = 0;
        try {
            statisticId = Long.parseLong(bundle.getString(Constant.BUNDLE_STATISTIC_ID));
        } catch (Exception ignored) {}

		Preferences preferences = new Preferences(context);

		TipsModel tipM = new TipsModel(context);
		Tip tip = tipM.get(index);

        TextView tipText = (TextView) rootView.findViewById(R.id.textTip);
        LinearLayout layoutTipDetailsText = (LinearLayout) rootView.findViewById(R.id.layoutTipDetailsText);

		tipText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.valueOf(preferences.getPrefFontSizeTips()) + 2);
		tipText.setText(tip.getText());

		int background = context.getResources().getIdentifier("bg_tips_details_"+tip.getColor(),
                "drawable", context.getPackageName());

		if (Build.VERSION.SDK_INT >= 16) {
			layoutTipDetailsText.setBackground(context.getResources().getDrawable(background));
		} else {
			layoutTipDetailsText.setBackgroundDrawable(context.getResources().getDrawable(background));
		}

		// Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.list_cares);
        RelativeLayout layoutTitleCare = (RelativeLayout) rootView.findViewById(R.id.layout_title_care);

        CaresModel careM = new CaresModel(container.getContext());
        ArrayList<Care> cares = new ArrayList<Care>();
        cares.add(careM.get(tip.getCareId()));

        if(cares.size() > 0){
	        ListCareAdapter adapter = new ListCareAdapter(context, cares);

	        listView.setAdapter(adapter);

	       	listView.setVisibility(View.VISIBLE);
	       	layoutTitleCare.setVisibility(View.VISIBLE);
        }

        if(statisticId > 0) {
            StatisticsModel statisticsModel = new StatisticsModel(context);
            StatisticalReadingTips statisticalReadingTips = new StatisticalReadingTips();

            statisticalReadingTips.setHourReading(Calendar.getInstance().getTimeInMillis());
            statisticalReadingTips.setStatisticalId(statisticId);
            statisticalReadingTips.setTipId(tip.getID());
            statisticalReadingTips.setUserId(preferences.getUserCodeLogged());

            statisticsModel.insertStaticalReadingTips(statisticalReadingTips);
        }
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
