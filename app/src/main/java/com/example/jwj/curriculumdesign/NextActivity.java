package com.example.jwj.curriculumdesign;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guessmusic.bean.UserBean;
import com.guessmusic.dao.DBDao;
import com.guessmusic.utils.DialogUtil;
import com.guessmusic.utils.HTTPConnection;
import com.guessmusic.utils.JsonUtil;
import com.guessmusic.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.guessmusic.SQLite.MusicDBHelper.UUID;

/**
 * Created by len on 2017/8/22.
 */

public class NextActivity extends Activity{

    private MediaPlayer mpBtn;
    private MediaPlayer mpWin;

    private TextView nTxtTitle;
    private TextView nTxtLevel;
    private TextView nTxtAnswer;
    private ImageView nIvNext;
    private ImageView nIvShare;
    private ImageView nIvBack;//下一关中通关的按钮
    private TextView nTxCoin;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Log.i("NextActivity","start");
        initView();
        initData();
        initListener();

    }
    private  Bundle b;
    private void updateUserInfo() {
        DBDao dbDao = new DBDao(NextActivity.this);
        UserBean user = dbDao.getUserInfo();
        b = new Bundle();
        b.putString("uid", UUID);
        b.putString("uscore", user.getuScore() + "");
        b.putString("ulevel", user.getuLevel() + "");
        b.putString("udelnum", user.getuDelNum() + "");
        b.putString("uidenum", user.getuIdeNum() + "");

        HTTPConnection httpCon = new HTTPConnection();
        httpCon.updateUserInfo(this, handler, b);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                //  ToastUtil.showToast(getApplicationContext(), "网络连接失败");
            }else{
                JSONObject json = JsonUtil.fromString(msg.getData().getString("str"));
                try {
                    if (json.getInt("code") != 100) {
                        //ToastUtil.showToast(getApplicationContext(), "同步用户信息失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void playSound(int soundID){
        final MediaPlayer sound = MediaPlayer.create(this,soundID);
        sound.start();
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                sound.reset();
                sound.release();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {

       //     DialogUtil.showNormalDialog(this);
        }

        return false;
    }

    private void initData() {
        Intent intent = getIntent();
        level = intent.getIntExtra("level", -1);
        String answer = intent.getStringExtra("answer");

        playSound(R.raw.win);
        nTxtLevel.setText(level + "");
        nTxtAnswer.setText(answer);

        //加粗
        TextPaint paint = nTxtTitle.getPaint();
        paint.setFakeBoldText(true);
        updateUserInfo();

        if (intent.getIntExtra("isPassAll", 0) == 1) {
            //通关1
            nIvNext.setVisibility(View.INVISIBLE);
            nIvBack.setVisibility(View.VISIBLE);
        } else {
            //加载下一关0
            nIvNext.setVisibility(View.VISIBLE);
            nIvBack.setVisibility(View.INVISIBLE);
        }
        if(intent.getIntExtra("isRePass",0) == 1){
            //再玩一次此关
            nTxCoin.setText("×   0");
        }else{
            //第一次过此关
            nTxCoin.setText("×   100");
        }
    }

    private void initListener() {
        nIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.btn);
                Intent intent =new Intent();
                intent.putExtra("level", level);
                Log.i("NA:level",level+"");
                NextActivity.this.setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
        nIvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.btn);
                Intent intent =new Intent();
                intent.putExtra("level", level);
                Log.i("NA:level",level+"");
                NextActivity.this.setResult(RESULT_OK,intent);
                finish();
            }
        });
        nIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.btn);
            ToastUtil.showToast(getApplicationContext(),"分享到微信");
            }
        });


    }

    private void initView() {
        nTxtTitle=(TextView)findViewById(R.id.next_activity_tx_title);
         nTxtLevel = (TextView)findViewById(R.id.next_activity_tv_level);
         nTxtAnswer = (TextView)findViewById(R.id.next_activity_tv_answer);
         nIvNext = (ImageView) findViewById(R.id.next_activity_im_next);
         nIvShare = (ImageView) findViewById(R.id.next_activity_im_share);
        nIvBack = (ImageView) findViewById(R.id.next_activity_im_back);
        nTxCoin = (TextView)findViewById(R.id.next_activity_tx_addcoin);
//        mpBtn = MediaPlayer.create(this,R.raw.btn);
//        mpWin = MediaPlayer.create(this,R.raw.win);
    }
}
