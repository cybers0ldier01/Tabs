package com.example.misha.modulea.Link;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "links")
public class MyLink {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "just_link")
    private String just_link;

    @ColumnInfo(name="date")
    private String date;

    @ColumnInfo(name="status")
    private int status;

    public MyLink(String just_link, String date, int status) {

        this.just_link = just_link;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJust_link() {
        return just_link;
    }

    public void setJust_link(String just_link) {
        this.just_link = just_link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
