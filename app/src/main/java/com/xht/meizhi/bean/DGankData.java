package com.xht.meizhi.bean;

import java.util.List;

/**
 * Created by xht on 2016/11/8 17:28.
 */

public class DGankData {
    public List<DGank> results;

    @Override
    public String toString() {
        return "DGankData{" +
                "results=" + results +
                '}';
    }

    public List<DGank> getResults() {
        return results;
    }

    public void setResults(List<DGank> results) {
        this.results = results;
    }
}
