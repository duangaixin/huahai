package com.nk.hhImg.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nk.framework.baseUtil.TimeUtils;
import com.nk.hhImg.R;
import com.nk.hhImg.user.HistoryResultEntity;

import java.util.List;

/**
 * Created by dax on 2016/9/23.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryResultEntity> mHistoryDatas;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HistoryResultEntity> historyResult) {
        this.mHistoryDatas = historyResult;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mHistoryDatas == null ? 0 : mHistoryDatas.size();
    }

    @Override
    public HistoryResultEntity getItem(int position) {
        return mHistoryDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_history, viewGroup, false);
            holder.btn = (Button) view.findViewById(R.id.delete_button);
            holder.txt_success = (TextView) view.findViewById(R.id.txt_success);
            holder.txt_all = (TextView) view.findViewById(R.id.txt_num);
            holder.txt_time = (TextView) view.findViewById(R.id.txt_time);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        HistoryResultEntity entity = mHistoryDatas.get(position);

        holder.txt_time.setText(TimeUtils.getTime(entity.getScanTime()));
        holder.txt_success.setText(entity.getSuccessSize());
        holder.txt_all.setText(entity.getTotleSize());

        return view;
    }

    class ViewHolder {
        Button btn;
        TextView txt_success;
        TextView txt_all;
        TextView txt_time;
    }
}
