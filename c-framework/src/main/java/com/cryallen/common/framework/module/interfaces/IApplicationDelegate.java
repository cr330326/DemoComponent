package com.cryallen.common.framework.module.interfaces;

import android.content.res.Configuration;

import java.util.Map;

/**
 * 各个组件的Application接口类
 * Created by chenran3 on 2017/12/6.
 */
public interface IApplicationDelegate {

    void onCreate();

    void enterBackground();

    void enterForeground();

    void receiveRemoteNotification(Map<String, String> var1);

    void onTerminate();

    void onConfigurationChanged(Configuration var1);

    void onLowMemory();

    void onTrimMemory(int var1);
}
