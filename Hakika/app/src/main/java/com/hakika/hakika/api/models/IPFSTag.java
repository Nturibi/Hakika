package com.hakika.hakika.api.models;

import com.hakika.hakika.api.HakikaAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.ipfs.api.IPFS;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

public class IPFSTag {
    private byte[] ipfsKey;
    private boolean ipfsFetched;
    private Map<String, String> attachmentIpfsKeys;
    private Map<String, String> extraAttributes;

    private Map<String, byte[]> attachments;

    public IPFSTag(byte[] ipfsKey) {
        this.ipfsKey = ipfsKey;
    }

    public boolean hasFetched() {
        return ipfsFetched;
    }

    public Map<String, String> getAttachmentIpfsKeys() {
        return Collections.unmodifiableMap(attachmentIpfsKeys);
    }

    public Map<String, String> getExtraAttributes() {
        return Collections.unmodifiableMap(extraAttributes);
    }

    public byte[] getIpfsKey() {
        return ipfsKey;
    }

    public Map<String, byte[]> fetchAttachments(IPFS ipfs) throws IOException {
        if (attachments != null) return attachments;
        try {
            attachments = new HashMap<>();
            for (Map.Entry<String, String> entry : attachmentIpfsKeys.entrySet()) {
                Multihash hash = Multihash.fromBase58(entry.getValue());
                attachments.put(entry.getKey(), HakikaAPI.getIPFSFile(ipfs, hash.toBytes()));
            }
        } catch (Exception exc) {
            attachments = null;
            throw new IOException(exc);
        }
        return attachments;
    }

    public void fetchFromIPFS(IPFS ipfs) throws IOException {
        if (hasFetched()) return;
        JSONObject obj = HakikaAPI.getIPFSJson(ipfs, ipfsKey);
        if (obj == null) return;
        try {
            if (obj.has("attachments")) {
                JSONObject dict = obj.getJSONObject("attachments");
                for (Iterator<String> itr = dict.keys(); itr.hasNext(); ) {
                    String name = itr.next();
                    String s = dict.getString(name);
                    try {
                        Multihash.fromBase58(s);
                        attachmentIpfsKeys.put(name, s);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
                ipfsFetched = true;
            }

            if (obj.has("extraAttributes")) {
                JSONObject object = obj.getJSONObject("extraAttributes");
                for (Iterator<String> itr = object.keys(); itr.hasNext(); ) {
                    String key = itr.next();
                    extraAttributes.put(key, object.getString(key));
                }
            }
            ipfsFetched = true;
        } catch (JSONException exc) {
            exc.printStackTrace();
        }
    }

    public static IPFSTag uploadTag(IPFS ipfs, Map<String, byte[]> attachments, Map<String, String> extraAttributes) throws IOException {
        Map<String, String> uploaded = new HashMap<>();
        for (Map.Entry<String, byte[]> attachment : attachments.entrySet()) {
            String hash = HakikaAPI.uploadToIPFS(ipfs, attachment.getValue()).toBase58();
            uploaded.put(attachment.getKey(), hash);
        }

        JSONObject attachDict = new JSONObject(uploaded);
        JSONObject extra = new JSONObject(extraAttributes);

        JSONObject information = new JSONObject();

        try {
            information.put("attachments", attachDict);
            information.put("extraAttributes", extra);
        } catch (JSONException exc) {
            exc.printStackTrace();
            return null;
        }

        byte[] encoded = information.toString().getBytes(StandardCharsets.UTF_8);
        return new IPFSTag(HakikaAPI.uploadToIPFS(ipfs, encoded).toBytes());
    }

    private static IPFSTag defaultTag = null;
    public static IPFSTag defaultTag(IPFS ipfs) {
        if (defaultTag != null) return defaultTag;
        try {
            defaultTag = uploadTag(ipfs, new HashMap<>(), new HashMap<>());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        return defaultTag;
    }
}
