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

public class Data extends Application {

    public static String nameStudent;

    public static String nameErrorcategory = "Error Category";

    public static int[] trainAll = {75,75,75,75,75,63,63,20,63,20,78,78,30,30,78,30};
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

    // FragmentOneFive
    // Result
    public static int numNochmal1 = 1;
    public static int numARadierer1 = 1;
    public static int numAllRadierer1 = 1;


    //FragmentSixTen
    // Result
    public static int numNochmal6 = 6;
    public static int numARadierer6 = 6;
    public static int numAllRadierer6 = 6;


    //FragmentElevenFifteen
    public static int numNochmal11 = 11;
    public static int numARadierer11 = 11;
    public static int numAllRadierer11 = 11;

    //FragmentSixteen
    // Result
    public static int numNochmal16 = 16;
    public static int numARadierer16 = 16;
    public static int numAllRadierer16 = 16;

    public static void setTrainAll(int index, int value) {
        trainAll[index] = value;
    }

    public static void calDaten(){

        prozTrainWrong = new double[16];
        for(int i = 0; i < 16; i++){
            if(trainAll[i] == 0){
                prozTrainWrong[i] = 0;
            } else {
                prozTrainWrong[i] = (trainWrong[i] * 100) / trainAll[i];
            }

        }

        prozTrainWrongError = new double[16];

        for(int i = 0; i < 16; i++){
            if(trainAll[i] == 0){
                prozTrainWrongError[i] = 0;
            } else {
                prozTrainWrongError[i] = (trainWrongError[i] * 100) / trainAll[i];
            }
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
            if(numAllWords[i] == 0) {
                prozAllTrainWrong[i] = 0;
            } else {
                prozAllTrainWrong[i] = (numAllWrong[i]*100) / numAllWords[i];
            }

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
            if( numAllWords[i] == 0) {
                prozAllTrainWrongError[i] = 0;
            } else {
                prozAllTrainWrongError[i]= (numAllWrongError[i]*100) / numAllWords[i];
            }

        }


    }





    @Override
    public void onCreate(){
        super.onCreate();
    }

    public static void setNameStudent(String name){
        nameStudent = name;
    }


}
