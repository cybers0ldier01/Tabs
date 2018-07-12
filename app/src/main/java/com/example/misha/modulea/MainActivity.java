package com.example.misha.modulea;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misha.modulea.Database.LinkRepository;
import com.example.misha.modulea.Link.MyLink;
import com.example.misha.modulea.Local.LinkDataSourceClass;
import com.example.misha.modulea.Local.LinkDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    ImageButton button;
    static LinkAdapter linkAd;
    TabHost tabHost;
    public static final String EXTRA_MESSAGE = "com.example.misha.modulea.MESSAGE";
    Button btn;
    TextView tv;
    static Context context;
    static List<MyLink> links = new ArrayList<>();
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
                for (MyLink loc : links) {
                    status_sort.put(loc, loc.getStatus());
                }
                Map<MyLink, Integer> map = sortByValues((HashMap) status_sort);
                links = new ArrayList<>(map.keySet());
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links);
                lv.setAdapter(linkAd);
                Toast toast4 = Toast.makeText(getApplicationContext(), "Sort by status", Toast.LENGTH_SHORT);
                toast4.show();

            case R.id.date:
                for (MyLink loc : links) {
                    status_date.put(loc, loc.getDate());
                }
                Map<MyLink, Integer> map1 = sortByValuesBackward((HashMap) status_date);
                links = new ArrayList<>(map1.keySet());
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links);
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
        for (Iterator it = list.iterator(); it.hasNext(); ) {
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
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    ProgressDialog mProgressDialog;


    //========================URL_CHECK_METHODS============================
    //Returns true if url is valid
    public static boolean isValid(String url) {
        // Try creating a valid URL
        try {
            new URL(url).toURI();
            return true;
        }
        // If there was an Exception while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    //if url is image, return stat 1, else if url web-site or video return 2
    public static int checkURL(String u) throws IOException {
        int status;
        String format = "";
        String extension = "";
        int i = u.lastIndexOf('.');
        if (i > 0) {
            extension = u.substring(i + 1);
        }
        //set format to FileName  --> name with correct format
        switch (extension) {
            case "gif":
                format += "gif";
                break;
            case "png":
                format += "png";
                break;
            case "jpg":
                format += "jpg";
                break;
            case "jpeg":
                format += "jpeg";
                break;
            case "bmp":
                format += "bmp";
                break;
            case "apng":
                format += "apng";
                break;
            case "ico":
                format += "ico";
                break;
            case "wmp":
                format += "wmp";
                break;
        }
        if (format.equals(extension)) {

            status = 1;


        } else {
            status = 2;
        }
        return status;
    }
//==============================================================================


    int statAfter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }
        context = getApplicationContext();
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
        linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links);
        lv.setAdapter(linkAd);   // присваиваем адаптер списку
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final int id = i;

                String url = links.get(id).getJust_link();

                try {
                    statAfter = checkURL(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Disposable disposablen = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                        links.get(id).setStatus(statAfter);
                        linkRepository.updateLink(links.get(id));
                        linkAd.notifyDataSetChanged();
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
                if (links.get(id).getStatus() == 1) {
                    statAfter = 1;
                    Toast.makeText(getApplicationContext(),"URL will be deleted from DB in 15 seconds",Toast.LENGTH_LONG).show();
                    start_alarm(links.get(id).getId());

                }

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.moduleb");
                intent.addCategory("com.example.moduleb");
                intent.putExtra("url", url);
                intent.putExtra("stat", statAfter);
                intent.putExtra("from", "history");
                startActivity(intent);
            }
        });


        btn = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.editText);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_test));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_history));


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
                        Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllLinkSuccess(List<MyLink> myLinks) {
        links.clear();
        links.addAll(myLinks);
        linkAd.notifyDataSetChanged();
    }
    //=================================DOWNLOAD=========================================


    int statBefore;

    public void downloadImageFromUrl(View view) throws IOException {
        String field = tv.getText().toString();
        statBefore = checkURL(field);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();
        } else {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            final String date_local = dateFormat.format(date);
            Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                    MyLink link = new MyLink(tv.getText().toString(), date_local, statBefore);
                    links.add(link);
                    linkRepository.insertLink(link);
                    linkAd.notifyDataSetChanged();
                    emitter.onComplete();
                }
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Toast.makeText(MainActivity.this, "Link added!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            if (isValid(field)) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.moduleb");
                intent.addCategory("com.example.moduleb");
                intent.putExtra("url", tv.getText().toString());
                intent.putExtra("stat", statBefore);
                intent.putExtra("from", "test");
                startActivity(intent);

            } else {
                Toast.makeText(MainActivity.this, "" + "Fields must be filled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean checkIfURLExists(String targetUrl) {
        HttpURLConnection httpUrlConn;
        try {
            httpUrlConn = (HttpURLConnection) new URL(targetUrl)
                    .openConnection();

            httpUrlConn.setRequestMethod("HEAD");

            httpUrlConn.setConnectTimeout(1000);
            httpUrlConn.setReadTimeout(1000);

            /*System.out.println("Response Code: "
                    + httpUrlConn.getResponseCode());
            System.out.println("Response Message: "
                    + httpUrlConn.getResponseMessage());*/

            return (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage() + "type" + e.getClass().getName(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void start_alarm(int id) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent myIntent = new Intent(this, DB_Delete.class);
        myIntent.putExtra("ID_Link", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + 15000, pendingIntent);


    }
}





//git
