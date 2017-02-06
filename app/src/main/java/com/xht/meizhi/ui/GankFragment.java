package com.xht.meizhi.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.bumptech.glide.Glide;
import com.xht.meizhi.R;
import com.xht.meizhi.adapter.GankListAdapter;
import com.xht.meizhi.base.BaseActivity;
import com.xht.meizhi.bean.DGank;
import com.xht.meizhi.bean.DGankData;
import com.xht.meizhi.bean.Gank;
import com.xht.meizhi.bean.GankData;
import com.xht.meizhi.custom.LoveVideoView;
import com.xht.meizhi.custom.VideoImageView;
import com.xht.meizhi.net.GankFactory;
import com.xht.meizhi.util.Once;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xht on 2016/11/7 17:18.
 * 测试更新提交
 */

public class GankFragment extends Fragment {

    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";

    private int mYear, mMonth, mDay;
    private List<Gank> mGankList;
    private String mVideoPreviewUrl;
    private Subscription mSubscription;

    private RecyclerView mRecyclerView;
    private ViewStub mEmptyViewStub;
    private ViewStub mVideoViewStub;
    private VideoImageView mVideoImageView;
    private LoveVideoView mVideoView;

    private GankListAdapter mAdapter;

    boolean mIsVideoViewInflated = false;

    public static GankFragment newInstance(int year, int month, int day) {
        GankFragment fragment = new GankFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseArguments();

        mGankList = new ArrayList<>();
        mAdapter = new GankListAdapter(mGankList);

        /**
         * Fragment是依附于Activity的。当Activity销毁时，Fragment会随之销毁。
         * 而当Activity配置发生改变（如屏幕旋转）时候，旧的Activity会被销毁，然后重新生成一个新屏幕旋转状态下的Activity，
         * 自然而然的Fragment也会随之销毁后重新生成，而新生成的Fragment中的各个对象也与之前的那个Fragment不一样，
         * 伴随着他们的动作、事件也都不一样。所以，这时候如果想保持原来的Fragment中的一些对象，
         * 或者想保持他们的动作不被中断的话，就迫切的需要将原来的Fragment进行非中断式的保存
         */
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gank, container, false);

        initView(rootView);

        initRecyclerView();

