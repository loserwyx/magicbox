package com.syz.magicbox.magicbox.adapter;

import android.content.Context;
import android.os.ParcelUuid;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.BitmapUtils;
import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.domain.LocalImage;

import java.util.List;

/**
 * Created by chx on 2017/2/27.
 */

public class PublishItemAdapter extends RecyclerView.Adapter<PublishItemAdapter.ViewHolder> {

    private Context mContext;
    private List<LocalImage> publishItems;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView iv_img;
        TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            iv_img = (ImageView) itemView.findViewById(R.id.iv_publish_item);
            tv_title = (TextView) itemView.findViewById(R.id.tv_publish_item_title);
        }
    }
    public PublishItemAdapter(List<LocalImage> items){
        publishItems = items;
    }

    @Override
    public PublishItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.publish_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PublishItemAdapter.ViewHolder holder, int position) {
        LocalImage image = publishItems.get(position);
        holder.tv_title.setText(image.getName());
        //第三方库加载图片
        Glide.with(mContext).load(image.getImageId()).into(holder.iv_img);
    }

    @Override
    public int getItemCount() {
        return publishItems.size();
    }
}
