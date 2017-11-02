package com.xht.meizhi.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xht.meizhi.R;
import com.xht.meizhi.adapter.MeizhiAdapter;
import com.xht.meizhi.adapter.OnMeizhiTouchListener;
import com.xht.meizhi.base.ToolbarActivity;
import com.xht.meizhi.bean.MeizhiList;
import com.xht.meizhi.bean.MeizhiWithVideoInfo;
import com.xht.meizhi.bean.MeizhiWithVideoList;
import com.xht.meizhi.bean.VideoList;
import com.xht.meizhi.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
测试
*/
public class MainActivity extends ToolbarActivity {

    // 数据请求的页数
    private int page = 1;
    private RecyclerView mRecyclerView;
    private MeizhiAdapter meizhiAdapter;
    private ImageView mLoading;
    private RelativeLayout mLoadingLayout;

    // 上下文
    private Context mContext = MainActivity.this;
    private AnimationDrawable mAnimationDrawable;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoading = (ImageView) findViewById(R.id.iv_loading);
        mLoadingLayout = (RelativeLayout) findViewById(R.id.rl_loading);

        mLoading.setBackgroundResource(R.drawable.loading);
        mAnimationDrawable = (AnimationDrawable) mLoading.getBackground();
        mAnimationDrawable.start();

