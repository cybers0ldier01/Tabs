package com.example.moduleb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Set;

public class SecondActivity extends AppCompatActivity {

    private static final String APP_A_URL_TAG = "com.example.moduleb";
    private static final String LINK_TAG = "link_tag";

    /**
     * Attributes for displaying image
     */
    private ImageView img;
    private Bitmap bitmap;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intentFromAppA = getIntent();
        if (!checking(intentFromAppA)) {
            DialogWindow();
        } else {
            url = intentFromAppA.getStringExtra(LINK_TAG);}
    }

    public void DialogWindow() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SecondActivity.this);
        alertDialog.setTitle("Oops...");
        alertDialog.setMessage("You need to start this app from module A! It will be closed automatically in 10 seconds.");
        alertDialog.setNegativeButton("Close",
             new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                            finish();
                            }
                        });
             new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long l) {
                        alertDialog.setMessage("You need to start this app from module A!" + " 00:" + (l / 1000));
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
}