package com.guessmusic.bean;

/**
 * Created by len on 2017/8/19.
 */

public class UserBean {

    public static String uId;
    public static  String uPhoneInfo;
    public static  int uScore;
    public static  int uLevel;
    public static  int uDelNum;
    public static int uIdeNum;
    public static int uCoin;

    public int getuCoin() {
        return uCoin;
    }

    public void setuCoin(int uCoin) {
        UserBean.uCoin = uCoin;
    }

    public int getuLevel() {
        return uLevel;
    }

    public void setuLevel(int uLevel) {
        this.uLevel = uLevel;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setuPhoneInfo(String uPhoneInfo) {
        this.uPhoneInfo = uPhoneInfo;
    }

    public void setuScore(int uScore) {
        this.uScore = uScore;
    }



    public void setuDelNum(int uDelNum) {

        this.uDelNum = uDelNum;
    }

    public void setuIdeNum(int uIdeNum) {
        this.uIdeNum = uIdeNum;
    }

    public int getuIdeNum() {
        return uIdeNum;
    }

    public int getuDelNum() {
        return uDelNum;
    }

    public int getuScore() {
        return uScore;
    }



    public String getuId() {
        return uId;
    }

    public String getuPhoneInfo() {
        return uPhoneInfo;
    }
}
