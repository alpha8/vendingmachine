package com.yihuyixi.vendingmachine.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;
import java.util.List;

public class VideoUtils {
    private static HttpProxyCacheServer proxy; //视频缓存代理
    private static List<String> videoUrls = new ArrayList<>();
    private static int INDEX = 0;
    private static VideoUtils instance = new VideoUtils();

    private VideoUtils() {
        videoUrls.add("http://1252423336.vod2.myqcloud.com/950efb46vodtransgzp1252423336/85f5d37d4564972818869478170/v.f20.mp4");
        videoUrls.add("http://1252423336.vod2.myqcloud.com/950efb46vodtransgzp1252423336/2dcd67395285890789363058257/v.f20.mp4");
        videoUrls.add("http://1252423336.vod2.myqcloud.com/950efb46vodtransgzp1252423336/85f5db404564972818869478317/v.f20.mp4");
    }

    public static VideoUtils getInstance(Context context) {
        proxy = new HttpProxyCacheServer.Builder(context)
//                .maxCacheSize(1024 * 1024 * 1024)   //1 Gb for cache
                .maxCacheFilesCount(10)
                .build();
        return instance;
    }

    public void playNextVideo(final VideoView videoView) {
        videoView.setVideoPath(getNextVideo());
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                playNextVideo(videoView);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                videoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                return true;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setVolume(1f, 1f);   // volume range: 0-1
            }
        });
    }

    private String getNextVideo() {
        String url = videoUrls.get(INDEX);
        if(++INDEX >= videoUrls.size()) {
            INDEX = 0;
        }
        return proxy.getProxyUrl(url);
    }
}
