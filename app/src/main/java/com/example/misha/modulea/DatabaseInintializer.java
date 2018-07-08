package com.example.misha.modulea;


import android.os.AsyncTask;
import android.util.Log;

import com.example.misha.modulea.modul.Link;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class DatabaseInintializer {

    private static final String TAG = DatabaseInintializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    /*public static void GetpopulateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }*/

    public static Link addLink(final AppDatabase db, Link link) {
        db.userDao().insertAll(link);
        return link;
    }


    public static List<Link> getLinks(final AppDatabase db) {
        List<Link> LinkList = db.userDao().getAll();
        return LinkList;
    }

    private static void populateWithTestData(AppDatabase db) {
        Link user = new Link("tipa_ssilka",1, "23");
        List<Link> userList = db.userDao().getAll();
        Log.d(DatabaseInintializer.TAG, "Rows Count: " + userList.size());
    }
    public static int getCount(AppDatabase db){
        return db.userDao().countLinks();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
