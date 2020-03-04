package com.example.simpleteacher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.simpleteacher.asyncTask.ReadInfoURLTask;
import com.example.simpleteacher.asyncTask.ReadResultURLTask;
import com.example.simpleteacher.asyncTask.ReadTrainingURLTask;
import com.example.simpleteacher.asyncTask.initFirstWordListsTask;
import com.example.simpleteacher.helper.userDBHelper;
import com.example.simpleteacher.helper.userResultDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;
import com.example.simpleteacher.helper.wordDBHelper;
import com.example.simpleteacher.main.FragmentLastGraph;
import com.example.simpleteacher.main.FragmentOneFive;
import com.example.simpleteacher.main.FragmentSync;
import com.example.simpleteacher.main.MainFragment;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    private final String[] admin = {"Admin1","Ufo1", "Ufo2", "Ufo3", "Ufo4", "Ufo5","Ufo6", "Ufo7", "Ufo8",
            "Ufo9", "Ufo10", "Ufo11", "Ufo12","Komet1", "Komet2", "Komet3", "Komet4", "Komet5", "Komet6", "Komet7",
            "Planet1", "Planet2", "Planet3", "Planet4","Planet5", "Planet6", "Planet7", "Planet8", "Planet9",
            "Planet10", "Planet11", "Planet12", "Planet13", "Planet14", "Planet15","Planet16","Planet17","Planet18",
            "Planet19", "Planet20", "Planet21", "Planet22", "Planet23", "Planet24", "Planet25", "Planet26", "Planet27", "Planet28", "Planet29",
            "Alien1","Alien2","Alien3","Alien4","Alien5","Alien6","Alien7","Alien8","Alien9","Alien10","Alien11",
            "Alien12","Alien13","Alien14","Alien15","Alien16","Alien17","Alien18","Alien14","Alien15","Alien16","Alien17","Alien18",
            "Alien19","Alien20","Alien21","Alien22"};
    private final String[] ADM1 = {"Ufo1", "Ufo2", "Ufo3", "Ufo4", "Ufo5"};
    private final String[] AUN1 = {"Ufo6", "Ufo7", "Ufo8"};
    private final String[] ASB1 = {"Ufo9", "Ufo10", "Ufo11", "Ufo12"};
    private final String[] BSR1 = {"Komet1", "Komet2", "Komet3"};
    private final String[] BBH1 = {"Komet4", "Komet5", "Komet6", "Komet7"};
    private final String[] ASE2 = {"Planet1", "Planet2", "Planet3", "Planet4"};
    private final String[] ABR2 = {"Planet5", "Planet6", "Planet7", "Planet8", "Planet9"};
    private final String[] AFT2 = {"Planet10", "Planet11", "Planet12", "Planet13", "Planet14",
            "Planet15","Planet16","Planet17","Planet18"};
    private final String[] ABT2 = {"Planet19", "Planet20", "Planet21", "Planet22", "Planet23", "Planet24"};
    private final String[] ASH2 = {"Planet25", "Planet26", "Planet27", "Planet28", "Planet29"};
    private final String[] BLR2 = {"Alien1","Alien2","Alien3","Alien4","Alien5"};
    private final String[] BJN2 = {"Alien6","Alien7","Alien8","Alien9","Alien10","Alien11"};
    private final String[] BWR2 = {"Alien12","Alien13"};
    private final String[] BBM2 = {"Alien14","Alien15","Alien16","Alien17","Alien18"};
    private final String[] BBG2 = {"Alien19","Alien20","Alien21","Alien22"};
    Intent secondIntent;
    RadioGroup radioGroup;
    public String strText, status;


    // added by Hwang TODO: delete this comment
    public static final int READ_PERMITION_REQUEST_CODE = 200;
    public static final int WRITE_PERMITION_REQUEST_CODE = 300;

    public static final int INTRODUCTION = 0;
    public static final int TRIAL = 1;
    public static final int DIAGNOSTIC = 2;
    public static final int TRAINING = 3;
    public static final int SYNC = 4;

    public wordDBHelper mWDBHelper;
    public userDBHelper mUserDBHelper;
    public userTrainingDBHelper mUserTrainingDBHelper;
    public SQLiteDatabase mWDB = null;
    public SQLiteDatabase mUserDB = null;
    public SQLiteDatabase mUserTrainingDB = null;


    public static userResultDBHelper resultDBHelper;
    public static SQLiteDatabase resultDB = null;

    public static String BASE_DATABASE_NAME = "wordList.db";
    private static initFirstWordListsTask initFirstWordListThread;
    private static initFirstWordListsTask initFirstWordListThread2;
    private static initFirstWordListsTask initFirstWordListThread3;
    private static initFirstWordListsTask initFirstWordListThread4;

    public ArrayList<String[]> mUserDataList;
    public ArrayList<String[]> mUserTrainingList;

    public boolean is_Started = false;
    public static SharedPreferences pref;
    public static ReadInfoURLTask mReadInfoUrlTask = null;
    public static ReadTrainingURLTask mReadTrainingUrlTask = null;
    public static ReadResultURLTask mReadResultURLTask = null;
    private String remoteDBUserData;
    private String severUserData;
    private String TAG = "ItemActivity";
    public Data mData;
    private Context mContext;
    public String[] mID;
    public int mIDlength;
    private FragmentOneFive mFrag;
    private FragmentSync mFrag4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        secondIntent = getIntent();
        mID = secondIntent.getStringArrayExtra("ID");
        mIDlength = secondIntent.getIntExtra("length", mID.length);
        mContext = ItemActivity.this;

        try {
            mData = (Data) getApplication();
        } catch(ClassCastException e){
            Log.d("Data class", "Dataclass error");
        }

        pref = getApplicationContext().getSharedPreferences("Mypref", 0);
