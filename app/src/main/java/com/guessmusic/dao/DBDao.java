package com.guessmusic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.guessmusic.SQLite.MusicDBHelper;
import com.guessmusic.bean.MusicBean;
import com.guessmusic.bean.UserBean;

import java.util.logging.Level;

/**
 * Created by len on 2017/8/19.
 */

public class DBDao {
    final  MusicDBHelper musicDBHelper;

    public DBDao(Context context) {
        this.musicDBHelper = new MusicDBHelper(context);
    }

    //从数据库中读取用户数据到程序中
    public UserBean getUserInfo(){
        SQLiteDatabase db=musicDBHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from tbl_user limit 0,1;", null);
        if(cursor.moveToFirst()){
            String uid=cursor.getString(cursor.getColumnIndex("uId"));
            String phoneInfo=cursor.getString(cursor.getColumnIndex("uPhoneInfo"));
            int score=cursor.getInt(cursor.getColumnIndex("uScore"));
            int level=cursor.getInt(cursor.getColumnIndex("uLevel"));
            int delNum=cursor.getInt(cursor.getColumnIndex("uDelNum"));
            int ideNum=cursor.getInt(cursor.getColumnIndex("uIdeNum"));
            int coin=cursor.getInt(cursor.getColumnIndex("uCoin"));

            UserBean user=new UserBean();
            user.setuId(uid);
            user.setuPhoneInfo(phoneInfo);
            user.setuScore(score);
            user.setuLevel(level);
            user.setuDelNum(delNum);
            user.setuIdeNum(ideNum);
            user.setuCoin(coin);
            return user;
        }
        cursor.close();
        return null;
    }

    //从数据库中读取一首音乐数据到程序中
    public MusicBean getMusicInfo(int musicIndex){
        SQLiteDatabase db=musicDBHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from tbl_music where mId = ?;", new String []{musicIndex+1+""});
        //Log.i("musicIndex",musicIndex+"");

        if(cursor.moveToFirst()){
            int mid=cursor.getInt(cursor.getColumnIndex("mId"));
            String maddress=cursor.getString(cursor.getColumnIndex("mAddress"));
            String mmusicName=cursor.getString(cursor.getColumnIndex("mMusicName"));
            String mcd=cursor.getString(cursor.getColumnIndex("mCD"));
           //(mId integer primary key AUTOINCREMENT,mAddress TEXT,mMusicName TEXT,mCD TEXT)";

            Log.i("tbl_music",mid+","+maddress+","+mmusicName+","+mcd);

            MusicBean music = new MusicBean();
            music.setmId(mid);
            music.setmAddress(maddress);
            music.setmMusicName(mmusicName);
            music.setmCD(mcd);
            return music;
        }
        Log.i("tbl_music","null");
        cursor.close();
        return null;
    }
    public int countMusicInfos(){
        SQLiteDatabase db=musicDBHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("select count(*) from tbl_music", null);
        //Log.i("musicIndex",musicIndex+"");
        if(cursor.moveToFirst()){
            int num=cursor.getInt(cursor.getColumnIndex("count(*)"));
            return num;
        }
        cursor.close();
        return 0;
    }
    public int  getServicePageNum(){
        SQLiteDatabase db=musicDBHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("select servicePageNum from tbl_user limit 0,1;", null);
        if(cursor.moveToFirst()){

            int servicePageNum=cursor.getInt(cursor.getColumnIndex("servicePageNum"));

            return servicePageNum;
        }
        cursor.close();
        return -1;
    }
}
