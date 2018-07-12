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

    @Query("SELECT * FROM links WHERE id=:id")
    Flowable<MyLink> getOneLink(int id);

    @Query("SELECT * FROM links")
    Flowable<List<MyLink>> getAllLinks();


    @Query("SELECT * FROM links ORDER BY status")
    Flowable<List<MyLink>> getAllLinksOrderByStatus();

    @Insert
    void insertLink(MyLink... links);

    @Query("DELETE FROM links")
    void deleteAllLinks();

    @Query("DELETE FROM links WHERE id=:id")
    void deleteOneLink(int id);

    @Query("UPDATE links SET status=:stat WHERE id=:id")
    void updateOneLink(int id, int stat);
}
