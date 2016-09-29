package com.nk.hhImg.checkversion;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by dgx on 2016/9/7.
 */
public class FileUtils {
	private String SDPATH;

	public static final String HISTORY_DATA = "historyData";

	public String getSDPATH() {
		return SDPATH;
	}
	public FileUtils() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	public void delFile(String path, String fileName) {
		File mfile = new File(SDPATH + path + "/" + fileName);
		mfile.delete();
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileDirExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String path, String fileName) {
		File file = new File(SDPATH + path + "/" + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @throws IOException
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input, Handler mHandler, int total) throws IOException {
		int hasDown = 0;
		File file = null;
		OutputStream output = null;
		creatSDDir(path);
		file = creatSDFile(path + "/" + fileName + ".ing");
		output = new FileOutputStream(file);
		byte buffer[] = new byte[1024];
		int off = 0;

		while ((off = input.read(buffer)) != -1) {
			output.write(buffer, 0, off);
			hasDown = hasDown + off;
			int progress = hasDown * 100 / total;
			Message msg = new Message();
			msg.what = HttpDownloader.STATUS_PROGRESS;
			msg.arg1 = progress;
			mHandler.sendMessage(msg);
		}
		output.flush();
		file.renameTo(new File(SDPATH+path + "/" + fileName));
		return file;
	}

	public void writeObjToFile(String fileName, Object obj) {

		File file = new File(SDPATH, fileName);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object readObjectFromFile(String fileName)
	{
		Object obj = null;
		File file = new File(SDPATH, fileName);
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			obj = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}