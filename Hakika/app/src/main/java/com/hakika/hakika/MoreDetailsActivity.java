package com.hakika.hakika;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
