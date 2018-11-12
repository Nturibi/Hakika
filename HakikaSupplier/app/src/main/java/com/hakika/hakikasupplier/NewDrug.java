package com.hakika.hakikasupplier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewDrug extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drug);

        Button AddDrug = findViewById(R.id.btAddPage);
        AddDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd(view);
            }
        });
    }

    private void onAdd(View view) {
        EditText genericInput = findViewById(R.id.etDrugName);
        EditText brandInput = findViewById(R.id.etBrandName);
        EditText batchNum = findViewById(R.id.etBatchNum);
        EditText prodDate = findViewById(R.id.etProdDate);
        EditText expDate = findViewById(R.id.etExpDate);
        EditText drugUse = findViewById(R.id.etUse);
        EditText prodLocation = findViewById(R.id.etProdLocation);
    }
}
