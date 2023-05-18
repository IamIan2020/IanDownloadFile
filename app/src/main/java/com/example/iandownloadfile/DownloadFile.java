package com.example.iandownloadfile;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by ian on 2018/2/12.
 */

public class DownloadFile {
    public static String TAG = "DownloadFile";


    public static String unpackZip(Context context) {//下載加解壓第一層
        String sentUrl = "aaa";

        String downloadUrl = "https://demo.stbear.com.tw/files/HealthEduImage.zip";

        String path = context.getExternalCacheDir().toString();

        String zipname = "HealthEduImage.zip";//downloadUrl.replace(downloadUrl, "");

        Log.d(TAG, "unpackZip downloadUrl== " + downloadUrl + " path= " + path);
//        Log.d(TAG, "unpackZip zipname== " + zipname);
        InputStream is;
        ZipInputStream zis;
        boolean unpackOK = false;
        String returnRes = "";
        HttpURLConnection connection = null;
        try {
//            createFolder(path + "");
            int count;

            URL url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
//            con.setRequestProperty("User-Agent", USER_AGENT);

            // For POST only - START
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(sentUrl.getBytes());
            os.flush();
            os.close();

            connection.connect();
            // getting file length
//            int lenghtOfFile = connection.getContentLength();
            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            Log.d("DownloadFile", "unpackZip path + fileName== " + "" + path + "/" + zipname);
            OutputStream output = new FileOutputStream("" + path + File.separator + zipname);
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            Log.d("DownloadFile", "unpackZip total == " + total);
            Log.d(TAG, "upath/zipname== " + path + File.separator + zipname);
            is = new FileInputStream(path + File.separator + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    _dirChecker(path + "/" + ze.getName());
                    Log.d("DownloadFile", "_dirChecker == " + path + "/" + ze.getName());
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int countSize = 0;

                    String filename = ze.getName();
                    Log.d("DownloadFile", "getNextEntry == " + path + "/" + filename);
                    FileOutputStream fout = new FileOutputStream(path + "/" + filename);

                    while ((countSize = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, countSize);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();

                    if (filename.indexOf(zipname) != -1) {
                        Log.d("DownloadFile", "gilename.indexOf != -1");
                        unpackOK = setUnPackZip(path + File.separator + filename);
                    }
                }
                zis.closeEntry();
            }

//            unpackOK = true;
//            ze.clone();
            zis.close();
            is.close();
        } catch (IOException e) {
            returnRes = e.toString();
//            unpackOK = false;

            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return returnRes;
    }

    public static boolean setUnPackZip(String orgfileName) {//解壓第二層
        InputStream is;
        ZipInputStream zis;
        try {
            is = new FileInputStream(orgfileName);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
//                String[] separated = ze.getName().split("/");
//                if (separated.length >= 2) {
//                    createFolder(orgfileName + File.separator + separated[0]);
//                }

//                Log.d("DownloadFile", "ze.getName(== " + ze.getName());
                if (ze.isDirectory()) {
//                    Log.d("DownloadFile", "setUnPackZip ze.isDirectory()");
                    _dirChecker(orgfileName + File.separator + ze.getName());
                } else {
//                    Log.d("DownloadFile", "setUnPackZip ze.isDirectory() else");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int countSize = 0;

                    String filename = ze.getName();
//                    Log.d("DownloadFile", "setUnPackZip getNextEntry == " + orgfileName + "/" + filename);
                    FileOutputStream fout = new FileOutputStream(orgfileName + File.separator + filename);

                    // reading and writing
                    while ((countSize = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, countSize);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                }

                zis.closeEntry();
            }
            is.close();
            zis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    private static void _dirChecker(String dir) {
        File f = new File(dir);
//        Log.d("DownloadFile", "_dirChecker dir " + dir);

        if (dir.length() >= 0 && !f.isDirectory()) {
            f.mkdirs();
        }
    }

//    public static void createFolder(String name) {
//        File folder = new File(name);
////        Log.d("DownloadFile", "folder == " + folder.toString());
//        if (!folder.exists()) {
////            Log.d(TAG, "folder.mkdir()" + folder.mkdir());
//        } else {//older.exists()
////            Log.d(TAG, "older.exists()");
//        }
//    }
}
