package com.xht.meizhi.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xht.meizhi.R;
import com.xht.meizhi.base.ToolbarActivity;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xht on 2016/11/7 09:46.
 */

public class PictureActivity extends ToolbarActivity {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";

    private ImageView mImageView;
    private String mImageUrl, mImageTitle;
    private PhotoViewAttacher mPhotoViewAttacher;

    private Context mContext = PictureActivity.this;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_picture;
    }

    /**
     * toolbar是否带返回箭头
     *
     * @return
     */
    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageView = (ImageView) findViewById(R.id.iv_pic);

        parseIntent();

        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);

        Glide.with(mContext).load(mImageUrl).into(mImageView);

        setAppBarAlpha(0.7f);
        setTitle(mImageTitle);
        setupPhotoAttacher();
    }

    private void setupPhotoAttacher() {
        // 关联PhotoView，具有双击缩放的功能
        mPhotoViewAttacher = new PhotoViewAttacher(mImageView);
        // 单击图片，隐藏或显示Toolbar
        mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Log.i("TAG","PictureActivity------setupPhotoAttacher()---隐藏或显示toolbar");
                hideOrShowToolbar();
            }
        });
        // 长按弹框提示保存图片
        mPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(PictureActivity.this)
                        .setMessage("保存到手机")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImage();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    /**
     * 保存图片到手机
     */
    private void saveImage() {

    }

    public static Intent newIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, desc);
        return intent;
    }

    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        // TODO: 把图片的一些信息，比如 who，加载到 Overflow 当中
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                Log.i("TAG", "PictureActivity---onOptionsItemSelected()---分享");
                return true;
            case R.id.action_save:
                Log.i("TAG", "PictureActivity---onOptionsItemSelected()---保存图片");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoViewAttacher.cleanup();
    }
}
