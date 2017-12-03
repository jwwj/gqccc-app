package com.guessmusic.SQLite;

import java.util.UUID;

/**
 * Created by len on 2017/8/19.
 */

public class MakeUuid {

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String get() {
        String uuid = UUID.randomUUID().toString();
//去掉“-”符号
        return uuid.replaceAll("-", "");
    }
}
