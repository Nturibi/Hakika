package com.hakika.hakika.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenneth on 11/4/18.
 */

public class MoreItem {

    public String from;
    public  String to;
    public Date fromDate;
    public Date toDate;

    public MoreItem(String from, String to, Date fromDate,Date toDate){
        this.from = from;
        this.to = to;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public  void setFrom(String from){
        this.from = from;
    }
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
