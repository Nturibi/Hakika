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
import com.hakika.hakika.api.HakikaAPI;
import com.hakika.hakika.api.models.DrugTransfer;
import com.hakika.hakika.models.MoreItem;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kenneth on 11/4/18.
 */

public class MoreItemAdapter extends ArrayAdapter<DrugTransfer> {
    public static final String FROM = "FROM";
    public static final String TO = "TO";
    public static final String FROM_DATE = "FROMDATE";
    public static final String SERIAL_NUMBER = "SERIALNUMBER";


    private Context context;

    private Map<String, String> nameMap;
    public MoreItemAdapter(Context context, List<DrugTransfer> users, Map<String, String> friendlyNames) {
        super(context, 0, users);
        nameMap = friendlyNames;
    }

    private String truncateString(String str, int maxLength) {
        if (str.length() > maxLength) {
            return str.substring(0, maxLength-3) + "...";
        } else {
            return str;
        }
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

        String fromAddress = item.getFromAddress();
        String toAddress = item.getToAddress();

        String idStringFrom = String.format("%s (%s)", truncateString(fromAddress, 19), nameMap.get(fromAddress));
        String idStringTo = String.format("%s (%s)", truncateString(toAddress, 19), nameMap.get(toAddress));

        tvFrom.setText(idStringFrom);
        tvTo.setText(idStringTo);
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