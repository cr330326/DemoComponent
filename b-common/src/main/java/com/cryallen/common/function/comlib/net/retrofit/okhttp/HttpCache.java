package com.cryallen.common.function.comlib.net.retrofit.okhttp;


import com.cryallen.common.function.comlib.utils.FileUtil;
import okhttp3.Cache;

/**
 * Created by chenran3 on 2018/1/15.
 */
public class HttpCache {

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 50 * 1024 * 1024;

    public static Cache getCache() {
        return new Cache(FileUtil.getNetCacheDirPath(), HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }
}
