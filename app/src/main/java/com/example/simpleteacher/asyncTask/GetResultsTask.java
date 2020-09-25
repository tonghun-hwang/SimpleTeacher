package com.example.simpleteacher.asyncTask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.helper.DiffAlgorithm;
import com.example.simpleteacher.helper.userLastSessionDBHelper;
import com.example.simpleteacher.helper.userResultDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;
import com.example.simpleteacher.helper.wordDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GetResultsTask extends AsyncTask <String, Void, String> {
    private ItemActivity mParent;
    private final String TAG = "Result";
    private String[] mIds;
    private List<String[]> dataList;
    private Context mContext;
    private List<Integer> categoryErrorData;
    private List<Integer> categoryCorrectData;
    private SQLiteDatabase resultDB;
    private userResultDBHelper resultDBHelper;

    public GetResultsTask(Context parent, String[] ids) {
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
        String wordCorrect;
        String wordWritten;
        int letterIndex;

        /* get database from word */
        if (mParent.mWDB == null || !mParent.mWDB.isOpen()) {
            mParent.mWDB = mParent.mWDBHelper.getWritableDatabase();
        }

        int i = (int) mParent.currentItemId;
        try {
            categoryErrorData = new ArrayList<Integer>();
            categoryCorrectData = new ArrayList<Integer>();
            /* get database from training */
            dbName = "training_" + mIds[i] + "_0.db";
            Log.d(TAG, dbName);
            mParent.mUserTrainingDBHelper = new userTrainingDBHelper(mParent, dbName, mIds[i], 1);
            mParent.mUserTrainingDB = mParent.mUserTrainingDBHelper.getWritableDatabase();

            /* get the number of mistakes from training database */
            Cursor ct = mParent.mUserTrainingDB.rawQuery("SELECT * FROM "
                    + mIds[i]
                    + " WHERE EVENT = '" + mParent.mData.TRAIN_ALL + "'"
                    , null);
            if (ct.getCount() != 0) {
                ct.moveToFirst();
                do {
                    wordCorrect = ct.getString(ct.getColumnIndex("CORRECT_WORD"));
                    wordWritten = ct.getString(ct.getColumnIndex("PARAM1"));

                    /* get diff of the two words */
                    getErrorCategories(wordCorrect, wordWritten);

                } while (ct.moveToNext());
            }

            ct.close();

//            /* get the number of correct words from training database */
//            ct = mParent.mUserTrainingDB.rawQuery("SELECT * FROM "
//                    + mIds[i]
//                    + " WHERE EVENT = '" + mParent.mData.TRAIN_INC + "'"
//                    + " AND (REPEAT_COUNT == 1)", null);
//            if (ct.getCount() != 0) {
//                ct.moveToFirst();
//                do {
//                    wordCorrect = ct.getString(ct.getColumnIndex("CORRECT_WORD"));
//
//                    /* get the selected word in word database */
//                    Cursor cw = mParent.mWDB.rawQuery("SELECT * FROM "
//                                    + mParent.mWDBHelper.TABLE_DIAGNOSTIC
//                                    + " WHERE WORD = '" + wordCorrect + "'"
//                            , null);
//                    if (cw != null) {
//                        if (cw.getCount() != 0) {
//                            cw.moveToFirst();
//                            do {
//                                /* get error category information */
//                                String category = cw.getString(cw.getColumnIndex("RAW_CATEGORY"));
//                                String tempAll = category.split("\\{\\(")[1];
//                                tempAll = tempAll.split("\\)\\}")[0];
//                                String[] temp = tempAll.split("\\),\\(");
//                                Integer[] cat = new Integer[2];
//                                int index;
//
//                                for (int j = 0; j < temp.length; j++) {
//                                    cat[0] = Integer.valueOf(temp[j].split(",")[2]);
//                                    cat[1] = Integer.valueOf(temp[j].split(",")[3]);
//                                    categoryCorrectData.add(mParent.mWDBHelper.getCode(cat[0], cat[1]));
//                                }
//
//                            } while (cw.moveToNext());
//                        }
//                    }
//                    cw.close();
//
//                } while (ct.moveToNext());
//                ct.close();
//            }

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
        String outputDB = "result1_" + mIds[userIndex] + ".db";
        resultDBHelper = new userResultDBHelper(mParent, outputDB,1);
        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getWritableDatabase();
        }

        for (int i = 3; i < 8; i++) {
            getErrorRateMain(i, userIndex);
            end = wordDBHelper.categorySum[i - 2] - wordDBHelper.categorySum[i - 3];
            for (int j = 1; j <= end; j++) {
                if (i >= 3 && i < 6) {
                    getErrorRate(i, j, userIndex);
                } else {
                    getErrorRate(i, j - 1, userIndex);
                }
            }
        }

        if (resultDB != null) {
            if (resultDB.isOpen()) {
                resultDB.close();
                resultDB = null;
            }
        }

        if (resultDBHelper != null) {
            resultDBHelper.close();
            resultDBHelper = null;
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

    public String[] getErrorCategories (String correctWord, String writtenWord) {
        Log.d(TAG, "getErrorCategorie(): ");
        //if (mCursor == null) {
        //    return null;
        //}

        if (mParent.mWDB == null || !mParent.mWDB.isOpen()) {
            mParent.mWDB = mParent.mWDBHelper.getReadableDatabase();
        }
        Cursor tmpCur = mParent.mWDB.rawQuery("SELECT * FROM "
                + wordDBHelper.TABLE_DIAGNOSTIC + " WHERE WORD = '" + correctWord + "'", null);

        if (tmpCur.getCount() == 0) {
            return null;
        }

        tmpCur.moveToFirst();
        int corWordIndex = tmpCur.getInt(tmpCur.getColumnIndex("ID")) - 1;
        String rawCategory = tmpCur.getString(tmpCur.getColumnIndex("RAW_CATEGORY"));
        tmpCur.close();
        if (mParent.mWDB.isOpen()) {
            mParent.mWDB.close();
        }

        LinkedList<DiffAlgorithm.Diff> diffs = new LinkedList<>();
        DiffAlgorithm diffWord = new DiffAlgorithm();

        /* ignore the upper or lower case except the first letter. */
        writtenWord = writtenWord.substring(0, 1) + writtenWord.substring(1).toLowerCase();
        diffs = diffWord.diff_main(correctWord, writtenWord, true);

        int length = 0;
        int templength = 0;
        String result = new String();
        if (correctWord.equals(writtenWord)) {
            result = "Currect";

        } else {
            for (int i = 0; i < diffs.size(); i++) {
                if (diffs.get(i).operation == DiffAlgorithm.Operation.EQUAL) {
                    String temp = diffs.get(i).text + ",=" + String.valueOf(correctWord.indexOf(diffs.get(i).text, length) + 1);
                    length += (diffs.get(i).text.length());
                    templength = length;
                } else if (diffs.get(i).operation == DiffAlgorithm.Operation.DELETE) {
                    result += diffs.get(i).text + "," + String.valueOf(length + 1) + ",-;";
                    length += (diffs.get(i).text.length());
                } else if (diffs.get(i).operation == DiffAlgorithm.Operation.INSERT) {
                    result += diffs.get(i).text + "," + String.valueOf(templength + 1) + ",+;";
                }
            }
        }

        if (mParent.mWDB == null || !mParent.mWDB.isOpen()) {
            mParent.mWDB = mParent.mWDBHelper.getReadableDatabase();
        }
        String[] diagnosedResult  = mParent.mWDBHelper.getDiagnosedCategory(correctWord, writtenWord, rawCategory, result);

        String outputDB = "result1_" + mIds[(int) mParent.currentItemId] + ".db";
        resultDBHelper = new userResultDBHelper(mParent, outputDB,1);
        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getWritableDatabase();
        }
        boolean res = resultDBHelper.replaceData(resultDB, diagnosedResult, corWordIndex);
        if (res == false) {
            Log.w(TAG, "db word: " + diagnosedResult[0]);
        }
        if (resultDB.isOpen()) {
            resultDB.close();
        }
        if (resultDBHelper != null) {
            resultDBHelper.close();
            resultDBHelper = null;
        }

        if (mParent.mWDB.isOpen()) {
            mParent.mWDB.close();
        }
        return diagnosedResult;
    }

    public double getErrorRateMain (int main, int userIndex) {

        int errCategorized = 0;
        int totalErrCategorized = 0;

        int end = wordDBHelper.categorySum[main - 2] - wordDBHelper.categorySum[main - 3];
        for (int i = 1; i <= end; i++) {
            if (main >= 3 && main < 6) {
                errCategorized += getErrorNumber(main, i, userIndex);
                totalErrCategorized += getErrorTotal(main, i, userIndex);
            } else {
                errCategorized += getErrorNumber(main, i - 1, userIndex);
                totalErrCategorized += getErrorTotal(main, i - 1, userIndex);
            }
        }

        double errorRate = (double) errCategorized / totalErrCategorized;

        /* save in database */
        int id = wordDBHelper.categorySum[main - 3] + 32;
        int mSessionNo = 0;
        int [] numbers = new int [] {mSessionNo, main, -1, errCategorized, totalErrCategorized};
        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getWritableDatabase();
        }
        boolean res = resultDBHelper.replaceSummaryData(resultDB,
                userResultDBHelper.RESULT_DIAG_MAIN,numbers, id);
        if (resultDB.isOpen()) {
            resultDB.close();
        }

        return errorRate;
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
        int errCategorized = getErrorNumber(main, sub, userIndex);

        /* get number of totalErrCategorized */
        int totalErrCategorized = getErrorTotal(main, sub, userIndex);

        int mSessionNo = 0;
        int [] numbers = new int [] {mSessionNo, main, sub, errCategorized, totalErrCategorized};

        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getWritableDatabase();
        }
        resultDBHelper.replaceSummaryData(resultDB,
                resultDBHelper.RESULT_DIAG_DETAIL, numbers, id);
        if (resultDB.isOpen()) {
            resultDB.close();
        }

    }

    public int getErrorNumber (int main, int sub, int userIndex) {
        int errCategorized = 0;
        int userCategory = getCategory(userIndex);
        int errorCode = mParent.mWDBHelper.getCode(main, sub) & userCategory;

        /* Number of categorized error */
        /* without multi bit */
        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getWritableDatabase();
        }
        Cursor userCursorSingle = resultDB.rawQuery("SELECT * FROM "
                        + userResultDBHelper.RESULT_DIAGNOSTIC
                        + " WHERE (CATEGORY_ERROR & CAST(" + errorCode + " AS INTEGER) > 0) "
                        + "AND (MULTI_BIT == 0)"
                , null);
        errCategorized = userCursorSingle.getCount();
        userCursorSingle.close();

        /* with multi bit */
        Cursor userCursorMulti = resultDB.rawQuery("SELECT * FROM "
                        + userResultDBHelper.RESULT_DIAGNOSTIC
                        + " WHERE (CATEGORY_TOTAL & CAST(" + errorCode + " AS INTEGER) > 0) "
                        + "AND (MULTI_BIT == 1)"
                , null);

        /* get Error num */
        String[] tmp;
        String result;
        int tmpCatMain;
        int tmpCatSub;
        String currentWord;
        Cursor tmpCur;
        if (userCursorMulti.getCount() > 0) {
            for (int i = 0; i < userCursorMulti.getCount(); i++) {
                userCursorMulti.moveToPosition(i);
                /* get diagnosed error category from database for the result of each child */
                result = userCursorMulti.getString(userCursorMulti.getColumnIndex("CATEGORY_ERROR_RAW"));
                tmp = result.split(";");
                /* search matched with a specific category */
                for (int k = 0; k < tmp.length; k++) {
                    try {
                        if (tmp[k] != "") {
                            tmpCatMain = Integer.valueOf(tmp[k].split(",")[2]);
                            tmpCatSub = Integer.valueOf(tmp[k].split(",")[3]);
                            if (tmpCatMain == main) {
                                if (tmpCatSub == sub) {
                                    errCategorized++;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                        Log.e(TAG, "Exception: get error categories.");
                    }
                }
            }
        }
        userCursorMulti.close();
        if (resultDB.isOpen()) {
            resultDB.close();
        }
        return errCategorized;
    }

    public int getErrorTotal (int main, int sub, int userIndex) {
        int totalErrCategorized = 0;
        int userCategory = getCategory(userIndex);
        int errorCode = mParent.mWDBHelper.getCode(main, sub) & userCategory;

        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getReadableDatabase();
        }
        Cursor userCursorSingle = resultDB.rawQuery("SELECT * FROM "
                        + userResultDBHelper.RESULT_DIAGNOSTIC
                        + " WHERE (CATEGORY_TOTAL & CAST(" + errorCode + " AS INTEGER) > 0) "
                        + "AND (MULTI_BIT == 0)"
                , null);
        totalErrCategorized = userCursorSingle.getCount();
        userCursorSingle.close();


        /* with multi bit */
        Cursor userCursorMulti = resultDB.rawQuery("SELECT * FROM "
                        + userResultDBHelper.RESULT_DIAGNOSTIC
                        + " WHERE (CATEGORY_TOTAL & CAST(" + errorCode + " AS INTEGER) > 0) "
                        + "AND (MULTI_BIT == 1)"
                , null);

        /* get Error num */
        int tmpCatMain;
        int tmpCatSub;
        String currentWord;
        Cursor tmpCur;
        if (mParent.mWDB == null || !mParent.mWDB.isOpen()) {
            mParent.mWDB = mParent.mWDBHelper.getReadableDatabase();
        }
        if (userCursorMulti.getCount() > 0) {
            for (int i = 0; i < userCursorMulti.getCount(); i++) {
                userCursorMulti.moveToPosition(i);

                /* get base error category from database for the total categorized places */
                currentWord = userCursorMulti.getString(userCursorMulti.getColumnIndex("CORRECT_WORD"));
                tmpCur = mParent.mWDB.rawQuery("SELECT * FROM "
                                + wordDBHelper.TABLE_DIAGNOSTIC
                                + " WHERE WORD = '" + currentWord + "'",
                        null);
                String tmpCatAll;
                String[] tmpCatAllArray;
                if (tmpCur.getCount() > 0) {
                    tmpCur.moveToFirst();
                    tmpCatAll = tmpCur.getString(tmpCur.getColumnIndex("RAW_CATEGORY"));
                    String temp = tmpCatAll.split("\\{\\(")[1];
                    temp = temp.split("\\)\\}")[0];
                    tmpCatAllArray = temp.split("\\),\\(");
                    /* search matched with a specific category */
                    for (int k = 0; k < tmpCatAllArray.length; k++) {
                        tmpCatMain = Integer.valueOf(tmpCatAllArray[k].split(",")[2]);
                        tmpCatSub = Integer.valueOf(tmpCatAllArray[k].split(",")[3]);
                        if (tmpCatMain == main) {
                            if (tmpCatSub == sub) {
                                totalErrCategorized++;
                            }
                        }
                    }
                    tmpCur.close();
                }
            }
        }
        userCursorMulti.close();
        if (mParent.mWDB.isOpen()) {
            mParent.mWDB.close();
        }
        if (resultDB.isOpen()) {
            resultDB.close();
        }
        return totalErrCategorized;
    }

    private void extractSubCategory() {
        try {
            /* create output jpg file */
            String envPath = Environment.getExternalStorageDirectory().toString();
            String hottDiagPath = envPath + "/HOT-T/Diag/";
            File csvFolder = new File(hottDiagPath);
            if (!csvFolder.exists()) {
                boolean success = csvFolder.mkdir();
            }

            String folderPath = hottDiagPath + "admin/";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                boolean success = folder.mkdir();
            }

            int i = (int) mParent.currentItemId;
            String fileName = mParent.mID[i] + ".csv";
            String filePath = folderPath + fileName;
            mParent.writeCSVFile(folderPath, filePath, getDataList(mParent.mID[i]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getDataList(String userName) {
        Log.d(TAG, "getDataList");
        /* replace the mLastSDB database */
        String dbName = "result1_" + userName + ".db";

        resultDBHelper = new userResultDBHelper(mParent, dbName, 1);
        if (resultDB == null || !resultDB.isOpen()) {
            resultDB = resultDBHelper.getWritableDatabase();
        }

        Cursor c = resultDB.rawQuery("SELECT * FROM "
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

        if (resultDB != null) {
            if (resultDB.isOpen()) {
                resultDB.close();
                resultDB = null;
            }
        }

        if (resultDBHelper != null) {
            resultDBHelper.close();
            resultDBHelper = null;
        }

        return dataList;
    }
}
