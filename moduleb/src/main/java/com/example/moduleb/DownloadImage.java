package com.example.moduleb;

import android.os.AsyncTask;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImage extends AsyncTask<Void, Void, Void> {

        private String imageURL;
        public void setURL(String url){
            imageURL = url;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            int r = (int) (Math.random() * 2147483647);
            String fileName = "image" + String.valueOf(r);

            try {
                URL url = new URL(imageURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                File SDCardRoot = new File(Environment.getExternalStorageDirectory() + "/BIGDIG/test/B");
                if (!SDCardRoot.exists()) {
                    SDCardRoot.mkdirs();
                }

                File file;

                String extension = "";
                int i = imageURL.lastIndexOf('.');
                if (i > 0) {
                    extension = imageURL.substring(i + 1);
                }

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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

