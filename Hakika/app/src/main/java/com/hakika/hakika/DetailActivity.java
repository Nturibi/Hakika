package com.hakika.hakika;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hakika.hakika.models.MoreItem;

import java.io.BufferedReader;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        String from = "";
        String to  = "";
        String fromDate = "";
        String serialNumber = "";
        if(extras != null){
            from = extras.getString(MoreItemAdapter.FROM);
            to = extras.getString(MoreItemAdapter.TO);
            fromDate = extras.getString(MoreItemAdapter.FROM_DATE);
            serialNumber = extras.getString(MoreItemAdapter.SERIAL_NUMBER);
        }

        TextView tvFrom = findViewById(R.id.tvFrom);
        tvFrom.setText(from);
//        Toast.makeText( getApplicationContext(), getIntent().getStringExtra(MoreItemAdapter.FROM),
//                Toast.LENGTH_SHORT).show();
//        Toast.makeText( getApplicationContext(), extras.getString(MoreItemAdapter.TO),
//                Toast.LENGTH_SHORT).show();

        TextView tvTo = findViewById(R.id.tvTo);
        tvTo.setText(to);
    }

}