//        readTrainingURL(mID, mReadTrainingUrlTask);
//        readResultURL(mID, mReadResultURLTask);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, secondIntent.getStringArrayExtra("ID"));

        ListView listview = (ListView) findViewById(R.id.student_list) ;
        listview.setAdapter(adapter);

        // added by Hwang TODO: delete this comment
        if (is_Started == false) {
            createPreferences();
            is_Started = true;
        }

        // userInfo.db
        mUserDBHelper = new userDBHelper(this, 1);
        mUserDB = mUserDBHelper.getWritableDatabase();
        mUserDataList = new ArrayList<>();

        remoteDBUserData = ItemActivity.pref.getString("dbUser", "");
        severUserData = ItemActivity.pref.getString("userData", "");
        Log.d(TAG, "file " + remoteDBUserData);
        readInfoURL(remoteDBUserData, mReadInfoUrlTask);
        // TODO : use strText
        mWDBHelper = new wordDBHelper(this, BASE_DATABASE_NAME);
        initWordList();

        mFrag = (FragmentOneFive)
                getSupportFragmentManager().findFragmentById(R.id.fragment2);


        updateUI();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String dbName;
                String host;
                String url;
                mData.mSessionBlock = 0;

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(), "ID: " + strText, Toast.LENGTH_SHORT).show();

                mData.setNameStudent(strText);

                mWDB = mWDBHelper.getWritableDatabase();

                mData.setDaten();
                Log.i(TAG, "ID "+ strText + " is read.");
                getSessionCategory(mData.nameStudent);
                setTrainingDB(mData.nameStudent);

                updateFragView();
                updateUI();
            }
        });
    }


    public void setStatus(String status){

        Log.i(TAG, "set a status: " + status);
        this.status = status;
    }

    public void updateUI() {
        // TODO : use strText
        FragmentManager fm = getSupportFragmentManager();
        Log.i("Fragments open", "Fragment are called");
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment1, new MainFragment());
        fragmentTransaction.replace(R.id.fragment2, new FragmentOneFive());
        fragmentTransaction.replace(R.id.fragment3, new FragmentLastGraph());
        fragmentTransaction.replace(R.id.fragment4, new FragmentSync());
        fragmentTransaction.commit();
    }

    public void updateFragView() {
        Log.d(TAG, "updateNoteFragView()");
        getSessionCategory(mData.nameStudent);

        mData.calDaten();

        mFrag = (FragmentOneFive)
                getSupportFragmentManager().findFragmentById(R.id.fragment2);

        if (mFrag != null) {
            mFrag.filledData(mData);
            mFrag.generateGraph(mData);

            FragmentManager fm = getSupportFragmentManager();
            Log.i("Fragments open", "Fragment are called");
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment2, mFrag);
            fragmentTransaction.commit();
        }
    }


    

    public void onBackPressed() {
        Log.i(TAG,"onBackPressed() for logout");
        //super.onBackPressed(); //run this function without this code

        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("Möchten Sie sich abmelden?");
        d.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        d.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        d.show();
    }

    public void setTrainingDB(String stName) {
        // userTraining.db
        String dbName;
        String host;
        String url;

        mWDB = mWDBHelper.getWritableDatabase();

        for (int i = 1; i <= 16; i++){
            Log.d(TAG,"readDatabank:" + i);
            try {
            /*
            dbName = "training_" + stName + "_" + i + ".db";
            host = ItemActivity.pref.getString("host", "");
            url = host + "/HOT-T/Results/" + stName + "/" + dbName;
            */
                dbName = "training_" + stName + "_" + i + ".db";
                mUserTrainingDBHelper = new userTrainingDBHelper(this, dbName, stName, 1);
                mUserTrainingDB = mUserTrainingDBHelper.getWritableDatabase();

                mData.setTrainAll(i-1, getNumTotalWords(mData.nameStudent)); // all words
                mData.setTrainWrongError(i-1,getNumTotallyWrongWordsInCategory(mData.nameStudent)); // wrong words
                mData.setTrainWrong(i-1, getNumTotallyWrongWords(mData.nameStudent));
                mData.setNumNochmal(i-1, getNumButtonEar(mData.nameStudent));
                mData.setNumAErase(i-1, getNumEraseOne(mData.nameStudent));
                mData.setNumAllErase(i-1, getNumEraseAll(mData.nameStudent));

                getNumTotalTries(mData.nameStudent);
                getNumTotalTriesInCategory(mData.nameStudent);

            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if(mUserTrainingDB.isOpen()) {
                    mUserTrainingDB.close();
                }
            }
        }
    }
    public String getID(String[] strArr, int ind){
        return strArr[ind];
    }

    public void getSessionCategory(String stName) {
        Log.d(TAG, "getSessionCategory(): ");

        String dbName = "result_" + stName + ".db";

        resultDBHelper = new userResultDBHelper(this, dbName,1);
        if (resultDB == null || !resultDB.isOpen()) {
            Log.d(TAG, "readable database open: ");
            resultDB = resultDBHelper.getReadableDatabase();
        }

        Cursor c = resultDB.rawQuery("SELECT * FROM "
                 + userResultDBHelper.RESULT_DIAG_MAIN
                + " ORDER BY ERROR_RATE DESC", null);
        Log.d(TAG, "getSessionCategory(): "+ c.moveToFirst());

       // String main = "3";
        //String errorRate = "0.3";
        if ( c == null) {
            Log.d(TAG, "cursor is null "); // => cursor : c.moveToFirst() return false is because cursor is empty but not null (query was successful).
        } else if (c != null && c.getCount() > 0) {
                c.moveToFirst();
         //       main = c.getString(c.getColumnIndex("CATEGORY_MAIN"));
//                Log.d(TAG, "getSessionCategory()11: " + main);

                for (int ind = 0; ind < 3; ind++) {
                    mData.setErrorcategory(ind, c.getString(c.getColumnIndex("CATEGORY_MAIN")));
                    Log.d(TAG, "getSessionCategory(" + ind + "): " + c.getString(c.getColumnIndex("CATEGORY_MAIN")));
                    c.moveToNext();
                }



                //errorRate = c.getString(c.getColumnIndex("ERROR_RATE"));

                c.close();
            }
            if (resultDB.isOpen()) {

                Log.d(TAG, "getSessionCategory() closed ");
                resultDB.close();
            }
            //return Integer.valueOf(main);

    }

    public int getNumWrongTries(String stName) {
        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_WRONG + "'", null);
        int res = c.getCount();
        Log.d(TAG, "getNumWrongTries: " + res);
        c.close();
        return res;
    }

    public int getNumCorrectTries(String stName) {

        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_ALL + "'" +
                " AND (REPEAT_COUNT <= 3)", null);
        int res = c.getCount();

        Log.d(TAG, "getNumCorrectTries: " + res);
        c.close();
        return res;
    }

    public int getNumWrongTriesInCategoy(String stName) {
        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_WRONG + "'", null);
        int categorisedWord = getNumCategorizedWords(c);

        Log.d(TAG, "getNumWrongTriesInCategoy: " + categorisedWord);
        c.close();
        return categorisedWord;
    }


    public int getNumCorrectTriesInCategory(String stName) {
        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_ALL + "'" +
                " AND (REPEAT_COUNT <= 3)", null);
        int categorisedWord = getNumCategorizedWords(c);

        Log.d(TAG, "getNumCorrectTriesInCategory: " + categorisedWord);
        c.close();
        return categorisedWord;
    }


    // easy words inclusive
    public int getNumTotalTries(String stName) {
        int res = getNumWrongTries(stName) + getNumCorrectTries(stName);
        Log.d(TAG, "getNumTotalTries: " + res);
        return res;
    }

    // easy words exclusive
    public int getNumTotalTriesInCategory(String stName) {
        int res = getNumWrongTriesInCategoy(stName) + getNumCorrectTriesInCategory(stName);
        Log.d(TAG, "getNumTotalTriesInCategory: " + res);
        return res;
    }

    // count words
    public int getNumTotalWords(String stName) {
        int res = 0;
        Cursor cAll = mUserTrainingDB.rawQuery("SELECT * FROM " + stName, null);
        int countAll = cAll.getCount();
        if (countAll != 0) {
            Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                    " WHERE EVENT = '" + mData.TRAIN_ALL + "'", null);
            // TODO
            res = c.getCount() + 1;
            Log.d(TAG, "getNumTotalWords: " + res);
            c.close();
        }
        cAll.close();
        return res;
    }

    // wrong wrote at all 3 times && easy words inclusive
    public int getNumTotallyWrongWords(String stName) {
        int res = 0;
        Cursor cAll = mUserTrainingDB.rawQuery("SELECT * FROM " + stName, null);
        int countAll = cAll.getCount();
        if (countAll != 0) {
            Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                    " WHERE EVENT = '" + mData.TRAIN_ALL + "'" +
                    " AND (REPEAT_COUNT == 4)", null);
            res = c.getCount() + 1;
            Log.d(TAG, "getNumTotallyWrongWords: " + res);
            c.close();
        }
        cAll.close();
        return res;
    }

    // all words inclusive error category
    public int getNumTotalWordsInCategory(String stName) {
        int res = 0;
        Cursor cAll = mUserTrainingDB.rawQuery("SELECT * FROM " + stName, null);
        int countAll = cAll.getCount();
        if (countAll != 0) {
            Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                    " WHERE EVENT = '" + mData.TRAIN_ALL + "'", null);
            int categorisedWord = getNumCategorizedWords(c);
            res = categorisedWord + 1;
            Log.d(TAG, "getNumTotalWords: " + res);
            c.close();
        }
        cAll.close();
        return res;
    }

    // wrong wrote at all 3 times && easy words inclusive
    public int getNumTotallyWrongWordsInCategory(String stName) {
        int res = 0;
        Cursor cAll = mUserTrainingDB.rawQuery("SELECT * FROM " + stName, null);
        int countAll = cAll.getCount();
        if (countAll != 0) {
            Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                    " WHERE EVENT = '" + mData.TRAIN_ALL + "'" +
                    " AND (REPEAT_COUNT == 4)", null);
            int categorisedWord = getNumCategorizedWords(c);
            res = categorisedWord + 1;
            Log.d(TAG, "getNumTotallyWrongWords: " + res);
            c.close();
        }
        cAll.close();
        return res;
    }


    public int getNumEraseOne(String stName) {
        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_ERASE + "'", null);
        int res = c.getCount();
        Log.d(TAG, "getNumEraseOne: " + res);
        c.close();
        return res;
    }

    public int getNumEraseAll(String stName) {
        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_ERASE_ALL + "'", null);
        int res = c.getCount();
        Log.d(TAG, "getNumEraseAll: " + res);
        c.close();
        return res;
    }

    public int getNumButtonEar(String stName) {

        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = '" + mData.TRAIN_BUTTON_EAR + "'", null);
        int res = c.getCount();

        Log.d(TAG, "getNumButtonEar: " + res);
        c.close();
        return res;
    }

    // Extracting data function for InCategories
    private int getNumCategorizedWords(Cursor c) {
        int sum = 0;

        int res = c.getCount();
        if (res > 0) {
            c.moveToFirst();
            if (mWDB == null || !mWDB.isOpen()) {
                mWDB = mWDBHelper.getWritableDatabase();
            }
            do {
                String word = c.getString(c.getColumnIndex(mUserTrainingDBHelper.CORRECT_WORD));

                /*
                 * TODO: if there is requery function, it would be faster,
                 *  because close is not needed.
                 */
                Cursor cw = mWDB.rawQuery("SELECT * FROM " + "table_easy" +
                        " WHERE WORD = '" + word + "'", null);
                if (cw.getCount() > 0) {
                    sum++;
                }
                cw.close();
            } while (c.moveToNext());
            res -= sum;
        }
        return res;
    }

    public void getWrongTries(String stName) {
        Cursor c = mUserTrainingDB.rawQuery("SELECT * FROM " + stName +
                " WHERE EVENT = 'mistakeDlg'", null);
        int res = c.getCount();
        Log.d(TAG, "getWrongTries: " + res);
    }

    private void readInfoURL(String urlName, ReadInfoURLTask tempTask) {
        Log.d(TAG, "readURL: " + urlName);

        if (tempTask != null) {
            return;
        }
        tempTask = new ReadInfoURLTask(this, urlName);
        tempTask.execute((Void) null);
    }

    public void readTrainingURL(String[] urlNameList, ReadTrainingURLTask tempTask) {
        Log.d(TAG, "readTrainingURL: " + urlNameList);

        if (tempTask != null) {
            return;
        }
        tempTask = new ReadTrainingURLTask(this, admin);
        tempTask.execute((Void) null);
    }

    public void readResultURL(String[] urlNameList, ReadResultURLTask tempTask) {
        Log.d(TAG, "readResultURL: " + urlNameList);

        if (tempTask != null) {
            Log.d(TAG, "tempTask is not null ");
            return;
        }
        tempTask = new ReadResultURLTask(this, admin);
        tempTask.execute((Void) null);
    }

    private void createPreferences() {
        SharedPreferences.Editor editor = pref.edit();
        String exStoragePath = Environment.getExternalStorageDirectory().getPath();
        String host = "https://seafile.projekt.uni-hannover.de/dav/sportwiss-errorlesslearning";
        editor.putString("root", exStoragePath + "/");
        editor.putString("server", "https://seafile.projekt.uni-hannover.de/");
        editor.putString("name", "hottsportwiss");
        editor.putString("email", "hottsportwiss@gmail.com");
        editor.putString("token", "fc4c019d18a3fb01e37e7159e5969651bf0aec7a");
        editor.putString("host", host);
        editor.putString("rootPic", exStoragePath + "/HOT-T/Pictures_all/");
        editor.putString("DownPath", exStoragePath + "/Download/");
        editor.putString("csvPath", exStoragePath + "/HOT-T/");
        editor.putString("picPath", exStoragePath + "/Download/HOT-T/Pictures_all/");
        editor.putString("userDir", "/PERSONAL_DATA");
        editor.putString("userCategory", host + "/PERSONAL_DATA/UserCategory.csv");
        editor.putString("userData", host + "/PERSONAL_DATA/UserInfo.csv");

        editor.putString("dbCategory", host + "/PERSONAL_DATA/userCategory.db");
        editor.putString("dbUser", host + "/PERSONAL_DATA/userInfo.db");

        editor.commit();
    }

    private void initWordList() {
        Log.d(TAG, "getWordLists() ");
        Log.d(TAG, "initWordList()");

        String envPath = Environment.getExternalStorageDirectory().toString();
        String hottPath = envPath + "/HOT-T";

        String server = "https://seafile.projekt.uni-hannover.de";
        String hostPath = server + "/dav/sportwiss-errorlesslearning";
        String urlHottPath = hostPath + "/HOT-T";

        String csvPath = hottPath + "/master-wordlist_intro_9-q.csv";
        String urlPath = urlHottPath + "/master-wordlist_intro_9-q.csv";
        initFirstWordListThread = new initFirstWordListsTask(this, wordDBHelper.TABLE_TRIAL);
        initFirstWordListThread.execute(urlPath, csvPath);

        csvPath = hottPath + "/master-wordlist_diagnostic_9-q.csv";
        urlPath = urlHottPath + "/master-wordlist_diagnostic_9-q.csv";
        initFirstWordListThread2 = new initFirstWordListsTask(this, wordDBHelper.TABLE_DIAGNOSTIC);
        initFirstWordListThread2.execute(urlPath, csvPath);

        csvPath = hottPath + "/master-wordlist_training_9-q.csv";
        urlPath = urlHottPath + "/master-wordlist_training_9-q.csv";
        initFirstWordListThread3 = new initFirstWordListsTask(this, wordDBHelper.TABLE_TRAINING);
        initFirstWordListThread3.execute(urlPath, csvPath);

        csvPath = hottPath + "/master-wordlist_easywords_9-q.csv";
        urlPath = urlHottPath + "/master-wordlist_easywords_9-q.csv";
        initFirstWordListThread4 = new initFirstWordListsTask(this, wordDBHelper.TABLE_EASY);
        initFirstWordListThread4.execute(urlPath, csvPath);
    }

    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}




