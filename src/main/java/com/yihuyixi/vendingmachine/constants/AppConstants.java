package com.yihuyixi.vendingmachine.constants;

public final class AppConstants {
    public static final String BASE_API = "http://www.yihuyixi.com/cms";
    public static final String PS_API = "http://www.yihuyixi.com/ps/download";
    public static final String WX_API = "http://www.yihuyixi.com/wxservice";
    public static final String OSS_API = "http://www.yihuyixi.com/oss";

    public static final int FLAG_NO_DATA = 0;
    public static final int FLAG_GOODS = 1;
    public static final int FLAG_RELOAD_GOODS = 10;
    public static final int FLAG_QRCODE_URL = 2;
    public static final int FLAG_ERROR = 3;
    public static final int FLAG_SDK_SUCCESS = 4;
    public static final int FLAG_SDK_FAIL = 5;

    public static final int FLAG_UPDATE_COUNTDOWN = 6;
    public static final int FLAG_CLOSE_DETAIL = 7;

    public static final int FLAG_PAY_SUCCESS = 8;
    public static final int FLAG_PAY_FAIL = 9;

    public static final String INTENT_GOODS = "goodsinfo";

    public static final String PAY_SUCCESS = "SUCCESS";

    public static final int PAY_TIMEOUT_SECONDS = 120;

    public static final String TAG_YIHU = "YiHuYiXiApp";
}
