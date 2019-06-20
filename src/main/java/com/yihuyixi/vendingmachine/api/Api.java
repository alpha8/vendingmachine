package com.yihuyixi.vendingmachine.api;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yihuyixi.vendingmachine.bean.ChannelResponse;
import com.yihuyixi.vendingmachine.bean.DeviceInfo;
import com.yihuyixi.vendingmachine.bean.GoodsType;
import com.yihuyixi.vendingmachine.bean.OrderInfo;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.utils.GZIPUtils;
import com.yihuyixi.vendingmachine.vo.Artwork;
import com.yihuyixi.vendingmachine.vo.DeviceInfoResponse;
import com.yihuyixi.vendingmachine.vo.ExtGoods;
import com.yihuyixi.vendingmachine.vo.ExtGoodsResponse;
import com.yihuyixi.vendingmachine.vo.OrderResponse;
import com.yihuyixi.vendingmachine.vo.PayVO;
import com.yihuyixi.vendingmachine.vo.PictureInfo;
import com.yihuyixi.vendingmachine.vo.ResponseEntity;
import com.yihuyixi.vendingmachine.vo.ResponseVO;
import com.yihuyixi.vendingmachine.vo.VendorOrderResponse;
import com.yihuyixi.vendingmachine.vo.VendorResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static final String GOODS_API_URL = AppConstants.OSS_API + "/virtualShelf/list?deviceId=";
    private static final String QRCODE_API_URL = AppConstants.OSS_API + "/preorder/add";
    private static final String QUERY_ORDER_API_URL = AppConstants.WX_API + "/wx/orderquery?preId=";
    private static final String QUERY_SPECIAL_GOODS_API_URL = AppConstants.SPECIAL_GOODS_API + "/specialfield/list";
    private static final String QUERY_GOODS_API_URL = AppConstants.BASE_API + "/yihu/artwork/";
    private static final String QUERY_ORDERS_API_URL = AppConstants.CMS_API + "/order/list";
    private static final String TAKEN_SUCCESS_URL = AppConstants.CMS_API + "/vendor/pickSuccess";
    private static final String QUERY_DEVICEINFO_URL = AppConstants.OSS_API + "/device/get/";
    private static final String STOCK_OUT_URL = AppConstants.OSS_API + "/stock/out";

    private static final String CONTENT_TYPE = "contentType";
    private static final String JSON_TYPE = "application/json; charset=utf-8";
    private static OkHttpClient client;
    private static Api instance = new Api();

    private Api() {
        client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
    }

    public static Api getInstance() {
        return instance;
    }

    public String getTakenQrcode(String vendorId, String requestId) {
        String url = String.format("%s/qrcode/artwork?vendorId=%s&requestId=%s", AppConstants.CMS_API, vendorId, requestId);
        Log.d(AppConstants.TAG_YIHU, "取货二维码： " + url);
        return url;
    }

    public boolean takenSuccess(String json) throws AppException {
        Log.d(AppConstants.TAG_YIHU, "takenSuccess params: " + json);
        RequestBody body = RequestBody.create(MediaType.parse(JSON_TYPE), json);
        Request request = new Request.Builder().url(TAKEN_SUCCESS_URL)
                .addHeader("Connection", "close")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d(AppConstants.TAG_YIHU, "takenSuccess response:" + result);
            VendorOrderResponse resp = JSON.parseObject(result, VendorOrderResponse.class);
            return resp.getResult() == 0;
        }catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public List<OrderInfo> getOrderList(String vendorId, int userId) throws AppException {
        StringBuilder url = new StringBuilder(QUERY_ORDERS_API_URL)
                .append("?currentPage=1&pageSize=50");
        if (userId > 0) {
            url.append("&userId=").append(userId);
        }
        if (!"".equals(vendorId)) {
            url.append("&vendorId=").append(vendorId);
        }
        Log.d(AppConstants.TAG_YIHU, "getOrderList url=" + url.toString());
        Request request = new Request.Builder().url(url.toString())
                .addHeader("Connection", "close")
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = "";
            try {
                result = response.body().string();
            } catch(Exception e) {
                result = GZIPUtils.uncompressToString(response.body().bytes());
            }
            OrderResponse responseVO = JSON.parseObject(result, OrderResponse.class);
            if (responseVO == null) {
                throw new NoDataException("未查询到数据");
            }
            Log.d(AppConstants.TAG_YIHU, "getOrderList fetch size: " + responseVO.getTotalRecords());
            return responseVO.getOrders();
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public VendorResponse.VendorUser checkUserLogin(String vendorId, String requestId) throws AppException {
        String url = String.format("%s/vendor/user/get?vendorId=%s&requestId=%s", AppConstants.OSS_API, vendorId, requestId);
        Log.d(AppConstants.TAG_YIHU, String.format("checkUserLogin url=%s", url));
        Request request = new Request.Builder().url(url)
                .addHeader("Connection", "close")
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            VendorResponse resp = JSON.parseObject(result, VendorResponse.class);
            if (resp.getResult() == 0) {
                return resp.getData();
            }
            return null;
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public PayVO checkPayState(String orderNo) throws AppException {
        String url = QUERY_ORDER_API_URL + orderNo;
        Log.d(AppConstants.TAG_YIHU, String.format("queryPayState url=%s", url));
        Request request = new Request.Builder().url(url)
                .addHeader("Connection", "close")
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            ResponseEntity resp = JSON.parseObject(result, ResponseEntity.class);
            if (resp.getResult() == 0 && AppConstants.PAY_SUCCESS.equalsIgnoreCase(resp.getData().getTrade_state())) {
                return resp.getData();
            }
            return null;
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public ResponseEntity getWxPay(String json) throws AppException {
        Log.d(AppConstants.TAG_YIHU, "getWxPay params: " + json);
        RequestBody body = RequestBody.create(MediaType.parse(JSON_TYPE), json);
        Request request = new Request.Builder().url(QRCODE_API_URL)
                .addHeader("Connection", "close")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            ResponseEntity resp = JSON.parseObject(result, ResponseEntity.class);
            Log.d(AppConstants.TAG_YIHU, "getWxPay response:" + resp.toString());
            if (resp.getResult() != 0) {
                throw new AppException("获取微信二维码失败");
            }
            return resp;
        }catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public List<ExtGoods> getPromoteGoods(GoodsType type, int pageIndex, int pageSize) throws AppException{
        pageIndex = pageIndex <= 0 ? 1 : pageIndex;
        StringBuilder url = new StringBuilder(QUERY_SPECIAL_GOODS_API_URL)
                .append("?currentPage=").append(pageIndex);
        if (pageSize > 0) {
            url.append("&pageSize=").append(pageSize);
        }
        if (type == GoodsType.BARGAIN) {
            url.append("&type=1&status=0");
        } else {
            url.append("&type=2&online=true");
        }
        Log.d(AppConstants.TAG_YIHU, "getPromoteGoods url=" + url.toString());
        Request request = new Request.Builder().url(url.toString())
                .addHeader("Connection", "close")
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = "";
            try {
                result = response.body().string();
            } catch(Exception e) {
                result = GZIPUtils.uncompressToString(response.body().bytes());
            }
            return handleSpecialGoodsResponse(result);
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    private List<ExtGoods> handleSpecialGoodsResponse(String result) {
        ExtGoodsResponse responseVO = JSON.parseObject(result, ExtGoodsResponse.class);
        if (responseVO == null) {
            throw new NoDataException("未查询到数据");
        }
        Log.d(AppConstants.TAG_YIHU, "handleSpecialGoodsResponse fetch size: " + responseVO.getTotal());
        return responseVO.getList();
    }

    public Artwork getGoodsById(String id) throws AppException {
        StringBuilder url = new StringBuilder(QUERY_GOODS_API_URL).append(id);
        Log.d(AppConstants.TAG_YIHU, "getGoodsById url=" + url.toString());
        Request request = new Request.Builder().url(url.toString())
                .addHeader("Connection", "close")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = "";
            try {
                result = response.body().string();
            }catch(Exception e) {
                result = GZIPUtils.uncompressToString(response.body().bytes());
            }
            return JSON.parseObject(result, Artwork.class);
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public List<ProductInfo> getGoods(String deviceId) throws AppException {
        StringBuilder url = new StringBuilder(GOODS_API_URL).append(deviceId);
        Log.d(AppConstants.TAG_YIHU, "getGoods url=" + url.toString());
        Request request = new Request.Builder().url(url.toString())
                .addHeader("Connection", "close")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = "";
            try {
                result = response.body().string();
            }catch(Exception e) {
                result = GZIPUtils.uncompressToString(response.body().bytes());
            }
            return handleGoodsResponse(result);
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    private List<ProductInfo> handleGoodsResponse(String result) {
        ResponseVO responseVO = JSON.parseObject(result, ResponseVO.class);
        if (responseVO == null || responseVO.getData() == null || responseVO.getData().getProducts() == null) {
            throw new NoDataException("未查询到数据");
        }
        List<ProductInfo> products = new ArrayList<>();
        for(Artwork artwork : responseVO.getData().getProducts()) {
            ProductInfo p = new ProductInfo();
            p.setId(artwork.getPid());
            p.setName(artwork.getName());
            p.setPrice(artwork.getPrice());
            p.setSellCount(artwork.getSalesCount());
            p.setSellpoint(artwork.getSellPoint());
            p.setStatus(artwork.getStatus());
            p.setStock(artwork.getStock());
            List<PictureInfo> pictures = artwork.getPictures();
            if (pictures != null && !pictures.isEmpty()) {
                p.setPictureId(pictures.get(0).getId());
                String urlFormat = "%s/%s?w=750&h=500&v=v2";
                p.setAvatar(String.format(urlFormat, AppConstants.PS_API, pictures.get(0).getId()));
                List<String> pics = new ArrayList<>();
                for (PictureInfo pic: pictures) {
                    pics.add(String.format(urlFormat,AppConstants.PS_API,pic.getId()));
                }
                p.setIcons(pics);
            }
            products.add(p);
        }
        return products;
    }

    public DeviceInfo getDeviceInfo(String deviceId) throws AppException {
        String url = QUERY_DEVICEINFO_URL + deviceId;
        Request request = new Request.Builder().url(QUERY_DEVICEINFO_URL)
                .addHeader("Connection", "close")
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        Log.d(AppConstants.TAG_YIHU, String.format("getDeviceInfo url=%s", url));
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            DeviceInfoResponse resp = JSON.parseObject(result, DeviceInfoResponse.class);
            if (resp.getResult() == 0) {
                return resp.getData();
            }
            return null;
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }

    public ProductInfo stockOut(String json) throws AppException {
        RequestBody body = RequestBody.create(MediaType.parse(JSON_TYPE), json);
        Request request = new Request.Builder().url(STOCK_OUT_URL)
                .addHeader("Connection", "close")
                .post(body)
                .build();
        Log.d(AppConstants.TAG_YIHU, String.format("stockOut url=%s, json=%s", STOCK_OUT_URL, json));
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            ChannelResponse resp = JSON.parseObject(result, ChannelResponse.class);
            if (resp.getResult() == 0 && resp.getData() != null) {
                return resp.getData().getProduct();
            }
            return null;
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！", e);
        }
    }
}
