package com.hakika.hakika;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.hakika.hakika.models.MoreItem;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MoreDetailsActivity extends AppCompatActivity {
    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        result ="";
        if(extras != null){
            result = extras.getString(QrCodeScanner. EXTRA_MESSAGE);
            result = result.replace("http://", "");
        }

        //@peter Add your data here
        // Construct the data source
        ArrayList<MoreItem> arrayOfMoreItems = new ArrayList<MoreItem>();
        for (int i =0; i < 10; i++){
            //Get the progress of the medicine and display
            MoreItem newItem = new MoreItem("India", "Nairobi", new Date(), new Date());
            arrayOfMoreItems.add(newItem);
        }
// Create the adapter to convert the array to views
        MoreItemAdapter adapter = new MoreItemAdapter(this, arrayOfMoreItems);
// Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvMore);
        listView.setAdapter(adapter);
    }

}
