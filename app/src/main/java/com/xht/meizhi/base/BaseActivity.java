package com.xht.meizhi.base;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xht.meizhi.R;
import com.xht.meizhi.net.GankApi;
import com.xht.meizhi.net.GankFactory;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xht on 2016/11/5.
 */

public class BaseActivity extends AppCompatActivity {

    public static final GankApi gankApi = GankFactory.getGankIOSingleton();

    private CompositeSubscription mCompositeSubscription;

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    /**
     * toolbar中overflow中的菜单选项
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                // 跳转到关于界面
//                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_login:
                // 登录Github
//                loginGitHub();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 在activity销毁的时候解除订阅
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
