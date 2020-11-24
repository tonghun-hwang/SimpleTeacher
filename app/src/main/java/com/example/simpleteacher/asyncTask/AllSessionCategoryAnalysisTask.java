package com.example.simpleteacher.asyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.helper.userCategorySessionDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;
import com.example.simpleteacher.helper.wordDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllSessionCategoryAnalysisTask extends AsyncTask <String, Void, String> {
    private ItemActivity mParent;
    private final String TAG = "AllSesCat";
    private final int SESSION_LENGTH = 16;
    private String[] mIds;
    private List<String[]> dataList;
    private Context mContext;
    private List<Integer> categoryErrorData;
    private List<Integer> categoryCorrectData;

    public AllSessionCategoryAnalysisTask(Context parent, String[] ids) {
        mParent = (ItemActivity) parent;
        mContext = parent;
        mIds = ids;
        dataList = new ArrayList<>();
        categoryErrorData = null;
        categoryCorrectData = null;
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
        String word;
        int letterIndex;

        /* get database from word */
        if (mParent.mWDB == null || !mParent.mWDB.isOpen()) {
            mParent.mWDB = mParent.mWDBHelper.getWritableDatabase();
        }

        for (int i = 0; i < mIds.length; i++) {
            try {
                for (int sNum = 1; sNum < SESSION_LENGTH; sNum++) {
                    categoryErrorData = new ArrayList<Integer>();
                    categoryCorrectData = new ArrayList<Integer>();
                    /* get database from training */
                    dbName = "training_" + mIds[i] + "_" + sNum + ".db";
                    mParent.mUserTrainingDBHelper = new userTrainingDBHelper(mParent, dbName, mIds[i], 1);
                    mParent.mUserTrainingDB = mParent.mUserTrainingDBHelper.getWritableDatabase();

                    /* get the number of mistakes from training database */
                    Cursor ct = mParent.mUserTrainingDB.rawQuery("SELECT * FROM "
                            + mIds[i]
                            + " WHERE EVENT = '" + mParent.mData.TRAIN_WRONG + "'"
                            + " AND (REPEAT_COUNT == 1)", null);
                    if (ct.getCount() != 0) {
                        ct.moveToFirst();
                        do {
                            word = ct.getString(ct.getColumnIndex("CORRECT_WORD"));
                            letterIndex = ct.getInt(ct.getColumnIndex("LETTER_INDEX"));

                            /* get the selected word in word database */
                            Cursor cw = mParent.mWDB.rawQuery("SELECT * FROM "
                                            + mParent.mWDBHelper.TABLE_TRAINING
                                            + " WHERE WORD = '" + word + "'"
                                    , null);
                            if (cw != null) {
                                if (cw.getCount() != 0) {
                                    cw.moveToFirst();
                                    do {
                                        /* get error category information */
                                        String category = cw.getString(cw.getColumnIndex("RAW_CATEGORY"));
                                        String tempAll = category.split("\\{\\(")[1];
                                        tempAll = tempAll.split("\\)\\}")[0];
                                        String[] temp = tempAll.split("\\),\\(");
                                        Integer[] cat = new Integer[2];
                                        int index;

                                        for (int j = 0; j < temp.length; j++) {
                                            index = Integer.valueOf(temp[j].split(",")[1]);
                                            if ((letterIndex + 1) == index) {
                                                cat[0] = Integer.valueOf(temp[j].split(",")[2]);
                                                cat[1] = Integer.valueOf(temp[j].split(",")[3]);
                                                categoryErrorData.add(mParent.mWDBHelper.getCode(cat[0], cat[1]));
                                            } else if ((letterIndex + 1) > index) {
                                                cat[0] = Integer.valueOf(temp[j].split(",")[2]);
                                                cat[1] = Integer.valueOf(temp[j].split(",")[3]);
                                                categoryCorrectData.add(mParent.mWDBHelper.getCode(cat[0], cat[1]));
                                            }
                                        }

                                    } while (cw.moveToNext());
                                }
                            }
                            cw.close();

                        } while (ct.moveToNext());
                    }

                    ct.close();

                    /* get the number of correct words from training database */
                    ct = mParent.mUserTrainingDB.rawQuery("SELECT * FROM "
                            + mIds[i]
                            + " WHERE EVENT = '" + mParent.mData.TRAIN_INC + "'"
                            + " AND (REPEAT_COUNT == 1)", null);
                    if (ct.getCount() != 0) {
                        ct.moveToFirst();
                        do {
                            word = ct.getString(ct.getColumnIndex("CORRECT_WORD"));

                            /* get the selected word in word database */
                            Cursor cw = mParent.mWDB.rawQuery("SELECT * FROM "
                                            + mParent.mWDBHelper.TABLE_TRAINING
                                            + " WHERE WORD = '" + word + "'"
                                    , null);
                            if (cw != null) {
                                if (cw.getCount() != 0) {
                                    cw.moveToFirst();
                                    do {
                                        /* get error category information */
                                        String category = cw.getString(cw.getColumnIndex("RAW_CATEGORY"));
                                        String tempAll = category.split("\\{\\(")[1];
                                        tempAll = tempAll.split("\\)\\}")[0];
                                        String[] temp = tempAll.split("\\),\\(");
                                        Integer[] cat = new Integer[2];
                                        int index;

                                        for (int j = 0; j < temp.length; j++) {
                                            cat[0] = Integer.valueOf(temp[j].split(",")[2]);
                                            cat[1] = Integer.valueOf(temp[j].split(",")[3]);
                                            categoryCorrectData.add(mParent.mWDBHelper.getCode(cat[0], cat[1]));
                                        }

                                    } while (cw.moveToNext());
                                }
                            }
                            cw.close();

                        } while (ct.moveToNext());
                        ct.close();
                    }

                    if (mParent.mUserTrainingDB != null) {
                        if (mParent.mUserTrainingDB.isOpen()) {
                            mParent.mUserTrainingDB.close();
                            mParent.mUserTrainingDB = null;
                        }
                    }
                    if (mParent.mUserTrainingDBHelper != null) {
                        mParent.mUserTrainingDBHelper.close();
                        mParent.mUserTrainingDBHelper = null;
                    }

                    getUserErrorRate(i, sNum);

                }

            } catch (Exception e) {
                String err = e.getStackTrace().toString();
                Log.d(TAG, "error: " + err);
                res = res + " " + i;
            } finally {
                if (mParent.mUserTrainingDB != null) {
                    if (mParent.mUserTrainingDB.isOpen()) {
                        mParent.mUserTrainingDB.close();
                        mParent.mUserTrainingDB = null;
                    }
                }

                if (mParent.mUserTrainingDBHelper != null) {
                    mParent.mUserTrainingDBHelper.close();
                    mParent.mUserTrainingDBHelper = null;
                }

                if (mParent.mCSDB != null) {
                    if (mParent.mCSDB.isOpen()) {
                        mParent.mCSDB.close();
                    }
                }
            }
        }
        if (mParent.mWDB != null) {
            if (mParent.mWDB.isOpen()) {
                mParent.mWDB.close();
            }
        }
        extractSubCategory();
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute(): " + result);
    }

    public void getUserErrorRate(int userIndex, int sessionNo) {
        int end = 0;
        /* replace the mCSDB database */
        String outputDB = "S" + sessionNo + "_" + mIds[userIndex] + ".db";
        mParent.mCSDBHelper = new userCategorySessionDBHelper(mParent, outputDB, 1);
        if (mParent.mCSDB == null || !mParent.mCSDB.isOpen()) {
            mParent.mCSDB = mParent.mCSDBHelper.getWritableDatabase();
        }

        for (int i = 3; i < 8; i++) {
            end = wordDBHelper.categorySum[i - 2] - wordDBHelper.categorySum[i - 3];
            for (int j = 1; j <= end; j++) {
                if (i >= 3 && i < 6) {
                    getErrorRate(i, j, userIndex, sessionNo);
                } else {
                    getErrorRate(i, j - 1, userIndex, sessionNo);
                }
            }
        }

        if (mParent.mCSDB != null) {
            if (mParent.mCSDB.isOpen()) {
                mParent.mCSDB.close();
                mParent.mCSDB = null;
            }
        }

        if (mParent.mCSDBHelper != null) {
            mParent.mCSDBHelper.close();
            mParent.mCSDBHelper = null;
        }
    }

    public int getCategory(int userIndex) {
        if (mParent.mUserDB == null || !mParent.mUserDB.isOpen()) {
            mParent.mUserDB = mParent.mUserDBHelper.getReadableDatabase();
        }
        Cursor c = mParent.mUserDB.rawQuery("SELECT * FROM "
                + mParent.mUserDBHelper.TABLE_NAME
                + " WHERE USERID = '" + mIds[userIndex] + "'", null);
        int category = 0;
        if (c.getCount() == 1) {
            c.moveToFirst();
            category = c.getInt(c.getColumnIndex("CATEGORY"));
        }
        return category;
    }

    public void getErrorRate(int main, int sub, int userIndex, int sessionNo) {
        /* get id for database */
        int id = 32;
        if (main >= 3 && main < 6 && sub >= 1) {
            // there is not sub-catetory 0
            id = wordDBHelper.categorySum[main - 3] + sub - 1;
        } else if (main >= 6 && main < 8 && sub >= 0){
            // there is sub-catetory 0
            id = wordDBHelper.categorySum[main - 3] + sub;
        }

        /* get category */
        int category = getCategory(userIndex);

        /* get number of Categorized */
        int errCategorized = 0;
        for (int i = 0; i < categoryErrorData.size(); i++) {
            if ((categoryErrorData.get(i)
                    & mParent.mWDBHelper.getCode(main, sub)
                    & category) != 0) {
                errCategorized++;
            }
        }

        /* get number of totalErrCategorized */
        int totalErrCategorized = errCategorized;
        for (int i = 0; i < categoryCorrectData.size(); i++) {
            if ((categoryCorrectData.get(i)
                    & mParent.mWDBHelper.getCode(main, sub)
                    & category) != 0) {
                totalErrCategorized++;
            }
        }

        int [] numbers = new int [] {sessionNo, main, sub, errCategorized, totalErrCategorized};

        mParent.mCSDBHelper.replaceSummaryData(mParent.mCSDB,
                mParent.mCSDBHelper.RESULT_TRAIN_DETAIL, numbers, id);

    }

    private void extractSubCategory() {
        try {
            /* create output jpg file */
            String envPath = Environment.getExternalStorageDirectory().toString();
            String hottDiagPath = envPath + "/HOT-T/AllSession/";
            File csvFolder = new File(hottDiagPath);
            if (!csvFolder.exists()) {
                boolean success = csvFolder.mkdir();
            }

            String folderPath = hottDiagPath + "admin/";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                boolean success = folder.mkdir();
            }

            for (int i = 0; i < mParent.mIDlength; i ++) {
                String folderUserPath = folderPath + mParent.mID[i] + "/";
                File userFolder = new File(folderUserPath);
                if (!userFolder.exists()) {
                    boolean success = userFolder.mkdir();
                }

                for (int sNum = 1; sNum < SESSION_LENGTH; sNum++) {
                    String fileName = mParent.mID[i] + "_s" + sNum + ".csv";
                    String filePath = folderUserPath + fileName;
                    mParent.writeCSVFile(folderPath, filePath, getDataList(mParent.mID[i], sNum));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getDataList(String userName, int sessionNo) {
        Log.d(TAG, "getDataList");
        /* replace the mCSDB database */
        String dbName = "S" + sessionNo + "_" + userName + ".db";

        mParent.mCSDBHelper = new userCategorySessionDBHelper(mParent, dbName, 1);
        if (mParent.mCSDB == null || !mParent.mCSDB.isOpen()) {
            mParent.mCSDB = mParent.mCSDBHelper.getWritableDatabase();
        }

        Cursor c = mParent.mCSDB.rawQuery("SELECT * FROM "
                + userCategorySessionDBHelper.RESULT_TRAIN_DETAIL
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

        if (mParent.mCSDB != null) {
            if (mParent.mCSDB.isOpen()) {
                mParent.mCSDB.close();
                mParent.mCSDB = null;
            }
        }

        if (mParent.mCSDBHelper != null) {
            mParent.mCSDBHelper.close();
            mParent.mCSDBHelper = null;
        }

        return dataList;
    }
}
