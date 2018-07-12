package com.example.misha.modulea.Local;

import com.example.misha.modulea.Database.LinkDataSource;
import com.example.misha.modulea.Link.MyLink;

import java.util.List;

import io.reactivex.Flowable;

public class LinkDataSourceClass implements LinkDataSource {

    private LinkDAO linkDAO;
    private static LinkDataSource mInstance;

    public LinkDataSourceClass(LinkDAO linkDAO) {
        this.linkDAO = linkDAO;
    }
    public static LinkDataSource getInstance(LinkDAO linkDAO){
        if(mInstance==null){
            mInstance=new LinkDataSourceClass(linkDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<MyLink> getOneLink(int id) {
        return linkDAO.getOneLink(id);
    }

    @Override
    public Flowable<List<MyLink>> getAllLinks() {
        return linkDAO.getAllLinks();
    }
    @Override
    public Flowable<List<MyLink>> getAllLinksOrderByStatus() { return linkDAO.getAllLinksOrderByStatus();
    }
    @Override
    public void insertLink(MyLink... links) {
        linkDAO.insertLink(links);
    }


    @Override
    public void deleteOneLink(int id) { linkDAO.deleteOneLink(id);}

    @Override
    public void deleteAllLinks() { linkDAO.deleteAllLinks(); }

    @Override
    public void updateOneLink(int id, int stat) {linkDAO.updateOneLink(id,stat);}

}
