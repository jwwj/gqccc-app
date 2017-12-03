package com.guessmusic.bean;

import android.widget.Button;

/**
 * Created by len on 2017/8/24.
 */

public class LevelBtn {
    public int levelText;//备选按钮的文字
    public int levelIndex;//备选按钮的索引
    public Button levelButton;//备选按钮
    public boolean isPass;//设置是否通过本关

    public LevelBtn(){
        this.levelText=-1;
        this.isPass=false;
    }
}
