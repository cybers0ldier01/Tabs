package com.example.misha.modulea.Database;


import com.example.misha.modulea.Link.MyLink;

import java.util.List;

import io.reactivex.Flowable;

public interface LinkDataSource {

    Flowable<MyLink> getOneLink(String link);
    Flowable<List<MyLink>> getAllLinks();
    Flowable<List<MyLink>> getAllLinksOrderByStatus();
    void insertLink(MyLink... links);
    void updateLink(MyLink... links);
    void deleteLink(MyLink link);
    void deleteAllLinks();
}
