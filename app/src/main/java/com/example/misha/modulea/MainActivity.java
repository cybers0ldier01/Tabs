package com.example.misha.modulea;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.misha.modulea.Database.LinkRepository;
import com.example.misha.modulea.Link.MyLink;
import com.example.misha.modulea.Local.LinkDataSourceClass;
import com.example.misha.modulea.Local.LinkDatabase;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    ImageButton button;
    LinkAdapter linkAd;
    TabHost tabHost;
    public static final String EXTRA_MESSAGE = "com.example.misha.modulea.MESSAGE";
    Button btn;
    TextView tv;
    MainActivity context = this;
    List<MyLink> links = new ArrayList<>();
   // ArrayList<MyLink> local;
    Map<MyLink, Integer> status_sort = new HashMap<>();
    Map<MyLink, String> status_date = new HashMap<>();
    ListView lv;
    private CompositeDisposable compositeDisposable;
    private LinkRepository linkRepository;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.status:
                for(MyLink loc : links){ status_sort.put(loc, loc.getStatus());}
                Map<MyLink, Integer> map = sortByValues((HashMap) status_sort);
                links = new ArrayList<>(map.keySet());
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links );
                lv.setAdapter(linkAd);
                Toast toast4 = Toast.makeText(getApplicationContext(), "Sort by status", Toast.LENGTH_SHORT);
                toast4.show();

            case R.id.date:
                for(MyLink loc : links){ status_date.put(loc, loc.getDate());}
                Map<MyLink, Integer> map1 = sortByValuesBackward((HashMap) status_date);
                links = new ArrayList<>(map1.keySet());
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links );
                lv.setAdapter(linkAd);
                Toast toast1 = Toast.makeText(getApplicationContext(), "Sort by date", Toast.LENGTH_SHORT);
                toast1.show();
        }
        return true;
    }

private static HashMap sortByValues(HashMap map) {
    List list = new LinkedList(map.entrySet());
    // Defined Custom Comparator here
    Collections.sort(list, new Comparator() {
        public int compare(Object o1, Object o2) {
            return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
        }
    });
    // Here I am copying the sorted list in HashMap
    // using LinkedHashMap to preserve the insertion order
    HashMap sortedHashMap = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry) it.next();
        sortedHashMap.put(entry.getKey(), entry.getValue());
    }
    return sortedHashMap;
}
    private static HashMap sortByValuesBackward(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();
        LinkDatabase linkDatabase = LinkDatabase.getInstance(this);
        linkRepository = LinkRepository.getmInstance(LinkDataSourceClass.getInstance(linkDatabase.linkDAO()));
        //Load all Data from Database
        loadData();

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Test");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Test");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("History");
        spec.setContent(R.id.tab2);
        spec.setIndicator("History");
        host.addTab(spec);

        lv = (ListView) findViewById(R.id.listview); // находим список
        linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links );
        lv.setAdapter(linkAd);   // присваиваем адаптер списку

        btn = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.editText);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_test));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_history));




        //View.OnClickListener oclbtn =
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(URLUtil.isValidUrl(tv.getText().toString())){
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    final String date_local = dateFormat.format(date);
                    Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                            MyLink link = new MyLink(tv.getText().toString(),date_local,3);
                            links.add(link);
                            linkRepository.insertLink(link);
                            emitter.onComplete();
                        }
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    Toast.makeText(MainActivity.this,"Link added!",Toast.LENGTH_SHORT).show();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(MainActivity.this,""+ throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });


                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.moduleb");
                    intent.addCategory("com.example.moduleb");
                    intent.putExtra("url", tv.getText().toString());
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "url is not valid", Toast.LENGTH_SHORT).show();
                }

            }
        });
        MyLink link = new MyLink("ddgd","535",3);


        //db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
    }

    private void loadData() {

        Disposable disposable = linkRepository.getAllLinks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<MyLink>>() {
                    @Override
                    public void accept(List<MyLink> myLinks) throws Exception {
                        onGetAllLinkSuccess(myLinks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,""+throwable.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLinkSuccess(List<MyLink> myLinks) {
        links.clear();
        links.addAll(myLinks);
        linkAd.notifyDataSetChanged();
    }


}

