package com.nk.hhImg.user.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.user.HistoryHelper;
import com.nk.hhImg.user.HistoryResult;
import com.nk.hhImg.user.HistoryResultEntity;
import com.nk.hhImg.user.adapter.HistoryAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by win on 2016/9/23.
 */
public class HistoryActivity extends HhBaseActivity {
    @Bind(R.id.history_lv)
    ListView lv;

    HistoryAdapter mAdapter;
    List<HistoryResultEntity> histDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        setTopTitle("历史记录");

        mAdapter = new HistoryAdapter(this);

        lv.setAdapter(mAdapter);
        getData();

    }

    private void getData() {

        HistoryResult historyData = HistoryHelper.getHistoryData();
        mAdapter.setData(historyData.getHistDatas());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
