package com.cryallen.common.function.comlib.net.retrofit.okhttp;

import java.io.IOException;

import com.cryallen.common.function.comlib.utils.AppUtil;
import com.cryallen.common.function.comlib.utils.HttpUtil;
import com.cryallen.common.function.comlib.utils.NetServerUtil;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Created by chenran3 on 2018/1/15.
 * 无网络时的缓存拦截器
 */
public class NoNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 无网络时，设置超时为4周
        int maxStale = 60 * 60 * 24 * 28;
        Request request = chain.request();

        if (!NetServerUtil.isNetworkConnected(AppUtil.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .removeHeader("User-Agent")
                    .header("User-Agent", HttpUtil.getUserAgent())
                    .build();

            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    }
}
