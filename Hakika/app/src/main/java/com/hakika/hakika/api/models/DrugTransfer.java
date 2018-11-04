package com.hakika.hakika.api.models;

import com.hakika.hakika.api.HakikaAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ipfs.api.IPFS;
import io.ipfs.multihash.Multihash;

public class DrugTransfer {
    public DrugTransfer(String fromAddress, String toAddress, String serialNumber,
                        byte[] ipfsKey,
                        Date date) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.serialNumber = serialNumber;
        this.date = date;
        this.tag = new IPFSTag(ipfsKey);
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Date getDate() {
        return date;
    }

    public IPFSTag getTag() {
        return tag;
    }

    private String fromAddress;
    private String toAddress;
    private String serialNumber;
    private IPFSTag tag;
    private Date date;

}
