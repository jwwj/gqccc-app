package com.guessmusic.bean;

import android.widget.Button;

/**
 * Created by len on 2017/6/30.
 */

public class OptionBtn {
    public String optionText;//备选按钮的文字
    public int optionIndex;//备选按钮的索引
    public Button optionButton;//备选按钮
    public boolean isViseable;//设置是否可见

    public OptionBtn(){
        this.optionText="";
        this.isViseable=true;
    }
}
