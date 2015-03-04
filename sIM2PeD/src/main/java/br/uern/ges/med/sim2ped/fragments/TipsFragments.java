package br.uern.ges.med.sim2ped.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.adapter.TipsAdapter;
import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.model.TipsModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

@SuppressLint("NewApi")
public class TipsFragments extends Fragment {

    private OnTipSelectedListener mListener;
	
    public TipsFragments(){}
    
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView;
		
		Preferences preferences = new Preferences(container.getContext());

        String LAYOUT_TIPS = preferences.getPrefLayoutTips();
		
		if(LAYOUT_TIPS.equals("inline")) {
			rootView = inflater.inflate(R.layout.layout_tip_inline, container, false);
		} else {
			rootView = inflater.inflate(R.layout.layout_tip, container, false);
		}
		
		if(LAYOUT_TIPS.equals("grid2")){
			LAYOUT_TIPS = "inline";
		}
		
		int margin = (int) rootView.getResources().getDimension(R.dimen.margin_layout_tips);
		
		if(LAYOUT_TIPS.equals("inline"))
			margin += 1;
		
		//MainActivity.incrementHitCount(0, "R");
		
		StaggeredGridView gridView = (StaggeredGridView) rootView.findViewById(R.id.tipsStaggeredGridView);
		
		gridView.setItemMargin(margin); // set the GridView margin
				
		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well

		TipsModel tipM = new TipsModel(container.getContext());
        ArrayList<Tip> tips = tipM.
                getRandom(Integer.parseInt(preferences.getPrefNumberOfTipsDisplayed()));

		final TipsAdapter adapter = new TipsAdapter(rootView.getContext(), R.id.textTip,
                LAYOUT_TIPS, tips);
	
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
				mListener.onTipSelected(adapter.getItem(position).getID());

				//String item = adapter.getItem(position).toString();
			    //Toast.makeText(container.getContext(), "You have chose: "+ item, Toast.LENGTH_LONG).show();
			}
		});
		
		return rootView;
	}
	
	// Container Activity must implement this interface
    public interface OnTipSelectedListener {
        public void onTipSelected(long ID);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTipSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTipSelectedListener");
        }
    }
}
