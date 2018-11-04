package com.hakika.hakika;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static final String EXTRA_MESSAGE = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        Button userLogin = (Button) findViewById(R.id.btUserlogin);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin();
            }
        });

        //set the email and password
        EditText emailText =  (EditText) findViewById(R.id.etEmail);
        emailText.setText("ken@gmail.com");
        EditText passwordText = (EditText) findViewById(R.id.etPassword);
        passwordText.setText("1234567");
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        Toast.makeText(this, "Got user", Toast.LENGTH_SHORT).show();
//        System.out.println("Got the user!!!");
    }

    private void onClickLogin(){
        EditText emailText =  (EditText) findViewById(R.id.etEmail);
        String email = emailText.getText().toString();

        EditText passwordText = (EditText) findViewById(R.id.etPassword);
        String password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(getApplicationContext(), QrCodeScanner.class);
                            FirebaseUser user = mAuth.getCurrentUser();
                            intent.putExtra(EXTRA_MESSAGE,user.getDisplayName() );
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText( getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

}
