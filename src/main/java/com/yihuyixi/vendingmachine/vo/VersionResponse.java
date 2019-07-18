package com.yihuyixi.vendingmachine.vo;

import java.io.Serializable;

public class VersionResponse implements Serializable {
    private int result;
    private VersionVO data;

    public static class VersionVO implements Serializable {
        private String remark;
        private String url;
        private String versionName;
        private String versionCode;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        @Override
        public String toString() {
            return "VersionVO{" +
                    "remark='" + remark + '\'' +
                    ", url='" + url + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode='" + versionCode + '\'' +
                    '}';
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public VersionVO getData() {
        return data;
    }

    public void setData(VersionVO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VersionResponse{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
