package com.cryallen.common.function.comlib.net.xutil;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;

import com.cryallen.common.function.tool.log.EFLog;


public class XUtilCallBack<ResultType> implements Callback.CommonCallback<ResultType>{
    private static final String TAG = "XUtilCallBack";

    @Override
    public void onSuccess(ResultType result) {
        //可以根据公司的需求进行统一的请求成功的逻辑处理
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        EFLog.e(TAG,"Error Message :"+ ex.getMessage());
        //可以根据公司的需求进行统一的请求网络失败的逻辑处理
        if (ex instanceof HttpException) {

        }
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}