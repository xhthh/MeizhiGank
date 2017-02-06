package com.xht.meizhi.bean;

import java.util.List;

/**
 * Created by xht on 2016/11/3 10:33.
 */

public class VideoList {
    private List<VideoInfo> results;

    @Override
    public String toString() {
        return "VideoList{" +
                "results=" + results +
                '}';
    }

    public List<VideoInfo> getResults() {
        return results;
    }

    public void setResults(List<VideoInfo> results) {
        this.results = results;
    }
}
