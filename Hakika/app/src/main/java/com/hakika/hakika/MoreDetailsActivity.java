package com.hakika.hakika;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.hakika.hakika.api.HakikaAPI;
import com.hakika.hakika.api.models.DrugTransfer;
import com.hakika.hakika.models.MoreItem;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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

        HakikaAPI api = HakikaAPI.createRestrictedAPI();
        List<DrugTransfer> transferList = api.getTransferHistory(result);

        //@peter Add your data here
        // Construct the data source
// Create the adapter to convert the array to views
        MoreItemAdapter adapter = new MoreItemAdapter(this, transferList);
// Attach the adapter to a ListView
        ListView listView = findViewById(R.id.lvMore);
        listView.setAdapter(adapter);
    }

}
