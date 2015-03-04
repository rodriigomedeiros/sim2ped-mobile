package br.uern.ges.med.sim2ped.adapter;

import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.uern.ges.med.sim2ped.R;

public class NavigationAdapter extends ArrayAdapter<NavigationItemAdapter> {

    private HashSet<Integer> checkedItems;
	public NavigationAdapter(Context context) {
		super(context, 0);
		this.checkedItems = new HashSet<Integer>();		
	}

	public void addHeader(String title) {
		add(new NavigationItemAdapter(title, 0, true));
	}

	public void addItem(String title, int icon) {
		add(new NavigationItemAdapter(title, icon, false));
	}

	public void addItem(NavigationItemAdapter itemModel) {
		add(itemModel);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).isHeader ? 0 : 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isHeader;
	}

	public void setChecked(int pos, boolean checked)
    {
        if (checked) {
            this.checkedItems.add(pos);
        } else {
            this.checkedItems.remove(pos);
        }    
        
        this.notifyDataSetChanged();        
    }

	public void resetarCheck()
    {
        this.checkedItems.clear();
        this.notifyDataSetChanged();
    }	
	
	public static class ViewHolder {
		public final FrameLayout icon;		
		public final TextView title;
		public final TextView counter;		
		public final LinearLayout colorLinear;

		public ViewHolder(TextView title, TextView counter, FrameLayout icon, LinearLayout colorLinear) {
			this.title = title;
			this.counter = counter;
			this.icon = icon;			
			this.colorLinear = colorLinear;
		}
	}

	@SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
		View view = convertView;
		NavigationItemAdapter item = getItem(position);
		
		if (view == null) {

			int layout;
						
			if (item.sub){
				layout = R.layout.navigation_list_item_sub;
			} else {
				layout = R.layout.navigation_list_item;
			}

			view = LayoutInflater.from(getContext()).inflate(layout, null);

			TextView txttitle = (TextView) view.findViewById(R.id.title);
			TextView txtcounter = (TextView) view.findViewById(R.id.counter);
			FrameLayout imgIcon = (FrameLayout) view.findViewById(R.id.icon);
			//View viewNavigation = (View) view.findViewById(R.id.viewNavigation);
			
			LinearLayout linearColor = (LinearLayout) view.findViewById(R.id.drawer_menu_row);
			view.setTag(new ViewHolder(txttitle, txtcounter, imgIcon, linearColor));
		}

        if ((holder == null)) {
            Object tag = view.getTag();
            if (tag instanceof ViewHolder) {
                holder = (ViewHolder) tag;
            }
        }
				
		if (item != null && holder != null) {
			if (holder.title != null){
				holder.title.setText(item.title);
			}
			
			/*
			if (holder.counter != null) {
				if (item.counter > 0) {
					holder.counter.setVisibility(View.VISIBLE);
					
					int counter = ((NavigationMain)getContext()).getCounterItemDownloads();
					holder.counter.setText("" + counter);
				} else {
					holder.counter.setVisibility(View.GONE);
				}
			} */

			if (holder.icon != null) {
				if (item.icon != 0 && !item.sub) {
					holder.title.setTextSize(20);
					holder.icon.setVisibility(View.VISIBLE);
					//holder.icon.setImageResource(item.icon);
					holder.icon.setBackgroundResource(item.icon);
				} else if(item.icon != 0 && item.sub){
					holder.title.setTextSize(14);
					holder.icon.setVisibility(View.VISIBLE);
					holder.icon.setBackgroundResource(item.icon);
				} else {
					holder.title.setTextSize(22);					
					holder.icon.setVisibility(View.GONE);
				}
			}
		}

		
		if(item.disable){
			if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				holder.colorLinear.setAlpha(0.3f);
			} else {
				AlphaAnimation alpha = new AlphaAnimation(1f, 0.3f);
		        alpha.setDuration(0);
		        alpha.setFillAfter(true);
		        holder.colorLinear.startAnimation(alpha);
			}
		} else { //If item is enabled.
			if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				holder.colorLinear.setAlpha(1f);
			} else {
				AlphaAnimation alpha = new AlphaAnimation(0.3f, 1f);
		        alpha.setDuration(0);
		        alpha.setFillAfter(true);
		        holder.colorLinear.startAnimation(alpha);
			}
		}
		
		//holder.viewNavigation.setVisibility(View.GONE);
		if (!item.isHeader) {
			if (checkedItems.contains(Integer.valueOf(position))  && !item.sub) {
				holder.title.setTypeface(null,Typeface.BOLD);
				//holder.viewNavigation.setVisibility(View.VISIBLE);

				holder.icon.getBackground().setColorFilter(Color.BLACK, Mode.SRC_ATOP);
		        view.invalidate();
			} else {				
				holder.title.setTypeface(null,Typeface.NORMAL);
				holder.icon.getBackground().clearColorFilter();
				holder.icon.invalidate();
			}			
		}
		
		//view.setBackgroundResource(R.drawable.list_selector);		
	    return view;		
	}

}