package com.yihuyixi.vendingmachine.api;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yihuyixi.vendingmachine.bean.ProductInfo;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.yihuyixi.vendingmachine.exception.AppException;
import com.yihuyixi.vendingmachine.exception.NoDataException;
import com.yihuyixi.vendingmachine.vo.Artwork;
import com.yihuyixi.vendingmachine.vo.PictureInfo;
import com.yihuyixi.vendingmachine.vo.ResponseEntity;
import com.yihuyixi.vendingmachine.vo.ResponseVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static final String GOODS_API_URL = AppConstants.BASE_API + "/artwork/list?artworkTypeName=tea&currentPage=1";
    private static final String QRCODE_API_URL = AppConstants.OSS_API + "/preorder/add";
    private static final String QUERY_ORDER_API_URL = AppConstants.WX_API + "/wx/orderquery?preId=";

    private static final String CONTENT_TYPE = "contentType";
    private static final String JSON_TYPE = "application/json;charset=utf-8";
    private static final OkHttpClient client = new OkHttpClient();
    private static Api instance = new Api();

    private Api() {
    }

    public static Api getInstance() {
        return instance;
    }

    public boolean checkPayState(String orderNo) throws AppException {
        String url = QUERY_ORDER_API_URL + orderNo;
        Log.d(AppConstants.TAG_YIHU, String.format("queryPayState url=%s", url));
        Request request = new Request.Builder().url(url)
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            ResponseEntity resp = JSON.parseObject(result, ResponseEntity.class);
            if (resp.getResult() == 0 && AppConstants.PAY_SUCCESS.equalsIgnoreCase(resp.getData().getTrade_state())) {
                return true;
            }
            return false;
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！");
        }
    }

    public ResponseEntity getWxPay(String json) throws AppException {
        Log.d(AppConstants.TAG_YIHU, "getWxPay params: " + json);
        RequestBody body = RequestBody.create(MediaType.parse(JSON_TYPE), json);
        Request request = new Request.Builder().url(QRCODE_API_URL)
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
            throw new AppException("网络异常，请稍候再试！");
        }
    }

    public List<ProductInfo> getGoods(int pageSize) throws AppException {
        StringBuilder url = new StringBuilder(GOODS_API_URL).append("&scoreSort=true&commodityStatesId=2&price=0-800");
        if (pageSize > 0) {
            url.append("&pageSize=" + pageSize);
        }
        Log.d(AppConstants.TAG_YIHU, "getGoods url=" + url.toString());
        Request request = new Request.Builder().url(url.toString())
                .addHeader(CONTENT_TYPE, JSON_TYPE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            return handleGoodsResponse(result);
//            return null;
        } catch(IOException e) {
            throw new AppException("网络异常，请稍候再试！");
        }

    }

    private List<ProductInfo> handleGoodsResponse(String result) {
        ResponseVO responseVO = JSON.parseObject(result, ResponseVO.class);
        if (responseVO == null || responseVO.getTotalRecords() == 0) {
            throw new NoDataException("未查询到数据");
        }
        Log.d(AppConstants.TAG_YIHU, "getGoods fetch size: " + responseVO.getTotalRecords());
        List<ProductInfo> products = new ArrayList<>();
        for(Artwork artwork : responseVO.getArtworks()) {
            ProductInfo p = new ProductInfo();
            p.setId(artwork.getId());
            p.setName(artwork.getName());
            p.setPrice(artwork.getPrice());
            p.setSellCount(artwork.getSalesCount());
            p.setSellpoint(artwork.getSellPoint());
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
}
