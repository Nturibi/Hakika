package hakika.hakika.com.hakikasupplier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Transfer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Intent intent = getIntent();
        TextView recipientView = findViewById(R.id.tvRecipientName);
        recipientView.append(intent.getStringExtra("RECIPIENT"));

    }
}
