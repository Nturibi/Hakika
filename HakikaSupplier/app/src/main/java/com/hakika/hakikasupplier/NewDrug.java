package com.hakika.hakikasupplier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewDrug extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drug);

        Button AddDrug = findViewById(R.id.btAdd);
        AddDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd(view);
            }
        });
    }

    private void onAdd(View view) {
//        EditText genericInout
    }
}
