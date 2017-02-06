package com.xht.meizhi.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht.meizhi.R;
import com.xht.meizhi.bean.Gank;
import com.xht.meizhi.ui.WebActivity;
import com.xht.meizhi.util.StringStyles;

import java.util.List;

/**
 * Created by xht on 2016/11/8 09:59.
 */

public class GankListAdapter extends AnimRecyclerViewAdapter<GankListAdapter.ViewHolder> {

    private List<Gank> mGankList;

    public GankListAdapter(List<Gank> gankList) {
        mGankList = gankList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gank, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gank gank = mGankList.get(position);

        // 进来第一条显示
        if (position == 0) {
            showCategory(holder);
        } else {
            /**
             * type："拓展资源","Android","瞎推荐","休息视频","iOS","福利","前端"
             * position=1之后 判断下一个条目 是否与上一条type相同
             * 如果相同隐藏category，直接显示title
             */
            boolean theCategoryOfLastEqualsToThis = mGankList.get(
                    position - 1).getType().equals(mGankList.get(position).getType());
            if (!theCategoryOfLastEqualsToThis) {
                showCategory(holder);
            } else {
                hideCategory(holder);
            }
        }

        // 设置type
        holder.category.setText(gank.getType());

        // 给desc后面加上作者
        SpannableStringBuilder builder = new SpannableStringBuilder(gank.getDesc()).append(
                StringStyles.format(holder.gank.getContext(), " (via. " +
                        gank.getWho() +
                        ")", R.style.ViaTextAppearance));
        CharSequence gankText = builder.subSequence(0, builder.length());

        // 设置title
        holder.gank.setText(gankText);

        // 给title设置动画
        showItemAnim(holder.gank, position);
    }


    @Override
    public int getItemCount() {
        return mGankList.size();
    }

    private void showCategory(ViewHolder holder) {
        if (!isVisibleOf(holder.category)) holder.category.setVisibility(View.VISIBLE);
    }


    private void hideCategory(ViewHolder holder) {
        if (isVisibleOf(holder.category)) holder.category.setVisibility(View.GONE);
    }

    /**
     * 检查view是否显示
     */
    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView category;
        private TextView gank;
        private LinearLayout ll_gank_parent;

        public ViewHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.tv_category);
            gank = (TextView) itemView.findViewById(R.id.tv_title);
            ll_gank_parent = (LinearLayout) itemView.findViewById(R.id.ll_gank_parent);

            ll_gank_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击条目进入webactivity
                    Gank gank = mGankList.get(getLayoutPosition());
                    Intent intent = WebActivity.newIntent(v.getContext(), gank.getUrl(), gank.getDesc());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
