package indi.seven.android.utils.math;

import android.util.Log;

import java.util.HashSet;
import java.util.Random;

/**
 * Description: Generate the appropriate randoms. <br/>
 * Date: 2016/11/9 <br/>
 * @author mr.hoo7793@gmail.com
 */


public class RandomGenerator {

    private static final String TAG = RandomGenerator.class.getSimpleName();

    private Random mRandom;

    private RandomGenerator(){
        mRandom = new Random();
    }

    private static class Holder{
        private static final RandomGenerator INSTANCE = new RandomGenerator();
    }

    public static RandomGenerator getInstance(){
        return  Holder.INSTANCE;
    }

    public int getIntRandom(){
        return mRandom.nextInt();
    }

    public int getNaturalRandom(){
        int random = getIntRandom();
        return Math.abs(random);
    }

    public int getNaturalRandom(int max){
        int random = getNaturalRandom();
        return random % max;
    }

    /**
     * 获取一个小于max且不等于no的随机数
     * @param max 小于max
     * @param no  不等于no
     * @return
     */
    public int getNaturalRandom(int max,int no){
        if(max <= 1){
            return 0;
        }
        int random = no;
        while(random == no){
            random = getNaturalRandom(max);
        }
        return random;
    }

    /**
     * 产生num个小于max的不重复正数
     * @param num 随机数个数
     * @param max 小于max
     * @return 产生的随机数组
     */
    public int[] getNaturalRandomArray(int num, int max){
        if(num > max){
            Log.e(TAG, "IntRandomArray size " + num + " is more than the max number " + max);
            return null;
        }
        HashSet<Integer> integerHashSet = new HashSet<>();
        int [] randomArray = new int[num];
        int cursor = 0;
        while(cursor < num){
            int random = getNaturalRandom(max);
            if(!integerHashSet.contains(random)){
                integerHashSet.add(random);
                randomArray[cursor] = random;
                cursor ++;
            }
        }
       return randomArray;
    }


}
