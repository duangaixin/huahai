package com.lzy.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.lzy.imagepicker.bean.ImageItem;
import com.nk.framework.baseUtil.IOUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dax on 2016/8/12.
 * 根据业务需要只进行了质量压缩
 */
public class CompressImageHelper {
    private static CompressImageHelper instance = null;

    private CompressImageHelper(){
    }

    public static CompressImageHelper getInstance() {
        if (instance == null) {
            synchronized (CompressImageHelper.class) {
                if (instance == null)
                    instance = new CompressImageHelper();
            }
        }
        return instance;
    }

/*
    *//**
     * Can't compress a recycled bitmap
     *
     * @param outWidth        期望的输出图片的宽度
     * @param outHeight       期望的输出图片的高度
     * @param maxFileSize       期望的输出图片的最大占用的存储空间
     * @return
     *//*
    public synchronized String compressImage(ImageItem item, int outWidth, int outHeight, int maxFileSize) {
        String srcImagePath=item.path;
        //进行大小缩放来达到压缩的目的
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcImagePath, options);
        //根据原始图片的宽高比和期望的输出图片的宽高比计算最终输出的图片的宽和高
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        float maxWidth = outWidth;
        float maxHeight = outHeight;
        float srcRatio = srcWidth / srcHeight;
        float outRatio = maxWidth / maxHeight;
        float actualOutWidth = srcWidth;
        float actualOutHeight = srcHeight;

        if (srcWidth > maxWidth || srcHeight > maxHeight) {
            //如果输入比率小于输出比率,则最终输出的宽度以maxHeight为准()
            //比如输入比为10:20 输出比是300:10 如果要保证输出图片的宽高比和原始图片的宽高比相同,则最终输出图片的高为10
            //宽度为10/20 * 10 = 5  最终输出图片的比率为5:10 和原始输入的比率相同

            //同理如果输入比率大于输出比率,则最终输出的高度以maxWidth为准()
            //比如输入比为20:10 输出比是5:100 如果要保证输出图片的宽高比和原始图片的宽高比相同,则最终输出图片的宽为5
            //高度需要根据输入图片的比率计算获得 为5 / 20/10= 2.5  最终输出图片的比率为5:2.5 和原始输入的比率相同
            if (srcRatio < outRatio) {
                actualOutHeight = maxHeight;
                actualOutWidth = actualOutHeight * srcRatio;
            } else if (srcRatio > outRatio) {
                actualOutWidth = maxWidth;
                actualOutHeight = actualOutWidth / srcRatio;
            } else {
                actualOutWidth = maxWidth;
                actualOutHeight = maxHeight;
            }
        }
        options.inSampleSize = computSampleSize(options, actualOutWidth, actualOutHeight);
        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = BitmapFactory.decodeFile(srcImagePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (scaledBitmap == null) {
            return null;//压缩失败
        }
        //生成最终输出的bitmap
        Bitmap actualOutBitmap = Bitmap.createScaledBitmap(scaledBitmap, (int) actualOutWidth, (int) actualOutHeight, true);
        if(actualOutBitmap != scaledBitmap)
            scaledBitmap.recycle();

        //处理图片旋转问题
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(srcImagePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }
            actualOutBitmap = Bitmap.createBitmap(actualOutBitmap, 0, 0,
                    actualOutBitmap.getWidth(), actualOutBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //进行有损压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options_ = 100;
        actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)

        int baosLength = baos.toByteArray().length;

        while (baosLength / 1024 > maxFileSize) {//循环判断如果压缩后图片是否大于maxMemmorrySize,大于继续压缩
            baos.reset();//重置baos即让下一次的写入覆盖之前的内容
            options_ = Math.max(0, options_ - 10);//图片质量每次减少10
            actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//将压缩后的图片保存到baos中
            baosLength = baos.toByteArray().length;
            if (options_ == 0)//如果图片的质量已降到最低则，不再进行压缩
                break;
        }
        actualOutBitmap.recycle();

        //将bitmap保存到指定路径
        FileOutputStream fos = null;
        String filePath = getOutputFileName(srcImagePath);
        try {
            fos = new FileOutputStream(filePath);
            //包装缓冲流,提高写入速度
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
            bufferedOutputStream.write(baos.toByteArray());
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }*/

