package com.example.simpleteacher.asyncTask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.helper.userLastSessionDBHelper;
import com.example.simpleteacher.helper.userResultDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;
import com.example.simpleteacher.helper.wordDBHelper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LastSessionAnalysisTask extends AsyncTask <String, Void, String> {
    private ItemActivity mParent;
    private final String TAG = "LastSess";
    private String[] mIds;
    private List<String[]> dataList;
    private Context mContext;
    private List<Integer> categoryErrorData;
    private List<Integer> categoryCorrectData;

    public LastSessionAnalysisTask(Context parent, String[] ids) {
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
                categoryErrorData = new ArrayList<Integer>();
                categoryCorrectData = new ArrayList<Integer>();
                /* get database from training */
                dbName = "training_" + mIds[i] + "_16.db";
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
                                        + mParent.mWDBHelper.TABLE_DIAGNOSTIC
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
                                        + mParent.mWDBHelper.TABLE_DIAGNOSTIC
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

                getUserErrorRate(i);

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

                if (mParent.mLastSDB != null) {
                    if (mParent.mLastSDB.isOpen()) {
                        mParent.mLastSDB.close();
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

    public void getUserErrorRate(int userIndex) {
        int end = 0;
        /* replace the mLastSDB database */
        String outputDB = "LastS_" + mIds[userIndex] + ".db";
        mParent.mLastSDBHelper = new userLastSessionDBHelper(mParent, outputDB, 1);
        if (mParent.mLastSDB == null || !mParent.mLastSDB.isOpen()) {
            mParent.mLastSDB = mParent.mLastSDBHelper.getWritableDatabase();
        }

        for (int i = 3; i < 8; i++) {
            end = wordDBHelper.categorySum[i - 2] - wordDBHelper.categorySum[i - 3];
            for (int j = 1; j <= end; j++) {
                if (i >= 3 && i < 6) {
                    getErrorRate(i, j, userIndex);
                } else {
                    getErrorRate(i, j - 1, userIndex);
                }
            }
        }

        if (mParent.mLastSDB != null) {
            if (mParent.mLastSDB.isOpen()) {
                mParent.mLastSDB.close();
                mParent.mLastSDB = null;
            }
        }

        if (mParent.mLastSDBHelper != null) {
            mParent.mLastSDBHelper.close();
            mParent.mLastSDBHelper = null;
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

    public void getErrorRate(int main, int sub, int userIndex) {
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

        int mSessionNo = 16;
        int [] numbers = new int [] {mSessionNo, main, sub, errCategorized, totalErrCategorized};

        mParent.mLastSDBHelper.replaceSummaryData(mParent.mLastSDB,
                mParent.mLastSDBHelper.RESULT_DIAG_DETAIL, numbers, id);

    }

    private void extractSubCategory() {
        try {
            /* create output jpg file */
            String envPath = Environment.getExternalStorageDirectory().toString();
            String hottDiagPath = envPath + "/HOT-T/LastSession/";
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
        /* replace the mLastSDB database */
        String dbName = "LastS_" + userName + ".db";

        mParent.mLastSDBHelper = new userLastSessionDBHelper(mParent, dbName, 1);
        if (mParent.mLastSDB == null || !mParent.mLastSDB.isOpen()) {
            mParent.mLastSDB = mParent.mLastSDBHelper.getWritableDatabase();
        }

        Cursor c = mParent.mLastSDB.rawQuery("SELECT * FROM "
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

        if (mParent.mLastSDB != null) {
            if (mParent.mLastSDB.isOpen()) {
                mParent.mLastSDB.close();
                mParent.mLastSDB = null;
            }
        }

        if (mParent.mLastSDBHelper != null) {
            mParent.mLastSDBHelper.close();
            mParent.mLastSDBHelper = null;
        }

        return dataList;
    }
}
