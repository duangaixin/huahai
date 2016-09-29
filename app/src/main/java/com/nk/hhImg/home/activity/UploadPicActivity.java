package com.nk.hhImg.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.imagepicker.CompressImageHelper;
import com.lzy.imagepicker.bean.ImageItem;
import com.nk.framework.baseUtil.HhToast;
import com.nk.framework.baseUtil.IOUtil;
import com.nk.framework.hhDialog.HhDialogManager;
import com.nk.framework.message.HhMessage;
import com.nk.framework.view.AlphaButton;
import com.nk.hhImg.R;
import com.nk.hhImg.controller.HhBaseActivity;
import com.nk.hhImg.home.adapter.UploadPicAdapter;
import com.nk.hhImg.home.bean.SelectInfoResult;
import com.nk.hhImg.home.interfa.IClickListener;
import com.nk.hhImg.home.interfa.IUploadCallBack;
import com.nk.hhImg.home.message.RereshMessage;
import com.nk.hhImg.home.presenter.UploadPresenter;
import com.nk.hhImg.user.HistoryHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by  dax on 2016/8/18.
 */
public class UploadPicActivity extends HhBaseActivity<UploadPresenter> implements IClickListener {
    @Bind(R.id.upload_lv)
    ListView lv;
    @Bind(R.id.btn_uploadAll)
    AlphaButton btn_uploadAll;
    @Bind(R.id.single_tv)
    TextView single_tv;

    ArrayList<ImageItem> images = new ArrayList<>();
    LinkedList<ImageItem> mUploadImages = new LinkedList<>();
    LinkedList<ImageItem> mSuccessImages = new LinkedList<>();
    LinkedList<ImageItem> mFailureItems = new LinkedList<>();
    Map<Integer, List<ImageItem>> mapList = new HashMap<>();

    private SelectInfoResult mSelectInfo;
    private UploadPicAdapter uploadPicAdapter;
    private boolean isUploadAllNow;
    private boolean uploaded;
    private boolean isSingleUpload = true;
    private static int mCurrentUploadSize;
    private ImageItem mCurrentItem;

    private static final int MESSAGE_UPLOAD_PROGRESS = 111;
    private ExecutorService mUploadService;
    private Semaphore mSemaphore;

    private int totalSize;
    private int totalSuccessSize;

