package com.guessmusic.utils;

/**
 * Created by len on 2017/8/29.
 */
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.jwj.curriculumdesign.LevelActivity;
import com.example.jwj.curriculumdesign.SelfDialog;
import com.guessmusic.SQLite.MusicDBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.UUID;

public class HTTPConnection {

private SelfDialog selfDialog;
// 以下代码实现了以GET方式发起HTTP请求
// 连接网络是耗时操作，一般新建线程进行

//    public Handler handler =new Handler(){};

    public void GET(final Context context, final Handler handler,final int pn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                boolean isConnectionError = false;
                InputStream in = null;
                try {
                    // 调用URL对象的openConnection方法获取HttpURLConnection的实例
                    URL url = new URL("http://gqccc.reallct.cn/api/musics?pn=" + pn + "&uid=" + MusicDBHelper.UUID);
                    connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方式，GET或POST
                    connection.setRequestMethod("GET");
                    // 设置连接超时、读取超时的时间，单位为毫秒（ms）
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // getInputStream方法获取服务器返回的输入流
                    in = connection.getInputStream();
                    Log.i("isConnectionError", isConnectionError + "");
                } catch (IOException e) {
                    Log.i("isConnectionErrorCatch", isConnectionError + "");
                    isConnectionError = true;
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                if (!isConnectionError) {
                    try {
                        // 使用BufferedReader对象读取返回的数据流
                        // 按行读取，存储在StringBuider对象response中
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        Log.i("http", stringBuilder.toString());

                        Bundle response = new Bundle();
                        response.putString("str", stringBuilder.toString());
                        Message msg = new Message();
                        msg.setData(response);
                        msg.what = 1;
                        handler.sendMessage(msg);

                        //..........
                        // 此处省略处理数据的代码
                        // 若需要更新UI，需将数据传回主线程，具体可搜索android多线程编程
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        if (connection != null) {
                            // 结束后，关闭连接
                            connection.disconnect();
                        }
                    }
                }
            }
        }).start();
    }

    public void updateUserInfo(final Context context, final Handler handler,final Bundle b) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                boolean isConnectionError = false;
                InputStream in = null;
                try {
                    //http://localhost:8080/ssm-gqccc/user?uid=bbb&uphone=ccc&uscore=100&ulevel=1&udelnum=100&uidenum=100
                    // 调用URL对象的openConnection方法获取HttpURLConnection的实例
                    URL url = new URL("http://gqccc.reallct.cn/api/updateuser?uid="+ b.getString("uid")+
                            "&uscore="+b.getString("uscore")+"&ulevel="+b.getString("ulevel")+"&udelnum="+b.getString("udelnum")+"&uidenum="+b.getString("uidenum"));
                    connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方式，GET或POST
                    connection.setRequestMethod("GET");
                    // 设置连接超时、读取超时的时间，单位为毫秒（ms）
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // getInputStream方法获取服务器返回的输入流
                    in = connection.getInputStream();
                //    Log.i("isConnectionError", isConnectionError + "");
                } catch (IOException e) {
                //    Log.i("isConnectionErrorCatch", isConnectionError + "");
                    isConnectionError = true;
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                if (!isConnectionError) {
                    try {
                        // 使用BufferedReader对象读取返回的数据流
                        // 按行读取，存储在StringBuider对象response中
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        Log.i("http", stringBuilder.toString());

                        Bundle response = new Bundle();
                        response.putString("str", stringBuilder.toString());
                        Message msg = new Message();
                        msg.setData(response);
                        msg.what = 1;
                        handler.sendMessage(msg);

                        //..........
                        // 此处省略处理数据的代码
                        // 若需要更新UI，需将数据传回主线程，具体可搜索android多线程编程

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            // 结束后，关闭连接
                            connection.disconnect();
                        }
                    }
                }
            }
        }).start();
    }

    public void register(final Context context, final Handler handler,final Bundle b) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                boolean isConnectionError = false;
                InputStream in = null;
                try {
                    //http://localhost:8080/ssm-gqccc/user?uid=bbb&uphone=ccc&uscore=100&ulevel=1&udelnum=100&uidenum=100
                    // 调用URL对象的openConnection方法获取HttpURLConnection的实例
                    URL url = new URL("http://gqccc.reallct.cn/api/user?uid="+ b.getString("uid")+
                            "&uphone="+b.getString("uphone")+"&uscore="+b.getString("uscore")+"&ulevel="+b.getString("ulevel")+"&udelnum="+b.getString("udelnum")+"&uidenum="+b.getString("uidenum"));
                    connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方式，GET或POST
                    connection.setRequestMethod("GET");
                    // 设置连接超时、读取超时的时间，单位为毫秒（ms）
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // getInputStream方法获取服务器返回的输入流
                    in = connection.getInputStream();
                 //   Log.i("isConnectionError", isConnectionError + "");
                } catch (IOException e) {
                   // Log.i("isConnectionErrorCatch", isConnectionError + "");
                    isConnectionError = true;
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                if (!isConnectionError) {
                    try {
                        // 使用BufferedReader对象读取返回的数据流
                        // 按行读取，存储在StringBuider对象response中
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        Log.i("http", stringBuilder.toString());

                        Bundle response = new Bundle();
                        response.putString("str", stringBuilder.toString());
                        Message msg = new Message();
                        msg.setData(response);
                        msg.what=1;
                        handler.sendMessage(msg);

                        //..........
                        // 此处省略处理数据的代码
                        // 若需要更新UI，需将数据传回主线程，具体可搜索android多线程编程

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            // 结束后，关闭连接
                            connection.disconnect();
                        }
                    }
                }
            }
        }).start();
    }
}
