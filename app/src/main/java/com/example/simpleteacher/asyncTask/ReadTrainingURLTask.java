package com.example.simpleteacher.asyncTask;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.MainActivity;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class ReadTrainingURLTask extends AsyncTask<Void, Void, Integer> {
    private final String TAG = "Main.ReadTrainingURL";
    private final int HTTP_CONNECTION_TIMEOUT = 2500;
    // private final String mFile;
    private ItemActivity mParent;
    private MainActivity mainActivity;
    private Fragment fragmentSync;
    private String[] mID;
    private String mUrl;
    private List<String[]> mList;
    private SQLiteDatabase mDB;
    public String status;
    Cursor cursor;
    private List<String> mUrlList;

    public ReadTrainingURLTask(ItemActivity parent, String[] idList/*, String fileName*/) {
        mParent = parent;
        mID = idList;
        mList = mParent.mUserDataList;
        mDB = mParent.mUserDB;
        mUrlList = getUrlList(idList);

        //mFile = fileName;
    }

    private List<String> getUrlList(String[] idList) {
        List<String> urlList = new ArrayList<String>();
        for (int i = 0; i < idList.length; i++) {
            for (int j = 0; j < 16; j++) {
                String url = getURLName(idList[i], j);
                urlList.add(url);
            }
        }
        return urlList;
    }

    private String getURLName(String stName, int index) {
        String dbName;
        String host;
        String url;

        dbName = getDBName(stName, index);
        Log.d(TAG,"readResultDB: " + dbName);
        host = mParent.pref.getString("host", "");
        url = host + "/HOT-T/Results/" + stName + "/" + dbName;

        return url;
    }

    private String getDBName(String stName, int index) {
        String dbName;
        dbName = "training_" + stName + "_" + index + ".db";

        return dbName;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "doInBackground(): ReadURLTask");
        // TODO: attempt authentication against a network service.
        int res = 0;
        int index = 0;
        Iterator<String> iter = mUrlList.iterator();
        while(iter.hasNext()) {
            String mUrl = iter.next();
            try {
                res = readURL(mUrl, index);
                if (res != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "doInBackground(): ReadURLTask: res != HttpURLConnection.HTTP_OK");
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
        cursor = mParent.mUserDB.rawQuery("SELECT * FROM "
                + mParent.mUserDBHelper.TABLE_NAME, null);
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExcute(): readTrainingURLTask");
        super.onPreExecute();
        mParent.updateSyncFragView();

    }

    @Override
    protected void onPostExecute(final Integer result) {
        Log.d(TAG, "onPostExcute(): readTrainingURLTask");
        Log.d(TAG, "onPostExcute(): readCode: " + result);
        String Status = "OK: ";
        if (result > 0) {
            Status = "Fail: ";
        }
        SharedPreferences.Editor editor = mParent.pref.edit();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String update = formatter.format(Calendar.getInstance().getTimeInMillis());
        editor.putString("syncDate", Status + update);
        editor.commit();
        mParent.setStatus(Status + update);
        /*if (fragmentSync.txtUpdate != null) {
            fragmentSync.txtUpdate.setText(Status + update);
            fragmentSync.postTrain();
        }*/
        mParent.updateUI();
    }

    @Override
    protected void onCancelled() {
        mParent.mReadTrainingUrlTask = null;
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
                    Log.d(TAG, "readURL: " + urlCsvName);
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

    private int readURL(String stURL, int index) {
        Log.d(TAG, "readTrainingURL: " + stURL);
        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });

        //File file = new File(mParent.getDatabasePath(mFile).toString());
        int responseCode = 0;

        try {
            File file = new File(getlocalDBFileName(stURL));

            URL url = new URL(stURL);
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

    private String getlocalDBFileName(String stURL) {
        String[] fileNames = stURL.split("/");
        String dbName = fileNames[fileNames.length - 1];
        Log.d(TAG,  mParent.getDatabasePath(dbName).toString());
        return mParent.getDatabasePath(dbName).toString();
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
