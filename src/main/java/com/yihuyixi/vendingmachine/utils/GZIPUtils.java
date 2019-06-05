package com.yihuyixi.vendingmachine.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import okhttp3.internal.Util;

public class GZIPUtils {
    private static final String GZIP_ENCODE_UTF_8 = "UTF-8";
    private static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";
 
    /**
     * 字符串压缩为GZIP字节数组
     * 
     * @param str
     * @return
     */
    public static byte[] compress(String str) throws IOException {
        return compress(str, GZIP_ENCODE_UTF_8);
    }
 
    /**
     * 字符串压缩为GZIP字节数组
     * 
     * @param str
     * @param encoding
     * @return
     */
    public static byte[] compress(String str, String encoding) throws IOException{
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            return out.toByteArray();
        }finally {
            Util.closeQuietly(gzip);
        }
    }
 
    /**
     * GZIP解压缩
     * 
     * @param bytes
     * @return
     */
    public static byte[] uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } finally{
            Util.closeQuietly(out);
        }
    }
 
    /**
     * 
     * @param bytes
     * @return
     */
    public static String uncompressToString(byte[] bytes) throws IOException {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }
 
    /**
     * 
     * @param bytes
     * @param encoding
     * @return
     */
    public static String uncompressToString(byte[] bytes, String encoding) throws IOException{
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = null;
        try {
            ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } finally {
            Util.closeQuietly(ungzip);
        }
    }
}