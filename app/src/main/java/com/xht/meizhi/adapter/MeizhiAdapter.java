package com.xht.meizhi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xht.meizhi.R;
import com.xht.meizhi.bean.MeizhiWithVideoInfo;
import com.xht.meizhi.custom.RatioImageView;

import java.util.List;

/**
 * Created by xht on 2016/11/3 16:52.
 */

public class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.ViewHolder> {

    private Context mContext;
    private List<MeizhiWithVideoInfo> data;

    public MeizhiAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<MeizhiWithVideoInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizhi_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MeizhiWithVideoInfo info = this.data.get(position);

        holder.info = info;

        // 限制描述文字的长度
        int limit = 48;
        String text = info.getDesc().length() > limit ? info.getDesc().substring(0, limit) +
                "..." : info.getDesc();

//        holder.card.setTag(text);

        holder.tv_title.setText(text);

        Glide.with(mContext).load(data.get(position).getUrl()).into(holder.riv_item_img);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private OnMeizhiTouchListener mOnMeizhiTouchListener;

    public void setOnMeizhiTouchListener(OnMeizhiTouchListener onMeizhiTouchListener) {
        this.mOnMeizhiTouchListener = onMeizhiTouchListener;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title;
        public RatioImageView riv_item_img;
        View card;
        MeizhiWithVideoInfo info;

        public ViewHolder(View view) {
            super(view);
            card = view;
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            riv_item_img = (RatioImageView) view.findViewById(R.id.riv_item_img);

            riv_item_img.setOnClickListener(this);
            riv_item_img.setOriginalSize(50, 50);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnMeizhiTouchListener.onTouch(v, riv_item_img, card, info);
        }
    }


}
