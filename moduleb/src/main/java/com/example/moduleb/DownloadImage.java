package com.example.moduleb;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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

        //FEATURE
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //======================DOWNLOAD_IMAGE========================================
        //==============TO_/storage/emulated/0/BIGDIG/B===============================
        @Override
        protected Void doInBackground(Void... voids) {
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
//            Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();
        }
        //================================FILE_WAS_DOWNLOADED===========================
    }

