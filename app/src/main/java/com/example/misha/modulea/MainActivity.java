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

import java.net.URL;
import java.util.Objects;



public class MainActivity extends AppCompatActivity {
    String[] names = {"Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей"};
    ImageButton button;
    LinkAdapter<String> linkAd;

    TabHost tabHost;
    public static final String EXTRA_MESSAGE = "com.example.misha.modulea.MESSAGE";
    Button btn;
    TextView tv;
    MainActivity context = this;

    //AppDatabase db;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ListView lv = (ListView) findViewById(R.id.listview); // находим список
        linkAd = new LinkAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(linkAd);   // присваиваем адаптер списку


        btn = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.editText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        View.OnClickListener oclbtn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

