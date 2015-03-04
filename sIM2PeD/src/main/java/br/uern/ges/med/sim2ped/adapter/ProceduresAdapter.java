package br.uern.ges.med.sim2ped.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.Procedure;

public class ProceduresAdapter extends ArrayAdapter<Procedure> {
    private int listSize = 0;

	// View lookup cache
    private static class ViewHolder {
        TextView contextName;
        TextView titleCare;
        TextView hourResponse;
        TextView response;
        View borderBottom;
    }

    public ProceduresAdapter(Context context, ArrayList<Procedure> procedure) {
       super(context, R.layout.item_list_procedure, procedure);
        listSize = procedure.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Procedure procedure = getItem(position);
       // Check if an existing view is being reused, otherwise inflate the view
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_list_procedure, parent, false);
          viewHolder.contextName = (TextView) convertView.
                  findViewById(R.id.text_item_list_procedure_contextName);
           viewHolder.titleCare = (TextView) convertView.
                   findViewById(R.id.text_item_list_procedure_titleCare);
           viewHolder.hourResponse = (TextView) convertView.
                   findViewById(R.id.text_item_list_procedure_hourResponse);
           viewHolder.response = (TextView) convertView.
                   findViewById(R.id.text_item_list_procedure_response);
           viewHolder.borderBottom = convertView.findViewById(R.id.border_bottom_item_procedure);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       
        // Populate the data into the template view using the data object
        viewHolder.contextName.setText(procedure.getContextName());
        viewHolder.titleCare.setText(procedure.getTitleCare());
        viewHolder.hourResponse.setText(procedure.getHourResponse());
        viewHolder.response.setText(procedure.getResponse());

        if(position == listSize-1)
            viewHolder.borderBottom.setVisibility(View.GONE);
       
       // Return the completed view to render on screen
       return convertView;
   }
}