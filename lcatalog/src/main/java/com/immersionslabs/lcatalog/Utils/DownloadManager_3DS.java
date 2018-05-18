package com.immersionslabs.lcatalog.Utils;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager_3DS {
    private String DOWNLOAD_URL;
    private String Article_3ds_file_name, Article_name;

    public DownloadManager_3DS(String url, String article_3ds_file_name, String article_name) {

        DOWNLOAD_URL = url;
        Article_3ds_file_name = article_3ds_file_name;
        Article_name = article_name;

        try {
            Download();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Download() throws FileNotFoundException, IOException {
        URL u = new URL(DOWNLOAD_URL);
        URLConnection conn = u.openConnection();
        int contentLength = conn.getContentLength();

        DataInputStream stream = new DataInputStream(u.openStream());

        byte[] buffer = new byte[contentLength];
        stream.readFully(buffer);
        stream.close();

        String file_location = Environment.getExternalStorageDirectory() + "/L_CATALOG/Models/" + Article_name + "/" + Article_3ds_file_name;

        DataOutputStream file_out = new DataOutputStream(new FileOutputStream(file_location));
        file_out.write(buffer);
        file_out.flush();
        file_out.close();
    }
}

