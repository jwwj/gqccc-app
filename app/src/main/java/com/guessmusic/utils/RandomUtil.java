package com.guessmusic.utils;

/**
 * Created by len on 2017/7/3.
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class RandomUtil {
    public static String[] RandomString(String[] str) {
        if (str != null) {
            // String[] newStr = null;
            List list = Arrays.asList(str);
            Collections.shuffle(list);
            String[] newStr = (String[]) list.toArray(new String[0]);
            return newStr;
        } else {
            return null;
        }
    }

    public static Random randomDel = new Random();;
    public static int RandomDel() {
        int result=randomDel.nextInt(MusicUtil.FIRST_OPTIONS.length);
        return result;
    }
    public static Random randomIdea = new Random();;
    public static int RandomIdea(int index) {
        int result=randomIdea.nextInt(index);
        return result;
    }

    //生成num个不重复范围是scope的随机数
    public static int[] randomInts(int num,int scope) {
        if (num > scope) {
            return null;
        } else {
            Random randomInt = new Random();
            int result[] = new int[num];
            int random;
            boolean flag = true;
            int i = 0;
            while (i < num) {
                random = randomInt.nextInt(scope);
                for (int j = 0; j < i; j++) {
                    if (result[j] == random) {
                        flag = false;
                    }
                }
                if (flag == true) {
                    result[i] = random;
                    i++;
                } else {
                    flag = true;
                }
            }
            return result;
        }
    }
}