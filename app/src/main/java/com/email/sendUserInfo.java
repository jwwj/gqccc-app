package com.email;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
/**
 * Created by len on 2017/8/15.
 */

public class sendUserInfo {




    public static void SendCode(String email,String info) throws MessagingException,IOException{
        Map<String,String> map= new HashMap<String,String>();
        SendMail mail = new SendMail("jwj164816960@qq.com","dmqktleooxxcbifh");
        map.put("mail.smtp.host", "smtp.qq.com");
        map.put("mail.smtp.auth", "true");
        map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        map.put("mail.smtp.port", "465");
        map.put("mail.smtp.socketFactory.port", "465");
        mail.setPros(map);
        mail.initMessage();
        //String str="164816960@qq.com";
        mail.setRecipient(email);
        mail.setSubject("手机分辨率");

        //  Toast.makeText(getApplicationContext(), MainActivity.getScreen(), Toast.LENGTH_SHORT).show();

        mail.setText(getSystemModel()+info);
        mail.setDate(new Date());
        mail.setFrom("Mobile");
        // mail.setContent("谢谢合作", "text/html; charset=UTF-8");
        System.out.println(mail.sendMessage());

    }


    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }


}
