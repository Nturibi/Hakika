package com.hakika.hakikasupplier.models;

import java.util.Date;

public class DrugItem {
    public String drugName;
    public Date acquisitionDate;

    public DrugItem(String drugName, Date acquisitionDate) {
        this.drugName = drugName;
        this.acquisitionDate = acquisitionDate;
    }

    public void setDrugName(String name) {this.drugName = name;}
    public String getDrugName() {return this.drugName;}
    public void setAcquisitionDate(Date date) {this.acquisitionDate = date;}
    public Date getAcquisitionDate() {return this.acquisitionDate;}
}
