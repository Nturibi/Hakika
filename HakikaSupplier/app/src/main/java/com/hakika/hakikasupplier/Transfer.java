package com.hakika.hakikasupplier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Transfer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Intent intent = getIntent();
        TextView recipientView = findViewById(R.id.tvRecipientName);
        recipientView.append(intent.getStringExtra("RECIPIENT"));

        Button addDrug = findViewById(R.id.btAddPage);
        addDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText enteredDrug = findViewById(R.id.etDrugName);
                String drugName = enteredDrug.getText().toString();
                enteredDrug.getText().clear();
                TextView drugItem = new TextView(getApplicationContext());
                drugItem.setText(drugName);
                LinearLayout addedDrugs = findViewById(R.id.llAddedDrugs);
                addedDrugs.addView(drugItem);
            }
        });


        Button sendDrugs = findViewById(R.id.btSend);
        sendDrugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = new Toast(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Transfer Successful!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), Home.class);
//                startActivity(intent);
            }
        });
    }
}
