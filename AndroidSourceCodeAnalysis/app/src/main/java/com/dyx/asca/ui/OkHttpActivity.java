package com.dyx.asca.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.dyx.asca.R;
import com.dyx.asca.model.PersonBean;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
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
    private OkHttpClient client = new OkHttpClient();
    private OkHttpClient cacheClient;
    private OkHttpClient timeOutClient;
    private OkHttpClient authClient;
    private TextView mTextView;

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_MARK_DOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_MARK_PNG = MediaType.parse("image/png");
    private static final String API_URL = "http://api.dayongxin.com/login";
    private static final String API_TOKEN = "xxxxxxxxxxx";
    private Moshi moshi = new Moshi.Builder().build();
    private JsonAdapter<Content> contentJsonAdaptert = moshi.adapter(Content.class);
    private static final File CACHE_FILE = new File("/xx/xx");
    private String responseBody1 = "";
    private String responseBody2 = "";
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sned)
    public void onViewClicked() {
        /**
         * get请求
         */
        getRequest();
        /**
         * post请求
         */
        postRequest();
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

    private void postRequest() {
        /**
         * 1、post字符串
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postStringBySync();
            }
        }).start();
        //异步
        postStringByAsyn();
        /**
         * 2、post数据流
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postStreamBySync();
            }
        }).start();
        //异步
        postStreamByAsyn();
        /**
         * 3、post文件
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postFileBySync();
            }
        }).start();
        //异步
        postFileByAsyn();
        /**
         * 4、post Form参数
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postFormParamsBySync();
            }
        }).start();
        //异步
        postFormParamsByAsyn();
        /**
         * 5、post multipart请求
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postMultipartBySync();
            }
        }).start();
        //异步
        postMultipartByAsyn();
        /**
         * 6、使用moshi解析json响应
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postMoshiParseJsonBySync();
            }
        }).start();
        //异步
        postMoshiParseJsonByAsyn();
        /**
         * 7、响应缓存
         */
        cacheResponse(CACHE_FILE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                postCacheBySync();
            }
        }).start();
        //异步
        postCacheByAsyn();
        /**
         * 8、取消访问
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                cancelCallBySync();
            }
        }).start();
        //异步
        cancelCallByAsyn();
        /**
         * 9、超时
         */
        configTimeOuts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                timeOutsBySync();
            }
        }).start();
        //异步
        timeOutsByAsyn();
        /**
         * 10、配置每个call
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                perCallBySync();
            }
        }).start();
        //异步
        perCallByAsyn();
        /**
         * 11、处理Authentication
         */
        Authorize();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                authBySync();
            }
        }).start();
        //异步
        authByAsyn();
    }

    private void authByAsyn() {
        Request request = new Request.Builder().url(API_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Logger.d(response.body().string());
                }
            }
        });
    }

    private void authBySync() {
        Request request = new Request.Builder().url(API_URL).build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
                Logger.d(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Authorize() {
        authClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            @javax.annotation.Nullable
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                if (response.request().header("Authorization") != null) {
                    return null;
                }
                //Authorize response
                Logger.d(response);
                //challenges
                Logger.d(response.challenges());
                String credentials = Credentials.basic("dayongxin", "123456");
                return response.request().newBuilder().header("Authorization", credentials).build();
            }
        }).build();
    }

    private void perCallByAsyn() {
        Request request = new Request.Builder().url(API_URL).build();

        OkHttpClient client1 = client.newBuilder().readTimeout(1000, TimeUnit.SECONDS).build();

        client1.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    Logger.d(response.body().string());
                }
            }
        });

        OkHttpClient client2 = client.newBuilder().readTimeout(1000, TimeUnit.SECONDS).build();

        client2.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    Logger.d(response.body().string());
                }
            }
        });
    }

    private void perCallBySync() {
        Request request = new Request.Builder().url(API_URL).build();

        OkHttpClient client1 = client.newBuilder().readTimeout(1000, TimeUnit.SECONDS).build();

        try {
            Response response = client1.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                Logger.d(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        OkHttpClient client2 = client.newBuilder().readTimeout(3000, TimeUnit.SECONDS).build();

        try {
            Response response = client2.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                Logger.d(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void timeOutsByAsyn() {
        Request request = new Request.Builder().url(API_URL).build();
        timeOutClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    //响应完成
                    Logger.d(response.body().string());
                }
            }
        });
    }

    private void timeOutsBySync() {
        Request request = new Request.Builder().url(API_URL).build();

        try {
            Response response = timeOutClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                //响应完成
                Logger.d(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configTimeOuts() {
        timeOutClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private void cancelCallByAsyn() {
        Request request = new Request.Builder().url(API_URL).build();

        final long nano_time = System.nanoTime();
        final Call call = client.newCall(request);

        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                //开始时间
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));
                //取消
                call.cancel();
                //结束时间
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));
            }
        }, 1, TimeUnit.SECONDS);
        //执行Call
        Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //失败
                    Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)) + response.body().string());
                } else {
                    //成功
                    Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)) + response.body().string());
                }
            }
        });
    }

    private void cancelCallBySync() {
        Request request = new Request.Builder().url(API_URL).build();

        final long nano_time = System.nanoTime();
        final Call call = client.newCall(request);

        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                //开始时间
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));
                //取消
                call.cancel();
                //结束时间
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));
            }
        }, 1, TimeUnit.SECONDS);
        //执行Call
        Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)));
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                //失败
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)) + response.body().string());
            } else {
                //成功
                Logger.d(String.valueOf(((System.nanoTime() - nano_time) / 1e9f)) + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postCacheByAsyn() {
        Request request = new Request.Builder().url(API_URL).build();
        cacheClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    responseBody1 = response.body().string();
                    //1、response
                    Logger.d("Response 1 response" + response);
                    //2、cacheResponse
                    Logger.d("Response 1 response" + response.cacheResponse());
                    //3、networkResponse
                    Logger.d("Response 1 response" + response.networkResponse());
                }
            }
        });

        cacheClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    responseBody2 = response.body().string();
                    //1、response
                    Logger.d("Response 2 response" + response);
                    //2、cacheResponse
                    Logger.d("Response 2 response" + response.cacheResponse());
                    //3、networkResponse
                    Logger.d("Response 2 response" + response.networkResponse());
                }
            }
        });
        //判断responseBody1与responseBody2是否相等
        Logger.d(responseBody1.equals(responseBody2));
    }

    private void postCacheBySync() {
        Request request = new Request.Builder().url(API_URL).build();

        String responseBody1 = "";
        try {
            Response response1 = cacheClient.newCall(request).execute();
            if (!response1.isSuccessful()) {
                throw new IOException("Unexpected code:" + response1);
            } else {
                responseBody1 = response1.body().string();
                //1、response
                Logger.d("Response 1 response" + response1);
                //2、cacheResponse
                Logger.d("Response 1 response" + response1.cacheResponse());
                //3、networkResponse
                Logger.d("Response 1 response" + response1.networkResponse());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseBody2 = "";
        try {
            Response response2 = cacheClient.newCall(request).execute();
            if (!response2.isSuccessful()) {
                throw new IOException("Unexpected code:" + response2);
            } else {
                responseBody2 = response2.body().string();
                //1、response
                Logger.d("Response 2 response" + response2);
                //2、cacheRespons
                Logger.d("Response 2 response" + response2.cacheResponse());
                //3、networkRespons
                Logger.d("Response 2 response" + response2.networkResponse());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //判断responseBody1与responseBody2是否相等
        Logger.d(responseBody1.equals(responseBody2));
    }

    private void cacheResponse(File cacheFile) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(cacheFile, cacheSize);
        cacheClient = new OkHttpClient.Builder().cache(cache).build();
    }

    private void postMoshiParseJsonByAsyn() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    Content content = contentJsonAdaptert.fromJson(response.body().source());

                    for (Map.Entry<String, ContentFile> entry : content.files.entrySet()) {
                        Logger.d(entry.getKey() + ":" + entry.getValue().content);
                    }
                }
            }
        });
    }

    private void postMoshiParseJsonBySync() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                Content content = contentJsonAdaptert.fromJson(response.body().source());

                for (Map.Entry<String, ContentFile> entry : content.files.entrySet()) {
                    Logger.d(entry.getKey() + ":" + entry.getValue().content);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postMultipartByAsyn() {
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "dayongxin")
                .addFormDataPart("image", "ic_header.png", RequestBody.create(MEDIA_TYPE_MARK_PNG, new File("/xxx/xxx/img.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + API_TOKEN).url(API_URL)
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    String result = response.body().string();
                }
            }
        });
    }

    private void postMultipartBySync() {
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "dayongxin")
                .addFormDataPart("image", "ic_header.png", RequestBody.create(MEDIA_TYPE_MARK_PNG, new File("/xxx/xxx/img.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + API_TOKEN).url(API_URL)
                .post(multipartBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                String result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postFormParamsByAsyn() {
        RequestBody formBody = new FormBody.Builder().add("keywords", "android").build();
        Request request = new Request.Builder().url(API_URL).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    String result = response.body().string();
                }
            }
        });
    }

    private void postFormParamsBySync() {
        RequestBody formBody = new FormBody.Builder().add("keywords", "android").build();

        Request request = new Request.Builder().url(API_URL).post(formBody).build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                String result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postFileByAsyn() {
        File file = new File("README.md");

        Request request = new Request.Builder().url(API_URL).post(RequestBody.create(MEDIA_TYPE_MARK_DOWN, file)).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    String result = response.body().string();
                }
            }
        });
    }

    private void postFileBySync() {
        File file = new File("README.md");

        Request request = new Request.Builder().url(API_URL).post(RequestBody.create(MEDIA_TYPE_MARK_DOWN, file)).build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                String result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postStreamByAsyn() {
        final String postBody = "{\n" +
                "    \"name\": \"dayongxin\",\n" +
                "    \"age\": 18\n" +
                "}";
        RequestBody requestBody = new RequestBody() {
            @javax.annotation.Nullable
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_JSON;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(postBody);
            }
        };
        Request request = new Request.Builder().url(API_URL).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    String result = response.body().string();
                }
            }
        });
    }

    private void postStreamBySync() {
        final String postBody = "{\n" +
                "    \"name\": \"dayongxin\",\n" +
                "    \"age\": 18\n" +
                "}";
        RequestBody requestBody = new RequestBody() {
            @javax.annotation.Nullable
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_JSON;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(postBody);
            }
        };
        Request request = new Request.Builder().url(API_URL).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                String result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postStringByAsyn() {
        String postBody = "{\n" +
                "    \"name\": \"dayongxin\",\n" +
                "    \"age\": 18\n" +
                "}";
        Request request = new Request.Builder().url(API_URL).post(RequestBody.create(MEDIA_TYPE_JSON, postBody)).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code:" + response);
                } else {
                    String result = response.body().string();
                }
            }
        });
    }

    private void postStringBySync() {
        String postBody = "{\n" +
                "    \"name\": \"dayongxin\",\n" +
                "    \"age\": 18\n" +
                "}";
        Request request = new Request.Builder().url(API_URL).post(RequestBody.create(MEDIA_TYPE_JSON, postBody)).build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code:" + response);
            } else {
                String result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRequest() {
        /**
         * 1、发送简单的网络请求
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                sendSimpleNetworkRequestBySync();
            }
        }).start();
        /**
         * 异步：OkHttp normally creates a new worker thread to dispatch the network request and uses the same thread to handle the response.
         */
        sendSimpleNetworkRequestByAsyn();
        /**
         * 2、添加查询参数
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                sendQueryParameterNetworkRequestBySync();
            }
        }).start();
        //异步
        sendQueryParameterNetworkRequestByAsyn();
        /**
         * 3、添加headers参数
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步
                sendAuthorizationHeaderNetworkRequestBySync();
            }
        }).start();
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
//        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().onlyIfCached().build()).build();
        //无缓存
//        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().noCache().build()).build();
        /**
         * 3、设置缓存过期时间
         */
        //设置缓存响应的最大年龄
        Request request = new Request.Builder().url("").cacheControl(new CacheControl.Builder().maxAge(1, TimeUnit.DAYS).build()).build();
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
                    throw new IOException("Unexpected code " + response);
                } else {
                    //成功
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTextView != null && !TextUtils.isEmpty(result)) {
                                mTextView.setText(result);
                            }
                        }
                    });
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
                 * 获取响应header列表
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

    static class Content {
        Map<String, ContentFile> files;
    }

    static class ContentFile {
        String content;
    }
}
