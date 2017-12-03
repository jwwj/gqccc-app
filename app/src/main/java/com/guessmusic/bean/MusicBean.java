package com.guessmusic.bean;

/**
 * Created by len on 2017/8/21.
 */

public class MusicBean {
    private int mId;//音乐序号
    private String mAddress;//文件地址+文件名字
    private String mMusicName;
    private String mCD;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmMusicName() {
        return mMusicName;
    }

    public String getmCD() {
        return mCD;
    }



    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setmMusicName(String mMusicName) {
        this.mMusicName = mMusicName;
    }

    public void setmCD(String mCD) {
        this.mCD = mCD;
    }
}
