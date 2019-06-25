package com.yihuyixi.vendingmachine.constants;

import com.yihuyixi.vendingmachine.bean.DeviceInfo;

public final class AppConstants {
    public static final String BASE_API = "http://www.yihuyixi.com";
    public static final String CDN_BASE_API = "http://imgcdn.yihuyixi.com";
    public static final String CMS_API = BASE_API + "/cms";
    public static final String PS_API = CDN_BASE_API + "/ps/download";
    public static final String WX_API = BASE_API + "/wxservice";
    public static final String OSS_API = BASE_API + "/oss";
    public static final String SPECIAL_GOODS_API = BASE_API + "/webspread";

    public static final int FLAG_NO_DATA = 0;
    public static final int FLAG_GOODS = 1;
    public static final int FLAG_QRCODE_URL = 2;
    public static final int FLAG_ERROR = 3;
    public static final int FLAG_SDK_INIT_SUCCESS = 1000;
    public static final int FLAG_SDK_INIT_FAIL = 1001;
    public static final int FLAG_SDK_SUCCESS = 1100;
    public static final int FLAG_SDK_FAIL = 1101;
    public static final int FLAG_NETWORK_ERROR = 9999;

    public static final int FLAG_UPDATE_COUNTDOWN = 6;
    public static final int FLAG_CLOSE_DETAIL = 7;
    public static final int FLAG_PAY_SUCCESS = 8;
    public static final int FLAG_PAY_FAIL = 9;
    public static final int FLAG_RELOAD_GOODS = 10;
    public static final int FLAG_UPDATE_STOCK_INFO = 12;

    public static final int FLAG_DETAIL_QRCODE_URL = 200;
    public static final int FLAG_DETAIL_BANNER = 201;
    public static final int FLAG_UPDATE_DEVICE_INFO = 8888;

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

    public static final long SPLASH_DELAY_TIME = 3000L;
    public static final int PAGE_SIZE = 80;

    public static String VENDOR_ID = "";
    public static DeviceInfo CURRENT_DEVICE = null;
    public static boolean IS_DEVICE_CHECKING = false;

    public static String LAST_SHIPMENT_LEVEL = "";
    public static String LAST_PREORDER_ID = "";
    public static String LAST_PICK_CODE = "";
}
