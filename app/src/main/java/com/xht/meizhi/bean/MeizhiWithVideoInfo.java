package com.xht.meizhi.bean;

/**
 * Created by xht on 2016/11/3 10:34.
 */

public class MeizhiWithVideoInfo {

    private String desc;

    private String publishedAt;

    private String url;

    @Override
    public String toString() {
        return "MeizhiWithVideoInfo{" +
                "desc='" + desc + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
