package com.example.jwj.curriculumdesign;

import com.guessmusic.SQLite.MusicDBHelper;
import com.guessmusic.bean.AnswerBtn;
import com.guessmusic.bean.OptionBtn;
import com.guessmusic.bean.UserBean;
import com.guessmusic.dao.DBDao;
import com.guessmusic.listeners.MyOpBtnListener;

import com.guessmusic.utils.MusicUtil;
import com.guessmusic.utils.RandomUtil;
import com.guessmusic.utils.ToastUtil;
import com.guessmusic.views.MyGridView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private MyGridView mGv_showOptions;//显示选项的控件
    //   private String[] strs=MusicUtil.FIRST_OPTIONS;
    private ImageView mIv_back; //返回按钮
    private List<OptionBtn> mOpBtns;//备选按钮的集合
    private List<AnswerBtn> mAnBtns;//答案按钮的集合
    private LinearLayout mLL_answer;//答案按钮的总布局

    private MediaPlayer mpEnter;
    private MediaPlayer mpBtn;
    private MediaPlayer mpCancel;//错误提示音
    private MediaPlayer mp;//播放器
    private ImageView mpIvPlay;//播放按钮
    private ImageView mpIvCD;//cd
    private ImageView mpIvPoint;//杆
    // private Animation circle_anim;

    private TextView hTxtLevel;//关卡的Textview
    private TextView hTxtCoin;//标题中金币的TextView

    public int musicIndex = 0;//播放第几个音乐即第几关,=关卡数-1
    public int coinNum = 0;//玩家当前金币
    public int ideaNum = 100;//提示按钮的金币
    public int delNum = 50;//删除按钮的金币

    private ImageView mIvBuy1;//提示按钮
    private ImageView mIvBuy2;//删除按钮
    private ImageView mIvShare;//分享按钮
    private TextView mTxtIdea;//提示按钮金币的TextView
    private TextView mTxtDel;//删除按钮金币的TextView

    private int icount;//提示次数
    private int delcount;//删除次数

    public MusicDBHelper musicDBHelper;//数据库

    public MusicUtil cu;

    private SelfDialog selfDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //初始化数据
        Intent intent = getIntent();
        int level = intent.getIntExtra("level",-1);
        intent.removeExtra("level");
        if(level < MusicUtil.Music_Count) {
            musicIndex = level;
        }else {
            musicIndex = level - 1;
        }
       initData();
        //初始化监听
       initListener();
        //发送用户信息
        //    initEmail();
    }



    private void playSound(String soundUri){
        Uri uri = Uri.parse(soundUri);
        mp = MediaPlayer.create(this,uri);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.reset();
                mp.release();
                mp = null;
                musicStop();
            }
        });
    }
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
    public void finish() {
        super.finish();
        release();
   //     ToastUtil.showToast(getApplicationContext(),"finish()");
    }
    public void release(){
        if(mp !=null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
//        if(mpBtn!=null) {
//            mpBtn.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mpBtn.release();
//                    mpBtn = null;
//                }
//            });
//        }
//        if(mpEnter !=null) {
//            mpEnter.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mpEnter.release();
//                    mpEnter = null;
//                }
//            });
//        }
//        if(mpCancel !=null) {
//            mpCancel.stop();
//            mpCancel.release();
//            mpCancel = null;
//        }
    }

    private boolean flag=false;
    //设置播放器 播放完停止
    private void musicSet() {
//        if(flag){mp.release(); Log.i("mp:release","1");}else{flag=true;}
     //    ToastUtil.showToast(getApplicationContext(), Environment.getExternalStorageDirectory().getPath() + "/gqccc/music/__00000.m4a");
     //   Uri playUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/gqccc/__00011.mp3");
     //       mp = MediaPlayer.create(this,playUri);

        // mp = MediaPlayer.create(this, Uri.parse(cu.Music));
        // musicStart();
//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                musicStop();
//            }
//        });
    }

    //修改音乐停止后杆和播放按钮的变化
    private void musicStop() {
        mpIvPlay.setVisibility(View.VISIBLE);
        mpIvPoint.setPivotX(mpIvPoint.getWidth() / 2);
        mpIvPoint.setPivotY(0);
        mpIvPoint.setRotation(0);
        mpIvCD.clearAnimation();
    }
    private void musicStart(){
//        if(!mp.isPlaying()){
//            mp.start();
//        }
        playSound(cu.Music);
        musicStartAnim();

    }
    private void musicStartAnim(){
        //播放按钮消失，杆移动到cd上
        mpIvPlay.setVisibility(View.INVISIBLE);
        mpIvPoint.setPivotX(mpIvPoint.getWidth() / 2);
        mpIvPoint.setPivotY(50);
        mpIvPoint.setRotation(20);
        //cd转动动画
        Animation circle_anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_body_cd_circle);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            mpIvCD.startAnimation(circle_anim);  //开始动画
        }
    }

    //初始化监听
    private void initListener() {

//        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp1) {
//                mp.start();
//            }
//        });
        mpIvCD.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                release();
                musicStop();
                playSound(R.raw.enter);
            }
        });
        mpIvPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.enter);
                musicStart();
            }
        });
        mIvBuy1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDialog = new SelfDialog(MainActivity.this);
                selfDialog.setTitle("提示");
                if (icount == 0) {
                    if (coinNum - ideaNum >= 0) {
                        selfDialog.setMessage("是否花费" + ideaNum + "个金币，提示一个字符？");
                        selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                icount = 1;//提示次数
                                //ToastUtil.showToast(getApplicationContext(), "提示已花费" + ideaNum);
                                coinNum -= ideaNum;
                                if (ideaNum < 100) {
                                    ideaNum += 100;
                                }
                                UserBean.uCoin = coinNum;
                                UserBean.uDelNum = delNum;
                                UserBean.uIdeNum = ideaNum;
                                musicDBHelper.updateUserDB(UserBean.uScore,coinNum, UserBean.uLevel, delNum, ideaNum);
                                hTxtCoin.setText(coinNum + "");
                                mTxtIdea.setText(ideaNum + "");
                                //出现提示按钮
                                int anIndex = RandomUtil.RandomIdea(mAnBtns.size());
                                OptionBtn opbtn = mGv_showOptions.idea(cu, musicIndex, anIndex);
                                if (opbtn != null) {
                                    // ToastUtil.showToast(getApplicationContext(),opbtn.optionText);

                                    showAnswerBtnWords(opbtn, true, anIndex);
                                }
                                selfDialog.dismiss();
                            }
                        });
                    } else {
                        selfDialog.setImBtnNum(1);
                        selfDialog.setMessage("金币余额不足");
                        selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                //ToastUtil.showToast(getApplicationContext(), "金币余额不足");
                                selfDialog.dismiss();
                            }
                        });
                    }
                } else {
                    selfDialog.setImBtnNum(1);
                    selfDialog.setMessage("每关只能提示一次");
                    selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            //ToastUtil.showToast(getApplicationContext(), "金币余额不足");
                            selfDialog.dismiss();
                        }
                    });
                }

               // selfDialog.dismiss();

                selfDialog.setNoOnclickListener(new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        //Toast.makeText(MainActivity.this,"点击了--取消--按钮",Toast.LENGTH_LONG).show();
                        selfDialog.dismiss();
                    }
                });
                selfDialog.show();
            }
        });
        mIvBuy2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDialog = new SelfDialog(MainActivity.this);
                selfDialog.setTitle("删除");

                if (coinNum - delNum >= 0) {
                selfDialog.setMessage("是否花费"+delNum+"个金币，删除三个非答案选项？");
                selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        //Toast.makeText(MainActivity.this,"点击了--确定--按钮",Toast.LENGTH_LONG).show();

                            //ToastUtil.showToast(getApplicationContext(), "删除已花费" + delNum);
                            coinNum -= delNum;
                            if (delNum < 50) {
                                delNum += 50;
                            }
                            UserBean.uCoin=coinNum;
                            UserBean.uDelNum=delNum;
                            UserBean.uIdeNum=ideaNum;
                            musicDBHelper.updateUserDB(UserBean.uScore,coinNum,UserBean.uLevel, delNum, ideaNum);
                            hTxtCoin.setText(coinNum + "");
                            mTxtDel.setText(delNum + "");

                            //删除三个选择按钮
                            for (int i = 0; i < 3; i++) {
                                OptionBtn opbtn = mGv_showOptions.delet(cu, musicIndex);
                                delcount++;
                                if (opbtn != null) {
                                    // ToastUtil.showToast(getApplicationContext(),opbtn.optionText);
                                    opbtn.optionButton.setVisibility(View.INVISIBLE);
                                    opbtn.isViseable = false;
                                }
                            }

                        selfDialog.dismiss();
                    }
                });
                } else {
                    selfDialog.setImBtnNum(1);
                    selfDialog.setMessage("金币余额不足");
                    selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            //ToastUtil.showToast(getApplicationContext(), "金币余额不足");
                            selfDialog.dismiss();
                        }
                    });
                }
                selfDialog.setNoOnclickListener(new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                      //Toast.makeText(MainActivity.this,"点击了--取消--按钮",Toast.LENGTH_LONG).show();
                        selfDialog.dismiss();
                    }
                });
                selfDialog.show();
            }
        });
        mIvShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(getApplicationContext(), "分享");
            }
        });

        mIv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //关闭当前页面
                playSound(R.raw.btn);
                finish();
            }
        });


        mGv_showOptions.registOpBtnListener(new MyOpBtnListener() {
            @Override
            public void onOpBtnClick(OptionBtn btn) {
                //  ToastUtil.showToast(getApplicationContext(),"天天向上");
                //第一步：答案按钮上文字的显示
                playSound(R.raw.enter);
                showAnswerBtnWords(btn, false);
                //第三步：检验答案的准确性和完整性
                int i = checkedAnswer();
                switch (i) {
                    case 1:
                        // ToastUtil.showToast(getApplicationContext(),"内容不完整");
                        break;
                    case 2:
                        //ToastUtil.showToast(getApplicationContext(), "恭喜你，答对了！");
                        nextRound();
                        //ToastUtil.showToast(getApplicationContext(), musicIndex + "," + coinNum + ",");
                        break;
                    case 3:
                        wrong();
                        //ToastUtil.showToast(getApplicationContext(),"回答错误！");
                        break;
                }
            }
        });
    }

    private void wrong(){
        playSound(R.raw.cancel);
        flashColor(Color.YELLOW);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_btn);//加载动画资源文件
        for (int i = 0; i < mAnBtns.size(); i++) {
            mAnBtns.get(i).answerBtutton.startAnimation(shake); //给组件播放动画效果
        }
    }
    private int speed = 0;//答案按钮闪烁次数 即颜色第几次改变
    //错误后答案按钮闪烁三次
    private void flashColor(int color) {
        if (speed < 6) {
            speed++;
            for (int i = 0; i < mAnBtns.size(); i++) {
                mAnBtns.get(i).answerBtutton.setTextColor(color);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (speed == 1 || speed == 3 || speed == 5) {
                        flashColor(Color.WHITE);
                    } else {
                        flashColor(Color.YELLOW);
                    }
                }
            }, 100);
        } else {
            speed = 0;
            return;
        }
    }

    //加载下一关
    private void nextRound() {

       // mp.reset();
//        if(mp!=null) {
//            mp.stop();
//            mp.release();
//        }
        musicStop();

        DBDao dbDao = new DBDao(this);
        //   int level=0;
        if (musicIndex < dbDao.countMusicInfos()-1) {
            musicIndex = musicIndex + 1;
            // level= musicIndex + 1;
//            hTxtCoin.setText(coinNum + "");
//           hTxtLevel.setText(level + "");
            for (int i = 0; i < mAnBtns.size(); i++) {
                clearAnswer(mAnBtns.get(i),false);
            }
            Intent intent = new Intent(this, NextActivity.class);
            if(musicIndex>UserBean.uLevel-1) {
                //第一次过本关
                coinNum = coinNum + 100;
                UserBean.uScore += 100;
                UserBean.uCoin = coinNum;
                UserBean.uLevel += 1;
                Log.i("MA:uLevel", UserBean.uLevel + "");
                musicDBHelper.updateUserDB(UserBean.uScore, coinNum, UserBean.uLevel, delNum, ideaNum);
                release();
                //设置第二个Activity数据
                intent.putExtra("level", musicIndex);
                intent.putExtra("answer", cu.MUSIC_ANSWER);
                Log.i("Next", musicIndex + "");
                intent.putExtra("isPassAll", 0);
                intent.putExtra("isRePass",0);
                //启动第二个Activity
                startActivityForResult(intent, 0);
            }else{
                //非第一次过本关
                release();
                //设置第二个Activity数据
                intent.putExtra("level", musicIndex);
                intent.putExtra("answer", cu.MUSIC_ANSWER);
                Log.i("Next", musicIndex + "");
                intent.putExtra("isPassAll", 0);
                intent.putExtra("isRePass",1);
                //启动第二个Activity
                startActivityForResult(intent, 0);
            }
            //   ToastUtil.showToast(getApplicationContext(),musicIndex+"");
        } else {
            //通关
            Intent intent = new Intent(this, NextActivity.class);
            //设置第二个Activity数据
            intent.putExtra("level",musicIndex);
            intent.putExtra("answer",cu.MUSIC_ANSWER);
            intent.putExtra("isPassAll",1);
            intent.putExtra("isRePass",0);
            Log.i("Next",musicIndex+"");
            //启动第二个Activity
            startActivityForResult(intent,0);
            //ToastUtil.showToast(getApplicationContext(), "您已经通关！");
//            musicIndex = 0;
//            mp = MediaPlayer.create(this, Uri.parse(cu.Music));//通关后重新加载播放器
//            //level = musicIndex + 1;
////            hTxtLevel.setText(level + "");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==0&&resultCode==RESULT_OK){
            musicIndex =intent.getIntExtra("level",-1);
            //initData();
            initView();
            initData();
        }
        if(requestCode==0&&resultCode==RESULT_CANCELED){
           finish();
        }
    }

    //检查答案是否正确
    private int checkedAnswer() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < mAnBtns.size(); i++) {
            if (mAnBtns.get(i).answerText.equals("")) {
                return 1;
            } else {
                buffer.append(mAnBtns.get(i).answerText);
            }
        }
        if (buffer.toString().equals(cu.MUSIC_ANSWER)) {
            return 2;
        } else {
            return 3;
        }
    }

    //显示答案按钮上的文字
    private void showAnswerBtnWords(OptionBtn btn, boolean isIdea) {
        for (int i = 0; i < mAnBtns.size(); i++) {
            if (mAnBtns.get(i).answerText.equals("")) {
                mAnBtns.get(i).answerText = btn.optionText;
                mAnBtns.get(i).answerIndex = btn.optionIndex;
                mAnBtns.get(i).answerBtutton.setText(btn.optionText);
                //第二步：使得被选框文字显示
                btn.isViseable = false;
                btn.optionButton.setVisibility(View.INVISIBLE);
                if (isIdea == true) {
                    mAnBtns.get(i).answerBtutton.setOnClickListener(null);
                    mAnBtns.get(i).answerBtutton.setBackgroundResource(0);
                }
                break;
            }
        }
    }

    private void showAnswerBtnWords(OptionBtn btn, boolean isIdea, int i) {
        if (!mAnBtns.get(i).answerText.equals("")) {
            clearAnswer(mAnBtns.get(i),false);
        }
            mAnBtns.get(i).answerText = btn.optionText;
            mAnBtns.get(i).answerIndex = btn.optionIndex;
            mAnBtns.get(i).answerBtutton.setText(btn.optionText);
            //第二步：使得被选框文字显示
            btn.isViseable = false;
            btn.optionButton.setVisibility(View.INVISIBLE);
            if (isIdea == true) {
                mAnBtns.get(i).answerBtutton.setOnClickListener(null);
                mAnBtns.get(i).answerBtutton.setBackgroundResource(0);
            }

    }

    //初始化数据
    private void initData( ) {
        //加载用户数据，每次启动时均执行
      //  musicIndex = UserBean.uLevel - 1;
        Log.i("MA:musicIndex",musicIndex+"");
        coinNum = UserBean.uCoin;
        ideaNum = UserBean.uIdeNum;
        delNum = UserBean.uDelNum;
        //加载music
        cu.getMusic(this,musicIndex);
        Log.i("musicIndex",musicIndex+"");
        musicStart();
     //   Log.i("cu",cu.Music+","+cu.MUSIC_ANSWER);
        //初始化备选按钮
        initOpBtns();
        //将数据传递给GridView
        mGv_showOptions.updateGridView(mOpBtns);

        hTxtLevel.setText(musicIndex + 1 + "");
        hTxtCoin.setText(coinNum + "");
        mTxtDel.setText(delNum + "");
        mTxtIdea.setText(ideaNum + "");

        //初始化答案按钮
         initAnBtns();
        icount = 0;
        delcount = 0;
        //将数据设置给LinearLayout
        if (mLL_answer.getChildCount() != 0) {
            mLL_answer.removeAllViews();
        }
        //传递适配按钮大小的参数
        Resources res = getResources();
        int p = res.getInteger(R.integer._110);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(p, p);
        params.leftMargin = 20;

        for (int i = 0; i < mAnBtns.size(); i++) {
            mLL_answer.addView(mAnBtns.get(i).answerBtutton, params);
        }
        //初始化删除的随机数
        RandomUtil.randomDel = new Random();
        //初始化提示的随机数
        RandomUtil.randomIdea = new Random();
        //初始化随机opbtn
     // MusicUtil.FIRST_OPTIONS = RandomUtil.RandomString(MusicUtil.FIRST_OPTIONS1);
        //      mGv_showOptions.setAdapter(new MyGridViewAdapter(strs, this));
    }

    private void initAnBtns() {
        mAnBtns = new ArrayList<AnswerBtn>();
        for (int i = 0; i < cu.MUSIC_ANSWER.length(); i++) {
            // ToastUtil.showToast(this,MusicUtil.MUSIC_ANSWER[musicIndex]+":"+MusicUtil.MUSIC_ANSWER[musicIndex].length());
            final AnswerBtn btn = new AnswerBtn();
            View view = View.inflate(this, R.layout.main_activity_foot_gridview_option_button, null);
            btn.answerBtutton = (Button) view.findViewById(R.id.main_activity_gv_option_item_btn);
            btn.answerBtutton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   ToastUtil.showToast(getApplicationContext(),"好好学习");
                    clearAnswer(btn,true);
                }
            });
            btn.answerBtutton.setTextColor(Color.WHITE);
            btn.answerBtutton.setBackgroundResource(R.drawable.game_wordblank);
            mAnBtns.add(btn);
        }
    }

    private void clearAnswer(AnswerBtn btn,boolean isPlaympEnter) {
        if (btn.answerText != "") {
            if(isPlaympEnter) {
                playSound(R.raw.enter);
            }
            btn.answerText = "";
            btn.answerBtutton.setText("");
            mOpBtns.get(btn.answerIndex).isViseable = true;
            mOpBtns.get(btn.answerIndex).optionButton.setVisibility(View.VISIBLE);
        }
    }
    //初始化备选按钮
    private void initOpBtns() {
        mOpBtns = new ArrayList<OptionBtn>();
        for (int i = 0; i < MusicUtil.FIRST_OPTIONS.length; i++) {
            Log.i("opbtn", MusicUtil.FIRST_OPTIONS[i]);
            OptionBtn btn = new OptionBtn();
            btn.optionText = MusicUtil.FIRST_OPTIONS[i];
            btn.isViseable = true;
            mOpBtns.add(btn);
        }
        Log.i("mOpBtns",mOpBtns.toString());
    }

    //初始化控件
    private void initView() {
        mGv_showOptions = (MyGridView) findViewById(R.id.activity_main_foot_gv);
        mIv_back = (ImageView) findViewById(R.id.activity_main_head_iv_back);
        mLL_answer = (LinearLayout) findViewById(R.id.activity_foot_ll);
        mIvBuy1 = (ImageView) findViewById(R.id.main_activity_btn_buy1);
        mIvBuy2 = (ImageView) findViewById(R.id.main_activity_btn_buy2);
        mIvShare = (ImageView) findViewById(R.id.main_activity_btn_share);
        mpIvPlay = (ImageView) findViewById(R.id.activity_main_body_iv_play);
        mpIvPoint = (ImageView) findViewById(R.id.activity_main_body_iv_point);
        mpIvCD = (ImageView) findViewById(R.id.activity_main_body_iv_cd);
        hTxtCoin = (TextView) findViewById(R.id.main_activity_tv_coin);
        hTxtLevel = (TextView) findViewById(R.id.main_activity_tv_level);
        mTxtDel = (TextView) findViewById(R.id.main_activity_btn_coin2);
        mTxtIdea = (TextView) findViewById(R.id.main_activity_btn_coin1);
        musicDBHelper = new MusicDBHelper(this);
        cu = new MusicUtil();

//        mpCancel = MediaPlayer.create(this,R.raw.cancel);
//        mpBtn = MediaPlayer.create(this,R.raw.btn);
//        mpEnter = MediaPlayer.create(this,R.raw.enter);

    }
}
