package com.nk.hhImg.checkversion.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nk.framework.application.BaseApplication;
import com.nk.framework.baseUtil.KeyValueSPUtils;
import com.nk.framework.baseUtil.L;
import com.nk.framework.hhDialog.HhDialogManager;
import com.nk.framework.view.ProgressPieView;
import com.nk.hhImg.R;
import com.nk.hhImg.checkversion.HttpDownloader;
import com.nk.hhImg.checkversion.UpdataAppManager;
import com.nk.hhImg.checkversion.bean.CheckAppVersionContent;
import com.nk.hhImg.checkversion.interfa.FileDownProgressListener;
import com.nk.hhImg.controller.HhBaseActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by dgx on 2016/9/7.
 */
public class UpdateActivity extends HhBaseActivity {
	CheckAppVersionContent mCheckAppVersionContent;
	TextView tipTextView;
	ProgressPieView circleProgress;
	Dialog dialog;

	/**
	 * 本地缓存路径
	 */
	public final static String Path = "HuaHai";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_update);
		check();
	}

	private void check() {

		/*mCheckAppVersionContent = (CheckAppVersionContent) getIntent()
				.getSerializableExtra(UpdataAppManager.INTENT_UPDATEAPP);*/

		Bundle  bundle= getIntent().getExtras();
		mCheckAppVersionContent = (CheckAppVersionContent) bundle.get(UpdataAppManager.INTENT_UPDATEAPP);
		if (UpdataAppManager.NEED_FORCE_UPDATETR
				.equals(mCheckAppVersionContent.getState())) {
			showForceDialog();
		} else {
			showNoForceDialog();
		}

	}

	private void showNoForceDialog() {

		final Dialog dialog = new AlertDialog.Builder(UpdateActivity.this, R.style.dialog)
				.show();
		dialog.setCancelable(false);
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_update);

		TextView update_content = (TextView) window
				.findViewById(R.id.update_content);
		CheckBox mBox = (CheckBox) window
				.findViewById(R.id.update_id_check);
		Button update_id_ok = (Button) window
				.findViewById(R.id.update_id_ok);
		Button update_id_cancel = (Button) window
				.findViewById(R.id.update_id_cancel);

		try {
			update_content.setText(URLDecoder.decode(
					mCheckAppVersionContent.getTitle(), "utf-8"));

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		update_id_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
				startDown();
			}
		});
		update_id_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
				UpdateActivity.this.finish();
			}
		});

		mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					KeyValueSPUtils.putString(getApplicationContext(),
							UpdataAppManager.XML_KEY_INGNORE,
							mCheckAppVersionContent.getVersion());

					L.e("忽略=======" + KeyValueSPUtils.getString(getApplicationContext(), UpdataAppManager.XML_KEY_INGNORE, ""));
				} else {
					KeyValueSPUtils.putString(getApplicationContext(),
							UpdataAppManager.XML_KEY_INGNORE, "");

					L.e("没有忽略=======" + KeyValueSPUtils.getString(getApplicationContext(), UpdataAppManager.XML_KEY_INGNORE, ""));
				}

			}
		});
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void startDown() {
		HttpDownloader mHttpDownloader = new HttpDownloader();
		mHttpDownloader.setDownListener(mDownProgressLisener);
		mHttpDownloader.downFile(mCheckAppVersionContent.getUrl(),
				Path,
				mCheckAppVersionContent.getUrl());
	}

	FileDownProgressListener mDownProgressLisener = new FileDownProgressListener() {
		@Override
		public void onStart() {

			showDownloadProgressDialog(UpdateActivity.this, "正在准备下载...", 0);

		}

		@Override
		public void onSuccess(String path) {
			dismiss();
			installApp(path);
			if (!mCheckAppVersionContent.getState()
					.equals(UpdataAppManager.NEED_FORCE_UPDATETR)) {
				UpdateActivity.this.finish();
			}
		}
		@Override
		public void onFailed() {
			dismiss();
			if (!UpdataAppManager.NEED_FORCE_UPDATETR
					.equals(mCheckAppVersionContent.getState())) {
				HhDialogManager.showTip(UpdateActivity.this, "", "下载失败！", "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UpdateActivity.this.finish();
					}
				});
			} else {
				HhDialogManager.showTip(UpdateActivity.this, "", "下载失败！", "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseApplication.clearAllActivity();
						Process.killProcess(Process.myPid());
					}
				});
			}
		}

		@Override
		public void onProgress(int progress) {
			showDownloadProgressDialog(UpdateActivity.this, "安装文件已下载"
					+ progress + "%", progress);
		}
	};

	public void showDownloadProgressDialog(Context context, String msg,
										   int progress) {
		if (((Activity) context).isFinishing()) {
			return;
		} else if (dialog != null && dialog.isShowing()) {
			if (tipTextView != null) {
				tipTextView.setText(msg);
			}
			if (circleProgress != null) {
				circleProgress.setProgress(progress);
			}
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_progress_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);

		circleProgress = (ProgressPieView) v
				.findViewById(R.id.img);
		circleProgress.setProgress(progress);
		circleProgress.setText(progress+"%");
		tipTextView = (TextView) v.findViewById(R.id.tipTextView);
		tipTextView.setText(msg);
		tipTextView.setTextSize(20);

		dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setCancelable(false);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		dialog.show();

	}

	private void installApp(String path) {
		Uri uri = Uri.fromFile(new File(path)); // 获取文件的Uri
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setDataAndType(uri,
				"application/vnd.android.package-archive");
		startActivity(installIntent);
	}

	private void showForceDialog() {
		final Dialog dialog = new AlertDialog.Builder(UpdateActivity.this, R.style.dialog)
				.show();
		dialog.setCancelable(false);
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_force_update);
		TextView umeng_update_content = (TextView) window
				.findViewById(R.id.update_content);
		try {
			umeng_update_content.setText(URLDecoder.decode(
					mCheckAppVersionContent.getTitle(), "utf-8"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		CheckBox mBox = (CheckBox) window
				.findViewById(R.id.update_id_check);

		Button update_id_ok = (Button) window
				.findViewById(R.id.update_id_ok);
		Button update_id_cancel = (Button) window
				.findViewById(R.id.update_id_cancel);
		update_id_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startDown();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mCheckAppVersionContent != null) {
			if (!mCheckAppVersionContent.getState()
					.equals(UpdataAppManager.NEED_FORCE_UPDATETR)) {
				super.onBackPressed();
			}
		}
	}
}
