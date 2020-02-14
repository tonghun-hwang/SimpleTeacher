package com.example.simpleteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.simpleteacher.asyncTask.ReadInfoURLTask;
import com.example.simpleteacher.asyncTask.ReadTrainingURLTask;
import com.example.simpleteacher.helper.userDBHelper;
import com.example.simpleteacher.helper.userTrainingDBHelper;
import com.example.simpleteacher.main.FragmentLastGraph;
import com.example.simpleteacher.main.FragmentOneFive;
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
    public String strText;

    // added by Hwang TODO: delete this comment
    public userDBHelper mUserDBHelper;
    public userTrainingDBHelper mUserTrainingDBHelper;
    public SQLiteDatabase mUserDB = null;
    public SQLiteDatabase mUserTrainingDB = null;

    public ArrayList<String[]> mUserDataList;
    public ArrayList<String[]> mUserTrainingList;

    public boolean is_Started = false;
    public static SharedPreferences pref;
    public static ReadInfoURLTask mReadInfoUrlTask = null;
    public static ReadTrainingURLTask mReadTrainingUrlTask = null;
    private String remoteDBUserData;
    private String severUserData;
    private String TAG = "ItemActivity";

    public static final int INTRODUCTION = 0;
    public static final int TRIAL = 1;
    public static final int DIAGNOSTIC = 2;
    public static final int TRAINING = 3;
    public static final int SYNC = 4;

    private Data mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        secondIntent = getIntent();

        try{
            mData = (Data) getApplication();


        }catch(ClassCastException e){
            Log.d("Dataclass", "Dataclass error");
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, secondIntent.getStringArrayExtra("ID"));

        ListView listview = (ListView) findViewById(R.id.student_list) ;
        listview.setAdapter(adapter);

        // added by Hwang TODO: delete this comment
        pref = getApplicationContext().getSharedPreferences("Mypref", 0);
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position);
                mData.setNameStudent(strText);
                mData.setDaten();
                Log.i("Id_Reading", "ID is read.");
                setTrainingDB(mData.nameStudent);
                getWrongTries(mData.nameStudent);

                Toast.makeText(getApplicationContext(), "ID: " + strText, Toast.LENGTH_SHORT).show();
                mData.calDaten();
                // TODO : use strText
                FragmentManager fm = getSupportFragmentManager();
                Log.i("Fragments open", "Fragment are called");
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment1, new MainFragment());
                fragmentTransaction.replace(R.id.fragment2, new FragmentOneFive());
                fragmentTransaction.replace(R.id.fragment3, new FragmentLastGraph());
                fragmentTransaction.commit();

            }
        });
    }


    public void setTrainingDB(String stName) {
        // userTraining.db
        String dbName;
        String host;
        String url;
        for (int i = 1; i <= 16; i++){
            dbName = "training_" + stName + "_" + i + ".db";
            host = ItemActivity.pref.getString("host", "");
            url = host + "/HOT-T/Results/" + stName + "/" + dbName;
            mUserTrainingDBHelper = new userTrainingDBHelper(this, dbName, stName, 1);
            mUserTrainingDB = mUserTrainingDBHelper.getWritableDatabase();
            readTrainingURL(url, dbName, mReadTrainingUrlTask);
        }
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



    private void readTrainingURL(String urlName, String dbName, ReadTrainingURLTask tempTask) {
        Log.d(TAG, "readTrainingURL: " + urlName);

        if (tempTask != null) {
            return;
        }
        tempTask = new ReadTrainingURLTask(this, urlName, dbName);
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



}




