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
import com.hakika.hakika.models.MoreItem;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by kenneth on 11/4/18.
 */

public class MoreItemAdapter extends ArrayAdapter<MoreItem> {
    public static final String FROM = "FROM";
    public static final String TO = "TO";
    public static final String FROM_DATE = "FROMDATE";
    public static final String TO_DATE = "TODATE";


    private Context context;

    public MoreItemAdapter(Context context, ArrayList<MoreItem> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = getContext();
        // Get the data item for this position
        final MoreItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_more, parent, false);
        }
        // Lookup view for data population
        TextView tvFrom = (TextView) convertView.findViewById(R.id.tvFrom);
        TextView tvTo = (TextView) convertView.findViewById(R.id.tvTo);

        TextView tvFromDate = (TextView) convertView.findViewById(R.id.tvFromDate);
        TextView tvToDate = (TextView) convertView.findViewById(R.id.tvToDate);

        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        // Populate the data into the template view using the data object
        tvFrom.setText(item.from);
        tvTo.setText(item.to);
        tvFromDate.setText(   format.format(item.fromDate));
        tvToDate.setText( format.format(item.toDate) );
        // Return the completed view to render on screen
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(FROM,item.from );
                intent.putExtra(TO,item.to );
                intent.putExtra(FROM_DATE, format.format(item.fromDate) );
                intent.putExtra(TO_DATE,  format.format(item.toDate) );
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}