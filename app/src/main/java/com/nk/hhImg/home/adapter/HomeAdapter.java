package com.nk.hhImg.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nk.hhImg.R;

/**
 * Created by dax on 2016/7/27.
 */
public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int[] imgs;
    private String[] colors;
    private String[] texts;

    public HomeAdapter(Context context, int[] imgs, String[] colors, String[] texts) {
        this.mContext = context;
        this.imgs = imgs;
        this.colors = colors;
        this.texts = texts;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int i) {
        return imgs[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.home_item, viewGroup, false);
            holder.ll = (LinearLayout) view.findViewById(R.id.home_ll);
            holder.img = (ImageView) view.findViewById(R.id.home_img);
            holder.text = (TextView) view.findViewById(R.id.home_text);
            holder.root = (LinearLayout) view.findViewById(R.id.root);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.img.setImageResource(imgs[i]);
        GradientDrawable bgShape = (GradientDrawable) holder.ll.getBackground();
        bgShape.setColor(Color.parseColor(colors[i]));

        holder.text.setText(texts[i]);

        return view;
    }

    class ViewHolder {
        ImageView img;
        TextView text;
        LinearLayout ll;
        LinearLayout root;
    }
}