package com.nk.hhImg.checkversion.interfa;
/**
 * Created by dgx on 2016/9/7.
 */
public interface FileDownProgressListener {
	void onStart();

	 void onSuccess(String path);

	 void onFailed();

	 void onProgress(int progress);
}
