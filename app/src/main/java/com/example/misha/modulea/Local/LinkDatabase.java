package com.example.misha.modulea.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.misha.modulea.Link.MyLink;


import static com.example.misha.modulea.Local.LinkDatabase.DATABASE_VERSION;


@Database(entities = MyLink.class, version = DATABASE_VERSION)
public abstract class LinkDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TrueDevels-DB";

    public abstract LinkDAO linkDAO();
    private static LinkDatabase mInstance;
    public static LinkDatabase getInstance(Context context){
        if(mInstance==null){
            mInstance = Room.databaseBuilder(context,LinkDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  mInstance;
    }
}
