package com.yihuyixi.vendingmachine.constants;

public final class AppConstants {
    public static final String BASE_API = "http://www.yihuyixi.com";
    public static final String CMS_API = BASE_API + "/cms";
    public static final String PS_API = BASE_API + "/ps/download";
    public static final String WX_API = BASE_API + "/wxservice";
    public static final String OSS_API = BASE_API + "/oss";
    public static final String SPECIAL_GOODS_API = BASE_API + "/webspread";

    public static final int FLAG_NO_DATA = 0;
    public static final int FLAG_GOODS = 1;
    public static final int FLAG_QRCODE_URL = 2;
    public static final int FLAG_ERROR = 3;
    public static final int FLAG_SDK_SUCCESS = 4;
    public static final int FLAG_SDK_FAIL = 5;

    public static final int FLAG_UPDATE_COUNTDOWN = 6;
    public static final int FLAG_CLOSE_DETAIL = 7;
    public static final int FLAG_PAY_SUCCESS = 8;
    public static final int FLAG_PAY_FAIL = 9;
    public static final int FLAG_RELOAD_GOODS = 10;

    public static final int FLAG_DETAIL_QRCODE_URL = 200;
    public static final int FLAG_DETAIL_BANNER = 201;

    public static final int FLAG_TAKEN_SUCCESS = 300;
    public static final int FLAG_TAKEN_FAIL = 301;
    public static final int FLAG_TAKEN_ORDERS = 400;

    public static final String INTENT_GOODS = "goodsinfo";
    public static final String INTENT_SECTION = "section";
    public static final String INTENT_PROMOTE_GOODS = "promote_goods";
    public static final String INTENT_TAKEN_DEVICE = "deviceInfo";

    public static final String PAY_SUCCESS = "SUCCESS";

    public static final int PAY_TIMEOUT_SECONDS = 120;

    public static final String TAG_YIHU = "YiHuYiXiApp";

    public static final long SPLASH_DELAY_TIME = 1000L;
    public static final int PAGE_SIZE = 80;

    public static final String VENDOR_ID = "VM001";
}
