package com.example.moduleb;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "IMAGE WAS DELETED", Toast.LENGTH_LONG).show();
    }


}
