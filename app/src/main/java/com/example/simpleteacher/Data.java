package com.example.simpleteacher;
/**
* Error 붙여져 있는 것은 Errorcategory에 해당하는 항목입니다.
* 밑에 나와있는 파라미터값들은 데이터베이스에서 불러와야하는 값들입니다.
* Data 클래스 생성자를 만들어서 값을 불러와도 되지만 그렇게 되면 많은 것을 변경해야 해서 일단 그 부분은 안 건드렸습니다.
* 변경하고 싶으시면 변경해도 됩니다!!
* 이 데이터에 값이 저장되어 모든 프래그먼트에 값을 전달하게 됩니다. 싱글톤 패턴 생각하시면 될것같습니다!!
*
*
* @param trainAll int[], 한 세션에서 진행한 모든 단어 개수
* @param trainWrong int[],한 세션에서 틀린 단어 개수
* @param trainWrongError int[], 한 세션에서 틀린 단어 중 에러카테고리에 해당하는 단어 개수
 *
* @param numNochmal1 int, 1~5 세션에서 스피커버튼 눌린 횟수
* @param numNochmal6 int, 6~10 세션에서 스피커버튼 눌린 횟수6~10
* @param numNochmal11 int,11~15 세션에서 스피커버튼 눌린 횟수
 * @param numNochmal16 int, 16 세션에서 스피커버튼 눌린 횟수
 *
 * @param numARadierer1 int, 1~5 세션에서 A-지우개 눌린 횟수
 * @param numARadierer6 int, 6~10 세션에서 A-지우개 눌린 횟수
 * @param numARadierer11 int, 11~15세션에서 A-지우개 눌린 횟수
 * @param numARadierer16 int, 16 세션에서 A-지우개 눌린 횟수
 *
 * @param numAllRadierer1 int, 1~5 세션에서 Alles-지우개 눌린 횟수
 * @param numAllRadierer6 int, 6~10 세션에서 Alles-지우개 눌린 횟수
 * @param numAllRadierer11 int, 11~15 세션에서 Alles-지우개 눌린 횟수
 * @param numAllRadierer16 int, 16 세션에서 Alles-지우개 눌린 횟수
 *
* @author HyangGi Jang
* */
import android.app.Application;
import android.util.Log;

import static java.util.Arrays.fill;

public class Data extends Application {

    public String nameStudent;

    public String nameErrorcategory = "Error Category";

    public int[] trainAll = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] trainAllTries;
    public int[] trainWrong = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] trainWrongError = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] trainWrongTries;
    public int[] trainWrongErrorTries;
    public double[] prozTrainWrong = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public double[] prozTrainWrongError = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] numAllWords = new int[4];
    public int[] numAllWrong = new int[4];
    public int[] numAllWrongError = new int[4];
    public double[] prozAllTrainWrong = new double[4];
    public double[] prozAllTrainWrongError = new double[4];

    // FragmentOneFive
    public int numNochmal1 = 0;
    public int numARadierer1 = 0;
    public int numAllRadierer1 = 0;

    //FragmentSixTen
    public int numNochmal6 = 0;
    public int numARadierer6 = 0;
    public int numAllRadierer6 = 0;

    //FragmentElevenFifteen
    public int numNochmal11 = 0;
    public int numARadierer11 = 0;
    public int numAllRadierer11 = 0;

    //FragmentSixteen
    public int numNochmal16 = 0;
    public int numARadierer16 = 0;
    public int numAllRadierer16 = 0;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Data_Class", "initialize Data class");

        nameStudent = "";

        nameErrorcategory = "Error Category";

        trainAll = new int[16];
        trainAllTries = new int[16];
        trainWrongTries = new int[16];
        trainWrongErrorTries = new int[16];
        trainWrong = new int[16];
        trainWrongError = new int[16];
        trainWrongTries = new int[16];
        trainWrongErrorTries = new int[16];
        prozTrainWrong = new double[16];
        prozTrainWrongError = new double[16];
        numAllWords = new int[4];
        numAllWrong = new int[4];
        numAllWrongError = new int[4];
        prozAllTrainWrong = new double[4];
        prozAllTrainWrongError = new double[4];

        numNochmal1 = 0;
        numARadierer1 = 0;
        numAllRadierer1 = 0;

        numNochmal6 = 0;
        numARadierer6 = 0;
        numAllRadierer6 = 0;

        numNochmal11 = 0;
        numARadierer11 = 0;
        numAllRadierer11 = 0;

        numNochmal16 = 0;
        numARadierer16 = 0;
        numAllRadierer16 = 0;

    }

    public void setDaten(){

        fill(trainAll,0);
        fill(trainAllTries,0);
        fill(trainWrong,0);
        fill(trainWrongTries,0);
        fill(trainWrongError,0);
        fill(trainWrongErrorTries,0);
        fill(prozAllTrainWrong,0);
        fill(prozAllTrainWrongError,0);
        fill(prozTrainWrong,0);
        fill(prozTrainWrongError,0);
        fill(numAllWords,0);
        fill(numAllWrong,0);
        fill(numAllWrongError,0);

    }
    public void setTrainAll(int index, int value) {
        trainAll[index] = value;
    }

    public void calDaten(){

        for(int i = 0; i < 16; i++){
            if(trainAll[i] == 0){
                prozTrainWrong[i] = 0;
            } else {
                prozTrainWrong[i] = (trainWrong[i] * 100) / trainAll[i];
            }

        }

        for(int i = 0; i < 16; i++){
            if(trainAll[i] == 0){
                prozTrainWrongError[i] = 0;
            } else {
                prozTrainWrongError[i] = (trainWrongError[i] * 100) / trainAll[i];
            }
        }

        // View => numAllWords = (TextView) inf.findViewById(R.id.numAllWords);
        int count = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++)
            numAllWords[i] += trainAll[count];
            count++;
        }
        numAllWords[3] = trainAll[15];
        count = 0;


        // View => numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++)
                numAllWrong[i] += trainWrong[count];
            count++;
        }
        numAllWrong[3] = trainWrong[15];
        count = 0;
        ;
        for(int i = 0; i < 4; i++){
            if(numAllWords[i] == 0) {
                prozAllTrainWrong[i] = 0;
            } else {
                prozAllTrainWrong[i] = (numAllWrong[i]*100) / numAllWords[i];
            }

        }


        // View => numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++)
                numAllWrongError[i] += trainWrongError[count];
            count++;
        }
        numAllWrongError[3] = trainWrongError[15];
        count = 0;

        for(int i = 0; i < 4; i++){
            if( numAllWords[i] == 0) {
                prozAllTrainWrongError[i] = 0;
            } else {
                prozAllTrainWrongError[i]= (numAllWrongError[i]*100) / numAllWords[i];
            }

        }

    }

    public void setNameStudent(String name){
        nameStudent = name;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

}
