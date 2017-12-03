package com.example.jwj.curriculumdesign;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;

import com.guessmusic.SQLite.MusicDBHelper;
import com.guessmusic.bean.LevelBtn;
import com.guessmusic.bean.MusicBean;
import com.guessmusic.bean.UserBean;
import com.guessmusic.dao.DBDao;
import com.guessmusic.listeners.AutoLoadListener;
import com.guessmusic.listeners.MyLvBtnListener;
import com.guessmusic.utils.DialogUtil;
import com.guessmusic.utils.HTTPConnection;
import com.guessmusic.utils.JsonUtil;
import com.guessmusic.utils.MusicUtil;
import com.guessmusic.utils.ToastUtil;
import com.guessmusic.views.LevelGridView;
import com.guessmusic.views.MyGridView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.guessmusic.SQLite.MusicDBHelper.UUID;

/**
 * Created by len on 2017/8/24.
 */

public class LevelActivity extends Activity {

    private DBDao dbDao;
    private MusicDBHelper musicDBHelper;
    private List<LevelBtn> mLvBtns;//备选按钮的集合
    private LevelGridView lGv_showLevels;//显示选项的控件
    private ImageView lIvBack;
    private AutoLoadListener autoLoadListener;
    private AutoLoadListener.AutoLoadCallBack callBack;
    private SelfDialog selfDialog;
    private MediaPlayer mpBtn;
    private MediaPlayer mpAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        callBack = new AutoLoadListener.AutoLoadCallBack() {

            public void execute() {
                addLvBtn();
               //这段代码是用来请求下一页数据的
            }
        };
        initView();
        initData();
        initListener();
//        GridView view ;
    }

    @Override
    public void finish() {
        super.finish();
        mpBtn.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpBtn.release();
            }
        });
        mpAdd.release();
        //     ToastUtil.showToast(getApplicationContext(),"finish()");
    }

    @Override
    protected void onResume() {
        super.onRestart();
      //  if(!StartActivity.bgm.isPlaying())
            StartActivity.bgm.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        StartActivity.bgm.pause();
    }

    private void initListener() {
        lGv_showLevels.setOnScrollListener(autoLoadListener);

        lGv_showLevels.registOpBtnListener(new MyLvBtnListener() {
            @Override
            public void onLvBtnClick(LevelBtn btn) {
                //开启activity
                if(btn.isPass) {
                    mpBtn.start();
                    Intent intent = new Intent(LevelActivity.this, MainActivity.class);
                    intent.putExtra("level",btn.levelText-1);
                    startActivityForResult(intent, 0);
                }else{
                    Animation shake = AnimationUtils.loadAnimation(LevelActivity.this, R.anim.shake_btn);//加载动画资源文件
                    btn.levelButton.startAnimation(shake); //给组件播放动画效果
                }
            }
        });
        lIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭当前页面
                mpBtn.start();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  ToastUtil.showToast(getApplicationContext(),requestCode+"刷新level"+resultCode);
          onCreate(new Bundle());
    }

    private void initData() {
        initLvBtn();
        lGv_showLevels.updateGridView(mLvBtns);
    }
   // private int k = MusicUtil.Music_Count+1;
    private void addLvBtn(){
        addMusicData();

    }


    public static List<MusicBean> getLastJsonMusic() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String path = "";
                    URL url = null;
                    url = new URL(path);
                    URLConnection conn = url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    InputStream inStream = conn.getInputStream();
                   // return parseJSON(inStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }

    private static List<MusicBean> parseJSON(InputStream inStream) {
        List<MusicBean> musics =new ArrayList<MusicBean>();
   //     byte[] data = read(inStream);
        return null;
    }

