package br.uern.ges.med.sim2ped.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.preferences.Preferences;

public class TipsAdapter extends ArrayAdapter<Tip> {
	
	private Context context;
	private int textView;
	private String layout;

	public TipsAdapter(Context context, int textViewResourceId, String layout, ArrayList<Tip> dica) {
		super(context, textViewResourceId, dica);
		this.textView = textViewResourceId;
		this.context = context;
		this.layout = layout;
	}

	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder; 
		Preferences preferences = new Preferences(getContext());
		
		if (convertView == null) { 
			LayoutInflater inflater = LayoutInflater.from(getContext());
			
			if(layout.equals("inline"))
				convertView = inflater.inflate(R.layout.layout_square_tips_inline, parent, false);
			else 
				convertView = inflater.inflate(R.layout.layout_square_tips, parent, false);
			
			holder = new ViewHolder();
			
			if(layout.equals("inline"))
				holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.layoutTip);
			else
				holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.layoutTip);
			
			holder.textView = (TextView) convertView.findViewById(textView);
			convertView.setTag(holder);
		} 
		
		holder = (ViewHolder) convertView.getTag();
		
		String bg_color, txt_color;
		
		if(layout.equals("inline")) {
			bg_color = "bg_tips_"+ getItem(position).getColor() + "_inline";
			txt_color = "text_tips";
			
		} else { 
			bg_color = "bg_tips_"+ getItem(position).getColor();
			txt_color = "text_tips_"+ getItem(position).getColor();
		}
		
		int background = context.getResources().getIdentifier(bg_color, "drawable", context.getPackageName());
		int txtColor = context.getResources().getIdentifier(txt_color, "color", context.getPackageName());
		
		holder.textView.setText(getItem(position).getText());
		holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.valueOf(preferences.getPrefFontSizeTips()));
		holder.textView.setTextColor(context.getResources().getColor(txtColor));
		
		holder.textView.setTextAppearance(context, R.style.TextTipInline);
		
		if (Build.VERSION.SDK_INT >= 16) {
			if(layout.equals("inline"))
				holder.textView.setBackground(context.getResources().getDrawable(background));
			else
				holder.frameLayout.setBackground(context.getResources().getDrawable(background));
		} else {
			if(layout.equals("inline"))
				holder.textView.setBackgroundDrawable(context.getResources().getDrawable(background));
			else
				holder.frameLayout.setBackgroundDrawable(context.getResources().getDrawable(background));
		}
		
		if(layout.equals("inline"))
			holder.textView.setPadding(
                    context.getResources().getInteger(R.dimen.padding_text_tips_inline_left),
                    context.getResources().getInteger(R.dimen.padding_text_tips_inline_top),
                    context.getResources().getInteger(R.dimen.padding_text_tips_inline_right),
                    context.getResources().getInteger(R.dimen.padding_text_tips_inline_bottom));
		
		return convertView; 
		
	}

	static class ViewHolder {
		FrameLayout frameLayout;
		RelativeLayout relativeLayout;
		TextView textView;
	}
}