package com.hakika.hakika;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class VerifyActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "VERIFY";
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        result ="";
        if(extras != null){
            result = extras.getString(QrCodeScanner. EXTRA_MESSAGE);
            result = result.replace("http://", "");
        }
        // the varible result stores what was read from the qr code. make use of it to set the boolean!

        boolean isVerified = true;
        System.out.println();
        if (result.equals("beautiful")) isVerified = false;
        if (isVerified){
            verificationSuccessful();
        }else{
            verificationFailed();
        }


    }

    private void verificationSuccessful(){
        ImageView img = (ImageView)  findViewById(R.id.ivVerify);
        img.setImageResource(R.drawable.verified);
        TextView success = (TextView) findViewById(R.id.tvVerify);
        success.setText("This drug was successfully verified.");
        Button details = (Button) findViewById(R.id.btDetail);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDetails();
            }
        });

    }
    private void verificationFailed(){
        ImageView img = (ImageView)  findViewById(R.id.ivVerify);
        img.setImageResource(R.drawable.unverified);
        TextView failure = (TextView) findViewById(R.id.tvVerify);
        failure.setText("This drug is not verified.");
        Toast.makeText( getApplicationContext(), "Verification failed",
                Toast.LENGTH_SHORT).show();
    }
    private void onClickDetails(){
        Intent intent = new Intent(getApplicationContext(), MoreDetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, result);
        startActivity(intent);

    }
}
