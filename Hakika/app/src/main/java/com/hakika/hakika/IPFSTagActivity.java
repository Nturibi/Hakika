package com.hakika.hakika;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;


public class IPFSTagActivity extends AppCompatActivity {
    private Random random = new Random();

    private String from;
    private String to;
    private Date date;
    private String serialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipfstag);
    }

    private void viewFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(file));
        Intent intent2 = Intent.createChooser(intent, "Choose an application to open with:");
        startActivity(intent2);
    }

    private File saveFile(byte[] bytes) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Unable to write to external storage", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), random.nextLong()+".tmp");
            if (!file.mkdirs()) {
                Log.e("MoreDetailsActivity", "Directory "+file.getAbsolutePath()+" not created");
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            } catch (IOException exc) {
                Toast.makeText(this, "Error: "+exc.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            file.deleteOnExit();
            return file;
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
