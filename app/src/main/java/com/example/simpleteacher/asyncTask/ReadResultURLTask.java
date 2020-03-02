package com.example.simpleteacher.asyncTask;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class ReadResultURLTask extends AsyncTask<Void, Void, Integer> {
    private final String TAG = "Main.ReadResultURL";
    private final int HTTP_CONNECTION_TIMEOUT = 2500;
    private ItemActivity mParent;
    private String[] mIDList;
    private List<String> mUrlList;
    private List<String> mDBList;
    private List<String[]> mList;
    public static boolean Result = false;
    Cursor cursor;

    public ReadResultURLTask(ItemActivity parent, String[] idList) {
        mParent = parent;
        mIDList = idList;
        mUrlList = getUrlList(idList);
        mList = mParent.mUserDataList;
        mDBList = getDBList(idList);
        //mFile = fileName;
    }

    private List<String> getUrlList(String[] idList) {
        List<String> urlList = new ArrayList<String>();
        for (int i = 0; i < idList.length; i++) {
            String url = getURLName(idList[i]);
            urlList.add(url);
        }
        return urlList;
    }

    private String getURLName(String stName) {
        String dbName;
        String host;
        String url;

        dbName = getDBName(stName);
        Log.d(TAG,"readResultDB: " + dbName);
        host = mParent.pref.getString("host", "");
        url = host + "/HOT-T/Results/" + stName + "/" + dbName;

        return url;
    }

    private List<String> getDBList(String[] idList) {
        List<String> dbList = new ArrayList<String>();
        for (int i = 0; i < idList.length; i++) {
            String db = getDBName(idList[i]);
            dbList.add(db);
        }
        return dbList;
    }

    private String getDBName(String stName) {
        String dbName;
        dbName = "result_" + stName + ".db";

        return dbName;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "doInBackground()11111: ReadURLTask");
        // TODO: attempt authentication against a network service.
        Log.d(TAG, "aaaaaa 0");

        int res = 0;
        int index = 0;
        Iterator<String> iter = mUrlList.iterator();
        while(iter.hasNext()) {
            String mUrl = iter.next();
            try {
                res = readURL(mUrl, index);
                Log.d(TAG, "aaaaaa 00");
                if (res != HttpURLConnection.HTTP_OK) {
                    readLocalDB();
                    return res;
                }
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                index++;
            }
        }
        return res;
    }

    private void readLocalDB() {
        cursor = mParent.resultDB.rawQuery("SELECT * FROM "
                + mParent.resultDBHelper.RESULT_DIAG_MAIN, null);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final Integer result) {
        Log.d(TAG, "onPostExcute(): readResultURLTask");
        mParent.updateFragView();
    }

    @Override
    protected void onCancelled() {
        mParent.mReadResultURLTask = null;
    }

    private int readURL(String urlName, int index) {
        Log.d(TAG, "readResultURL: " + urlName);

        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });
        Log.d(TAG, "aaaaaa 1");
        File file = new File(mParent.getDatabasePath(mDBList.get(index)).toString());
        int responseCode = 0;
        Log.d(TAG, "aaaaaa 2");
        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
            responseCode = conn.getResponseCode();
            Log.d(TAG, "aaaaaa 3");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    Log.d(TAG, "aaaaaa 3");
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
        Log.d(TAG, "aaaaaa 5");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
