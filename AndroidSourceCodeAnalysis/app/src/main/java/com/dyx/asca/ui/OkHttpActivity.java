package com.dyx.asca.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.dyx.asca.R;
import com.dyx.asca.model.PersonBean;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Author：dayongxin
 * Function：
 */
public class OkHttpActivity extends AppCompatActivity {
    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    @BindView(R.id.btn_sned)
    Button btnSned;
    private Unbinder mUnbinder;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sned)
    public void onViewClicked() {
        /**
         * 1、发送简单的网络请求
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //同步
//                sendSimpleNetworkRequestBySync();
//            }
//        }).start();
        /**
         * 异步：OkHttp normally creates a new worker thread to dispatch the network request and uses the same thread to handle the response.
         */
        sendSimpleNetworkRequestByAsyn();
        /**
         * 2、添加查询参数
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //同步
//                sendQueryParameterNetworkRequestBySync();
//            }
//        }).start();
        //异步
        sendQueryParameterNetworkRequestByAsyn();
        /**
         * 3、添加headers参数
         */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //同步
//                sendAuthorizationHeaderNetworkRequestBySync();
//            }
//        }).start();
        //异步
        sendAuthorizationHeaderNetworkRequestByAsyn();
        /**
         * 4、发送认证请求
         */
        sendingAuthenticatedRequests();
        /**
         * 5、缓存网络请求
         */
        cachingNetworkResponses();
        /**
         * 6、打印okhttp Log信息
         */
        doHttpLogInterceptor();
        /**
         * 7、使用Facebook Stetho监视网络访问
         */
        useStethoMonitorNetworkCalls();
        /**
         * 8、使用Websockets
         */
        useWebsockets();
        /**
         * 9、在老的设备上支持TLS V1.2，详细代码见XApplication中supportTlsv12()
         */
    }

    /**
     * OkHttp handles all the work on a separate thread, so you don't have to worry about making Websocket calls on the main thread.
     */
    private void useWebsockets() {
        Request request = new Request.Builder().url("").build();
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @javax.annotation.Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
        webSocket.send("Hello WebSocket!");
        webSocket.close(1000, "WebSocket Closing!");
    }

    private void useStethoMonitorNetworkCalls() {
        OkHttpClient stethoClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
        Request request = new Request.Builder().url("").build();
        stethoClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void doHttpLogInterceptor() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.build();
    }

    private void cachingNetworkResponses() {
        /**
         * 1、创建代有缓存功能的OkHttpClient
         */
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(new File(getApplication().getCacheDir(), "okhttpcache"), cacheSize);
        OkHttpClient cacheClient = new OkHttpClient.Builder().cache(cache).build();
        /**
         * 2、为网络请求设置缓存选项
         */
        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().onlyIfCached().build()).build();
        //无缓存
//        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().noCache().build()).build();
        /**
         * 3、设置缓存过期时间
         */
        //设置缓存响应的最大年龄
//        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().maxAge(1, TimeUnit.DAYS).build()).build();
        //设置已超过其新鲜生命周期的缓存响应
//        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()).build();
        //设置响应将继续保持新鲜的最小秒数
//        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().minFresh(1, TimeUnit.DAYS).build()).build();
        /**
         * 4、处理缓存响应
         */
        cacheClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取缓存响应
                Response cacheResponse = response.cacheResponse();
            }
        });
    }

    private void sendingAuthenticatedRequests() {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        String token = null;
        String tokenSecret = null;
        consumer.setTokenWithSecret(token, tokenSecret);
        client.interceptors().add(new SigningInterceptor(consumer));
    }

    private void sendAuthorizationHeaderNetworkRequestByAsyn() {
        Request request = new Request.Builder().header("Authorization", "bearer xxxx").url("").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //失败
                } else {
                    //成功
                }
            }
        });
    }

    private void sendQueryParameterNetworkRequestByAsyn() {
        HttpUrl.Builder builder = HttpUrl.parse("").newBuilder();
        builder.addQueryParameter("version", "1.0");
        builder.addQueryParameter("system", "android");
        builder.addQueryParameter("date", "2017-10-17");
        String url = builder.build().toString();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //失败
                } else {
                    //成功
                    String responseData = response.body().string();
                    Logger.d(responseData);
                }
            }
        });
    }

    private void sendSimpleNetworkRequestByAsyn() {
        Request request = new Request.Builder()
                .url("https://www.baidu.com/")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /**
                 * 后去响应Body
                 */
                if (!response.isSuccessful()) {
                    //错误信息
                } else {
                    //成功
                    String responseData = response.body().string();
                    Logger.d(responseData);
                    /**
                     * 解析响应数据：
                     * 1、JSONObject
                     * 2、gson
                     */
                    //方法一：JSONObject
                    try {
                        JSONObject object = new JSONObject(responseData);
                        String name = object.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //方法二：gson
                    //TODO charStream
                    Gson gson = new Gson();
                    PersonBean personBean = gson.fromJson(response.body().charStream(), PersonBean.class);
                    String name = personBean.getName();
                }
                /**
                 * 获取响应header
                 */
                Headers headers = response.headers();
                //1、header获取方法一
                for (int i = 0; i < headers.size(); i++) {
                    Logger.d(headers.name(i) + ":" + headers.value(i));
                }
                //2、header获取方法二
                String header = response.header("Date");
            }
        });
    }

    private void sendAuthorizationHeaderNetworkRequestBySync() {
        Request request = new Request.Builder().header("Authorization", "bearer xxxx").url("").build();

        try {
            Response response = client.newCall(request).execute();
            Logger.d(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendQueryParameterNetworkRequestBySync() {
        HttpUrl.Builder builder = HttpUrl.parse("").newBuilder();
        builder.addQueryParameter("version", "1.0");
        builder.addQueryParameter("system", "android");
        builder.addQueryParameter("date", "2017-10-17");
        String url = builder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Logger.d(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSimpleNetworkRequestBySync() {
        Request request = new Request.Builder()
                .url("https://www.baidu.com/")
                .build();
        try {
            final Response response = client.newCall(request).execute();
            Logger.d(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
