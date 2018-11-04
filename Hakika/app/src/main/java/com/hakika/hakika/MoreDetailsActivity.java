package com.hakika.hakika;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.hakika.hakika.api.HakikaAPI;
import com.hakika.hakika.api.models.DrugTransfer;
import com.hakika.hakika.api.models.IPFSTag;
import com.hakika.hakika.models.MoreItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.ipfs.multihash.Multihash;

public class MoreDetailsActivity extends AppCompatActivity {
    private String result;
    private HakikaAPI api;
    private List<DrugTransfer> transferList;

    private Map<String, String> nameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        result ="";
        if(extras != null){
            result = extras.getString(QrCodeScanner. EXTRA_MESSAGE);
            result = result.replace("http://", "");
        }

        api = HakikaAPI.createRestrictedAPI();
        transferList = api.getTransferHistory(result);
        if (transferList.isEmpty()) {
            HakikaAPI adminapi = HakikaAPI.createAdminAPI();
            System.out.println("ADDRESSSSSSSSS: "+adminapi.getAddress());
            try {
                /*adminapi.addProducer(adminapi.getAddress(), IPFSTag.defaultTag(adminapi.getIpfs()));
                adminapi.addDrugs("Alprazolam", "Sleepy", "China", new Date(), new Date(), 10, "eat lots of pills",
                        new HashMap<>(), new HashMap<>(), 10);
                for (int i = 0; i < 10; i++) {
                    System.out.println("TRANSFERRING DRUGS");
                    adminapi.transferDrug(api.getAddress(), i + "", IPFSTag.defaultTag(adminapi.getIpfs()));
                }*/
                IPFSTag tag = adminapi.getProducerInformation(adminapi.getAddress());
                String defaultKey = new Multihash(IPFSTag.defaultTag(adminapi.getIpfs()).getIpfsKey()).toBase58();
                String ipfsKey = tag.getIpfsKey().length > 0 ? new Multihash(tag.getIpfsKey()).toBase58() : defaultKey;
                if (ipfsKey.equals(defaultKey)) {
                    HashMap<String, String> producerInformation = new HashMap<>();
                    producerInformation.put("friendlyName", "Kenneth's fake drug lab");
                    adminapi.setProducerInformation(adminapi.getAddress(), new HashMap<>(), producerInformation);
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        transferList = api.getTransferHistory(result);
        System.out.println("TRANSFER LIST: "+transferList);

        //@peter Add your data here
        // Construct the data source
// Create the adapter to convert the array to views
        MoreItemAdapter adapter = new MoreItemAdapter(this, transferList, nameMap);
// Attach the adapter to a ListView
        ListView listView = findViewById(R.id.lvMore);
        listView.setAdapter(adapter);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateNameMap();
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

    private void updateNameMap() {

        List<String> names = new ArrayList<>();
        for (DrugTransfer transfer : transferList) {
            names.add(transfer.getFromAddress());
            names.add(transfer.getToAddress());
        }

        try {
            for (String name : names) {
                String friendlyName = api.getFriendlyProducerName(name);
                if (friendlyName == null) {
                    friendlyName = api.getFriendlyUserName(name);
                }
                nameMap.put(name, friendlyName);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
