package com.hakika.hakika;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class IPFSTagActivity extends AppCompatActivity {
    private Random random = new Random();

    private Map<String, String> attributes;
    private Map<String, byte[]> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipfstag);

        Intent intent = getIntent();
        Serializable attr = intent.getSerializableExtra("attributes");
        attributes = (Map<String, String>) attr;
        Serializable fil = intent.getSerializableExtra("files");
        files = (Map<String, byte[]>) fil;

        ListView attributesTable = findViewById(R.id.extraAttributesTable);
        ArrayList<Map.Entry<String, String>> keyValueList = new ArrayList<>(attributes.entrySet());
        attributesTable.setAdapter(new ArrayAdapter<Map.Entry<String, String>>(this, 0, keyValueList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Map.Entry<String, String> entry = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_double_string, parent, false);
                }
                TextView tv = convertView.findViewById(R.id.textView1);
                TextView tv2 = convertView.findViewById(R.id.textView2)
                if (entry != null) {
                    tv.setText(entry.getKey());
                    tv2.setText(entry.getValue());
                }
                return convertView;
            }
        });

        ListView attachmentListView = findViewById(R.id.attachmentListView);
        ArrayList<Map.Entry<String, byte[]>> attachmentList = new ArrayList<>(files.entrySet());
        attachmentListView.setAdapter(new ArrayAdapter<Map.Entry<String, byte[]>>(this, 0,
                attachmentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Map.Entry<String, byte[]> entry = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_simple_string, parent, false);
                }
                TextView tv = convertView.findViewById(R.id.textView1);
                if (entry != null)
                    tv.setText(entry.getKey());
                return convertView;
            }
        });
        attachmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map.Entry<String, byte[]> attachment = attachmentList.get(i);
                File f = saveFile(attachment.getKey(), attachment.getValue());
                viewFile(f);
            }
        });
    }

    private void viewFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(file));
        Intent intent2 = Intent.createChooser(intent, "Choose an application to open with:");
        startActivity(intent2);
    }

    private File saveFile(String name, byte[] bytes) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Unable to write to external storage", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), name+"."+random.nextLong()+".tmp");
            if (!file.mkdirs()) {
                Log.e("IPFSTagActivity", "Directory "+file.getAbsolutePath()+" not created");
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
