package com.example.simpleteacher.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.simpleteacher.Data;
import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.R;

public class MainFragment extends Fragment {


    private View inf;


    RadioButton btnoneToFive, btnsixToTen, btnelevenToFifteen, btnsixteen;
    RadioGroup radioGroup;
    TextView nameTeacher, nameStudent, errorCategory;


    public MainFragment() { }

    private ItemActivity activity;
    private Data mData;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (ItemActivity) getActivity();
        mData = (Data) getActivity().getApplication();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        inf = inflater.inflate(R.layout.main_fragment, container, false);
        nameStudent = (TextView) inf.findViewById(R.id.idStudent);
        nameStudent.setText(mData.nameStudent);

        btnoneToFive = inf.findViewById(R.id.btnOneFive);
        btnsixToTen = inf.findViewById(R.id.btnSixTen);
        btnelevenToFifteen = inf.findViewById(R.id.btnElevenFifteen);
        btnsixteen = inf.findViewById(R.id.btnSixteen);

        radioGroup = inf.findViewById(R.id.radioGroup);

        return inf; /////hiermit kann fragmente ändern
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        radioGroup = (RadioGroup) inf.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.btnOneFive){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment2, new FragmentOneFive());
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if(checkedId == R.id.btnSixTen){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment2, new FragmentSixTen());
                    fragmentTransaction.commit();
                } else if(checkedId == R.id.btnElevenFifteen){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment2, new FragmentElevenFifteen());
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(checkedId == R.id.btnSixteen){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment2, new FragmentSixteen());
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }




}