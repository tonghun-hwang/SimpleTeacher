package com.example.simpleteacher.asyncTask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.simpleteacher.ItemActivity;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class initFirstWordListsTask extends AsyncTask<String, Void, String> {
    private final String TAG = "Main.FirstWordLists";

    private ItemActivity mParent;
    public final int HTTP_CONNECTION_TIMEOUT = 2500; //timeout: 5 sec

    public String mTableName;

    public initFirstWordListsTask(ItemActivity parent, String tableName) {
        mParent = parent;
        mTableName = tableName;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute(): ");
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground(): ");
        try {
            mParent.mWDB = mParent.mWDBHelper.getWritableDatabase();
            Cursor cur = mParent.mWDB.rawQuery("SELECT * FROM "
                    + mTableName, null);
            if (cur != null) {
                if (cur.getCount() > 0) {
                    Log.d(TAG, "DataBase already exists.: " + mTableName);
                    cur.close();
                } else {
                    readURL(params[0], params[1]);
                }
            }
            mParent.mWDB.close();
        } catch (Exception e) {
            e.getStackTrace();
            return "filed.";
        }
        return "Succeed.";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.d(TAG, "onProgressUpdate");
    }

    @Override
    protected void onCancelled(String result) {
        Log.d(TAG, "onCancelled");
    }

    private void readURL(String urlName, String fileName) {
        Log.d(TAG, "readURL: " + urlName);

        /* authorization for the data storage */
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("hottsportwiss@gmail.com", "Sportwiss2019".toCharArray());
            }
        });

        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(HTTP_CONNECTION_TIMEOUT); // 5 sec

            InputStream in = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.ISO_8859_1));
            String inputLine;
            String[] nextLine = null;

            /* Using variables of WordlistEntry */
            int id = 0;

            mParent.mWDB = mParent.mWDBHelper.getWritableDatabase();
            while ((inputLine = br.readLine()) != null) {
                try {
                    nextLine = inputLine.split(";");
                    if (nextLine.length > 0) {
                        // TODO: erase id
                        boolean res = false;
                        res = mParent.mWDBHelper.replaceData(mParent.mWDB, mTableName, nextLine, id);
                        if (res == false) {
                            Log.d(TAG, "word: " + nextLine[0]);
                        }
                        id++;
                    }
                } catch (Exception e) {
                    if (nextLine.length > 0) {
                        Log.e("DataAPIStub-wordlist", "skipped: " + nextLine[0]);
                    }
                    e.printStackTrace();
                }
            }
            mParent.mWDB.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            readCSVFile(fileName);
        }
    }

    private void readCSVFile(String fileName) {
        Log.d(TAG, "readCSVFile: " + fileName);
        boolean permissionGranted = ActivityCompat.checkSelfPermission(mParent,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        if (permissionGranted) {
            Log.d(TAG, "permission Granted: " + permissionGranted);
        } else {
            Log.d(TAG, "permission request.");
            ActivityCompat.requestPermissions(mParent,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    ItemActivity.READ_PERMITION_REQUEST_CODE);
            Log.d(TAG, "permission Granted: " + permissionGranted);
        }

        try (FileInputStream fis = new FileInputStream(fileName)) {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();
            CSVReaderBuilder builder = new CSVReaderBuilder(
                    new InputStreamReader(fis, StandardCharsets.ISO_8859_1));
            CSVReader reader = builder
                    .withCSVParser(parser)
                    .build();
            String[] nextLine;

            /* Using variables of WordlistEntry */
            int id = 0;
            if (mParent.mWDB == null || !mParent.mWDB.isOpen()) {
                mParent.mWDB = mParent.mWDBHelper.getWritableDatabase();
            }
            while ((nextLine = reader.readNext()) != null) {
                /* Using WordlistEntry */
                boolean res = mParent.mWDBHelper.replaceData(mParent.mWDB, mTableName, nextLine, id);
                if (res == false) {
                    Log.d(TAG, "word: " + nextLine[0]);
                }
                id++;
            }
            if (mParent.mWDB.isOpen()) {
                mParent.mWDB.close();
            }
        } catch (FileNotFoundException e1) {
            Log.e(TAG, "File is not found Exception.");
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            Log.e(TAG, "Unsupported Encodinng Exception.");
            e2.printStackTrace();
        } catch (IOException e3) {
            Log.e(TAG, "IO Exception");
            e3.printStackTrace();
        }
    }

}

