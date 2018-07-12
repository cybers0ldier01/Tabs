package com.example.misha.modulea.Database;

import com.example.misha.modulea.Link.MyLink;


import java.util.List;

import io.reactivex.Flowable;

public class LinkRepository implements LinkDataSource{

    private LinkDataSource mLocalDataSource;
    private static LinkRepository mInstance;

    public LinkRepository(LinkDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }
    public static LinkRepository getmInstance(LinkDataSource mLocalDataSource){
        if(mInstance==null){
            mInstance=new LinkRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<MyLink> getOneLink(int id) {
        return mLocalDataSource.getOneLink(id);
    }

    @Override
    public Flowable<List<MyLink>> getAllLinks() {
        return mLocalDataSource.getAllLinks();
    }

    @Override
    public Flowable<List<MyLink>> getAllLinksOrderByStatus() { return mLocalDataSource.getAllLinksOrderByStatus(); }

    @Override
    public void insertLink(MyLink... links) {
        mLocalDataSource.insertLink(links);
    }

    @Override
    public void updateLink(MyLink... links) {
        mLocalDataSource.updateLink(links);
    }

    @Override
    public void deleteLink(MyLink link) {
        mLocalDataSource.deleteLink(link);
    }

    @Override
    public void deleteOneLink(int id) { mLocalDataSource.deleteOneLink(id);}

    @Override
    public void deleteAllLinks() {
        mLocalDataSource.deleteAllLinks();
    }
}
