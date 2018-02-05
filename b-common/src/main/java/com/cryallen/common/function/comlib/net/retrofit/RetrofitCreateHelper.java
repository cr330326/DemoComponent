package com.cryallen.common.function.comlib.net.retrofit;

import android.os.Build;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.cryallen.common.function.comlib.net.retrofit.okhttp.CacheInterceptor;
import com.cryallen.common.function.comlib.net.retrofit.okhttp.HttpCache;
import com.cryallen.common.function.comlib.net.retrofit.okhttp.TrustManager;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by chenran3 on 2018/1/15.
 */
public class RetrofitCreateHelper {
    private static Retrofit retrofit;

    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static CacheInterceptor cacheInterceptor = new CacheInterceptor();

    /**
     * 创建自己的OKHttpClient
     * @return OKHttpClient对象
     */
    private static OkHttpClient getOkHttpClient() {
        try {
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA)
                    .build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    //SSL证书
                    .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())
                    //打印日志
                    .addInterceptor(interceptor)
                    //设置Cache拦截器
                    .addNetworkInterceptor(cacheInterceptor)
                    .addInterceptor(cacheInterceptor)
                    .cache(HttpCache.getCache())
                    //time out
                    .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                    //失败重连
                    .retryOnConnectionFailure(true);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if(Build.VERSION.SDK_INT >= 24){
                builder.connectionSpecs(Collections.singletonList(spec));
            }
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //构建Retrofit Json格式解析
    public static <T> T createJsonApi(String baseUrl, Class<T> service){

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit.create(service);
    }

    //构建Retrofit XML格式解析
    public static <T> T createXmlApi(String baseUrl, Class<T> service){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit.create(service);
    }

    //构建Retrofit String格式解析
    public static <T> T createStringApi(String baseUrl, Class<T> service){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit.create(service);
    }
}