    List<Runnable> mShutdownRunnables;
    private boolean isCancle;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_UPLOAD_PROGRESS:
                    int nowSize = (int) msg.obj;
                    if (btn_uploadAll != null) {
                        btn_uploadAll.setText("取消上传(" + nowSize + "/" + mCurrentUploadSize + ")");
                    }
                    break;
            }
        }
    };

    //需求：上传过程中单张和多张模式之间的切换
    //上传过程中底部按钮设为取消上传，点击后上传停止


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setTopTitle("上传影像");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<Integer> requestCodes = (List<Integer>) bundle.getSerializable("requestCodes");
            for (Integer code : requestCodes) {
                if (bundle.containsKey(String.valueOf(code))) {
                    List<ImageItem> items = (List<ImageItem>) bundle.getSerializable(String.valueOf(code));
                    images.addAll(items);
                    mapList.put(code, items);
                }
            }
            for (ImageItem imageItem : images) {
                if (imageItem.upladaState != ImageItem.UPLOAD_SUCESS) {
                    mUploadImages.add(imageItem);
                } else {
                    totalSuccessSize += 1;
                }
            }
            mSelectInfo = (SelectInfoResult) bundle.getSerializable("data");
        }
        mBasePresenter = new UploadPresenter(this);
        uploadPicAdapter = new UploadPicAdapter(this, images);
        totalSize = images.size();
        uploadPicAdapter.addClickLisener(this);
        lv.setAdapter(uploadPicAdapter);
    }

    @OnClick(R.id.single_tv)
    public void changeUploadMode() {
        isSingleUpload = !isSingleUpload;
        single_tv.setEnabled(false);
        single_tv.setText(isSingleUpload ? "单张" : "多张");
        if (isSingleUpload) {
            HhToast.showShort(this, "切换到单张模式");
        } else {
            HhToast.showShort(this, "切换到多张模式");
        }
        if(!isUploadAllNow) {
            single_tv.setEnabled(true);
        } else {
            changeUploadNow();
        }
    }

    private void changeUploadNow() {
        cancleUpload();
        upladAll();
        btn_uploadAll.setEnabled(true);
        single_tv.setEnabled(true);
    }

    @OnClick(R.id.btn_uploadAll)
    public void upladAll() {
        if(isUploadAllNow && !isCancle) {
            cancleUpload();
        } else {
            if (mUploadImages.isEmpty() && (mShutdownRunnables == null || mShutdownRunnables.isEmpty())) {
                HhToast.showShort(this, "没有需要上传的图片");
                return;
            }
            isCancle = false;
            if(mShutdownRunnables != null) {
                Iterator<ImageItem> iterator = images.iterator();
                while (iterator.hasNext()) {
                    ImageItem next = iterator.next();
                    if(next.upladaState != ImageItem.UPLOAD_SUCESS) {
                        mUploadImages.addLast(next);
                    }
                }
            }
            realUploadAll();
            single_tv.setEnabled(true);
        }
    }

    private void realUploadAll() {
        isUploadAllNow = true;
        uploadPicAdapter.setIsCanClick(false);
        mCurrentUploadSize = mUploadImages.size();
        while (mUploadImages.size() > 0) {
            ImageItem imageItem = mUploadImages.removeFirst();
            uploadThread(imageItem);
        }
    }

    private void compressImg(final ImageItem item) {
        showProgress();
        if (TextUtils.isEmpty(item.compressPath) || !new File(item.compressPath).exists()) {
            item.compressPath = CompressImageHelper.getInstance().compressImage(item, 400);
            uploadSingle(item);
        } else {
            uploadSingle(item);
        }
    }

    private void uploadThread(final ImageItem item) {
        if (mUploadService == null) {
            if (isSingleUpload || !isUploadAllNow) {
                mUploadService = Executors.newSingleThreadExecutor();
                mSemaphore = new Semaphore(1, true);
            } else {
                mUploadService = Executors.newFixedThreadPool(3);
                mSemaphore = new Semaphore(3, true);
            }
        }
        mUploadService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if(mSemaphore != null) {
                        mSemaphore.acquire();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                compressImg(item);
            }
        });
    }

    private synchronized void showProgress() {
        int nowSize = mSuccessImages.size() + mFailureItems.size() + 1;
        Message message = Message.obtain(mHandler);
        message.what = MESSAGE_UPLOAD_PROGRESS;
        message.obj = nowSize;
        mHandler.sendMessage(message);
    }

    private void uploadSingle(final ImageItem imageItem) {
        mCurrentItem = imageItem;
        mBasePresenter.uploadPicPrepapre(imageItem, mSelectInfo, new IUploadCallBack() {
            @Override
            public void onUploadStart() {
                uploaded = true;
                imageItem.upladaState = ImageItem.UPLOAD_LOADING;
                uploadPicAdapter  .setIsCanClick(false);
                uploadPicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUploadLoading(long total, long current, boolean isDownloading) {
                int nowProgress = (int) (((double) current / (double) total) * 100);
                imageItem.progress = nowProgress;
                uploadPicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUploadSucess(ImageItem item) {
                imageItem.upladaState = ImageItem.UPLOAD_SUCESS;
                uploadPicAdapter.notifyDataSetChanged();
                if (isUploadAllNow) {
                    mSuccessImages.addLast(imageItem);
                }
            }

            @Override
            public void onUploadFinish() {
                if (!mUploadImages.contains(imageItem)) {
                    deleteCompressFile(imageItem);
                }
                showUpaloadTip();
            }

            @Override
            public void onUploadError() {
                imageItem.upladaState = ImageItem.UPLOAD_FAILURE;
                mFailureItems.addLast(imageItem);
                deleteCompressFile(imageItem);
            }
        });
    }

    private void deleteCompressFile(ImageItem imageItem) {
        String path = imageItem.compressPath;
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File compressFile = new File(path);
        IOUtil.deleteFile(compressFile);
        imageItem.compressPath = null;
    }

    private void showUpaloadTip() {
        if(mSemaphore != null) {
            mSemaphore.release();
        }

        if (!isUploadAllNow) {
            updateState();
            resetUploadImages();
            return;
        }

        int failureSize = mFailureItems.size();
        int successSize = mSuccessImages.size();
        int nowUploadSize = failureSize + successSize;

        if (nowUploadSize == mCurrentUploadSize) {
            showFinishDialog(successSize, failureSize);
            updateState();
            resetUploadImages();
        }
        if(mShutdownRunnables != null &&  mSemaphore == null && mUploadService == null) {
            btn_uploadAll.setEnabled(true);
        }
    }

    private void showFinishDialog(int successSize, int failureSize) {
        totalSuccessSize += successSize;

        HhDialogManager.showTip(UploadPicActivity.this, "影像上传完毕",
                "总共上传:" + mCurrentUploadSize + "张\n" +
                        "上传成功:" + successSize + "张\n" +
                        "上传失败:" + failureSize + "张",
                "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

    private void resetUploadImages() {
        mSuccessImages.clear();
        mFailureItems.clear();
        if(!isCancle) {
            mUploadImages.addAll(mFailureItems);
            mUploadService.shutdown();
            mUploadService = null;
            mCurrentItem = null;
        } else {

        }
    }

    private void updateState() {
        if (!mUploadImages.isEmpty() || !mFailureItems.isEmpty() || (mShutdownRunnables != null && !mShutdownRunnables.isEmpty())) {
            btn_uploadAll.setEnabled(true);
            uploadPicAdapter.setIsCanClick(true);
            btn_uploadAll.setText("上传全部影像");
        } else {
            HhToast.showShort(this, "全部上传完成");
            btn_uploadAll.setText("上传全部影像");
            btn_uploadAll.setEnabled(false);
            uploadPicAdapter.setIsCanClick(false);
        }
    }

    private boolean isEnable() {
        return ((mShutdownRunnables == null ? 0 : mShutdownRunnables.size()) + mSuccessImages.size() + mFailureItems.size()) == mCurrentUploadSize;
    }

    @Override
    public void onBackPressed() {
        if (mCurrentItem != null && mCurrentItem.upladaState == mCurrentItem.UPLOAD_LOADING) {
            HhDialogManager.showAlert(UploadPicActivity.this, "温馨提示", "照片正在上传中，你确定要退出么", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isUploadAllNow = false;
                    dialog.dismiss();
                    if (uploaded) {
                        if (mUploadService != null) {
                            mUploadService.shutdownNow();
                            mUploadService = null;
                        }
                        saveHistory();
                        HhMessage.getInstance().sendMessage(new RereshMessage(mapList));
                    }
                    finish();
                }
            }, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            isUploadAllNow = false;
            if (uploaded) {
                HhMessage.getInstance().sendMessage(new RereshMessage(mapList));
            }
            finish();
        }
    }

    @Override
    public void finish() {
        saveHistory();
        super.finish();
    }

    private void cancleUpload() {
        isUploadAllNow = false;
        isCancle = true;
        mUploadImages.clear();
        if(mUploadService != null) {
            mShutdownRunnables = mUploadService.shutdownNow();
            btn_uploadAll.setEnabled(isEnable());
        }
        mUploadService = null;
        mSemaphore = null;
    }

    private void saveHistory() {
        HistoryHelper.recordUploadData(mapList);
        Pair<String, String> pair = new Pair<>(String.valueOf(totalSuccessSize), String.valueOf(totalSize));
        HistoryHelper.recordUpladResultNum(pair);
        HistoryHelper.saveHistoryData();
    }

    @Override
    public void onUploadClick(ImageItem item) {
        isUploadAllNow = false;
        btn_uploadAll.setEnabled(false);
        mUploadImages.remove(item);
        uploadThread(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
