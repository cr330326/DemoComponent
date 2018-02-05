package com.cryallen.appmain;

import android.content.res.Configuration;

import com.cryallen.common.annotation.EFModuleAnnotation;
import com.cryallen.common.framework.module.interfaces.IApplicationDelegate;
import com.cryallen.common.function.tool.log.EFLog;

import java.util.Map;

/**
 * Main组件全局应用配置
 * Created by chenran3 on 2017/12/6.
 */
@EFModuleAnnotation(moduleName = "b-main",delegateName = "com.cryallen.appmain.MainApplicationDelegate")
public class MainApplicationDelegate implements IApplicationDelegate {

    private static final String TAG = "MainApplicationDelegate";


    @Override
    public void onCreate() {
        EFLog.d(TAG, "*------------------onCreate()---------------->");
    }

    @Override
    public void enterBackground() {
        EFLog.d(TAG, "*------------------enterBackground()---------------->");
    }

    @Override
    public void enterForeground() {
        EFLog.d(TAG, "*------------------enterForeground()---------------->");
    }

    @Override
    public void receiveRemoteNotification(Map<String, String> message) {
        EFLog.d(TAG, "receiveRemoteNotification msg = " + message);
    }

    @Override
    public void onTerminate() {
        EFLog.d(TAG, "*------------------onTerminate()---------------->");
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        EFLog.d(TAG, "*------------------onConfigurationChanged()---------------->");
    }

    @Override
    public void onLowMemory() {
        EFLog.d(TAG, "*------------------onLowMemory()---------------->");
    }

    @Override
    public void onTrimMemory(int var1) {
        EFLog.d(TAG, "*------------------onTrimMemory()---------------->");
    }
}