        setupRecyclerView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData();
    }

    /**
     * 加载数据
     * 1、获取妹纸数据 MeizhiList
     * 2、获取视频数据 VideoList
     * 3、zip 把这两个数据源的数据拼接起来 --->MeizhiWithVideoList
     * 4、先把Observable<MeizhiWithVideoList>数据源转化为Observable<List<MeizhiWithVideoInfo>>，
     * 从对外发一个MeizhiWithVideoList对象变成对外发射一个List<MeizhiWithVideoInfo>对象
     * 5、再把Observale<List<MeizhiWithVideoInfo>>转化为Observable<MeizhiWithVideoInfo>数据源，
     * 变成了对外发射出10个MeizhiWithVideoInfo对象
     * 6、通过toSortedList对这10个MeizhiWithVideoInfo对象基于publishDate进行排序
     * (其中比较操作很耗cpu，放在Schedulers.computation()线程中做)
     * 7、获得最终数据,进行展示
     * <p>线程切换总结
     * 1）subscribeOn的调用切换之前的线程。
     * 2）observeOn的调用切换之后的线程。
     * 3）observeOn之后，不可再调用subscribeOn 切换线程
     * <p>
     * 1）下面提到的“操作”包括产生事件、用操作符操作事件以及最终的通过 subscriber 消费事件
     * 2）只有第一subscribeOn() 起作用（所以多个 subscribeOn() 毛意义）
     * 3）这个 subscribeOn() 控制从流程开始的第一个操作，直到遇到第一个 observeOn()
     * 4）observeOn() 可以使用多次，每个 observeOn() 将导致一次线程切换()，这次切换开始于这次 observeOn() 的下一个操作
     * 5）不论是 subscribeOn() 还是 observeOn()，每次线程切换如果不受到下一个 observeOn() 的干预，线程将不再改变，不会自动切换到其他线程
     * <p>
     * 例：
     * Observable
     * .map                    // 操作1
     * .flatMap                // 操作2
     * .subscribeOn(io)
     * .map                    //操作3
     * .flatMap                //操作4
     * .observeOn(main)
     * .map                    //操作5
     * .flatMap                //操作6
     * .subscribeOn(io)        //!!特别注意
     * .subscribe(handleData)
     * 假设这里我们是在主线程上调用这段代码，那么
     * <p>
     * 操作1，操作2是在io线程上，因为之后subscribeOn切换了线程
     * 操作3，操作4也是在io线程上，因为在subscribeOn切换了线程之后，并没有发生改变。
     * 操作5，操作6是在main线程上，因为在他们之前的observeOn切换了线程。
     * 特别注意那一段，对于操作5和操作6是无效的
     */
    private void loadData() {
        final Observable<MeizhiList> meizhiList = gankApi.getMeizhiList(page);
        Observable<VideoList> videoList = gankApi.getVideoList(page);

        Observable<MeizhiWithVideoList> meizhiWithVideoListObservable = Observable.zip(meizhiList, videoList, new Func2<MeizhiList, VideoList, MeizhiWithVideoList>() {
            @Override
            public MeizhiWithVideoList call(MeizhiList meizhiList, VideoList videoList) {
                return mergeVideoWithMeizhi(meizhiList, videoList);
            }
        });

        /*
        直接使用flatMap
        meizhiWithVideoListObservable.flatMap(new Func1<MeizhiWithVideoList, Observable<MeizhiWithVideoInfo>>() {
            @Override
            public Observable<MeizhiWithVideoInfo> call(MeizhiWithVideoList meizhiWithVideoList) {
                return Observable.from(meizhiWithVideoList.getData());
            }
        });
        */

        Subscription subscription = meizhiWithVideoListObservable.map(new Func1<MeizhiWithVideoList, List<MeizhiWithVideoInfo>>() {
            @Override
            public List<MeizhiWithVideoInfo> call(MeizhiWithVideoList meizhiWithVideoList) {
                return meizhiWithVideoList.getData();
            }
        }).flatMap(new Func1<List<MeizhiWithVideoInfo>, Observable<MeizhiWithVideoInfo>>() {
            @Override
            public Observable<MeizhiWithVideoInfo> call(List<MeizhiWithVideoInfo> meizhiWithVideoInfos) {
                return Observable.from(meizhiWithVideoInfos);
            }
        }).toSortedList(new Func2<MeizhiWithVideoInfo, MeizhiWithVideoInfo, Integer>() {
            @Override
            public Integer call(MeizhiWithVideoInfo meizhiWithVideoInfo, MeizhiWithVideoInfo meizhiWithVideoInfo2) {
                return meizhiWithVideoInfo2.getPublishedAt().compareTo(meizhiWithVideoInfo.getPublishedAt());
            }
        }).subscribeOn(Schedulers.computation())//计算所使用的Scheduler，比较操作很耗cpu，不要把计算工作放在 io() 中，可以避免创建不必要的线程
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<List<MeizhiWithVideoInfo>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("TAG", "loadData()----最终数据源---onCompleted");
                        // 结束动画隐藏布局
                        mAnimationDrawable.stop();
                        mLoadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TAG", "loadData()---最终数据源----onError()");
                    }

                    @Override
                    public void onNext(List<MeizhiWithVideoInfo> meizhiWithVideoInfos) {
                        Log.i("TAG", "loadData()---最终数据源----onNext--data==" + meizhiWithVideoInfos);
                        meizhiAdapter.setData(meizhiWithVideoInfos);
                    }
                });
        // 统一管理
        addSubscription(subscription);

    }

    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_meizhi);
        // 设置瀑布流
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        meizhiAdapter = new MeizhiAdapter(mContext);
        mRecyclerView.setAdapter(meizhiAdapter);

        meizhiAdapter.setOnMeizhiTouchListener(getOnMeizhiTouchListener());
    }

    private OnMeizhiTouchListener getOnMeizhiTouchListener() {
        return new OnMeizhiTouchListener() {
            @Override
            public void onTouch(View v, View meizhiView, View card, MeizhiWithVideoInfo meizhi) {
                if (meizhi == null)
                    return;
                if (v == meizhiView) {
                    startPictureActivity(meizhi, meizhiView);
                } else if (v == card) {
                    startGankActivity(meizhi.getPublishedAt());
                }
            }
        };
    }

    /**
     * 进入详情界面
     *
     * @param publishedAt 时间
     */
    private void startGankActivity(String publishedAt) {
        Intent intent = new Intent(this, GankActivity.class);
        intent.putExtra(GankActivity.EXTRA_GANK_DATE, publishedAt);
        startActivity(intent);
    }

    /**
     * 进入图片详情界面
     *
     * @param meizhi
     * @param transitView
     */
    private void startPictureActivity(MeizhiWithVideoInfo meizhi, View transitView) {
        Intent intent = PictureActivity.newIntent(mContext, meizhi.getUrl(), meizhi.getDesc());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this, transitView, PictureActivity.TRANSIT_PIC);
        ActivityCompat.startActivity(MainActivity.this, intent, optionsCompat.toBundle());
    }

    @Override
    public void onToolbarClick() {
        // ToolbarActivity中点击Toolbar的回调
        // RecyclerView返回第一条
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_notifiable);
        initNotifiableItemState(item);
        return true;
    }

    /**
     * 设置每天中午提醒
     * 默认为true
     *
     * @param item
     */
    private void initNotifiableItemState(MenuItem item) {
        boolean flag = (boolean) SPUtils.get(mContext, getString(R.string.action_notifiable), true);
        item.setChecked(flag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_trending:
                openGitHubTrending();
                return true;
            case R.id.action_notifiable:
                boolean isChecked = !item.isChecked();
                item.setChecked(isChecked);
                // 保存修改后的数据
                SPUtils.put(mContext, getString(R.string.action_notifiable), isChecked);
                Toast.makeText(mContext, isChecked ? getString(R.string.notifiable_on) : getString(R.string.notifiable_off), Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 打开Github热门
     */
    private void openGitHubTrending() {
        String url = getString(R.string.url_github_trending);
        String title = getString(R.string.action_github_trending);
        Intent intent = WebActivity.newIntent(this, url, title);
        startActivity(intent);
    }

    /**
     * 合并meizhi和video
     *
     * @param meizhiList
     * @param videoList
     * @return
     */
    public MeizhiWithVideoList mergeVideoWithMeizhi(MeizhiList meizhiList, VideoList videoList) {
        List<MeizhiWithVideoInfo> list = new ArrayList<>();
        for (int i = 0; i < meizhiList.getResults().size(); i++) {
            MeizhiWithVideoInfo info = new MeizhiWithVideoInfo();
            info.setUrl(meizhiList.getResults().get(i).getUrl());
            info.setPublishedAt(meizhiList.getResults().get(i).getPublishedAt());
            info.setDesc(videoList.getResults().get(i).getDesc());
            list.add(info);
        }
        return new MeizhiWithVideoList(list);
    }

}