private boolean flag = false;
    private void addMusicData(){
        //从服务器获取Music数据
        Handler handler =new Handler() {
            public void handleMessage(Message msg) {
                // UI界面的更新等相关操作
                if(msg.what==0){
                    selfDialog = new SelfDialog(LevelActivity.this);
                    selfDialog.setTitle("错误");
                    selfDialog.setMessage("网络连接失败，请重试");
                    selfDialog.setImBtnNum(1);
                    selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            if(!flag){
                                flag = true;
                                Runtime runtime = Runtime.getRuntime();
                                try {
                                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    });
                    if(!isFinishing()){
                        selfDialog.show(); flag = false;
                    }
                }else {
                    JSONObject json = JsonUtil.fromString(msg.getData().getString("str"));
                    try {
                        if (json.getInt("code") == 100) {
                            Log.i("code", json.getString("code"));
                            JSONObject pageInfo = json.getJSONObject("extend").getJSONObject("pageInfo");
                            for (int i = 0; i < pageInfo.getInt("size"); i++) {
                                JSONObject list = pageInfo.getJSONArray("list").getJSONObject(i);
                                String mid = list.getString("mid");
                                String maddress = list.getString("maddress");
                                String mmusicname = list.getString("mmusicname");
                                String mcd = list.getString("mcd");
                                Log.i("mid", mid + " ; " + maddress + " ; " + mmusicname + " ; " + mcd);

                                //把数据保存到本地服务器中
                                musicDBHelper.insertMusicDB(maddress, mmusicname, mcd);
                                lGv_showLevels.setSelection(lGv_showLevels.getLastVisiblePosition());
                            }
                            musicDBHelper.updateServicePageNum(MusicUtil.Service_pageNum + 1);
                            MusicUtil.initCountAndPageNum(LevelActivity.this);

                            mpAdd.start();
                            initData();
                        } else if (json.getJSONObject("extend").has("music")) {
                            selfDialog = new SelfDialog(LevelActivity.this);
                            selfDialog.setTitle("提示");
                            selfDialog.setMessage(json.getJSONObject("extend").getString("music"));
                            selfDialog.setImBtnNum(1);
                            selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick() {
                                    selfDialog.dismiss();
                                    DBDao dbDao = new DBDao(LevelActivity.this);
                                    UserBean user = dbDao.getUserInfo();
                                    Bundle b = new Bundle();
                                    b.putString("uid", UUID);
                                    b.putString("uscore", user.getuScore() + "");
                                    b.putString("ulevel", user.getuLevel() + "");
                                    b.putString("udelnum", user.getuDelNum() + "");
                                    b.putString("uidenum", user.getuIdeNum() + "");
                                    HTTPConnection httpCon = new HTTPConnection();
                                    httpCon.register(LevelActivity.this, new Handler(), b);

                                }
                            });
                            if(!isFinishing())
                            selfDialog.show();

                        } else {
                            selfDialog = new SelfDialog(LevelActivity.this);
                            selfDialog.setTitle("提示");
                            selfDialog.setMessage("关卡已全部加载，敬请期待");
                            selfDialog.setImBtnNum(1);
                            selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick() {
                                    selfDialog.dismiss();
                                }
                            });
                            if(!isFinishing())
                            selfDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        HTTPConnection httpCon = new HTTPConnection();
        httpCon.GET(this,handler,MusicUtil.Service_pageNum);
        Log.i("pageNum",MusicUtil.Service_pageNum+"");

    }


    private void initLvBtn() {
        mLvBtns = new ArrayList<LevelBtn>();
        Log.i("LA:uLevel",  UserBean.uLevel+"");
        for (int i = 0; i < MusicUtil.Music_Count; i++) {
            LevelBtn btn = new LevelBtn();
            if(i<=UserBean.uLevel){
                btn.levelText = i+1;
                btn.isPass = true;
            }else {
                btn.levelText = i+1;
                btn.isPass = false;
            }
            mLvBtns.add(btn);
        }
        Log.i("mLvBtns",mLvBtns.toString());
    }


    private void initView() {
        lGv_showLevels = (LevelGridView) findViewById(R.id.activity_level_gv);
        lIvBack = (ImageView)findViewById(R.id.activity_level_iv_back);
        autoLoadListener = new AutoLoadListener(callBack);
        musicDBHelper = new MusicDBHelper(this);
        dbDao = new DBDao(this);
        mpBtn = MediaPlayer.create(this,R.raw.btn);
        mpAdd = MediaPlayer.create(this,R.raw.dialog);
    }
}
