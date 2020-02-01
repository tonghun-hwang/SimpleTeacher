package com.example.simpleteacher;

import android.app.Application;

public class Data extends Application {

    public static String nameStudent;

    public static String nameErrorcategory = "Error Category";
    public static int[] trainAll = {10,10,10,10,10,20,20,20,20,20,30,30,30,30,30,30};
    public static int[] trainAllTries;
    public static int[] trainWrong = {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5};
    public static int[] trainWrongError = {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
    public static int[] trainWrongTries;
    public static int[] trainWrongErrorTries;
    public static double[] prozTrainWrong;
    public static double[] prozTrainWrongError;
    public static int[] numAllWords;
    public static int[] numAllWrong;
    public static int[] numAllWrongError;
    public static double[] prozAllTrainWrong;
    public static double[] prozAllTrainWrongError;


    public static void calDaten(){

        prozTrainWrong = new double[16];
        for(int i = 0; i < 16; i++){
            prozTrainWrong[i] = (trainWrong[i] * 100) / trainAll[i];
        }

        prozTrainWrongError = new double[16];
        for(int i = 0; i < 16; i++){
            prozTrainWrongError[i] = (trainWrongError[i] * 100) / trainAll[i];
        }

        // View => numAllWords = (TextView) inf.findViewById(R.id.numAllWords);
        int count = 0;
        numAllWords = new int[4];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++)
            numAllWords[i] += trainAll[count];
            count++;
        }
        numAllWords[3] = trainAll[15];
        count = 0;



        // View => numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll);
        numAllWrong = new int[4];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++)
                numAllWrong[i] += trainWrong[count];
            count++;
        }
        numAllWrong[3] = trainWrong[15];
        count = 0;

        prozAllTrainWrong = new double[4];
        for(int i = 0; i < 4; i++){
            prozAllTrainWrong[i]= (numAllWrong[i]*100) / numAllWords[i];

        }


        // View => numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError);
        numAllWrongError = new int[4];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++)
                numAllWrongError[i] += trainWrongError[count];
            count++;
        }
        numAllWrongError[3] = trainWrongError[15];
        count = 0;

        prozAllTrainWrongError = new double[4];
        for(int i = 0; i < 4; i++){
            prozAllTrainWrongError[i]= (numAllWrongError[i]*100) / numAllWords[i];

        }


    }

    // FragmentOneFive 11111111111111111
    // Result


    public static int numNochmal1 = 1;
    public static int numARadierer1 = 1;
    public static int numAllRadierer1 = 1;


    //FragmentSixTen 666666666666666666666
    // Result
    public static int numNochmal6 = 6;
    public static int numARadierer6 = 6;
    public static int numAllRadierer6 = 6;


    //FragmentElevenFifteen 1111111111111111111
    public static int numNochmal11 = 11;
    public static int numARadierer11 = 11;
    public static int numAllRadierer11 = 11;

    //FragmentSixteen
    // Result
    public static int numNochmal16 = 16;
    public static int numARadierer16 = 16;
    public static int numAllRadierer16 = 16;



    @Override
    public void onCreate(){
        super.onCreate();
    }

    public static void setNameStudent(String name){
        nameStudent = name;
    }


}
