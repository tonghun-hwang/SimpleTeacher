package com.example.simpleteacher.asyncTask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.helper.userResultDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fillDiagAnalysisTask extends AsyncTask <String, Void, String> {
    private final ItemActivity mParent;
    private final String TAG = "DiagAnal";
    private final String[] mIds;
    private List<String[]> dataList;
    private final Context mContext;

    public fillDiagAnalysisTask (Context parent, String[] ids) {
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

        for (int i = 1; i < mIds.length; i++) {
            try {
                /* result of words' error rate */
                resultName = "result_" + mIds[i] + ".db";
                mParent.resultDBHelper = new userResultDBHelper(mContext, resultName, 1);
                if (mParent.resultDB == null || !mParent.resultDB.isOpen()) {
                    Log.d(TAG, "readable database open: ");
                    mParent.resultDB = mParent.resultDBHelper.getReadableDatabase();
                }
                Cursor ct = mParent.resultDB.rawQuery("SELECT * FROM "
                                + userResultDBHelper.RESULT_DIAGNOSTIC
                        , null);
                int numWord = ct.getCount();
                ct.close();

                Cursor c = mParent.resultDB.rawQuery("SELECT * FROM "
                                + userResultDBHelper.RESULT_DIAGNOSTIC
                                + " WHERE CORRECT_WORD = WRITTEN_WORD"
                        , null);
                int correctW = c.getCount();
                data[0] = numWord;
                data[1] = numWord - correctW;
                data[2] = data[1] / data[0];
                c.close();

                /* result of buttons */
                dbName = "training_" + mIds[i] + "_0.db";
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

                data[3] = mParent.getNumEraseAll(mIds[i]);
                data[4] = mParent.getNumEraseOne(mIds[i]);
                data[5] = mParent.getNumButtonEar(mIds[i]);
                data[6] = mParent.getNumPushChar(mIds[i]);

                /* result of category */
                c = mParent.resultDB.rawQuery("SELECT * FROM "
                        + userResultDBHelper.RESULT_DIAG_MAIN, null);

                for (int j = 0; j < 5; j++) {
                    c.moveToPosition(j);
                    data[(j * 2) + 7] = c.getDouble(c.getColumnIndex("NUM_TOTAL")); // total words
                    data[(j * 2) + 8] = c.getDouble(c.getColumnIndex("ERROR_RATE")); //total wrong
                }
                String lastDate = c.getString(c.getColumnIndex("LAST_UPDATE"));
                c.close();

                /* replace the database */
                if (mParent.mAnalysisDB != null || !mParent.mAnalysisDB.isOpen()) {
                    mParent.mAnalysisDB = mParent.mAnalysisDBHelper.getWritableDatabase();
                }
                mParent.mAnalysisDBHelper.replaceData(mParent.mAnalysisDB, i, mIds[i], data, lastDate);

            } catch (Exception e) {
                String err = e.getStackTrace().toString();
                Log.d(TAG, "error: " + err);
                res = res + " " + i;
            } finally {
                if (mParent.resultDB != null) {
                    if (mParent.resultDB.isOpen()) {
                        mParent.resultDB.close();
                    }
                }
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
        String filePath = envPath + "/HOT-T/";
        String outputFile = filePath + "diagnosticSummary_lb.csv";

        if (mParent.mAnalysisDB != null || !mParent.mAnalysisDB.isOpen()) {
            mParent.mAnalysisDB = mParent.mAnalysisDBHelper.getWritableDatabase();
        }
        dataList = mParent.mAnalysisDBHelper.getDataList(mParent.mAnalysisDB);

        mParent.writeCSVFile(filePath, outputFile, dataList);
        extractSubCategory();
    }

    public void extractSubCategory() {
        try {
            /* create output jpg file */
            String envPath = Environment.getExternalStorageDirectory().toString();
            String hottDiagPath = envPath + "/HOT-T/diag/";
            File csvFolder = new File(hottDiagPath);
            if (!csvFolder.exists()) {
                boolean success = csvFolder.mkdir();
            }

            String folderName = mParent.teacherID;

            String folderPath = hottDiagPath + folderName + "/";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                boolean success = folder.mkdir();
            }

            for (int i = 0; i < mParent.mIDlength; i ++) {
                String fileName = mParent.mID[i] + ".csv";
                String filePath = folderPath + fileName;
                mParent.writeCSVFile(folderPath, filePath, getDataList(mParent.mID[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getDataList(String userName) {
        Log.d(TAG, "getDataList");
        String dbName = "result_" + userName + ".db";

        userResultDBHelper subResultDBHelper = new userResultDBHelper(mParent, dbName,1);
        SQLiteDatabase subResultDB = subResultDBHelper.getReadableDatabase();
        if (subResultDB == null || !subResultDB.isOpen()) {
            Log.d(TAG, "readable database open: ");
            subResultDB = subResultDBHelper.getReadableDatabase();
        }

        Cursor c = subResultDB.rawQuery("SELECT * FROM "
                + userResultDBHelper.RESULT_DIAG_DETAIL
                , null);

        if (c == null) {
            Log.d(TAG, "cursor is null "); // => cursor : c.moveToFirst() return false is because cursor is empty but not null (query was successful).
        }

        long res = -1;
        List<String[]> dataList = new ArrayList<String[]>();
        String[] columnName = new String[] {
                "ID",
                "SESSION",
                "CATEGORY_MAIN",
                "CATEGORY_SUB",
                "NUM_ERROR",
                "NUM_TOTAL",
                "ERROR_RATE",
                "LAST_UPDATE"
        };
        dataList.add(columnName);
        c.moveToFirst();
        if (c.getCount() == 0) {
            return dataList;
        }
        do {
            String[] data = new String[8];
            for (int i = 0; i < 8; i++) {
                data[i] = c.getString(i);
            }
            dataList.add(data);
        } while (c.moveToNext());
        c.close();

        if (subResultDB != null) {
            if(subResultDB.isOpen()) {
                subResultDB.close();
            }
        }

        return dataList;
    }
}
