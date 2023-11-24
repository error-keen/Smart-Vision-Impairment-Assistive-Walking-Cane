package com.example.new_main;

import android.util.Log;
import android.view.View;
import java.util.Calendar;

public class time_tool {
    private static final String TAG = "DayNightDetector";

    /**
     * 获取当前时间并判断是白天还是黑夜
     *
     * @param
     */
    public static void checkDayNight( ) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);
        // 判断是否是白天还是黑夜
        if (hour >= 6 && hour < 18) {
            Log.d(TAG, "当前是白天");
        } else {
            Log.d(TAG, "当前是黑夜");
        }
    }
    public static boolean checkDayOrNight() {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // 判断是否是白天还是黑夜
        if (hour >= 6 && hour < 18) {
            return true;
        } else {
            return false;
        }
    }
}
