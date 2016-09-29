package com.nk.hhImg.okhttp;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nk.framework.baseUtil.L;
import com.nk.framework.baseUtil.NetUtils;
import com.nk.hhImg.okhttp.callback.ResultCallback;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by dax on 2015/12/2.
 */
public class OkHttpClientManager {

    public static final int DEFAULT_TIMEOUT_MS = 20;
    //添加缓存


    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private RopResult ropResult;

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();

        /*//缓存
        File file = new File(BaseApplication.getInstance().getCacheDir(),"http");
        long httpCacheSize =10*1024*1024;// 10 MiB*/

//        mOkHttpClient.setCache(new Cache(file,httpCacheSize));

        mOkHttpClient.setConnectTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.SECONDS);
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 23) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
        if (false) {
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public void execute(final Request request, ResultCallback callback) {

        if (callback == null)
            callback = ResultCallback.DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                resCallBack.onStart(request);
            }
        });

        if (!NetUtils.isConnected()) {
            resCallBack.onFinish();
            resCallBack.onNoNetwork();
        }

        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Request request, final IOException e) {
                //TODO when cancel , should do?

                L.i("response  error==== \n" + e.toString());
                sendFailResultCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {

                //打印返回结果
                String resultStr = "";

                try {
                    resultStr = response.body().string();
                    L.e(resultStr);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendFailResultCallback(response.request(), e, resCallBack);
                }
                L.okmeg("response==== \n", resultStr);

                /*if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendResponseErr(response.body().string(), resCallBack);
//                        sendFailResultCallback(request, new RuntimeException(response.body().string()), resCallBack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
*/
                ropResult = new RopResult();
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    ropResult.setCode(jsonObject.getString("code"));
                    ropResult.setMessage(jsonObject.getString("message"));
//                    ropResult.setResult(jsonObject.getString("result"));


                 if (ropResult.isFail()){
                     sendResponseErr(ropResult.getMessage(), resCallBack);
                 }

                else {

                        if (resCallBack.mType == String.class) {

                            sendSuccessResultCallback(resultStr, resCallBack);
                        } else {
//                          ropResult.setResult(jsonObject.getString("result"));
                            Object o = mGson.fromJson(jsonObject.getString("result"), resCallBack.mType);
                            L.e(o.toString());
                            sendSuccessResultCallback(o, resCallBack);
                        }
                  }
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendResponseErr("json 解析出错!!", resCallBack);
                }


            }
        });
    }


    public <T> T execute(Request request, Class<T> clazz) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        String respStr = execute.body().string();
        return mGson.fromJson(respStr, clazz);
    }

    public void sendResponseErr(final String str, final ResultCallback callback) {

        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponseError(str);
                callback.onFinish();
            }
        });
    }


    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onFinish();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onFinish();
            }
        });
    }


    public void cancelTag(Object tag) {
        mOkHttpClient.cancel(tag);
    }




}

