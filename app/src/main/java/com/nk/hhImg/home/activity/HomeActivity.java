package com.nk.hhImg.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.L;
import com.nk.hhImg.R;
import com.nk.hhImg.checkversion.UpdataAppManager;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.home.adapter.HomeAdapter;
import com.nk.hhImg.user.activity.PersonActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/7/26.
 */
public class HomeActivity extends HhBaseActivity {
    public static final int REQUEST_CODE_SCAN = 111111;
    @Bind(R.id.home_gv)
    GridView gv;
    @Bind(R.id.home_person)
    ImageView img_person;
    private int position;
    private String sysCodeValue;
    private HomeAdapter adapter;
    int[] imgs = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d,
            R.mipmap.e, R.mipmap.f, R.mipmap.g, R.mipmap.h, R.mipmap.i};

    String[] colors = {"#ff9966", "#769FCD", "#47D6B6", "#99cc66", "#47D6B6", "#D25565",
            "#ffcc66", "#9999ff", "#ff9999"};

    String[] texts = {"车辆保险", "车辆理赔", "非车承保", "非车理赔", "费控系统",
            "销管系统", "增值税控", "电子商务", "再保险"};

    String[] types = {"ccb", "vhlclm", "fccb", "cclp", "fk", "xg", "zzs", "dzsw", "zbx"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setTopTitle("华海影像保险系统");
        hideBackView(true);
        new UpdataAppManager().startCheck(this);

        adapter = new HomeAdapter(this, imgs, colors, texts);
        gv.setAdapter(adapter);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                if (checkPermission(Manifest.permission.CAMERA)) {
                    goToScanActivity();
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, position);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == position) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToScanActivity();
            } else {
                HhToast.showShort(HomeActivity.this, "权限禁止，不能打开扫描页");
            }
        }else if(requestCode==111){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               goToPersonActivity();
            } else {
                HhToast.showShort(HomeActivity.this, "权限禁止，不能打开个人中心页");
            }
        }
    }

    private void goToScanActivity() {
        ActivityUtil.moveToActivityForResult(HomeActivity.this, CaptureActivity.class, HomeActivity.REQUEST_CODE_SCAN);
    }

    @OnClick(R.id.home_person)
    void toPerson() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            goToPersonActivity();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    111);
        }
    }

    private void goToPersonActivity() {
        ActivityUtil.moveToActivity(HomeActivity.this, PersonActivity.class);
    }

    private static Map<String, String> getParams(String url) {
        String paramsStr = url.substring(url.indexOf("?") + 1);
        if (TextUtils.isEmpty(paramsStr)) {
            return null;
        }
        String[] keyValues = paramsStr.split("&");
        if (keyValues == null) {
            return null;
        }
        Map<String, String> params = new HashMap<>();
        for (String keyValue : keyValues) {
            String[] str = keyValue.split("=");
            if (str == null) {
                continue;
            }
            params.put(str[0], str[1]);
        }
        return params;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == HomeActivity.REQUEST_CODE_SCAN) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String scanUrl = (String) bundle.get("result");
                L.e(scanUrl);
                if (!TextUtils.isEmpty(scanUrl) && scanUrl.contains("uip_hhbx")) {
                    Map<String, String> params = HomeActivity.getParams(scanUrl);
                    sysCodeValue = params.get("sysCode");
                    L.e(sysCodeValue);
                } else {
                    HhToast.showShort(this, "请使用华海二维码");
                }

                if (types[position].equals(sysCodeValue)) {

                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(HomeActivity.this, SelectPicActivity.class);
                    startActivity(intent);
                } else {
                    HhToast.showShort(this, "请使用正确的业务项");
                    return;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
