package com.guessmusic.utils;

import android.content.Context;
import android.util.Log;

import com.example.jwj.curriculumdesign.R;
import com.guessmusic.SQLite.MakeUuid;
import com.guessmusic.SQLite.MusicDBHelper;
import com.guessmusic.bean.MusicBean;
import com.guessmusic.dao.DBDao;

import java.util.Random;
import java.util.StringTokenizer;

//public class MusicUtil {
//    //第一关备选按钮
//    public final static String[] FIRST_OPTIONS1={"乡","故","征","服","童","话","同","桌","小","姐","七","里","香","董","奇","大","海","后","来","你","的","背","包","再","见","老","男","孩","龙","昏","传","人"};
//   public static String[] FIRST_OPTIONS=RandomUtil.RandomString(FIRST_OPTIONS1);
//    //第一关答案按钮
//   // public final static String[] FIRST_ANSWER={"太","原"};
//    //音乐
//    public final static int[] MUSIC={R.raw.__00000,R.raw.__00001,R.raw.__00002,R.raw.__00003,R.raw.__00004,R.raw.__00005,R.raw.__00006,R.raw.__00007,R.raw.__00008,R.raw.__00009,R.raw.__00010};
//    public final static String[] MUSIC_ANSWER={"征服","童话","同桌的你","七里香","传奇","大海","后来","你的背包","再见","老男孩","龙的传人"};
//}

public class MusicUtil {

    // private static String mMusicName;
    public String Music;//本关卡音乐文件地址
    public String MUSIC_ANSWER;//本关卡答案
    // private static String mMusicName;
    public String mCD;
    public static int Music_Count;//音乐总数
    public static int Service_pageNum;//需加载第几页

    //备选按钮
    public static String[] FIRST_OPTIONS1 = {"乡", "故", "征", "服", "童", "话", "同", "桌", "小", "姐", "七", "里", "香", "董", "奇", "大", "海", "后", "来", "你", "的", "背", "包", "再", "见", "老", "男", "孩", "龙", "昏", "传", "人"};
    public static String[] FIRST_OPTIONS = RandomUtil.RandomString(FIRST_OPTIONS1);

    //默认歌曲，初次开启需把如下数据加入数据库，不可删除
    public final static String[] MUSIC_NAME = {"征服", "童话", "同桌的你", "七里香", "传奇", "大海", "后来", "你的背包", "再见", "老男孩", "龙的传人", "月亮代表我的心", "大城小爱"};

    //    public static void initCountAndPageNum(Context context){
//        DBDao dbDao = new DBDao(context);
//        MusicDBHelper musicDBHelper = new MusicDBHelper(context);
//        Music_Count = dbDao.countMusicInfos();
//        int flag = dbDao.getServicePageNum();
//        if(flag == (MusicUtil.Music_Count-13)/5+1) Music_pageNum = -1;
//        else {
//            Music_pageNum = (MusicUtil.Music_Count - 13) / 5 + 1;
//            musicDBHelper.updateServicePageNum(Music_pageNum);
//        }
//    }
    public static void initCountAndPageNum(Context context) {
        DBDao dbDao = new DBDao(context);
        Music_Count = dbDao.countMusicInfos();
        Service_pageNum = dbDao.getServicePageNum();
    }

    public void getMusic(Context context, int musicIndex) {
        DBDao dbDao = new DBDao(context);
        MusicBean musicBean = dbDao.getMusicInfo(musicIndex);
        Log.i("MU:musicIndex", musicIndex + "");
        this.Music = musicBean.getmAddress();
        this.MUSIC_ANSWER = musicBean.getmMusicName();
        this.mCD = musicBean.getmCD();
        getOptions(context);
    }

    public void getOptions(Context context) {
        char[] ma = new char[32];

        for (int m = 0; m < MUSIC_ANSWER.length(); m++) {
            ma[m] = MUSIC_ANSWER.charAt(m);
        }
        //   Log.i("ma",String.valueOf(ma));

        DBDao dbDao = new DBDao(context);

        Random randomInt = new Random();
        // int result[] = new int[num];
        int random;
        boolean flag = true;
        int i = MUSIC_ANSWER.length();
        int result[] = new int[Music_Count];
        while (i < 32) {
            random = randomInt.nextInt(Music_Count);
            //      Log.i("random",random+"");
            if (result[random] == 1) {
                continue;
            } else {

                result[random] = 1;

                MusicBean musicBean = dbDao.getMusicInfo(random);
                char newChar[] = musicBean.getmMusicName().toCharArray();
                //     Log.i("newChar", String.valueOf(newChar));
                for (int k = 0; k < newChar.length; k++) {
                    //      Log.i("newChar[" + i + "]", "" + newChar[k]);
                    if (i >= 32) {
                        break;
                    }
                    for (int j = 0; j < i; j++) {
                        if (ma[j] == newChar[k]) {
                            flag = false;
                        }
                    }
                    if (flag == true) {
                        ma[i] = newChar[k];
                        i++;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        Log.i("newMa", String.valueOf(ma));
        for (int m = 0; m < 32; m++) {
            FIRST_OPTIONS1[m] = String.valueOf(ma[m]);

            //   Log.i("FOP",FIRST_OPTIONS1[m]);
        }
        FIRST_OPTIONS = RandomUtil.RandomString(FIRST_OPTIONS1);
    }
}