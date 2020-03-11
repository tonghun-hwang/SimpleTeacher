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

    public String[] errorcategory;

    public int[] trainAll;
    public int[] trainAllTries;
    public int[] trainWrong;
    public int[] trainWrongError;
    public int[] trainWrongTries;
    public int[] trainWrongErrorTries;
    public double[] prozTrainWrong;
    public double[] prozTrainWrongError;
    public int[] numAllWords;
    public int[] numAllWrong;
    public int[] numAllWrongError;
    public double[] prozAllTrainWrong;
    public double[] prozAllTrainWrongError;
    public int[] numNochmal;
    public int[] numAErase;
    public int[] numAllErase;
    public int[] arrNochmal;
    public int[] arrAErase;
    public int[] arrAllErase;
    public boolean postExeResult;
    public boolean postExeTrain;
    public boolean postExeBoth;

    public static final String TRAIN_ALL = "next";
    public static final String TRAIN_TOT_WRONG = "nextDismissMistake";
    public static final String TRAIN_WRONG = "mistakeDlg";
    public static final String TRAIN_KEYBOARD = "pushChar";
    public static final String TRAIN_ERASE = "eraseOne";
    public static final String TRAIN_ERASE_ALL = "eraseAll";
    public static final String TRAIN_STILL_THERE = "stillThereDlg";
    public static final String TRAIN_SPEAK_WORD = "speak";
    public static final String TRAIN_BUTTON_EAR = "btEar";
    private static final String TAG = "Data";

    public int mSessionBlock;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "initialize Data class");

        nameStudent = "";

        errorcategory = new String[3];

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
        numNochmal = new int[16];
        numAllErase = new int[16];
        numAErase = new int[16];
        arrAErase = new int[4];
        arrAllErase = new int[4];
        arrNochmal = new int[4];
        postExeResult = false;
        postExeTrain = false;
        postExeBoth = false;
    }

    public void setDaten(){
        Log.i(TAG, "initialize data to 0.");

        fill(errorcategory,"");
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
        fill(numNochmal,0);
        fill(numAErase,0);
        fill(numAllErase,0);
        fill(arrAErase,0);
        fill(arrAllErase,0);
        fill(arrNochmal,0);

    }

    public void setNameStudent(String nameStudent){
        Log.i(TAG, "set a name in nameStudent: " + nameStudent);
        this.nameStudent = nameStudent;
    }
    public void setErrorcategory(int index, String value) {
        Log.i(TAG, "errorcategory: Set a value: errorcategory[" + index +"] = " + value);
        this.errorcategory[index] = value;
    }

    public void setNumNochmal(int index, int value) {
        Log.i(TAG, "numNochmal: Set a value: numNochmal[" + index +"] = " + value);
        this.numNochmal[index] = value;
    }

    public void setNumAErase(int index, int value) {
        Log.i(TAG, "numAErase: Set a value: numAErase[" + index +"] = " + value);
        this.numAErase[index] = value;
    }

    public void setNumAllErase(int index, int value) {
        Log.i(TAG, "numAllErase: Set a value: numAllErase[" + index +"] = " + value);
        this.numAllErase[index] = value;
    }

    public void setTrainAll(int index, int value) {
        Log.i(TAG, "trainAll: Set a value: trainAll[" + index +"] = " + value);
        this.trainAll[index] = value;
    }
    public void setTrainAllTries(int index, int value) {
        Log.i(TAG, "trainAllTries: Set a value: trainAllTries[" + index +"] = " + value);
        this.trainAllTries[index] = value;
    }
    public void setTrainWrong(int index, int value) {
        Log.i(TAG, "trainWrong: Set a value: trainWrong[" + index +"] = " + value);
        this.trainWrong[index] = value;
    }
    public void setTrainWrongError(int index, int value) {
        Log.i(TAG, "trainWrongError: Set a value: trainWrongError[" + index +"] = " + value);
        this.trainWrongError[index] = value;
    }
    public void setTrainWrongTries(int index, int value) {
        Log.i(TAG, "trainWrongTries: Set a value: trainWrongTries[" + index +"] = " + value);
        this.trainWrongTries[index] = value;
    }
    public void setTrainWrongErrorTries(int index, int value) {
        Log.i(TAG, "trainWrongErrorTries: Set a value: trainWrongErrorTries[" + index +"] = " + value);
        this.trainWrongErrorTries[index] = value;
    }

    public void calDaten(){
        Log.i(TAG, "calculate the data aaaaa");

        // prozTrainWrong
        for(int i = 0; i < 16; i++){
            if(trainAll[i] == 0){
                prozTrainWrong[i] = 0;
            } else {
                prozTrainWrong[i] = (trainWrong[i] * 100) / trainAll[i];
            }
        }

        // prozTrainWrongError
        for(int i = 0; i < 16; i++){
            if(trainAll[i] == 0){
                prozTrainWrongError[i] = 0;
            } else {
                prozTrainWrongError[i] = (trainWrongError[i] * 100) / trainAll[i];
            }
        }

        // View => numAllWords = (TextView) inf.findViewById(R.id.numAllWords); 2.1
        int count = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                numAllWords[i] += trainAll[count];
                count++;
            }
        }
        numAllWords[3] = trainAll[15];
        count = 0;

        // View => numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll); 2.2
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                numAllWrong[i] += trainWrong[count];
                count++;
            }
        }
        numAllWrong[3] = trainWrong[15];
        count = 0;

        for(int i = 0; i < 4; i++){
            if(numAllWords[i] == 0) {
                prozAllTrainWrong[i] = 0;
            } else {
                prozAllTrainWrong[i] = (numAllWrong[i]*100) / numAllWords[i];
            }
        }

        // View => numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError); 2.3
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                numAllWrongError[i] += trainWrongError[count];
                count++;
            }
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

        // View => numNochmal = (TextView) inf.findViewById(R.id.numNochmal);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                arrNochmal[i] += numNochmal[count];
                count++;
            }
        }
        arrNochmal[3] = numNochmal[15];
        count = 0;

        // View => numARadierer = (TextView) inf.findViewById(R.id.numARadierer);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                arrAErase[i] += numAErase[count];
                count++;
            }
        }
        arrAErase[3] = numAErase[15];
        count = 0;

        // View => numALLRadierer = (TextView) inf.findViewById(R.id.numAllesRadierer);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                arrAllErase[i] += numAllErase[count];
                count++;
            }
        }
        arrAllErase[3] = numAllErase[15];
        count = 0;
    }

    public String[] getErrorcategory() {
        return errorcategory;
    }

    @Override
    public void onTerminate() {
        Log.v(TAG,"onTerminate()");
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        Log.v(TAG,"onLowMemory()");
        super.onLowMemory();
    }
}
