package com.yihuyixi.vendingmachine.utils;

import java.util.Date;

public class Utils {
    public static String getTimeStamp() {
        return String.valueOf(new Date().getTime());
    }
}
