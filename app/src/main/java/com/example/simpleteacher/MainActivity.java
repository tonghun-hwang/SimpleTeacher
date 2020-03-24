package com.example.simpleteacher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simpleteacher.helper.teacherDBHelper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    public EditText id;
    public EditText password;
    public Button btnLogin;
    private Data mData;
    private teacherDBHelper tchDBHelper;
    private SQLiteDatabase tchDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mData = (Data) getApplication();
        } catch(ClassCastException e){
            Log.d("Data class", "Dataclass error");
        }

        tchDBHelper = new teacherDBHelper(this, 1);
        tchDB = tchDBHelper.getReadableDatabase();

        final Intent intent = new Intent(this, ItemActivity.class);
        id = (EditText) findViewById(R.id.ID);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.button);

        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = "teacherInfo";
                String tchID = id.getText().toString();
                Cursor c = tchDB.rawQuery("SELECT * FROM "
                                + tableName
                                + " WHERE TEACHER_ID = '" + tchID + "'"
                        , null);
                if (c.getCount() == 1) {
                    c.moveToFirst();
                    String pw = c.getString(c.getColumnIndex("TEACHER_PW"));
                    if(pw.equals(password.getText().toString())) {
                        String students = c.getString(c.getColumnIndex("STUDENTS"));
                        String[] stud = students.split(", ");
                        intent.putExtra("ID", tchID);
                        intent.putExtra("STUDENTS", stud);

                        id.setText(null);
                        password.setText(null);

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwort ist falsch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID ist falsch", Toast.LENGTH_SHORT).show();
                }

                c.close();
                if (tchDB != null) {
                    if (tchDB.isOpen()) {
                        tchDB.close();
                    }

                }
            }
        });
    }

    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "Back Button pressed");
        moveTaskToBack(true);
        finishAndRemoveTask();
        //android.os.Process.killProcess(android.os.Process.myPid()); // kill the process
        //System.exit(0) : 현재 액티비티를 종료시킨다.
        System.runFinalization();  //간단히 말해 현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
        Log.d(TAG, "exit the app");
        System.exit(0);
    }
}
