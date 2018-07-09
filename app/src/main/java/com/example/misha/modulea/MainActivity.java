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

import com.example.misha.modulea.modul.Link;

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



public class MainActivity extends AppCompatActivity {
    ImageButton button;
    LinkAdapter linkAd;
    TabHost tabHost;
    public static final String EXTRA_MESSAGE = "com.example.misha.modulea.MESSAGE";
    Button btn;
    TextView tv;
    MainActivity context = this;
    List<Link> links = new ArrayList<Link>();
    ArrayList<Link> local;
    Map<Link, Integer> status_sort = new HashMap<Link, Integer>();
    Map<Link, String> status_date = new HashMap<Link, String>();
    ListView lv;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.arrow:
                Intent intent1 = new Intent(context, MainActivity.class);
                startActivity(intent1);
            case R.id.status:
                for(Link loc : links){ status_sort.put(loc, loc.getStatus());}
                Map<Link, Integer> map = sortByValues((HashMap) status_sort);
                local = new ArrayList<>(map.keySet());
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, local );
                lv.setAdapter(linkAd);
                Toast toast4 = Toast.makeText(getApplicationContext(), "Sort by status", Toast.LENGTH_SHORT);
                toast4.show();

            case R.id.date:
                for(Link loc : links){ status_date.put(loc, loc.getDate());}
                Map<Link, Integer> map1 = sortByValuesBackward((HashMap) status_date);
                local = new ArrayList<>(map1.keySet());
                linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, local );
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
        links = DatabaseInintializer.getLinks(AppDatabase.getAppDatabase(this));

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
        local = new ArrayList<>(links);
        lv =  findViewById(R.id.listview); // находим список
        linkAd = new LinkAdapter(this, android.R.layout.simple_list_item_1, local );
        lv.setAdapter(linkAd);   // присваиваем адаптер списку

        btn = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.editText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        View.OnClickListener oclbtn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String date_local = dateFormat.format(date);
                DatabaseInintializer.populateSync(AppDatabase.getAppDatabase(context));
                DatabaseInintializer.addLink(AppDatabase.getAppDatabase(context),new com.example.misha.modulea.modul.Link(tv.getText().toString(),1,date_local));
                int count = DatabaseInintializer.getCount(AppDatabase.getAppDatabase(context));
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.moduleb");
                if(URLUtil.isValidUrl(tv.getText().toString())){
                    Objects.requireNonNull(intent).putExtra("url", tv.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "url is not valid", Toast.LENGTH_SHORT).show();
                }

            }
        };

        btn.setOnClickListener(oclbtn);

        //db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
    }


}

