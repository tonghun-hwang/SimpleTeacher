package com.example.simpleteacher.asyncTask;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.R;

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
public class CheckInfoURLTask extends AsyncTask<Void, Void, Integer> {
    private final String TAG = "Main.CheckURL";
    private final int HTTP_CONNECTION_TIMEOUT = 2500;
    private ItemActivity mParent;
    private String mUrl;
    private List<String[]> mList;
    private SQLiteDatabase mDB;
    Cursor cursor;
    public TextView txtConnect;

    public CheckInfoURLTask(ItemActivity parent, String url) {
        mParent = parent;
        mUrl = url;
        mList = mParent.mUserDataList;
        mDB = mParent.mUserDB;
        txtConnect = mParent.findViewById(R.id.txtIsConnected);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "doInBackground(): CheckURLTask");
        // TODO: attempt authentication against a network service.
        int res = readURL(mUrl);
        return res;
    }

    @Override
    protected void onPostExecute(final Integer result) {
        Log.d(TAG, "onPostExcute(): UserLoginTask");
    }

    @Override
    protected void onCancelled() {
        mParent.mReadInfoUrlTask = null;
    }

    private int readURL(String urlName) {
        Log.d(TAG, "readURL: " + urlName);

        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });

        int responseCode = 0;

        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
            conn.setRequestMethod("HEAD");
            responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                txtConnect.setText("Connected");
            } else {
                txtConnect.setText("Not connected");
            }
        } catch (Exception e) {
            e.getStackTrace();
            Log.d(TAG, e.toString());
        }
        return responseCode;
    }
}
