package com.guessmusic.bean;

import android.widget.Button;

/**
 * 答案按钮对象的封装
 * Created by len on 2017/6/30.
 */

public class AnswerBtn {
    public String answerText;//按钮上的文字
    public int answerIndex;//答案按钮上的索引
    public Button answerBtutton;//button按钮
    public AnswerBtn(){
        this.answerText="";
    }
}
