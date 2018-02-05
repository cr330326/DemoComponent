package com.cryallen.common.function.comlib.net.retrofit.okhttp;


import java.io.IOException;

import com.cryallen.common.function.comlib.utils.AppUtil;
import com.cryallen.common.function.comlib.utils.HttpUtil;
import com.cryallen.common.function.comlib.utils.NetServerUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chenran3 on 2018/1/15.
 * 有网络时的缓存拦截器
 */
public class NetInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 有网络时, 缓存1分钟, 最大保存时长为60s
        int maxAge = 60;
        Request request = chain.request();

        if (NetServerUtil.isNetworkConnected(AppUtil.getContext())) {
            request = request.newBuilder()
                    .removeHeader("User-Agent")
                    .header("User-Agent", HttpUtil.getUserAgent())
                    .build();

            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }

        return chain.proceed(request);
    }
}
