package com.instadownloader.instadpdownloader.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.instadownloader.instadpdownloader.Utils.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ayush on 4/2/17.
 */
public class DownloadingPhoto extends AsyncTask<String, Void, Bitmap> {


    private String fileName;
    private String imageUrl;
    private Context context;

    public DownloadingPhoto(Context context, String fileName, String imageUrl) {
        this.context = context;
        this.fileName = fileName;
        this.imageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        Bitmap mIcon11 = null;
        try {

            File dp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + Constants.FOLDER_NAME, fileName);
            if (!dp.getParentFile().exists())
                dp.getParentFile().mkdirs();

            OutputStream output;

            URL url = new URL(imageUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            long downloaded = 0;
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            connection.connect();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fos;

            fos = new FileOutputStream(dp);
            output = new BufferedOutputStream(fos, 1024);

            byte[] data = new byte[1024];
            int x = 0;

            long total = downloaded;

            while ((x = input.read(data, 0, 1024)) >= 0) {
                output.write(data, 0, x);
                downloaded += x;
                //publishProgress("" + (int) ((downloaded * 100) / (total)));

            }
            // closing streams
            output.flush();
            output.close();
            input.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

    }
}
