package com.example.simpleteacher.asyncTask;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class ReadInfoURLTask extends AsyncTask<Void, Void, Integer> {
    private final String TAG = "Main.ReadURL";
    private final int HTTP_CONNECTION_TIMEOUT = 2500;
    private ItemActivity mParent;
    private String mUrl;
    private List<String[]> mList;
    private SQLiteDatabase mDB;
    Cursor cursor;

    public ReadInfoURLTask(ItemActivity parent, String url) {
        mParent = parent;
        mUrl = url;
        mList = mParent.mUserDataList;
        mDB = mParent.mUserDB;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "doInBackground(): ReadURLTask");
        // TODO: attempt authentication against a network service.
        int res = readURL(mUrl);
        if (res != HttpURLConnection.HTTP_OK) {
            String userDataCsv = ItemActivity.pref.getString("userData", "");
            res = readURLCSV(userDataCsv);
            if (res != HttpURLConnection.HTTP_OK) {
                readLocalDB();
                return res;
            }
        }
        return res;
    }

    private void readLocalDB() {
        cursor = mParent.mUserDB.rawQuery("SELECT * FROM "
                + mParent.mUserDBHelper.TABLE_NAME, null);
    }

    @Override
    protected void onPostExecute(final Integer result) {
        Log.d(TAG, "onPostExcute(): UserLoginTask");
    }

    @Override
    protected void onCancelled() {
        mParent.mReadInfoUrlTask = null;
    }

    private int readURLCSV(String urlCsvName) {

        Log.d(TAG, "readURLCSV: " + urlCsvName);
        /* authorization for the data storage */
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });

        int responseCode = 0;
        try {
            URL url = new URL(urlCsvName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);

            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    InputStream in = conn.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.ISO_8859_1));
                    String inputLine;
                    String[] nextLine = null;
                    Log.d(TAG, "readURL_Login: " + urlCsvName);
                    while ((inputLine = br.readLine()) != null) {
                        nextLine = inputLine.split(";");

                        /* data in the array list */
                        String[] temp = Arrays.copyOfRange(nextLine, 1, nextLine.length - 1);
                        mList.add(temp);

                        boolean res = mParent.mUserDBHelper.replaceData(mDB, nextLine);
                        Log.d(TAG, "user student: " + res);

                        Log.d(TAG, nextLine[0]);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    e.getStackTrace();
                } finally {
                    conn.disconnect();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            e.getStackTrace();
        }
        return responseCode;
    }

    private int readURL(String urlName) {
        Log.d(TAG, "readURL: " + urlName);

        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });

        File file = new File(mParent.getDatabasePath("userInfo_lb.db").toString());
        int responseCode = 0;

        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
            responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    InputStream in = conn.getInputStream();
                    copyInputStreamToFile(in, file);
                    in.close();
                } catch (Exception e) {
                    e.getStackTrace();
                    Log.d(TAG, e.toString());
                } finally {
                    conn.disconnect();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            Log.d(TAG, e.toString());
        }
        return responseCode;
    }

    private void copyInputStreamToFile(InputStream in, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
