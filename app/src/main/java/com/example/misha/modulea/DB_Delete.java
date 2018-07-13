package com.example.misha.modulea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.MessageQueue;
import android.widget.Toast;

import com.example.misha.modulea.Database.LinkRepository;
import com.example.misha.modulea.Local.LinkDataSourceClass;
import com.example.misha.modulea.Local.LinkDatabase;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DB_Delete extends BroadcastReceiver {
    int id;
    private LinkRepository linkRepository;

    @Override
    public void onReceive(Context context, final Intent intent) {

        LinkDatabase linkDatabase = LinkDatabase.getInstance(MainActivity.context);
        linkRepository = LinkRepository.getmInstance(LinkDataSourceClass.getInstance(linkDatabase.linkDAO()));

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {


                Bundle extras = intent.getExtras();

                try {
                    id = extras.getInt("ID_Link");
                } catch (NullPointerException e) {
                   // id = 8;
                }

                linkRepository.deleteOneLink(MainActivity.links.get(id).getId());
                MainActivity.links.remove(id);
                MainActivity.linkAd.notifyDataSetChanged();
                //linkAd.notifyDataSetChanged();
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        Toast.makeText(context, "IMAGE WAS DELETED", Toast.LENGTH_LONG).show();
    }
}