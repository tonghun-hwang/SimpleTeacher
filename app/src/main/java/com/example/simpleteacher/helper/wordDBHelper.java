package com.example.simpleteacher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simpleteacher.ItemActivity;

import java.text.DateFormat;
import java.util.Date;

public class wordDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_DIAGNOSTIC = "table_diagnostic";
    public static final String TABLE_TRIAL = "table_trial";
    public static final String TABLE_TRAINING = "table_training";
    public static final String TABLE_EASY = "table_easy";
    public static final String MAP_ERROR = "map_error";

    public static final String ID = "ID";
    public static final String WORD = "WORD";
    public static final String PICTURE = "PICTURE";
    public static final String EXTENTION = "EXTENTION";
    public static final String SENSITIVITY = "SENSITIVITY";
    public static final String SENTENCE = "SENTENCE";
    public static final String TRAINING_MODE = "TRAINING_MODE";
    public static final String CATEGORY = "CATEGORY";
    public static final String RAW_CATEGORY = "RAW_CATEGORY";
    public static final String TTS = "TTS";
    public static final String DATE = "LAST_UPDATE";

    public static final String POSITION = "POSITION";
    public static final String LETTER = "LETTER";

    private final int NUM_CATEGORY_3 = 9;
    private final int NUM_CATEGORY_4 = 5;
    private final int NUM_CATEGORY_5 = 3;
    private final int NUM_CATEGORY_6 = 10;
    private final int NUM_CATEGORY_1 = 1;
    private final int NUM_CATEGORY_2 = 1;
    private final int NUM_CATEGORY_7 = 1;
    public static int[] categorySum;
    private String[] mError;
    private String[] mDiagnosedError;

    private static final String TAG = "Main.wordDBHelper";

    private ItemActivity mParent;
    private int mStartMode;

    public wordDBHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        mParent = (ItemActivity) context;
        mStartMode = 0;

        categorySum = new int[] {0, NUM_CATEGORY_3,
                NUM_CATEGORY_3 + NUM_CATEGORY_4,
                NUM_CATEGORY_3 + NUM_CATEGORY_4 + NUM_CATEGORY_5,
                NUM_CATEGORY_3 + NUM_CATEGORY_4 + NUM_CATEGORY_5 + NUM_CATEGORY_6,
                NUM_CATEGORY_3 + NUM_CATEGORY_4 + NUM_CATEGORY_5 + NUM_CATEGORY_6
                        + NUM_CATEGORY_1,
                NUM_CATEGORY_3 + NUM_CATEGORY_4 + NUM_CATEGORY_5 + NUM_CATEGORY_6
                        + NUM_CATEGORY_1 + NUM_CATEGORY_2,
                NUM_CATEGORY_3 + NUM_CATEGORY_4 + NUM_CATEGORY_5 + NUM_CATEGORY_6
                        + NUM_CATEGORY_1 + NUM_CATEGORY_2 + NUM_CATEGORY_7
        };
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createWordListTable(db, TABLE_TRIAL);
        createWordListTable(db, TABLE_DIAGNOSTIC);
        createWordListTable(db, TABLE_TRAINING);
        createWordListTable(db, TABLE_EASY);
        db.execSQL("DROP TABLE IF EXISTS " + MAP_ERROR);
        createErrorMap(db, MAP_ERROR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIAGNOSTIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EASY);
        db.execSQL("DROP TABLE IF EXISTS " + MAP_ERROR);
        onCreate(db);
    }

    public void createWordListTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "WORD TEXT," +
                "PICTURE TEXT," +
                "EXTENTION TEXT," +
                "SENSITIVITY TEXT," +
                "SENTENCE TEXT," +
                "TRAINING_MODE TEXT," +
                "CATEGORY INTEGER," +
                "RAW_CATEGORY TEXT," +
                "TTS TEXT," +
                "LAST_UPDATE DATETIME);");
    }

    public void createErrorMap(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "WORD TEXT," +
                "CATEGORY INTEGER," +
                "POSITION TEXT," +
                "LETTER TEXT," +
                "LAST_UPDATE DATETIME);");
    }

    public boolean updateData(SQLiteDatabase db, String tableName, ContentValues values, int itemId) {
        String where = String.valueOf(itemId + 1);
        Log.d(TAG, "updateData");
        Log.d(TAG, "updateData: " + values.toString() + " " + where);
        Date date = new Date();
        values.put(DATE, DateFormat.getDateTimeInstance().format(date));
        db.update(tableName, values, "ID = ?", new String[] {where});
        return true;
    }

    // TODO: erase id
    public boolean replaceData(SQLiteDatabase db, String tableName, String[] data, int id) {
        //Log.d(TAG, "replaceData (mode): " + mParent.mStartMode);
        String startMode = null;
        switch (mStartMode) {
            case ItemActivity.TRIAL:
                startMode = "Trial";
                break;
            case ItemActivity.DIAGNOSTIC:
                startMode = "Diagnostic";
                break;
            case ItemActivity.TRAINING:
                startMode = "Training";
                break;
            default:
                startMode = "unknown";
                break;
        }

        long res = -1;
        if (data.length > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id + 1);
            contentValues.put(WORD, data[0]);
            contentValues.put(PICTURE, data[1]);
            contentValues.put(EXTENTION, "png");
            contentValues.put(SENSITIVITY, data[3]);
            contentValues.put(SENTENCE, data[8]);
            contentValues.put(TRAINING_MODE, startMode);
            contentValues.put(CATEGORY, getCategoryCode(db, data[10]));
            contentValues.put(RAW_CATEGORY, data[10]);
            contentValues.put(TTS, data[2]);
            contentValues.put(DATE, "0");
            res = db.replace(tableName,
                    null, contentValues);
        }
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String[] getDiagnosedCategory(String currectWord, String writtenWord, String data, String result) {
        Log.d(TAG, "getSessionCategory(): " + data + ", " + result);

        //TODO: delete in the real test
        String Message = new String();
        Message += currectWord + " " + writtenWord + "\n";
        Message += result + "\n";
        Message += data + "\n";

        /* for multi bit */
        String rawErrors = new String();

        String temp = data.split("\\{\\(")[1];
        temp = temp.split("\\)\\}")[0];
        String[] tempArray = temp.split("\\),\\(");
        int bitSumTotal = 0;
        int bitSum = 0;
        int cntSum = 0;
        int multiBit = 0;

        boolean unCategorized = false;
        int cntUncategorized = 0;
        int cntRepeat = 0;

        String[] tempDiag = result.split(";");
        int errPlaceIndex = 0;

        int cntSumTotal = tempArray.length;
        String[] tempDiagError = null;
        String[] errInfo = null;

        try {
            if (!currectWord.equals(writtenWord)) {
                for (int i = 0; i < tempArray.length; i++) {
                    mError = tempArray[i].split(",");
                    if ((bitSumTotal & getCode(mError[2], mError[3])) > 0) {
                        multiBit = 1;
                    } else {
                        bitSumTotal |= getCode(mError[2], mError[3]);
                    }
                    for (int j = 0; j < tempDiag.length; j++) {
                        mDiagnosedError = tempDiag[j].split(",");
                        if (j + 1 < tempDiag.length) {
                            tempDiagError = tempDiag[j + 1].split(",");
                        }

                        /* check the types of error category */
                        /* ignore cases when checking category 3 - 5 */
                        if (Integer.valueOf(mError[2]) >= 3
                                && Integer.valueOf(mError[2]) < 6
                                && !(currectWord.equalsIgnoreCase(writtenWord))) {
                            /* find error category */
                            int diagErrIdx = Integer.valueOf(mDiagnosedError[1]);
                            int diagErrIdxEnd = diagErrIdx + mDiagnosedError[0].length();
                            int errIdx = Integer.valueOf(mError[1]);
                            int errIdxEnd = errIdx + mError[0].length();
                            errPlaceIndex = diagErrIdx - errIdx;
                            Log.d(TAG, "---" + diagErrIdx + " " + errIdx + " " + errIdxEnd);
                            if (mDiagnosedError[2].equals("-")) {

                                /* check the place of the error */
                                if ((errPlaceIndex >= 0
                                        && mError[0].toLowerCase().indexOf(mDiagnosedError[0].toLowerCase()) == errPlaceIndex)
                                        || (-errPlaceIndex >= 0
                                        && mDiagnosedError[0].toLowerCase().indexOf(mError[0].toLowerCase()) == -errPlaceIndex)
                                        || (errIdx >= diagErrIdx && errIdx < diagErrIdxEnd)
                                        || (diagErrIdx >= errIdx && diagErrIdx < errIdxEnd)) {

                                    /* check the case of the letter */
                                    if (tempDiagError != null) {
                                        if (!((errIdx == 1) && (Integer.valueOf(tempDiagError[1]) == 1)
                                                && (tempDiagError[0].length() == 1)
                                                && (mDiagnosedError[0].length() == 1)
                                                && (tempDiagError[0].substring(0, 1).toLowerCase()
                                                .equals(mError[0].substring(0, 1).toLowerCase())))) {

                                            if (!(tempDiagError[2].equals("+")
                                                    && mDiagnosedError[0].equalsIgnoreCase(tempDiagError[0]))) {
                                                Message += tempDiag[j] + ": " + "(" + tempArray[i] + ")\n";
                                                rawErrors += tempArray[i] + ";";
                                                bitSum |= getCode(mError[2], mError[3]);
                                                cntSum++;
                                                unCategorized = false;
                                                cntRepeat++;
                                                j++;
                                            }
                                        }
                                    } else {
                                        Message += tempDiag[j] + ": " + "(" + tempArray[i] + ")\n";
                                        rawErrors += tempArray[i] + ";";
                                        bitSum |= getCode(mError[2], mError[3]);
                                        cntSum++;
                                        unCategorized = false;
                                    }
                                }
                            } else if (mDiagnosedError[2].equals("+")
                                    && (diagErrIdx > errIdx && diagErrIdx < errIdxEnd)) {
                                Message += tempDiag[j] + ": " + "(" + tempArray[i] + ")\n";
                                rawErrors += tempArray[i] + ";";
                                bitSum |= getCode(mError[2], mError[3]);
                                cntSum++;
                                unCategorized = false;
                            }

                        } else if (Integer.valueOf(mError[2]) == 6
                                && mDiagnosedError[2].equals("+")) {
                            /* Special cases for capital letters for Category 6 */

                            /* only check uppercase or not */
                            if (mError[1].equals(mDiagnosedError[1])
                                    && !Character.isUpperCase(mDiagnosedError[0].toCharArray()[0])) {
                                Message += tempDiag[j] + ": " + "(" + tempArray[i] + ")\n";
                                rawErrors += tempArray[i] + ";";
                                bitSum |= getCode(mError[2], mError[3]);
                                cntSum++;
                                unCategorized = false;
                            } else {
                                unCategorized = true;
                            }
                        } else if (Integer.valueOf(mError[2]) == 7
                                && mDiagnosedError[2].equals("+")) {
                            /* Special cases for capital letters for Category 7 */

                            /* only check uppercase or not */
                            if (mError[1].equals(mDiagnosedError[1])
                                    && !Character.isLowerCase(mDiagnosedError[0].toCharArray()[0])) {
                                Message += tempDiag[j] + ": " + "(" + tempArray[i] + ")\n";
                                rawErrors += tempArray[i] + ";";
                                bitSum |= getCode(mError[2], mError[3]);
                                cntSum++;
                                unCategorized = false;
                            } else {
                                unCategorized = true;
                            }
                        } else {
                            unCategorized = true;
                        }
                        Log.d(TAG, "unCa--- re " + cntRepeat);
                    }

                    /* check uncategorized error */
                    if (unCategorized == true) {
                        cntUncategorized++;
                        unCategorized = false;
                    }
                    Log.d(TAG, "unCa--- " + cntUncategorized);
                }
                cntUncategorized = cntUncategorized - cntRepeat;
                //cntSum = getPlaceCount(bitSum);

                Log.d(TAG, "getSessionCategory() " + bitSum);
            } else {
                for (int i = 0; i < tempArray.length; i++) {
                    mError = tempArray[i].split(",");
                    bitSumTotal |= getCode(mError[2], mError[3]);
                }
            }

            errInfo = new String[] {currectWord, writtenWord,
                    String.valueOf(bitSumTotal), String.valueOf(bitSum),
                    String.valueOf(cntUncategorized),
                    String.valueOf(multiBit),
                    rawErrors};

        } catch(Exception e) {
            e.getStackTrace();
            errInfo = new String[] {currectWord, writtenWord,
                    String.valueOf(bitSumTotal), "0",
                    "0", "0"};
        } finally {
            Message += "err/total: " + cntSum + "/  " + cntSumTotal
                    + "\nunCa/mulB: " + cntUncategorized + "/  " + multiBit + "\n";
            Log.d(TAG, Message);
        }
        return errInfo;
    }

    public int getCategoryCode(SQLiteDatabase db, String data) {
        //Log.d(TAG, "getCategoryCode() " + data);
        String temp = data.split("\\{\\(")[1];
        temp = temp.split("\\)\\}")[0];
        String[] tempArray = temp.split("\\),\\(");
        int bitSum = 0;

        //TODO: set a ERROR MAP
        for (int i = 0; i < tempArray.length; i++) {
            mError = tempArray[i].split(",");

            bitSum += getCode(mError[2], mError[3]);

        /*
            ContentValues contentValues = new ContentValues();
            contentValues.put(WORD, data[0]);
            contentValues.put(CATEGORY, getCode(mError[2], mError[3]));
            contentValues.put(POSITION, mError[1]);
            contentValues.put(LETTER, mError[0]);
            contentValues.put(DATE, "0");
            long res = db.replace(MAP_ERROR,
                    null, contentValues);
                    */
        }

        //Log.d(TAG, "getCategoryCode() " + bitSum);
        return bitSum;
    }

    private int getCode(String main, String sub) {
        int catMain = Integer.valueOf(main);
        int catSub = Integer.valueOf(sub);
        int bitMove = 0;

        if (catMain >= 3 && catMain < 6) {
            // there is not sub-catetory 0
            bitMove = categorySum[catMain - 3] + catSub - 1;
        } else if (catMain >= 6 && catMain < 8){
            // there is sub-catetory 0
            bitMove = categorySum[catMain - 3] + catSub;
        }

        return (1 << bitMove);
    }

    public int getCode(int catMain, int catSub) {
        int bitMove = 0;

        if (catMain >= 3 && catMain < 6 && catSub >= 1) {
            // there is not sub-catetory 0
            bitMove = categorySum[catMain - 3] + catSub - 1;
        } else if (catMain >= 6 && catMain < 8 && catSub >= 0){
            // there is sub-catetory 0
            bitMove = categorySum[catMain - 3] + catSub;
        }

        return (1 << bitMove);
    }

    public int getCodeMain(int catMain) {
        int bitSum = 0;
        int start = categorySum[catMain - 3];
        int end = categorySum[catMain - 2];

        if (catMain >= 3 && catMain < 8) {
            for (int i = start; i < end; i++) {
                bitSum |= 1 << i;
            }
        }

        return bitSum;
    }

    public int getPlaceCount(int code) {
        int totalLength = categorySum[categorySum.length - 1] + NUM_CATEGORY_7;
        int count = 0;

        for (int i = 0; i < totalLength; i++) {
            if (((code >> i) & 1) == 1) {
                count ++;

            }
        }

        return count;
    }
}