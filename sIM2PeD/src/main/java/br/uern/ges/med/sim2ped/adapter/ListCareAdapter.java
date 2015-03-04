package br.uern.ges.med.sim2ped.adapter;

import java.util.ArrayList;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.Care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListCareAdapter extends ArrayAdapter<Care> {
    
	// View lookup cache
    private static class ViewHolder {
        TextView care;
    }

    public ListCareAdapter(Context context, ArrayList<Care> cares) {
       super(context, R.layout.item_list_care, cares);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Care care = getItem(position);
       // Check if an existing view is being reused, otherwise inflate the view
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_list_care, parent, false);
          viewHolder.care = (TextView) convertView.findViewById(R.id.text_item_list_care);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       
       // Populate the data into the template view using the data object
       viewHolder.care.setText(care.getTitleCare());
       
       // Return the completed view to render on screen
       return convertView;
   }
}