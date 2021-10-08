package com.example.instagram2;

public class SetCount {
    public static String count_profile(int count){
        if (count > 1000000)
            return Math.round(count/100000.0)/10.0 + "M";
        else if (count > 100000)
            return Math.round(count/1000.0) + "K";
        else if (count > 10000)
            return Math.round(count/100.0)/10.0 + "K";
        else if (count > 1000)
            return String.format("%,d",count);
        else
            return String.valueOf(count);
    }

    public static String count_post(int count){
        if (count < 100)
            return "少於100則貼文";
        else if (count < 500)
            return "100+ 則貼文";
        else if (count < 1000)
            return "500+ 則貼文";
        else if (count < 5000)
            return "1000+ 則貼文";
        else if (count < 10000)
            return "5000+ 則貼文";
        else if (count < 100000000)
            return Math.round(count/1000.0)/10.0 + "萬 則貼文";
        else
            return Math.round(count/10000000.0)/10.0 + "億 則貼文";
    }
}
