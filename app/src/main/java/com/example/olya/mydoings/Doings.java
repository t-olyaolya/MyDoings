package com.example.olya.mydoings;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by tyuly on 03.10.2016.
 */

public class Doings { //модель
    private UUID mId; //id
    private String mTitle; //заголовок
    //private DateFormat mDate;    //дата
    private Date mDate;
    private boolean mDone; //выполненность


    public  Doings(){
        this(UUID.randomUUID());
    }
    public Doings(UUID uuid) {
       mId = uuid;
        mDate = new Date();


    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
       // mDate=DateFormat.getDateInstance();
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }


    public void setDone(boolean mDone) {
        this.mDone=mDone;
    }
    public boolean getDone(){
        return mDone;
    }



}
