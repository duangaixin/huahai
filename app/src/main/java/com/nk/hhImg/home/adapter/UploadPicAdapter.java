package com.nk.hhImg.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.UploadImagePreviewActivity;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.hhDialog.HhDialogManager;
import com.nk.framework.view.LoadingView;
import com.nk.framework.view.NumberProgressBar;
import com.nk.hhImg.R;
import com.nk.hhImg.home.interfa.IClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dax on 2016/8/18.
 */
public class UploadPicAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageItem> mData;
    private LayoutInflater mInflater;
    private IClickListener mClickLisener;

    private boolean isCanClick = true;

    public UploadPicAdapter(Context context, List<ImageItem> list) {
        this.mContext = context;
        this.mData = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setIsCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
        notifyDataSetChanged();
    }

    public void addClickLisener(IClickListener lisener) {
        mClickLisener = lisener;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_upload, viewGroup, false);

            holder.tv_fail = (TextView) view.findViewById(R.id.status_fail);
            holder.tv_prepare = (TextView) view.findViewById(R.id.status_pre);
            holder.tv_uploading = (TextView) view.findViewById(R.id.status_uploading);
            holder.tv_success = (TextView) view.findViewById(R.id.status_success);

            holder.img_status_pre = (ImageView) view.findViewById(R.id.img_status_1);
            holder.img_status_success = (ImageView) view.findViewById(R.id.img_status_2);
            holder.img_status_fail = (ImageView) view.findViewById(R.id.img_status_3);
            holder.img_status_loading = (LoadingView) view.findViewById(R.id.img_status_4);

            holder.upload_img = (ImageView) view.findViewById(R.id.upload_img);
            holder.pb = (NumberProgressBar) view.findViewById(R.id.upload_pb);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final ImageItem item = mData.get(position);
        ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext,
                item.path,
                holder.upload_img, 0, 0);
        holder.upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) mData);
                bundle.putInt("position", position);
                ActivityUtil.moveToActivity((Activity) mContext, UploadImagePreviewActivity.class, bundle);
            }
        });

        holder.pb.setVisibility(View.VISIBLE);
        holder.pb.setProgress(item.progress);
        int state = item.upladaState;
        switch (state) {
            case ImageItem.UPLOAD_NO:
                holder.tv_prepare.setVisibility(View.VISIBLE);
                holder.tv_success.setVisibility(View.GONE);
                holder.tv_fail.setVisibility(View.GONE);
                holder.tv_uploading.setVisibility(View.GONE);

                holder.img_status_pre.setVisibility(View.VISIBLE);
                holder.img_status_success.setVisibility(View.GONE);
                holder.img_status_fail.setVisibility(View.GONE);
                holder.img_status_loading.setVisibility(View.GONE);

                break;
            case ImageItem.UPLOAD_LOADING:
                holder.tv_prepare.setVisibility(View.GONE);
                holder.tv_success.setVisibility(View.GONE);
                holder.tv_fail.setVisibility(View.GONE);
                holder.tv_uploading.setVisibility(View.VISIBLE);

                holder.img_status_pre.setVisibility(View.GONE);
                holder.img_status_success.setVisibility(View.GONE);
                holder.img_status_fail.setVisibility(View.GONE);
                holder.img_status_loading.setVisibility(View.VISIBLE);

                holder.pb.setProgress(item.progress);
                holder.img_status_loading.startAnim();

                break;
            case ImageItem.UPLOAD_SUCESS:

                holder.img_status_loading.stopAnim();
                holder.tv_prepare.setVisibility(View.GONE);
                holder.tv_success.setVisibility(View.VISIBLE);
                holder.tv_fail.setVisibility(View.GONE);
                holder.tv_uploading.setVisibility(View.GONE);

                holder.img_status_pre.setVisibility(View.GONE);
                holder.img_status_success.setVisibility(View.VISIBLE);
                holder.img_status_fail.setVisibility(View.GONE);
                holder.img_status_loading.setVisibility(View.GONE);

                holder.pb.setVisibility(View.GONE);
                break;
            case ImageItem.UPLOAD_FAILURE:
                holder.tv_prepare.setVisibility(View.GONE);
                holder.tv_success.setVisibility(View.GONE);
                holder.tv_fail.setVisibility(View.VISIBLE);
                holder.tv_uploading.setVisibility(View.GONE);

                holder.img_status_pre.setVisibility(View.GONE);
                holder.img_status_success.setVisibility(View.GONE);
                holder.img_status_fail.setVisibility(View.VISIBLE);
                holder.img_status_loading.setVisibility(View.GONE);
                break;
        }

        if (!isCanClick) {
            holder.img_status_pre.setEnabled(false);
            holder.img_status_success.setEnabled(false);
            holder.img_status_fail.setEnabled(false);
            holder.img_status_loading.setEnabled(false);
        } else {
            holder.img_status_pre.setEnabled(true);
            holder.img_status_success.setEnabled(true);
            holder.img_status_fail.setEnabled(true);
            holder.img_status_loading.setEnabled(true);

            holder.img_status_pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HhDialogManager.showAlert(mContext, "温馨提示", "您确定要单独上传？", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mClickLisener != null) {
                                mClickLisener.onUploadClick(item);
                            }
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });

            holder.img_status_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HhDialogManager.showTip(mContext, null, "已上传成功，无需再次上传!", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            });


            holder.img_status_fail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HhDialogManager.showAlert(mContext, "温馨提示", "您确定要单独上传？", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mClickLisener != null) {
                                mClickLisener.onUploadClick(item);
                            }
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }
            });


        }

        return view;
    }

    class ViewHolder {
        ImageView upload_img;
        TextView tv_uploading;
        TextView tv_fail;
        TextView tv_prepare;
        TextView tv_success;
        ImageView img_status_pre;
        ImageView img_status_success;
        ImageView img_status_fail;
        LoadingView img_status_loading;
        NumberProgressBar pb;
    }

}
