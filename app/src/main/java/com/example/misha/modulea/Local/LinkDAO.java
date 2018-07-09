package com.example.misha.modulea.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.misha.modulea.Link.MyLink;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LinkDAO {

    @Query("SELECT * FROM links WHERE id=:link")
    Flowable<MyLink> getOneLink(String link);

    @Query("SELECT * FROM links")
    Flowable<List<MyLink>> getAllLinks();

    @Insert
    void insertLink(MyLink... links);

    @Update
    void updateLink(MyLink... links);

    @Delete
    void deleteLink(MyLink link);

    @Query("DELETE FROM links")
    void deleteAllLinks();
}
