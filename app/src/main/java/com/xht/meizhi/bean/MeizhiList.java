package com.xht.meizhi.bean;

import java.util.List;

/**
 * Created by xht on 2016/11/3 10:31.
 */

public class MeizhiList {
    private List<MeizhiInfo> results;

    @Override
    public String toString() {
        return "MeizhiList{" +
                "results=" + results +
                '}';
    }

    public List<MeizhiInfo> getResults() {
        return results;
    }

    public void setResults(List<MeizhiInfo> results) {
        this.results = results;
    }
}
