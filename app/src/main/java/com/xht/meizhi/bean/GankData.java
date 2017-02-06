package com.xht.meizhi.bean;

import java.util.List;

/**
 * Created by xht on 2016/11/8 10:38.
 */

public class GankData {
    public boolean error;
    public Result results;
    public List<String> category;

    public class Result {
        public List<Gank> Android;
        public List<Gank> iOS;
        public List<Gank> 休息视频;
        public List<Gank> 拓展资源;
        public List<Gank> 福利;
        public List<Gank> 瞎推荐;
        public List<Gank> App;

        @Override
        public String toString() {
            return "Result{" +
                    "Android=" + Android +
                    ", iOS=" + iOS +
                    ", 休息视频=" + 休息视频 +
                    ", 拓展资源=" + 拓展资源 +
                    ", 福利=" + 福利 +
                    ", 瞎推荐=" + 瞎推荐 +
                    ", App=" + App +
                    '}';
        }

        public List<Gank> get瞎推荐() {
            return 瞎推荐;
        }

        public void set瞎推荐(List<Gank> 瞎推荐) {
            this.瞎推荐 = 瞎推荐;
        }

        public List<Gank> getApp() {
            return App;
        }

        public void setApp(List<Gank> app) {
            App = app;
        }

        public List<Gank> getAndroid() {
            return Android;
        }

        public void setAndroid(List<Gank> android) {
            Android = android;
        }

        public List<Gank> getiOS() {
            return iOS;
        }

        public void setiOS(List<Gank> iOS) {
            this.iOS = iOS;
        }

        public List<Gank> get休息视频() {
            return 休息视频;
        }

        public void set休息视频(List<Gank> 休息视频) {
            this.休息视频 = 休息视频;
        }

        public List<Gank> get拓展资源() {
            return 拓展资源;
        }

        public void set拓展资源(List<Gank> 拓展资源) {
            this.拓展资源 = 拓展资源;
        }

        public List<Gank> get福利() {
            return 福利;
        }

        public void set福利(List<Gank> 福利) {
            this.福利 = 福利;
        }
    }

    public Result getResults() {
        return results;
    }

    public void setResults(Result results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "GankData{" +
                "error=" + error +
                ", results=" + results +
                ", category=" + category +
                '}';
    }
}
