package com.yihuyixi.vendingmachine.api;

import java.io.File;

public interface DownloadListener {
    void onProgress(long writeBytes, long contentLength);
    void onComplete(File outFile);
    void onError(String message);
}