    /**
     * Can't compress a recycled bitmap
     *
     * @param maxFileSize       期望的输出图片的最大占用的存储空间
     * @return
     */
    public  String compressImage(ImageItem item, int maxFileSize) {
        String srcImagePath=item.path;
        //进行大小缩放来达到压缩的目的
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap srcBitmap = null;
        Bitmap actualOutBitmap = null;
        try {
            srcBitmap = BitmapFactory.decodeFile(srcImagePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        //处理图片旋转问题
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(srcImagePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);

            if(orientation != -1 && orientation != 0) {

                Matrix matrix = new Matrix();
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    matrix.postRotate(90);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    matrix.postRotate(180);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    matrix.postRotate(270);
                }
                actualOutBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                        srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
                if(actualOutBitmap != srcBitmap) {
                    srcBitmap.recycle();
                }
            } else {
                actualOutBitmap = srcBitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //进行有损压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if(new File(item.path).length() > (maxFileSize + maxFileSize/2 )) {

            int options_ = 10;
            actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)

            int baosLength = baos.toByteArray().length;

            Log.e("compress", "原始length =========== " + baosLength);

            int finalSize = maxFileSize / 2;

            while (baosLength / 1024 < finalSize) {//循环判断如果压缩后图片是否大于maxMemmorrySize,大于继续压缩
                baos.reset();//重置baos即让下一次的写入覆盖之前的内容
                options_ = options_ + 10;
                actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//将压缩后的图片保存到baos中
                baosLength = baos.toByteArray().length;

                Log.e("compress", "压缩一次length =========== " + baosLength);
                if (options_ == 100)//如果图片的质量已降到最低则，不再进行压缩
                    break;
            }

            Log.e("compress", "压缩比例 =========== " + options_);


        } else {
            int options_ = 100;
            actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, options_, baos);//质量压缩方法，把压缩后的数据存放到baos中
        }

        actualOutBitmap.recycle();

        //将bitmap保存到指定路径
        FileOutputStream fos = null;
        String filePath = getOutputFileName(srcImagePath, "compress");
        try {
            fos = new FileOutputStream(filePath);
            //包装缓冲流,提高写入速度
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
            bufferedOutputStream.write(baos.toByteArray());
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    private int computSampleSize(BitmapFactory.Options options, float reqWidth, float reqHeight) {
        float srcWidth = options.outWidth;//20
        float srcHeight = options.outHeight;//10
        int sampleSize = 1;
        if (srcWidth > reqWidth || srcHeight > reqHeight) {
            int withRatio = Math.round(srcWidth / reqWidth);
            int heightRatio = Math.round(srcHeight / reqHeight);
            sampleSize = Math.min(withRatio, heightRatio);
        }
        return sampleSize;
    }

    /**
     * 将旋转后的bitmap保存到文件, 然后将bitmap回收, 将compress删除并置为null
     * @param item
     * @return
     */
    public ImageItem bmpToFile(ImageItem item) {
        if(item.bitmap != null) {
            try {
                item.path = getOutputFileName(item.path, "rotate");
                FileOutputStream fileOutputStream = new FileOutputStream(item.path);
                item.bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.close();
                item.bitmap.recycle();
                item.bitmap = null;
                File file = new File(item.compressPath);
                IOUtil.deleteFile(file);
                item.compressPath = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return item;
    }

    private String getOutputFileName(String srcFilePath, String type) {
        File srcFile = new File(srcFilePath);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "HuaHai/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriString = (file.getAbsolutePath() + File.separator + srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."))+ type +".jpg");
        return uriString;
    }

    /**
     * 主动刷新相册
     * @param uri
     */
    public void notifyUpdate(Context context, Uri uri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(uri);
        context.sendBroadcast(scanIntent);
    }
}
