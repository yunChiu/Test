package com.example.instagram;

import java.util.Calendar;

public class SetTime {
    public static String setPostTime(int timeStamp) {
        long diffSec = (System.currentTimeMillis() - timeStamp*1000L)/1000;
        if (diffSec < 60) {
            return diffSec + "秒前";
        }else if (diffSec < 60*60) {
            diffSec = diffSec / 60;
            return diffSec + "分前";
        }else if (diffSec < 60*60*24) {
            diffSec = diffSec / (60*60);
            return diffSec + "小時前";
        }else if (diffSec < 60*60*24*7) {
            diffSec = diffSec / (60*60*24);
            return diffSec + "天前";
        }else {
            Calendar postTime = Calendar.getInstance();
            postTime.setTimeInMillis(timeStamp*1000L);
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());
            if (now.get(Calendar.YEAR) == postTime.get(Calendar.YEAR)) {
                //月份 0~11
                return (postTime.get(Calendar.MONTH)+1) + "月" + postTime.get(Calendar.DAY_OF_MONTH) + "日";
            }else {
                return postTime.get(Calendar.YEAR) + "年" +
                        (postTime.get(Calendar.MONTH)+1) + "月" +
                        postTime.get(Calendar.DAY_OF_MONTH) + "日";
            }
        }
    }
}
