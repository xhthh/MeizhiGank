package com.xht.meizhi.bean;

import java.util.List;

/**
 * Created by xht on 2016/11/3 10:36.
 */

public class MeizhiWithVideoList {
    private List<MeizhiWithVideoInfo> data;

    public MeizhiWithVideoList(List<MeizhiWithVideoInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MeizhiWithVideoList{" +
                "data=" + data +
                '}';
    }

    public List<MeizhiWithVideoInfo> getData() {
        return data;
    }

    public void setData(List<MeizhiWithVideoInfo> data) {
        this.data = data;
    }
}
