package com.example.misha.modulea;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.misha.modulea.Database.LinkRepository;
import com.example.misha.modulea.Link.MyLink;
import com.example.misha.modulea.Local.LinkDataSourceClass;
import com.example.misha.modulea.Local.LinkDatabase;

import java.io.File;
import java.io.FileOutputStream;
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
              
                LinkDatabase linkDatabase = LinkDatabase.getInstance(this);
                linkRepository = LinkRepository.getmInstance(LinkDataSourceClass.getInstance(linkDatabase.linkDAO()));
                Disposable disposable = linkRepository.getAllLinksOrderByStatus()
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

                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links );
                lv.setAdapter(linkAd);
                Toast toast4 = Toast.makeText(getApplicationContext(), "Sort by status", Toast.LENGTH_SHORT);
                toast4.show();
                break;

            case R.id.date:
                for(MyLink loc : links){ status_date.put(loc, loc.getDate());}
                loadData();
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, links );
                lv.setAdapter(linkAd);
                Toast toast1 = Toast.makeText(getApplicationContext(), "Sort by date", Toast.LENGTH_SHORT);
                toast1.show();
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

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }

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




    //=================================DOWNLOAD=========================================
    public void downloadImageFromUrl(View view) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();
        } else {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            final String date_local = dateFormat.format(date);
            Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                    MyLink link = new MyLink(tv.getText().toString(), date_local, 3);
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
                            Toast.makeText(MainActivity.this, "Link added!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            MyAsyncTask asyncTask = new MyAsyncTask();
            asyncTask.execute();

            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.moduleb");
            intent.addCategory("com.example.moduleb");
            intent.putExtra("url", tv.getText().toString());
            startActivity(intent);
            }
        }

        //DOWNLOAD CLASS
        class MyAsyncTask extends AsyncTask<Void, Void, Void> {
            //FEATURE
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //======================DOWNLOAD_IMAGE========================================
            //==============TO_/storage/emulated/0/BIGDIG/B===============================
            @Override
            protected Void doInBackground(Void... voids) {
                tv = findViewById(R.id.editText);
                String imageURL = tv.getText().toString();
                //set random name
                int r = (int) (Math.random() * 2147483647);
                String fileName = "image" + String.valueOf(r);

                try {
                    URL url = new URL(imageURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    //path ot directory
                    File SDCardRoot = new File(Environment.getExternalStorageDirectory() + "/BIGDIG/test/B");
                    //exist check --> if dir not exist, it will create
                    if (!SDCardRoot.exists()) {
                        SDCardRoot.mkdirs();
                    }
                    //create image file
                    File file = new File(SDCardRoot, fileName);
                    //read format from url after '.'
                    String extension = "";
                    int i = imageURL.lastIndexOf('.');
                    if (i > 0) {
                        extension = imageURL.substring(i + 1);
                    }
                    //set format to FileName  --> name with correct format
                    switch (extension) {
                        case "gif":
                            fileName += ".gif";
                            break;
                        case "png":
                            fileName += ".png";
                            break;
                        case "jpg":
                            fileName += ".jpg";
                            break;
                        case "jpeg":
                            fileName += ".jpeg";
                            break;
                        case "bmp":
                            fileName += ".bmp";
                            break;
                        case "apng":
                            fileName += ".apng";
                            break;
                        case "ico":
                            fileName += ".ico";
                            break;
                        case "wmp":
                            fileName += ".wmp";
                            break;
                        default:
                            fileName = null;
                    }
                    file = new File(SDCardRoot, fileName);
                    //start downloading image
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    InputStream inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1];
                    int bufferLength = inputStream.read(buffer);
                    while (bufferLength > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        bufferLength = inputStream.read(buffer);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            //FEATURES
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();
            }
            //================================FILE_WAS_DOWNLOADED===========================
        }
    }
