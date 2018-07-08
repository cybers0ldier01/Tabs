package com.example.misha.modulea.modul;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "link")
public class Link {




    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "just_link")
    private String just_link;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "date")
    private String date;

    public Link(String just_link, int status, String date) {

        this.just_link = just_link;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJust_link() { return just_link; }

    public void setJust_link(String just_link) {
        this.just_link = just_link;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
