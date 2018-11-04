package com.hakika.hakikasupplier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Recipient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);

        Button setRecipient = findViewById(R.id.btConfirmRecipient);
        setRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText recipientField = findViewById(R.id.tvRecipient);
                String recipient = recipientField.getText().toString();
                CheckBox patient = findViewById(R.id.cbPatient);

                Intent intent = new Intent(getApplicationContext(), Transfer.class);
                intent.putExtra("RECIPIENT", recipient);
                startActivity(intent);
            }
        });
    }
}
