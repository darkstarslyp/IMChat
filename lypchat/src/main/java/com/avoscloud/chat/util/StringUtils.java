package com.avoscloud.chat.util;

/**
 * Created by Administrator on 2016/1/6 0006.
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String str){
        if(str==null){
            return true;
        }
        if(str.equals("")){
            return true;
        }
        return false;
    }

    public static boolean isNotNullOrEmpty(String str){
        if(str!=null&&!str.equals("")){
            return true;
        }

        return false;
    }


}
