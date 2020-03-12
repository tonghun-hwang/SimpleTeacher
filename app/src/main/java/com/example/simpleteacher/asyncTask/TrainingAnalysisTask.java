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
        double[] data = new double[17];

        for (int i = 0; i < mIds.length; i++) {
            try {
                for (int j = 0; j < 16; j++) {
                    /* result of buttons */
                    dbName = "training_" + mIds[i] + "_" + j + ".db";
                    mParent.mUserTrainingDBHelper = new userTrainingDBHelper(mContext, dbName, mIds[i], 1);
                    mParent.mUserTrainingDB = mParent.mUserTrainingDBHelper.getWritableDatabase();

                    total = mParent.getNumTotalWords(mIds[i]);
                    if (total == 0) {
                        mParent.mUserTrainingDB.close();
                        mParent.mUserTrainingDBHelper.close();

                        dbName = "training_" + mIds[i] + "_0ps.db";
                        mParent.mUserTrainingDBHelper = new userTrainingDBHelper(mContext, dbName, mIds[i], 1);
                        mParent.mUserTrainingDB = mParent.mUserTrainingDBHelper.getWritableDatabase();
                    }

                    data[0] = mParent.getNumTotalWords(mIds[i]); // all words
                    data[1] = mParent.getNumTotallyWrongWordsInCategory(mIds[i]); // wrong words
                    data[2] = mParent.getNumTotallyWrongWords(mIds[i]);
                    data[3] = mParent.getNumEraseAll(mIds[i]); // cat wrong words
                    data[4] = mParent.getNumEraseOne(mIds[i]); // cat wrong words
                    data[5] = mParent.getNumButtonEar(mIds[i]); // cat wrong words
                    data[6] = 0;
                }

                /* replace the database */
                if (mParent.mAnalysisDB != null || !mParent.mAnalysisDB.isOpen()) {
                    mParent.mAnalysisDB = mParent.mAnalysisDBHelper.getWritableDatabase();
                }
                mParent.mAnalysisDBHelper.replaceData(mParent.mAnalysisDB, i, mIds[i], data);

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
                if (mParent.mAnalysisDB != null) {
                    if (mParent.mAnalysisDB.isOpen()) {
                        mParent.mAnalysisDB.close();
                    }
                }
            }
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute(): " + result);
        String envPath = Environment.getExternalStorageDirectory().toString();
        String outputFile = envPath + "/HOT-T/diagnosticSummary.csv";

        if (mParent.mAnalysisDB != null || !mParent.mAnalysisDB.isOpen()) {
            mParent.mAnalysisDB = mParent.mAnalysisDBHelper.getWritableDatabase();
        }
        dataList = mParent.mAnalysisDBHelper.getDataList(mParent.mAnalysisDB);

        mParent.writeCSVFile(outputFile, dataList);
    }
}
