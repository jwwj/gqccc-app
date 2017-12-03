package com.guessmusic.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by len on 2017/6/30.
 */

public class ToastUtil {
    /**
     * @param context 上下文环境
     * @param text 需要提示给用户的信息
     */
    public static void showToast(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