        setVideoViewPosition(getResources().getConfiguration());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mGankList.size() == 0) loadData();
        if (mVideoPreviewUrl != null) {
            Glide.with(this).load(mVideoPreviewUrl).into(mVideoImageView);
        }
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    private void initView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_gank);
        mEmptyViewStub = (ViewStub) rootView.findViewById(R.id.stub_empty_view);
        mVideoViewStub = (ViewStub) rootView.findViewById(R.id.stub_video_view);
        mVideoImageView = (VideoImageView) rootView.findViewById(R.id.video_image);
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        mYear = bundle.getInt(ARG_YEAR);
        mMonth = bundle.getInt(ARG_MONTH);
        mDay = bundle.getInt(ARG_DAY);
    }

    /**
     * 设置RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setVideoViewPosition(Configuration configuration) {
        switch (configuration.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE: {
                if (mIsVideoViewInflated) {
                    // 显示videoview布局
                    mVideoViewStub.setVisibility(View.VISIBLE);
                } else {
                    mVideoView = (LoveVideoView) mVideoViewStub.inflate();
                    mIsVideoViewInflated = true;
                    final String tip = getString(R.string.tip_video_play);

                    // 只显示一次
                    new Once(mVideoView.getContext()).show(tip, new Once.OnceCallback() {
                        @Override
                        public void onOnce() {
                            Snackbar.make(mVideoView, tip, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.i_know, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                    });
                    if (mGankList.size() > 0 && mGankList.get(0).getType().equals("休息视频")) {
                        mVideoView.loadUrl(mGankList.get(0).getUrl());
                    }
                }
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT:
            case Configuration.ORIENTATION_UNDEFINED:
            default: {
                mVideoViewStub.setVisibility(View.GONE);
                break;
            }
        }
    }

    /**
     * 联网获取数据
     */
    private void loadData() {
        loadVideoPreview();
        mSubscription = BaseActivity.gankApi.getGankData(mYear, mMonth, mDay)
                .map(new Func1<GankData, GankData.Result>() {
                    @Override
                    public GankData.Result call(GankData gankData) {
//                        Log.i("xht", "GankFragment---map1--result==" + gankData.results);
                        return gankData.results;
                    }
                }).map(new Func1<GankData.Result, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankData.Result result) {
                        return addAllResults(result);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Gank>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("xht", "GankFragment---onError()");
                    }

                    @Override
                    public void onNext(List<Gank> ganks) {
                        if (ganks.isEmpty()) {
                            Log.i("xht", "GankFragment---onNext()---empty");
                        } else {
//                            Log.i("xht", "GankFragment---onNext()---更新数据==" + ganks);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void loadVideoPreview() {
        String where = String.format("{\"tag\":\"%d-%d-%d\"}", mYear, mMonth, mDay);
        Log.i("xht", "where===" + where);

        GankFactory.getDGankIOSingleton()
                .getDGankData(where)
                .map(new Func1<DGankData, List<DGank>>() {
                    @Override
                    public List<DGank> call(DGankData dGankData) {
                        Log.i("xht", "GankFragment---loadVideoPreview()---map1---call()---" + dGankData.results);
                        return dGankData.results;
                    }
                }).single(new Func1<List<DGank>, Boolean>() {

            @Override
            public Boolean call(List<DGank> dGanks) {
                Log.i("xht", "GankFragment---loadVideoPreview()---single---call()---" + (dGanks.size() > 0));
                return dGanks.size() > 0;
            }
        }).subscribe(new Subscriber<List<DGank>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("xht", "GankFragment---loadvideo---onError()");
            }

            @Override
            public void onNext(List<DGank> dGanks) {
                Log.i("xht", "GankFragment---loadvideo---onNext()===dGanks==" + dGanks);
            }
        });
    }

//    private void loadVideoPreview() {
//        String where = String.format("{\"tag\":\"%d-%d-%d\"}", mYear, mMonth, mDay);
//        Log.i("xht", "where===" + where);
//
//        GankFactory.getDGankIOSingleton()
//                .getDGankData(where)
//                .map(new Func1<DGankData, List<DGank>>() {
//                    @Override
//                    public List<DGank> call(DGankData dGankData) {
//                        Log.i("xht", "GankFragment---loadVideoPreview()---map1---call()---" + dGankData.results);
//                        return dGankData.results;
//                    }
//                }).single(new Func1<List<DGank>, Boolean>() {
//            @Override
//            public Boolean call(List<DGank> dGanks) {
//                Log.i("xht", "GankFragment---loadVideoPreview()---single---call()---" + (dGanks.size() > 0));
//                return dGanks.size() > 0;
//            }
//        }).map(new Func1<List<DGank>, DGank>() {
//            @Override
//            public DGank call(List<DGank> dGanks) {
//                Log.i("xht", "GankFragment---loadVideoPreview()---map2---call()---" + dGanks.get(0));
//                return dGanks.get(0);
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DGank>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("xht", "GankFragment---loadvideo---onError()");
//                    }
//
//                    @Override
//                    public void onNext(DGank dGank) {
//                        Log.i("xht", "GankFragment---loadvideo---onNext()===dGank==" + dGank);
//                    }
//                });
//    }

    private List<Gank> addAllResults(GankData.Result results) {
//        Log.i("xht", "GankFragment------addAllResults()---results==" + results);
        if (results.getAndroid() != null) mGankList.addAll(results.getAndroid());
        if (results.getiOS() != null) mGankList.addAll(results.getiOS());
        if (results.getApp() != null) mGankList.addAll(results.getApp());
        if (results.get拓展资源() != null) mGankList.addAll(results.get拓展资源());
        if (results.get瞎推荐() != null) mGankList.addAll(results.get瞎推荐());
        if (results.get休息视频() != null) mGankList.addAll(0, results.get休息视频());
        return mGankList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
