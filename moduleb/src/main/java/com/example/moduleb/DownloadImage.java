package com.example.moduleb;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImage extends AsyncTask<Void, Void, Void> {
    //DOWNLOAD CLASS
    //url setter

    private String imageURL;
    public void setURL(String url){
        imageURL = url;
    }
    DownloadImage context = this;
    //FEATURE
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //======================DOWNLOAD_IMAGE========================================
    //==============TO_/storage/emulated/0/BIGDIG/B===============================
    @Override
    protected Void doInBackground(Void... voids) {
        Log.e("myLogError", "in doInBackground");
        //SecondActivity secondActivity = new SecondActivity();
        //Toast.makeText(secondActivity.context, "in doInBackground", Toast.LENGTH_SHORT).show();

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

            //read format from url after '.'
            int positionPoint = imageURL.lastIndexOf('.');
            if (positionPoint == -1) {
                Log.e("myLogError", "Error could not create image file");
                return null;
            }

            String extension = imageURL.substring(positionPoint);
            //set format to FileName  --> name with correct format
            switch (extension) {
            case ".gif": case ".png": case ".jpg": case ".jpeg": case ".bmp": case ".apng": case ".ico": case ".wmp":
                //set random name
                int r = (int) (Math.random() * 2147483647);
                String fileName = "image" + String.valueOf(r) + extension;

                //create image file
                File file = new File(SDCardRoot, fileName);
                //start downloading image
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[1];
                int bufferLength = inputStream.read(buffer);
                while (bufferLength > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    bufferLength = inputStream.read(buffer);
                }
                break;

            default:
                Log.e("myLogError", "Error could not create image file: type image not found");
                break;
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
     //       Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();
    }
    //================================FILE_WAS_DOWNLOADED===========================
}

//git