package com.guessmusic.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jwj.curriculumdesign.MainActivity;
import com.example.jwj.curriculumdesign.R;
import com.example.jwj.curriculumdesign.StartActivity;
import com.guessmusic.utils.MusicUtil;

/**
 * Created by len on 2017/8/19.
 */

public class MusicDBHelper extends SQLiteOpenHelper {


    public static String UUID;
    private Context context;

    private SQLiteDatabase db;
    private static final String CREAT_USER_SQL = "create table tbl_user" + "(uId TEXT PRIMARY KEY , uPhoneInfo TEXT,uScore integer,uLevel integer,uDelNum integer,uIdeNum integer, servicePageNum integer,uCoin integer)";
    private static final String CREAT_MUSIC_SQL = "create table tbl_music" + "(mId integer primary key AUTOINCREMENT,mAddress TEXT,mMusicName TEXT,mCD TEXT)";

    public MusicDBHelper(Context context) {
        super(context, "music.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREAT_USER_SQL);
        db.execSQL(CREAT_MUSIC_SQL);
        UUID = MakeUuid.get();// 创建一个新的UUID
        initUserDB();
        initMusicDB();


//        for(int i=0;i<=10;i++) {
//            if(i<10) {
//                CopyFileToSDUtil toSD = new CopyFileToSDUtil("gqccc", "__0000" + i + ".m4a", context, R.raw.__00000 + i);
//            }else if(i>=10){
//                CopyFileToSDUtil toSD = new CopyFileToSDUtil("gqccc", "__000" + i + ".m4a", context, R.raw.__00000 + i);
//            }
//        }
//        copyFilesFromRaw(context,R.raw.__00000,"__00000", Environment.getExternalStorageDirectory().getPath()+"/gqccc");
        //      FileStorageHelper.copyFilesFromRaw(this,R.raw.doc_test,"doc_test",path + "/" + "mufeng");
    }

    private void initMusicDB() {
        for(int i=0;i<=12;i++) {
            int address = R.raw.__00000+i;
            if(i<10) {

                insertInitMusicDB("android.resource://" + context.getPackageName() + "/"+address, MusicUtil.MUSIC_NAME[i], null );
            }else if(i>=10){
                insertInitMusicDB("android.resource://" + context.getPackageName() + "/"+address, MusicUtil.MUSIC_NAME[i], null );
            }
        }
    }

    public void insertInitMusicDB(String mAddress, String mMusicName,String mCD) {
        //修改SQL语句
        String sql = "insert into tbl_music (mId,mAddress,mMusicName,mCD) values(null,'" + mAddress + "','"+mMusicName+"','"+mCD+"')";
        //执行SQL
        db.execSQL(sql);
        Log.i("Insert",sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //初始化用户数据,只在第一次打开APP执行
    public int initUserDB() {
//        db = getWritableDatabase();
        int i = 0;
        ContentValues cv = new ContentValues();
        cv.put("uId", UUID);
        cv.put("uPhoneInfo", StartActivity.screenParams);
        cv.put("uScore",0);
        cv.put("uLevel", 0);
        cv.put("uDelNum",50);
        cv.put("uIdeNum", 100);
        cv.put("servicePageNum",1);
        cv.put("uCoin",0);
        i = (int) db.insert("tbl_user", null, cv);
        //      db.close();
        return i;
    }
    public void updateServicePageNum(int servicePageNum) {
        db = getWritableDatabase();
        //修改SQL语句
        String sql = "update tbl_user set servicePageNum = " + servicePageNum +  " where uId = '" + UUID+"'";
        //执行SQL
        db.execSQL(sql);
        db.close();
    }

    public void updateUserDB(int uScore,int uCoin, int uLevel, int uDelNum, int uIdeNum) {
        db = getWritableDatabase();
        //修改SQL语句
        String sql = "update tbl_user set uScore ="+uScore+", uCoin = " + uCoin + ",uLevel = " + uLevel + ",uDelNum =" + uDelNum + ",uIdeNum =" + uIdeNum + " where uId = '" + UUID+"'";
        //执行SQL
        db.execSQL(sql);
        db.close();
    }




    public Cursor userQuery(){
        db=getWritableDatabase();
        Cursor c = db.query ("tbl_user",null,null,null,null,null,null);
        return c;
    }


    public void insertMusicDB( String mAddress, String mMusicName,String mCD) {
        db = getWritableDatabase();
        //修改SQL语句
        String sql = "insert into tbl_music (mAddress,mMusicName,mCD) values('" + mAddress + "','"+mMusicName+"','"+mCD+"')";
        //执行SQL
        db.execSQL(sql);
        db.close();
    }



    public void close() {
        if (db != null) {
            db.close();
        }
    }



}
