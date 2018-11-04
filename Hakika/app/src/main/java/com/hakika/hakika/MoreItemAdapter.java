package com.hakika.hakika;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hakika.hakika.api.models.DrugTransfer;
import com.hakika.hakika.models.MoreItem;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenneth on 11/4/18.
 */

public class MoreItemAdapter extends ArrayAdapter<DrugTransfer> {
    public static final String FROM = "FROM";
    public static final String TO = "TO";
    public static final String FROM_DATE = "FROMDATE";
    public static final String SERIAL_NUMBER = "SERIALNUMBER";


    private Context context;

    public MoreItemAdapter(Context context, List<DrugTransfer> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = getContext();
        // Get the data item for this position
        final DrugTransfer item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_more, parent, false);
        }
        // Lookup view for data population
        TextView tvFrom = convertView.findViewById(R.id.tvFrom);
        TextView tvTo = convertView.findViewById(R.id.tvTo);

        TextView tvFromDate = convertView.findViewById(R.id.tvFromDate);

        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        // Populate the data into the template view using the data object
        tvFrom.setText(item.getFromAddress());
        tvTo.setText(item.getToAddress());
        tvFromDate.setText(format.format(item.getDate()));
        // Return the completed view to render on screen
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(FROM, item.getFromAddress());
                intent.putExtra(TO, item.getToAddress());
                intent.putExtra(FROM_DATE, format.format(item.getDate()));
                intent.putExtra(SERIAL_NUMBER, item.getSerialNumber());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}