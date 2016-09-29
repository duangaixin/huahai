package com.nk.hhImg.checkversion.interfa;
/**
 * Created by dgx on 2016/9/7.
 */
public interface FileDownProgressListener {
	public void onStart();

	public void onSuccess(String path);

	public void onFailed();

	public void onProgress(int progress);
}
