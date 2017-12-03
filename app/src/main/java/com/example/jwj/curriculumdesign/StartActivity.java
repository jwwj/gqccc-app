package com.example.jwj.curriculumdesign;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.guessmusic.SQLite.MusicDBHelper;
import com.guessmusic.bean.LevelBtn;
import com.guessmusic.bean.UserBean;
import com.guessmusic.dao.DBDao;
import com.guessmusic.utils.HTTPConnection;
import com.guessmusic.utils.JsonUtil;
import com.guessmusic.utils.MusicUtil;
import com.guessmusic.utils.ToastUtil;
import com.guessmusic.utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.guessmusic.SQLite.MusicDBHelper.UUID;

/**
 * Created by len on 2017/8/20.
 */

public class StartActivity extends Activity{

    private MediaPlayer mpBtn;
    public static MediaPlayer bgm;

    private ImageView sIvStart;
    private ImageView sIvRank;
    private ImageView sIvWechat;

    private boolean flag=true;

    public  MusicDBHelper musicDBHelper;//数据库
    public static String screenParams;//用户屏幕信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_start);
        //verifyStoragePermissions();

        initView();
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onRestart();
        if(!bgm.isPlaying())
        bgm.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(flag) {
            bgm.pause();
        }else {
            flag = true;
        }
    }

    private void initData() {
        //获取用户手机屏幕信息
        Test_getScreen();

        bgm.setLooping(true);
        bgm.start();

        DBDao dbDao = new DBDao(this);
        UserBean user = dbDao.getUserInfo();
        UUID = user.getuId();
        UserBean.uLevel = user.getuLevel();
        Log.i("UUID", UUID+"");
        MusicUtil.initCountAndPageNum(this);
//        MusicUtil.Music_Count = dbDao.countMusicInfos();


        b.putString("uid",UUID);
        b.putString("uphone",screenParams);
        b.putString("uscore",user.getuScore()+"");
        b.putString("ulevel",user.getuLevel()+"");
        b.putString("udelnum",user.getuDelNum()+"");
        b.putString("uidenum",user.getuIdeNum()+"");


        HTTPConnection httpCon = new HTTPConnection();
        httpCon.register(this,handler,b);

    }
    private   Bundle b=new Bundle();
   private Handler handler =new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                //  ToastUtil.showToast(getApplicationContext(), "网络连接失败");
                HTTPConnection httpCon = new HTTPConnection();
                httpCon.register(StartActivity.this,new Handler(),b);
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

    private void initListener() {
        sIvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgm.pause();
                mpBtn.start();

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                //启动第二个Activity
                intent.putExtra("level", UserBean.uLevel);
                startActivity(intent);

            }
        });
        sIvRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//rank
                flag=false;
                mpBtn.start();
                Intent intent = new Intent(StartActivity.this, LevelActivity.class);
                //启动第二个Activity
                startActivity(intent);
              //  ToastUtil.showToast(getApplicationContext(),"Rank");
            }
        });
        sIvWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//wechat
               // flag=false;
//                mpBtn.start();
//                Intent intent = new Intent(StartActivity.this, WeChatActivity.class);
//                //启动第二个Activity
//                startActivity(intent);
                ToastUtil.showToast(getApplicationContext(),"WeChat");
            }
        });
    }

    private void initView() {
        bgm = MediaPlayer.create(this,R.raw.bgm);
        mpBtn =  MediaPlayer.create(this,R.raw.btn);

        sIvStart = (ImageView)  findViewById(R.id.start_activity_imv_start);
         sIvRank = (ImageView)  findViewById(R.id.start_activity_imv_rank);
        sIvWechat = (ImageView)  findViewById(R.id.start_activity_imv_wechat);

        musicDBHelper = new MusicDBHelper(this);
    }

    //获取屏幕信息
    public String Test_getScreen() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float density = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        //ToastUtil.showToast(getApplicationContext(), density + " , " + densityDPI+" : " + screenWidth + "x" + screenHeight);

        String screenParams = density + " , " + densityDPI + " , " + screenWidth + "x" + screenHeight;

        return this.screenParams = screenParams;
    }

    private void verifyStoragePermissions() {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框

                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
