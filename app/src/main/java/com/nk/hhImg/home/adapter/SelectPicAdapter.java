package com.nk.hhImg.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.nk.hhImg.R;

import java.util.List;

/**
 * Created by dax on 2016/8/18.
 */
public class SelectPicAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageItem> mData;
    private LayoutInflater mInflater;

    public SelectPicAdapter(Context mContext, List<ImageItem> data) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    public void setImages(List<ImageItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public List<ImageItem> getImages() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public ImageItem getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_select, viewGroup, false);
            holder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
            holder.fr_del = (FrameLayout) view.findViewById(R.id.fr_del);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageItem item = mData.get(i);
        int status = item.upladaState;
        if (status == item.UPLOAD_SUCESS) {
            holder.fr_del.setVisibility(View.GONE);
        }else {
            holder.fr_del.setVisibility(View.VISIBLE);
        }

        ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext,
                item.path,
                holder.iv_img, 0, 0);

        holder.fr_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView iv_img;
        FrameLayout fr_del;
    }
}