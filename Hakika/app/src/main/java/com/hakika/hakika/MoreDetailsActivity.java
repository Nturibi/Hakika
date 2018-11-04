package com.hakika.hakika;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        Bundle extras = getIntent().getExtras();
        result ="";
        if(extras != null){
            result = extras.getString(QrCodeScanner. EXTRA_MESSAGE);
            result = result.replace("http://", "");
        }

        //Ge the progress of the medicine and display
    }

}
