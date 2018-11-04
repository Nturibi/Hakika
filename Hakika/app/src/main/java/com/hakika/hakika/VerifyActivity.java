package com.hakika.hakika;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        Bundle extras = getIntent().getExtras();
        result ="";
        if(extras != null){
            result = extras.getString(QrCodeScanner. EXTRA_MESSAGE);
            result = result.replace("http://", "");
        }
        Toast.makeText( getApplicationContext(), result,
                Toast.LENGTH_SHORT).show();
        // the varible result stores what was read from the qr code. make use of it to set the boolean!

        boolean isVerified = true;
        if (isVerified){
            verificationSuccessful();
        }else{
            verificationFailed();
        }
        Button details = (Button) findViewById(R.id.btDetail);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDetails();
            }
        });


    }

    private void verificationSuccessful(){
        ImageView img = (ImageView)  findViewById(R.id.ivVerify);
        img.setImageResource(R.drawable.verified);
        TextView success = (TextView) findViewById(R.id.tvVerify);
        success.setText("This drug was successfully verified.");
    }
    private void verificationFailed(){
        ImageView img = (ImageView)  findViewById(R.id.ivVerify);
        img.setImageResource(R.drawable.unverified);
        TextView failure = (TextView) findViewById(R.id.tvVerify);
        failure.setText("This drug is not verified.");
    }
    private void onClickDetails(){
        Intent intent = new Intent(getApplicationContext(), MoreDetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, result);
        startActivity(intent);

    }
}
