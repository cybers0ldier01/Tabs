package com.example.moduleb;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Date;
import java.util.Set;


public class SecondActivity extends Activity {
    private static final String APP_A_URL_TAG = "com.example.moduleb";
    private static final String LINK_TAG = "url";
    String url;
    ImageView image;
    Bundle extras;
    ProgressDialog mProgressDialog;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from image.xml
        setContentView(R.layout.activity_second);

        if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }


        Intent intentFromAppA = getIntent();
        if (!checking(intentFromAppA)) {
            DialogWindow();
        } else {
            extras = intentFromAppA.getExtras();
            if(extras.getInt("stat")==2) {
                url =null;
                Toast.makeText(this,"URL is not an image",Toast.LENGTH_LONG).show();
            } else{
              url = intentFromAppA.getStringExtra(LINK_TAG);
                // Locate the ImageView in activity_main.xml
                image = (ImageView) findViewById(R.id.image);
                new UploadImage().execute(url);
                if(extras.getString("from").equals("history")){
                    //Toast.makeText(this,"URL will be deleted from DB in 15 seconds",Toast.LENGTH_LONG).show();
                    //start_alarm();
                    DownloadImage asyncTask = new DownloadImage();
                    asyncTask.setURL(url);
                    asyncTask.execute();
                }
            }
        }
    }

    //=============================
    public void DialogWindow() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
        alertDialog.setTitle("Oops...");
      //  alertDialog.setMessage("You need to start this app from module A! It will be closed automatically in 10 seconds.");
        alertDialog.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                alertDialog.setMessage("You need to start this app from module A! It will be closed automatically in " + " 00:0" + (l / 1000));
                alertDialog.show();
                }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
        alertDialog.show();
    }

    private boolean checking(Intent intent) {
        Set<String> ss = intent.getCategories();
        for (String temp : ss) {
            if (temp.equals(APP_A_URL_TAG)) return true;
        }
        return false;
    }
    //=================================

    // DownloadImage AsyncTask
    private class UploadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SecondActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Upload Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            image.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }
    public void start_alarm() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent myIntent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP,new Date().getTime()+15000, pendingIntent);
    }


}

//git


