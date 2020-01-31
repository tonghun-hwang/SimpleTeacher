package com.example.simpleteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleteacher.main.FragmentElevenFifteen;
import com.example.simpleteacher.main.FragmentLastGraph;
import com.example.simpleteacher.main.FragmentOneFive;
import com.example.simpleteacher.main.FragmentSixTen;
import com.example.simpleteacher.main.FragmentSixteen;
import com.example.simpleteacher.main.MainFragment;

public class ItemActivity extends AppCompatActivity {
    private static final String[] admin = {"Ufo1", "Ufo2", "Ufo3", "Ufo4", "Ufo5","Ufo6", "Ufo7", "Ufo8",
            "Ufo9", "Ufo10", "Ufo11", "Ufo12","Komet1", "Komet2", "Komet3", "Komet4", "Komet5", "Komet6", "Komet7",
            "Planet1", "Planet2", "Planet3", "Planet4","Planet5", "Planet6", "Planet7", "Planet8", "Planet9",
            "Planet10", "Planet11", "Planet12", "Planet13", "Planet14", "Planet15","Planet16","Planet17","Planet18",
            "Planet19", "Planet20", "Planet21", "Planet22", "Planet23", "Planet24", "Planet25", "Planet26", "Planet27", "Planet28", "Planet29",
            "Alien1","Alien2","Alien3","Alien4","Alien5","Alien6","Alien7","Alien8","Alien9","Alien10","Alien11",
            "Alien12","Alien13","Alien14","Alien15","Alien16","Alien17","Alien18","Alien14","Alien15","Alien16","Alien17","Alien18",
            "Alien19","Alien20","Alien21","Alien22"};
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
    Intent secondIntent;
    RadioGroup radioGroup;
    String strText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        secondIntent = getIntent();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, secondIntent.getStringArrayExtra("ID"));

        ListView listview = (ListView) findViewById(R.id.student_list) ;
        listview.setAdapter(adapter);

        // TODO : use strText

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position);
                Log.i("Id_Reading", "ID is read.");
                Data.setNameStudent(strText);
                Toast.makeText(getApplicationContext(), "ID: " + strText, Toast.LENGTH_SHORT).show();
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

    }




