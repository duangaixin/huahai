package com.nk.hhImg.checkversion;

import android.os.Handler;
import android.os.Message;

import com.nk.framework.baseUtil.L;
import com.nk.hhImg.checkversion.interfa.FileDownProgressListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dgx on 2016/9/7.
 */
public class HttpDownloader {
	private URL url = null;
	FileDownProgressListener mDownProgressLisener;
	public static final int STATUS_START = 1;
	public static final int STATUS_PROGRESS = 2;
	public static final int STATUS_FAILED = 3;
	public static final int STATUS_SUCCESS = 4;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STATUS_START:
				if (mDownProgressLisener != null) {
					mDownProgressLisener.onStart();
				}
				break;
			case STATUS_PROGRESS:
				if (mDownProgressLisener != null) {
					L.i("STATUS_PROGRESS " + msg.arg1);
					mDownProgressLisener.onProgress(msg.arg1);
				}
				break;
			case STATUS_FAILED:
				if (mDownProgressLisener != null) {
					mDownProgressLisener.onFailed();
				}
				break;
			case STATUS_SUCCESS:
				if (mDownProgressLisener != null) {

					mDownProgressLisener.onSuccess((String) msg.obj);
				}
				break;

			default:
				break;
			}

		};
	};

	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象 3.得到InputStram 4.从InputStream当中读取数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// 创建一个URL对象
			url = new URL(urlStr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// 使用IO流读取数据
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void setDownListener(FileDownProgressListener mDownProgressLisener) {
		this.mDownProgressLisener = mDownProgressLisener;
	}

	public void downFile(final String urlStr, final String path,
			final String fileName) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				downFile(urlStr, path, fileName, true);
			}
		}).start();

	}

	/**
	 * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName,
			boolean inner) {

		mHandler.sendEmptyMessage(STATUS_START);

		InputStream inputStream = null;
		FileUtils fileUtils = new FileUtils();
		try {

			if (fileUtils.isFileExist(path, fileName)) {
				L.e("fileName  已下载");
				Message message = new Message();
				message.what = STATUS_SUCCESS;
				message.obj = fileUtils.getSDPATH() + path + "/" + fileName;
				mHandler.sendMessage(message);
				return 1;
			} else {
				url = new URL(urlStr);
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				inputStream = urlConn.getInputStream();
				int totalSize=urlConn.getContentLength();
				File resultFile = fileUtils.write2SDFromInput(path, fileName,
						inputStream, mHandler,totalSize);
				if (resultFile == null) {
					mHandler.sendEmptyMessage(STATUS_FAILED);
					L.e("fileName  下载失败");
					return -1;
				}
			}

		} catch (Exception e) {
			mHandler.sendEmptyMessage(STATUS_FAILED);
			e.printStackTrace();
			if (fileUtils.isFileExist(path, fileName)) {
				fileUtils.delFile(path, fileName);
			}

			return -1;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}

			} catch (Exception e) {
				mHandler.sendEmptyMessage(STATUS_FAILED);
				e.printStackTrace();
			}
		}
		Message message = new Message();
		message.what = STATUS_SUCCESS;
		message.obj = fileUtils.getSDPATH() + path + "/" + fileName;
		mHandler.sendMessage(message);

		L.e("fileName  下载成功");
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}
}