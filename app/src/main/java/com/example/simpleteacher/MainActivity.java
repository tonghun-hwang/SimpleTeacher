package com.example.simpleteacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String[] admin = {"Admin1","Ufo1", "Ufo2", "Ufo3", "Ufo4", "Ufo5","Ufo6", "Ufo7", "Ufo8",
            "Ufo9", "Ufo10", "Ufo11", "Ufo12","Komet1", "Komet2", "Komet3", "Komet4", "Komet5", "Komet6", "Komet7",
            "Planet1", "Planet2", "Planet3", "Planet4","Planet5", "Planet6", "Planet7", "Planet8", "Planet9",
            "Planet10", "Planet11", "Planet12", "Planet13", "Planet14", "Planet15","Planet16","Planet17","Planet18",
            "Planet19", "Planet20", "Planet21", "Planet22", "Planet23", "Planet24", "Planet25", "Planet26", "Planet27", "Planet28", "Planet29",
            "Alien1","Alien2","Alien3","Alien4","Alien5","Alien6","Alien7","Alien8","Alien9","Alien10","Alien11",
            "Alien12","Alien13","Alien14","Alien15","Alien16","Alien17","Alien18", "Alien19","Alien20","Alien21","Alien22"};
    private static final String[] ADM1 = {"Ufo1", "Ufo2", "Ufo3", "Ufo4", "Ufo5"};
    private static final String[] AUN1 = {"Ufo6", "Ufo7", "Ufo8"};
    private static final String[] ASB1 = {"Ufo9", "Ufo10", "Ufo11", "Ufo12"};
    private static final String[] BSR1 = {"Komet1", "Komet2", "Komet3"};
    private static final String[] BBH1 = {"Komet4", "Komet5", "Komet6", "Komet7"};
    private static final String[] ASE2 = {"Planet1", "Planet2", "Planet3", "Planet4"};
    private static final String[] ABR2 = {"Planet5", "Planet6", "Planet7", "Planet8", "Planet9"};
    private static final String[] AFT2 = {"Planet10", "Planet11", "Planet12", "Planet13", "Planet14",
            "Planet15","Planet16","Planet17","Planet18"};
    private static final String[] ABT2 = {"Planet19", "Planet20", "Planet21", "Planet22", "Planet23", "Planet24"};
    private static final String[] ASH2 = {"Planet25", "Planet26", "Planet27", "Planet28", "Planet29"};
    private static final String[] BLR2 = {"Alien1","Alien2","Alien3","Alien4","Alien5"};
    private static final String[] BJN2 = {"Alien6","Alien7","Alien8","Alien9","Alien10","Alien11"};
    private static final String[] BWR2 = {"Alien12","Alien13"};
    private static final String[] BBM2 = {"Alien14","Alien15","Alien16","Alien17","Alien18"};
    private static final String[] BBG2 = {"Alien19","Alien20","Alien21","Alien22"};

    private String TAG = "MainActivity";
    HashMap<String, String> account = new HashMap<String, String>();
    EditText id;
    EditText password;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ID and Password for teachers
        account.put("admin", "admin1");
        account.put("ADM1", "mda1");
        account.put("AUN1", "nua1");
        account.put("ASB1", "bsa1");
        account.put("BSR1", "rsb1");
        account.put("BBH1", "hbb1");
        account.put("ASE2", "esa2");
        account.put("ABR2", "rba2");
        account.put("AFT2", "tfa2");
        account.put("ABT2", "tba2");
        account.put("ASH2", "hsa2");
        account.put("BLR2", "rlb2");
        account.put("BJN2", "njb2");
        account.put("BWR2", "rwb2");
        account.put("BBM2", "mbb2");
        account.put("BBG2", "gbb2");

        final Intent intent = new Intent(this, ItemActivity.class);
        id = (EditText) findViewById(R.id.ID);
        password = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.button);

        buttonLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account.containsKey(id.getText().toString())){
                    if (account.get(id.getText().toString()).equals(password.getText().toString())){
                        if( id.getText().toString().equals("admin")){
                            intent.putExtra("ID", admin);
                        } else if( id.getText().toString().equals("ADM1")){
                            intent.putExtra("ID", ADM1);
                        } else if( id.getText().toString().equals("AUN1")){
                            intent.putExtra("ID", AUN1);
                        } else if( id.getText().toString().equals("ASB1")){
                            intent.putExtra("ID", ASB1);
                        } else if( id.getText().toString().equals("BSR1")){
                            intent.putExtra("ID", BSR1);
                        } else if( id.getText().toString().equals("BBH1")){
                            intent.putExtra("ID", BBH1);
                        } else if( id.getText().toString().equals("ASE2")){
                            intent.putExtra("ID", ASE2);
                        } else if( id.getText().toString().equals("ABR2")){
                            intent.putExtra("ID", ABR2);
                        } else if( id.getText().toString().equals("AFT2")){
                            intent.putExtra("ID", AFT2);
                        } else if( id.getText().toString().equals("ABT2")){
                            intent.putExtra("ID", ABT2);
                        } else if( id.getText().toString().equals("ASH2")){
                            intent.putExtra("ID", ASH2);
                        } else if( id.getText().toString().equals("BLR2")){
                            intent.putExtra("ID", BLR2);
                        } else if( id.getText().toString().equals("BJN2")){
                            intent.putExtra("ID", BJN2);
                        } else if( id.getText().toString().equals("BWR2")){
                            intent.putExtra("ID", BWR2);
                        } else if( id.getText().toString().equals("BBM2")){
                            intent.putExtra("ID", BBM2);
                        } else if( id.getText().toString().equals("BBG2")){
                            intent.putExtra("ID", BBG2);
                        }
                        id.setText(null);
                        password.setText(null);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Passwort ist falsch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID ist falsch", Toast.LENGTH_SHORT).show();
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
