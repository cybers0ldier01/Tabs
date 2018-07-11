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
    public Flowable<MyLink> getOneLink(String link) {
        return linkDAO.getOneLink(link);
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
    public void updateLink(MyLink... links) {
        linkDAO.updateLink(links);
    }

    @Override
    public void deleteLink(MyLink link) {
        linkDAO.deleteLink(link);
    }

    @Override
    public void deleteAllLinks() {
        linkDAO.deleteAllLinks();
    }
}
