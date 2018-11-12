package com.hakika.hakikasupplier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hakika.hakikasupplier.models.DrugItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Home extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "USERNAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button transfer = findViewById(R.id.btTransfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Recipient.class);
                startActivity(intent);
            }
        });

        Button newDrugs = findViewById(R.id.btAddPage);
        newDrugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewDrug.class);
                startActivity(intent);
            }
        });

        populateDrigView();
    }

    private void populateDrigView() {
        MyDrugsAdapter adapter = new MyDrugsAdapter(this, generateSamples());
        ListView listView = findViewById(R.id.lvOwnedDrugs);
        listView.setAdapter(adapter);
    }

    private List<DrugItem> generateSamples() {
        List<DrugItem> myDrugs = new ArrayList<>();

        for (int i=0; i < 10; i++) {
            myDrugs.add(new DrugItem("Aspirin", new GregorianCalendar(2000+i, Calendar.FEBRUARY, i+0).getTime()));
            myDrugs.add(new DrugItem("Aspirin", new GregorianCalendar(2000+i, Calendar.APRIL, i+1).getTime()));
            myDrugs.add(new DrugItem("Aspirin", new GregorianCalendar(2000+i, Calendar.MAY, i+2).getTime()));
            myDrugs.add(new DrugItem("Aspirin", new GregorianCalendar(2000+i, Calendar.NOVEMBER, i+3).getTime()));
        }
        return  myDrugs;
    }
}
