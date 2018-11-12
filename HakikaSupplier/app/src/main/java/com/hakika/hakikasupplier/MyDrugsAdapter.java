package com.hakika.hakikasupplier;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hakika.hakikasupplier.models.DrugItem;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyDrugsAdapter extends ArrayAdapter<DrugItem> {
    public static final String name = "Name";
    public static final String date = "DATE";

    private Context context;

    public MyDrugsAdapter(Context context, List<DrugItem> myDrugs) {super(context, 0, myDrugs);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DrugItem drug = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drug_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        // Populate the data into the template view using the data object
        tvName.setText(drug.drugName);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        tvDate.setText(format.format(drug.acquisitionDate));
        // Return the completed view to render on screen
        return convertView;
    }
}

