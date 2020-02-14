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

    // FragmentOneFive
    public int numNochmal1;
    public int numARadierer1;
    public int numAllRadierer1;

    //FragmentSixTen
    public int numNochmal6;
    public int numARadierer6;
    public int numAllRadierer6;

    //FragmentElevenFifteen
    public int numNochmal11;
    public int numARadierer11;
    public int numAllRadierer11;

    //FragmentSixteen
    public int numNochmal16;
    public int numARadierer16;
    public int numAllRadierer16;

    public static final String TRAIN_ALL = "incWIdx";
    public static final String TRAIN_WRONG = "mistakeDlg";
    public static final String TRAIN_KEYBOARD = "pushChar";
    public static final String TRAIN_ERASE = "eraseOne";
    public static final String TRAIN_ERASE_ALL = "eraseAll";
    public static final String TRAIN_STILL_THERE = "stillThereDlg";
    public static final String TRAIN_SPEAK_WORD = "speak";
    public static final String TRAIN_BUTTON_EAR = "btEar";
    private static final String TAG = "Data";


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
        numNochmal = new int[16];
        numAllErase = new int[16];
        numAErase = new int[16];
        arrAErase = new int[4];
        arrAllErase = new int[4];
        arrNochmal = new int[4];

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
        fill(numNochmal,0);
        fill(numAErase,0);
        fill(numAllErase,0);
        fill(arrAErase,0);
        fill(arrAllErase,0);
        fill(arrNochmal,0);

    }

    public void setNameStudent(String nameStudent){
        this.nameStudent = nameStudent;
    }
    public void setNameErrorcategory(String nameErrorcategory) {
        this.nameErrorcategory = nameErrorcategory;
    }

    public void setNumNochmal(int index, int value) {
        this.numNochmal[index] = value;
    }

    public void setNumAErase(int index, int value) {
        this.numAErase[index] = value;
    }

    public void setNumAllErase(int index, int value) {
        this.numAllErase[index] = value;
    }

    public void setTrainAll(int index, int value) {
        this.trainAll[index] = value;
    }
    public void setTrainAllTries(int index, int value) {
        this.trainAllTries[index] = value;
    }
    public void setTrainWrong(int index, int value) {
        this.trainWrong[index] = value;
    }
    public void setTrainWrongError(int index, int value) {
        this.trainWrongError[index] = value;
    }
    public void setTrainWrongTries(int index, int value) {
        this.trainWrongTries[index] = value;
    }
    public void setTrainWrongErrorTries(int index, int value) {
        this.trainWrongErrorTries[index] = value;
    }

    public void calDaten(){

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

        // View => numAllWords = (TextView) inf.findViewById(R.id.numAllWords);
        int count = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                numAllWords[i] += trainAll[count];
                count++;
            }
                    }
        numAllWords[3] = trainAll[15];
        count = 0;


        // View => numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                numAllWrong[i] += trainWrong[count];
                count++;
            }
        }
        numAllWrong[3] = trainWrong[15];
        count = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                numAllWrongError[i] += trainWrongError[count];
                count++;
            }
        }
        numAllWrongError[3] = trainWrongError[15];
        count = 0;


        // View => numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError);
        for(int i = 0; i < 4; i++){
            if(numAllWords[i] == 0) {
                prozAllTrainWrong[i] = 0;
            } else {
                prozAllTrainWrong[i] = (numAllWrong[i]*100) / numAllWords[i];
            }

        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++) {
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

    }

    public void setNumNochmal1(int numNochmal1) {
        this.numNochmal1 = numNochmal1;
    }

    public void setNumARadierer1(int numARadierer1) {
        this.numARadierer1 = numARadierer1;
    }

    public void setNumAllRadierer1(int numAllRadierer1) {
        this.numAllRadierer1 = numAllRadierer1;
    }

    public void setNumNochmal6(int numNochmal6) {
        this.numNochmal6 = numNochmal6;
    }

    public void setNumARadierer6(int numARadierer6) {
        this.numARadierer6 = numARadierer6;
    }

    public void setNumAllRadierer6(int numAllRadierer6) {
        this.numAllRadierer6 = numAllRadierer6;
    }

    public void setNumNochmal11(int numNochmal11) {
        this.numNochmal11 = numNochmal11;
    }

    public void setNumARadierer11(int numARadierer11) {
        this.numARadierer11 = numARadierer11;
    }

    public void setNumAllRadierer11(int numAllRadierer11) {
        this.numAllRadierer11 = numAllRadierer11;
    }

    public void setNumNochmal16(int numNochmal16) {
        this.numNochmal16 = numNochmal16;
    }

    public void setNumARadierer16(int numARadierer16) {
        this.numARadierer16 = numARadierer16;
    }

    public void setNumAllRadierer16(int numAllRadierer16) {
        this.numAllRadierer16 = numAllRadierer16;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

}
