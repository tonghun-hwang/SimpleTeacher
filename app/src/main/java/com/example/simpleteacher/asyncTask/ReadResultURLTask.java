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
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class ReadResultURLTask extends AsyncTask<Void, Void, Integer> {
    private final String TAG = "Main.ReadResultURL";
    private final int HTTP_CONNECTION_TIMEOUT = 2500;
    private final String mFile;
    private ItemActivity mParent;
    private String mUrl;
    private List<String[]> mList;
    private SQLiteDatabase mDB;
    public static boolean Result = false;
    Cursor cursor;

    public ReadResultURLTask(ItemActivity parent, String url, String fileName) {
        mParent = parent;
        mUrl = url;
        mList = mParent.mUserDataList;
        mDB = mParent.mUserDB;
        mFile = fileName;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "doInBackground()11111: ReadURLTask");
        // TODO: attempt authentication against a network service.
        Log.d(TAG, "aaaaaa 0");
        int res = readURL(mUrl);
        Log.d(TAG, "aaaaaa 00");
        if (res != HttpURLConnection.HTTP_OK) {
            readLocalDB();
            return res;
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
        mParent.updateUI();
    }

    @Override
    protected void onCancelled() {
        mParent.mReadResultURLTask = null;
    }

    private int readURL(String urlName) {
        Log.d(TAG, "readResultURL: " + urlName);

        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });
        Log.d(TAG, "aaaaaa 1");
        File file = new File(mParent.getDatabasePath(mFile).toString());
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
