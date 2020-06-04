package com.example.simpleteacher.asyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.helper.userResultDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;

import java.util.ArrayList;
import java.util.List;

public class TrainingAnalysisTask extends AsyncTask <String, Void, String> {
    private final ItemActivity mParent;
    private final String TAG = "DiagAnal";
    private final String[] mIds;
    private List<String[]> dataList;
    private final Context mContext;

    public TrainingAnalysisTask(Context parent, String[] ids) {
        mParent = (ItemActivity) parent;
        mContext = parent;
        mIds = ids;
        dataList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute(): ");
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground(): ");
        String dbName;
        String resultName;
        String res = "OK!";
        int total = 0;
        double[] data = new double[18];
        int sessionBlock;
        int offset = 0;

        for (int i = 0; i < mIds.length; i++) {
            offset = 0;
            try {
                for (int j = 1; j <= 16; j++) {
                    if (j % 5 == 1 ) {
                        for (int k = 0; k < data.length; k++) {
                            data[k] = 0;
                        }
                    }
                    /* result of buttons */
                    dbName = "training_" + mIds[i] + "_" + j + ".db";
                    mParent.mUserTrainingDBHelper = new userTrainingDBHelper(mContext, dbName, mIds[i], 1);
                    mParent.mUserTrainingDB = mParent.mUserTrainingDBHelper.getWritableDatabase();

                    total = mParent.getNumTotalWords(mIds[i]);
                    if (total == 0) {
                        mParent.mUserTrainingDB.close();
                        mParent.mUserTrainingDBHelper.close();

                        dbName = "training_" + mIds[i] + "_" + j + "ps.db";
                        mParent.mUserTrainingDBHelper = new userTrainingDBHelper(mContext, dbName, mIds[i], 1);
                        mParent.mUserTrainingDB = mParent.mUserTrainingDBHelper.getWritableDatabase();
                    }

                    data[0] += mParent.getNumTotalWords(mIds[i]);
                    data[1] += mParent.getNumTotallyWrongWords(mIds[i]);

                    data[2] += mParent.getNumTotalWordsInCategory(mIds[i]);
                    data[(j * 2) + 6 - offset] = mParent.getNumTotalWordsInCategory(mIds[i]);
                    data[3] += mParent.getNumTotallyWrongWordsInCategory(mIds[i]); // wrong words
                    data[(j * 2) + 7 - offset] = mParent.getNumTotallyWrongWordsInCategory(mIds[i]);

                    data[4] += mParent.getNumEraseAll(mIds[i]);
                    data[5] += mParent.getNumEraseOne(mIds[i]);
                    data[6] += mParent.getNumButtonEar(mIds[i]);
                    data[7] += mParent.getNumPushChar(mIds[i]);

                    if (j % 5 == 0) {
                        if (j < 0 && j <= 5) {
                            sessionBlock = 1;
                            offset = 10;
                        } else if (j > 5 && j <= 10) {
                            sessionBlock = 2;
                            offset = 20;
                        } else if (j > 10 && j <= 15) {
                            sessionBlock = 3;
                            offset = 30;
                        } else {
                            sessionBlock = 1;
                            offset = 10;
                        }
                        /* replace the database */
                        if (mParent.mAnalysisTrainingDB != null || !mParent.mAnalysisTrainingDB.isOpen()) {
                            mParent.mAnalysisTrainingDB = mParent.mAnalysisTrainingDBHelper.getWritableDatabase();
                        }
                        mParent.mAnalysisTrainingDBHelper.replaceData(mParent.mAnalysisTrainingDB, i, mIds[i], data, sessionBlock);

                    } else if (j == 16) {
                        sessionBlock = 4;

                        /* replace the database */
                        if (mParent.mAnalysisTrainingDB != null || !mParent.mAnalysisTrainingDB.isOpen()) {
                            mParent.mAnalysisTrainingDB = mParent.mAnalysisTrainingDBHelper.getWritableDatabase();
                        }
                        mParent.mAnalysisTrainingDBHelper.replaceData(mParent.mAnalysisTrainingDB, i, mIds[i], data, sessionBlock);
                    }

                    if (mParent.mUserTrainingDB != null) {
                        if (mParent.mUserTrainingDB.isOpen()) {
                            mParent.mUserTrainingDB.close();
                        }
                    }
                    if (mParent.mUserTrainingDBHelper != null) {
                        mParent.mUserTrainingDBHelper.close();
                        mParent.mUserTrainingDBHelper = null;
                    }
                }
            } catch (Exception e) {
                String err = e.getStackTrace().toString();
                Log.d(TAG, "error: " + err);
                res = res + " " + i;
            } finally {
                if (mParent.mUserTrainingDB != null) {
                    if (mParent.mUserTrainingDB.isOpen()) {
                        mParent.mUserTrainingDB.close();
                    }
                }
                if (mParent.mUserTrainingDBHelper != null) {
                    mParent.mUserTrainingDBHelper.close();
                    mParent.mUserTrainingDBHelper = null;
                }
            }
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute(): " + result);
        for (int i = 1; i <= 4; i++) {
            writeCSV(i);
        }
    }

    private void writeCSV(int sessionBlock) {
        String envPath = Environment.getExternalStorageDirectory().toString();
        String outputFile = envPath + "/HOT-T/trainingSummary" + sessionBlock + ".csv";

        if (mParent.mAnalysisTrainingDB != null || !mParent.mAnalysisTrainingDB.isOpen()) {
            mParent.mAnalysisTrainingDB = mParent.mAnalysisTrainingDBHelper.getWritableDatabase();
        }
        dataList = mParent.mAnalysisTrainingDBHelper.getDataList(mParent.mAnalysisTrainingDB, sessionBlock);

        mParent.writeCSVFile(outputFile, dataList);
    }

}
