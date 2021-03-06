package com.yihuyixi.vendingmachine.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.yihuyixi.vendingmachine.constants.AppConstants;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class VideoUtils {
    private static HttpProxyCacheServer proxy; //视频缓存代理
    private static List<String> videoUrls = new ArrayList<>();
    private static int INDEX = 0;
    private static VideoUtils instance = new VideoUtils();

    private VideoUtils() {
        videoUrls.add("http://1252423336.vod2.myqcloud.com/950efb46vodtransgzp1252423336/2dcd67395285890789363058257/v.f20.mp4");
    }

    public static VideoUtils getInstance(Context context) {
        proxy = new HttpProxyCacheServer.Builder(context)
//                .maxCacheSize(1024 * 1024 * 1024)   //1 Gb for cache
                .maxCacheFilesCount(10)
                .build();
        return instance;
    }

    private volatile boolean exchangeBanner = false;
    public void playNextVideo(final VideoView videoView, final Banner mBanner) {
        try {
            videoView.setVideoPath(getNextVideo());
            videoView.requestFocus();
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mPlayer) {
//                    mPlayer.setDisplay(null);
//                    mPlayer.reset();
//                    mPlayer.setDisplay(videoView.getHolder());
                    exchangeBanner = !exchangeBanner;
                    if (exchangeBanner) {
                        exchangeBanner(videoView, mBanner);
                    } else {
                        mBanner.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        playNextVideo(videoView, mBanner);
                    }
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    exchangeBanner(videoView, mBanner); //播放异常，则停止播放，防止弹窗使界面阻塞
                    return true;
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        mp.start();
                        mp.setVolume(1f, 1f);   // volume range: 0-1
                    } catch (Exception e) {
                        Log.e(AppConstants.TAG_YIHU, e.getMessage(), e);
                        exchangeBanner(videoView, mBanner);
                    }
                }
            });
        } catch(Throwable e) {
            exchangeBanner = true;
            exchangeBanner(videoView, mBanner);
        }
    }

    private void exchangeBanner(VideoView videoView, Banner mBanner) {
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        mBanner.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBanner.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                playNextVideo(videoView, mBanner);
            }
        }, 10000);
    }

    private String getNextVideo() {
        String url = videoUrls.get(INDEX);
        if(++INDEX >= videoUrls.size()) {
            INDEX = 0;
        }
        return proxy.getProxyUrl(url);
    }
}
