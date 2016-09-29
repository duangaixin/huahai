package com.nk.hhImg.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.MyImagePreviewDelActivity;
import com.nk.framework.baseUtil.ActivityUtil;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.message.HhMessage;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.home.adapter.SelectPicAdapter;
import com.nk.hhImg.home.bean.SelectInfoResult;
import com.nk.hhImg.home.message.RereshMessage;
import com.nk.hhImg.home.presenter.SelectPresenter;
import com.nk.hhImg.user.HistoryHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dax on 2016/7/28.
 */
public class SelectPicActivity extends HhBaseActivity<SelectPresenter> {
    @Bind(R.id.ll_contain)
    LinearLayout llContain;
    @Bind(R.id.sv)
    ScrollView sv;
    @Bind(R.id.txt_num)
    TextView txt_num;
    @Bind(R.id.btn_toUpload)
    ImageView btn_toUpload;

    private ImagePicker imagePicker;
    private SparseArray<SelectPicAdapter> adapters;
    private LayoutInflater mInflater;
    private List<SelectInfoResult.TypesBean> types;
    private SelectInfoResult mSelectInfo;
    private String scanUrl;
    private String phoneNum;
    private String userToken;
    List<Integer> requestCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        setTopTitle("选择影像");
        HhMessage.getInstance().registListener(this);
        mBasePresenter = new SelectPresenter(this);
        getData();
        HistoryHelper.recordScanTime();
    }

    private void getData() {
      /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scanUrl = (String) bundle.get("result");
        }

        try {
            User user = UserApi.getUser();
            if (user != null) {
                phoneNum = user.getPhoneNum();
                userToken = user.getToken();
            }
        } catch (UserLostException e) {
            e.printStackTrace();
        }

        mBasePresenter.getSelectPageInfo(phoneNum, scanUrl, new IGetSelectInfoResult() {
            @Override
            public void getSelectInfoSuccess(SelectInfoResult result) {
                mSelectInfo = result;
                if (mSelectInfo.getToken().equals(userToken)) {
                    setDataOnUi(mSelectInfo);
                } else {
                    BaseApplication.closeActivity(HomeActivity.class);
                    ActivityUtil.moveToActivityAndFinish(SelectPicActivity.this, LoginActivity.class);
                }
            }
        });*/
        
                 String json = "{\"message\":\"成功\",\"result\":" +
                                "{\"busi_num\":\"20160807\",\"types\":" +
                                "[{\"typeName\":\"未分类\",\"typeCode\":\"noClassify\"}," +
                                "{\"typeName\":\"黄老师1\",\"typeCode\":\"1\"}," +
                                "{\"typeName\":\"类型1\",\"typeCode\":\"1\"}," +
                                "{\"typeName\":\"类型2\",\"typeCode\":\"3\"}," +
                                "{\"typeName\":\"黄老师2\",\"typeCode\":\"2\"}," +
                                "{\"typeName\":\"类型3\",\"typeCode\":\"2\"}," +
                                "{\"typeName\":\"黄老师3\",\"typeCode\":\"3\"}]," +
                                "\"key\":\"1\",\"" +
                                "url\":\"http://10.2.16.212:8080/uip_hhbx/" +
                                "PhoneUploadServlet\"},\"token\":\"工号不存在\"," +
                                "\"code\":\"99999\"}";
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            mSelectInfo = new Gson().fromJson(jsonObject.getJSONObject("result").toString(), SelectInfoResult.class);
                            setDataOnUi(mSelectInfo);

                 } catch (JSONException e) {
                        e.printStackTrace();
                    }
    }


    private void setDataOnUi(SelectInfoResult selectInfo) {
        mInflater = LayoutInflater.from(SelectPicActivity.this);
        sv.smoothScrollTo(0, 0);

        HistoryHelper.recordScanResult(selectInfo);
        types = selectInfo.getTypes();
        requestCodes = new ArrayList<>();
        adapters = new SparseArray<>();

        if (types != null && !types.isEmpty()) {
            for (int i = 0; i < types.size(); i++) {
                final int requestCode = i;
                requestCodes.add(requestCode);
                List<ImageItem> datas = new ArrayList<>();
                final SelectPicAdapter adapter = new SelectPicAdapter(SelectPicActivity.this, datas);
                adapters.put(requestCode, adapter);

                LinearLayout item = (LinearLayout) mInflater.inflate(R.layout.item_image_gird, llContain, false);
                TextView tvTitle = (TextView) item.findViewById(R.id.txt_title);
                ImageView ivOpen = (ImageView) item.findViewById(R.id.btn_open);
                GridView gvItem = (GridView) item.findViewById(R.id.no_grid);

                tvTitle.setText(types.get(i).getTypeName());
                txt_num.setText(selectInfo.getBusi_num());
                gvItem.setAdapter(adapter);

                gvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intentPreview = new Intent(SelectPicActivity.this, MyImagePreviewDelActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        startActivityForResult(intentPreview, requestCode);
                    }
                });

                ivOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            goToSelectPic(requestCode);
                        } else {
                            ActivityCompat.requestPermissions(SelectPicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                        }
                    }
                });
                llContain.addView(item);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int code : requestCodes) {
            if (requestCode == code) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToSelectPic(requestCode);
                } else {
                    HhToast.showShort(SelectPicActivity.this, "权限禁止，不能读取本地图库");
                }
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            for (int code : requestCodes) {
                if (requestCode == code) {
                    ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
                    ImageItem imageItem = new ImageItem();
                    imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
                    imagePicker.clearSelectedImages();
                    imagePicker.addSelectedImageItem(0, imageItem, true);
                    List<ImageItem> images = imagePicker.getSelectedImages();
                    SelectPicAdapter adapter = adapters.get(code);
                    List<ImageItem> img = adapter.getImages();
                    img.addAll(images);
                    adapter.setImages(img);
                    break;
                }
            }
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            for (int code : requestCodes) {
                if (code == requestCode) {
                    List<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    SelectPicAdapter adapter = adapters.get(code);
                    List<ImageItem> img = adapter.getImages();
                    img.addAll(images);
                    adapter.setImages(img);
                    break;
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            for (int code : requestCodes) {
                if (code == requestCode) {
                    List<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                    SelectPicAdapter adapter = adapters.get(code);
                    List<ImageItem> img = adapter.getImages();
                    img.clear();
                    img.addAll(images);
                    adapter.setImages(img);
                    break;
                }
            }
        }
    }

    private void goToSelectPic(int requestCode) {
        ActivityUtil.moveToActivityForResult(SelectPicActivity.this, ImageGridActivity.class, requestCode);
    }

    @OnClick(R.id.btn_toUpload)
    public void toUploadActivity() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mSelectInfo);

        if (requestCodes != null && !requestCodes.isEmpty()) {
            int uniqueCode = 0;
            for (int code : requestCodes) {
                SelectPicAdapter adapter = adapters.get(code);
                List<ImageItem> images = adapter.getImages();

                if (images != null && !images.isEmpty()) {
                    Iterator<ImageItem> iterator = images.iterator();
                    while (iterator.hasNext()) {
                        ImageItem item = iterator.next();
                        item.code = types.get(code).getTypeCode();
                        item.img_category = types.get(code).getTypeName();
                        item.uniqueCode = uniqueCode++;
                    }
                    bundle.putSerializable("requestCodes", (Serializable) requestCodes);
                    bundle.putSerializable(String.valueOf(code), (Serializable) images);
                }
            }
        }

        if (bundle.containsKey("requestCodes")) {
            ActivityUtil.moveToActivity(this, UploadPicActivity.class, bundle);
        } else {
            HhToast.showShort(this, "请选择图片后进入上传页面");
            return;
        }
    }

    public void onEventMainThread(RereshMessage message) {
        Map<Integer, List<ImageItem>> mapList = message.mapList;
        Set<Integer> integers = mapList.keySet();
        for (Integer code : integers) {
            SelectPicAdapter adapter = adapters.get(code);
            adapter.setImages(mapList.get(code));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        HhMessage.getInstance().unRegistListener(this);
    }
}
