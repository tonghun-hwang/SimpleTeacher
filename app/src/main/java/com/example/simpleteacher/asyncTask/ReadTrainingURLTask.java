package com.example.simpleteacher.asyncTask;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.MainActivity;
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
    private static final int TRAINING_ID = 1;
    private final String TAG = "Main.ReadTrainingURL";
    private final int HTTP_CONNECTION_TIMEOUT = 2500;
    // private final String mFile;
    private ItemActivity mParent;
    private Fragment fragmentSync;
    private List<String> mUrlList;
    private final int MAX_SESSION = 16;

    public ReadTrainingURLTask(ItemActivity parent, String[] idList/*, String fileName*/) {
        mParent = parent;
        mUrlList = getUrlList(idList);
    }

    private List<String> getUrlList(String[] idList) {
        List<String> urlList = new ArrayList<String>();
        for (int i = 0; i < idList.length; i++) {
            for (int j = 0; j <= MAX_SESSION; j++) {
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
        url = host + "/HOT-T/Results_LB/" + stName + "/" + dbName;

        return url;
    }

    private String getDBName(String stName, int index) {
        String dbName;
        dbName = "training_" + stName + "_" + index + ".db";

        return dbName;
    }

    private String getPsDBName(String stName) {
        String[] pathNames = stName.split(".db");
        String dbName = pathNames[0];
        String psDbName = dbName + "ps.db";

        Log.d(TAG, "getPsDBName(): " + psDbName);
        return psDbName;
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
                res = readURL(mUrl);
                if (res != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "doInBackground(): connect Filed");
//                    int resPs = readURL(getPsDBName(mUrl));
//                    if (resPs != HttpURLConnection.HTTP_OK) {
//                        Log.d(TAG, "doInBackground(): ReadURLTask: res != HttpURLConnection.HTTP_OK");
//                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                index++;
            }
        }

        int isListFinished = mUrlList.size() - index;
        return isListFinished;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExcute(): readTrainingURLTask");
        super.onPreExecute();
        //mParent.updateSyncFragView();
    }

    @Override
    protected void onPostExecute(final Integer result) {
        Log.d(TAG, "onPostExcute(): readTrainingURLTask");
        Log.d(TAG, "onPostExcute(): readCode: " + result);
        String Status;
        if (result == HttpURLConnection.HTTP_OK) {
            Status = "Finished training" ;
        } else {
            Status = "Failed to finish ";
        }

        mParent.setStatus(TRAINING_ID);
        if (mParent.getStatus() == TRAINING_ID) {
            SharedPreferences.Editor editor = mParent.pref.edit();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String update = formatter.format(Calendar.getInstance().getTimeInMillis());
            editor.putString("syncDate", update);
            editor.commit();

            TextView txtUpdate = mParent.findViewById(R.id.txtUpdated);
            TextView txtConnected = mParent.findViewById(R.id.txtIsConnected);
            txtUpdate.setText(update);
            txtConnected.setText(Status);
        }

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

    private int readURL(String stURL) {
        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });

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
                    Log.d(TAG, "readTrainingURL: " + stURL);
                    Log.d(TAG, e.toString());
                } finally {
                    conn.disconnect();
                }
            } else {
                conn.disconnect();
            }

        } catch (Exception e) {
            Log.d(TAG, "readTrainingURL: " + stURL);
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
