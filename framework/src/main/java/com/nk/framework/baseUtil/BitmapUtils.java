package com.nk.framework.baseUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by wuxin on 2015/12/23.
 */
public class BitmapUtils {

    static long kb = 1024;
    static long mb = kb * 1024;

    /**
     * 检查图片大小
     */
    public static boolean isTooLarge(File file) {

        int sizeM = (int) (file.length() / mb);

        return sizeM > 10;
    }

    /**
     * 获取图片exif信息
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }

        return degree;
    }

    /**
     * 获取图片真实方向并返回bitmap
     *
     * @param filepath
     * @param bitmap
     * @return
     */
    public static Bitmap getTrueDirection(String filepath, Bitmap bitmap) {

        Bitmap trueBitmap;

        int degree = 0;

        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }

        if (degree == 90 || degree == 180 || degree == 270) {

            Matrix matrix = new Matrix();
            matrix.setRotate(degree);

            trueBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            trueBitmap = bitmap;
        }

        return trueBitmap;
    }

    public static Bitmap getTrueBitmapByDegree(int degree, Bitmap bitmap) {

        Bitmap trueBitmap;

        if (degree == 90 || degree == 180 || degree == 270) {

            Matrix matrix = new Matrix();
            matrix.setRotate(degree);

            trueBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            trueBitmap = bitmap;
        }

        return trueBitmap;
    }

    /**
     * 保存拍照图片到相册
     */
    public static void saveFile(final Context context, final File file) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    File myDir = new File("/sdcard/DCIM/Camera");
                    myDir.mkdirs();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File mFile = new File(myDir, fname);
                    if (mFile.exists()) mFile.delete();

                    try {
                        FileOutputStream fos = new FileOutputStream(mFile);
                        FileInputStream fis = new FileInputStream(file);

                        byte[] data = new byte[1024];

                        while (fis.read(data) != -1) {

                            fos.write(data, 0, data.length);
                            fos.flush();
                        }

                        fis.close();
                        fos.close();

                        //通知相册更新
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(mFile);
                        intent.setData(uri);
                        context.sendBroadcast(intent);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }

    /**
     * 防止图片OOM
     *
     * @return
     */
    public static BitmapFactory.Options getSimpleBitmapOptions() {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        //2.为位图设置100K的缓存
        opts.inTempStorage = new byte[100 * 1024];
        //压缩图片
        opts.inSampleSize = 4;
        //3.设置位图颜色显示优化方式
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        //6.设置解码位图的尺寸信息
        opts.inInputShareable = true;

        return opts;
    }

    /**
     * 获取指定view以bitmap返回
     *
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(canvas);
        return screenshot;
    }

    /**
     * 将bitmap转换成byte数组，用于传递bitmap对象
     *
     * @param bmp
     * @return
     */
    private byte[] getBmpToByte(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 50, baos);

        return baos.toByteArray();
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 回收bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {

        if (bitmap != null && !bitmap.isRecycled()) {

            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    /**
     * 图片压缩并保持2100*2400
     *
     * @param capturePicturePath
     * @return
     */
    public static BitmapFactory.Options getBitmapOptions(String capturePicturePath) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(capturePicturePath, opts);

        L.e("opts_width=========" + opts.outWidth);
        L.e("opts_height=========" + opts.outHeight);

        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;

        if (outWidth > outHeight) {
            opts.inSampleSize = computerSizeInSampleSize(opts, 2400, 2100);
        } else {
            opts.inSampleSize = computerSizeInSampleSize(opts, 2100, 2400);
        }

        L.e("opts.inSampleSize=======" + opts.inSampleSize);

        opts.inJustDecodeBounds = false;
        //2.为位图设置100K的缓存
        opts.inTempStorage = new byte[100 * 1024];
        //3.设置位图颜色显示优化方式
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        //6.设置解码位图的尺寸信息
        opts.inInputShareable = true;

        return opts;
    }


    /**
     * 滤镜options
     * 由于内存太高，将bitmap压缩到小图(400)
     */
    public static BitmapFactory.Options getFilterOptions(Bitmap bitmap) {

        BitmapFactory.Options opts = new BitmapFactory.Options();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        L.e("滤镜用户图片======width===" + width);
        L.e("滤镜用户图片======height===" + height);

        if (width > height) {
            opts.inSampleSize = computerSizeInSampleSize(width, height, 800, 480);
        } else {
            opts.inSampleSize = computerSizeInSampleSize(width, height, 480, 800);
        }

        L.e("滤镜opts.inSampleSize=======" + opts.inSampleSize);

        opts.inJustDecodeBounds = false;
        //2.为位图设置100K的缓存
        opts.inTempStorage = new byte[100 * 1024];
        //3.设置位图颜色显示优化方式
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        //6.设置解码位图的尺寸信息
        opts.inInputShareable = true;

        return opts;
    }

    /**
     * 普通压缩到2400*2100
     * @param capturePicturePath
     * @return
     */
    public static Bitmap setBitmapToFileToUpload(String capturePicturePath) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(capturePicturePath, opts);

        L.e("opts_width=========" + opts.outWidth);
        L.e("opts_height=========" + opts.outHeight);

        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;

        if (outWidth > outHeight) {
            opts.inSampleSize = computerSizeInSampleSize(opts, 2400, 2100);
        } else {
            opts.inSampleSize = computerSizeInSampleSize(opts, 2100, 2400);
        }

        L.e("opts.inSampleSize=======" + opts.inSampleSize);

        opts.inJustDecodeBounds = false;
        //2.为位图设置100K的缓存
        opts.inTempStorage = new byte[100 * 1024];
        //3.设置位图颜色显示优化方式
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        //6.设置解码位图的尺寸信息
        opts.inInputShareable = true;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(capturePicturePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, opts);

        Bitmap trueDirectionBitmap = getTrueDirection(capturePicturePath, bitmap);

        if (trueDirectionBitmap != bitmap) {
           BitmapUtils.recycleBitmap(bitmap);
        }

        return trueDirectionBitmap;
    }

    /**
     * 滤镜压缩到1336*750
     * @param capturePicturePath
     * @return
     */
    public static Bitmap setFilterBitmapToFileToUpload(String capturePicturePath) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(capturePicturePath, opts);

        L.e("opts_width=========" + opts.outWidth);
        L.e("opts_height=========" + opts.outHeight);

        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;

        if (outWidth > outHeight) {
            opts.inSampleSize = computerSizeInSampleSize(opts, 1336, 750);
        } else {
            opts.inSampleSize = computerSizeInSampleSize(opts, 750, 1336);
        }

        L.e("opts.inSampleSize=======" + opts.inSampleSize);

        opts.inJustDecodeBounds = false;
        //2.为位图设置100K的缓存
        opts.inTempStorage = new byte[100 * 1024];
        //3.设置位图颜色显示优化方式
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        //6.设置解码位图的尺寸信息
        opts.inInputShareable = true;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(capturePicturePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, opts);

        Bitmap trueDirectionBitmap = getTrueDirection(capturePicturePath, bitmap);

        if (trueDirectionBitmap != bitmap) {
           BitmapUtils.recycleBitmap(bitmap);
        }

        return trueDirectionBitmap;
    }

    /**
     * 计算缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int computerSizeInSampleSize(BitmapFactory.Options options,
                                                int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = (int) Math.ceil((double) height
                    / (double) reqHeight);
            final int widthRatio = (int) Math.ceil((double) width / (double) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    /**
     * 计算缩放比例
     *
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int computerSizeInSampleSize(int outWidth, int outHeight,
                                                int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = outWidth;
        final int width = outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = (int) Math.ceil((double) height
                    / (double) reqHeight);
            final int widthRatio = (int) Math.ceil((double) width / (double) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }


    public static File bitmapToFile(Bitmap bitmap, String mPicPath) {

        File dirFile = new File("/sdcard/upload_");

        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String btmFormat = BitmapUtils.getExtName(mPicPath);

        File myRotateFile = new File(dirFile + "rotate." + btmFormat);


        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myRotateFile));


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);


            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myRotateFile;
    }

    /**
     * 保存使用滤镜的图片,jpg
     *
     * @param bitmap
     */
    public static File bitmapToFile(Bitmap bitmap) {

        File dirFile = new File("/sdcard/DCIM/Camera");

        File file = new File(dirFile, "upload.jpg");

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            L.e("格式为====JPEG==文件地址===" + file.getPath());
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 获取文件后缀名
     */
    public static String getExtName(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    /**
     * 获取网络图片宽高比
     */
    public static float getNetPicWH(String url) {
        try {
            URL m_url = new URL(url);
            HttpURLConnection con = (HttpURLConnection) m_url.openConnection();
            InputStream in = con.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            int height = options.outHeight;
            int width = options.outWidth;

            return (float) (width / height);
        } catch (Exception e) {
            e.printStackTrace();

            return 1;
        }
    }
}
