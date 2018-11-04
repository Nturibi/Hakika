package com.hakika.hakika.api.models;

import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Drug {
    private IPFSTag tag;
    private String serialNumber;
    private String currentOwner;
    private List<DrugTransfer> transfers;

    public Drug(String serialNumber, List<DrugTransfer> transfers, String currentOwner, byte[] ipfsHash) {
        this.serialNumber = serialNumber;
        this.currentOwner = currentOwner;
        this.transfers = transfers;
        tag = new IPFSTag(ipfsHash);
    }

    private void checkFetched() {
        if (!tag.hasFetched()) {
            throw new IllegalStateException("Must fetch tag from IPFS before getting data");
        }
    }

    public IPFSTag getTag() {
        return tag;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public List<DrugTransfer> getTransfers() {
        return Collections.unmodifiableList(transfers);
    }

    private String getAttribute(String attrName) {
        checkFetched();
        return tag.getExtraAttributes().get(attrName);
    }

    public String getGenericName() {
        return getAttribute("genericName");
    }

    public String getName() {
        return getAttribute("name");
    }

    public String getProductionLocation() {
        return getAttribute("productionLocation");
    }

    public Date getExpirationDate() {
        try {
            long timestamp = Long.parseLong(getAttribute("expirationDate"));
            return new Date(timestamp);
        } catch (NumberFormatException|NullPointerException exc) {
            return new Date(0);
        }
    }

    public Date getProductionDate() {
        try {
            long timestamp = Long.parseLong(getAttribute("productionDate"));
            return new Date(timestamp);
        } catch (NumberFormatException|NullPointerException exc) {
            return new Date(0);
        }
    }

    public long getBatchNumber() {
        try {
            long batchNumber = Long.parseLong(getAttribute("batchNumber"));
            return batchNumber;
        } catch (NumberFormatException|NullPointerException exc) {
            return -1;
        }
    }

    public String getUseInformation() {
        return getAttribute("useInformation");
    }

}
