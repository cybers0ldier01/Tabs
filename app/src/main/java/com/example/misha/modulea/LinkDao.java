package com.example.misha.modulea;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.misha.modulea.modul.Link;
import java.util.List;

@Dao
public interface LinkDao {

    @Query("SELECT * FROM LINK")
    List<Link> getAll();

    @Query("SELECT * FROM link where just_link LIKE  :link AND date LIKE :date")
    Link find(String link, String date);

    @Insert
    void insertAll(Link... links);

    @Delete
    void deleteAll(Link... links);

    @Query("SELECT COUNT(*) from LINK")
    int countLinks();
}