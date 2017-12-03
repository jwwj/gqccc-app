package com.example.jwj.curriculumdesign;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.guessmusic.bean.LevelBtn;
import com.guessmusic.bean.UserBean;
import com.guessmusic.listeners.AutoLoadListener;
import com.guessmusic.utils.MusicUtil;
import com.guessmusic.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by len on 2017/8/26.
 */

public class WeChatActivity extends Activity {

    private List<LevelBtn> mLvBtns;//备选按钮的集合
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
}