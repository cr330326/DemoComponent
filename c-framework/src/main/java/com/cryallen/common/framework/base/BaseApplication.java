package com.cryallen.common.framework.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.cryallen.common.framework.module.interfaces.IApplicationDelegate;
import com.cryallen.common.framework.manager.ModuleManager;
import com.cryallen.common.function.tool.log.EFLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 基础全局上下文环境
 * Created by chenran3 on 2017/12/6.
 */
public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    private static BaseApplication sInstance;
    private List<IApplicationDelegate> mAppDelegateList;

    private int mCurrentCount = 0;
    private WeakReference<Activity> mCurrentActivity = null;
    private boolean isCurrent = false;
    private List<Activity> mActivityList = new ArrayList();
    private List<Activity> mResumeActivity = new ArrayList();

    public BaseApplication() {}

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        this.mCurrentCount = 0;
        this.registerActivityLifecycleCallbacks(new BaseApplication.ActivityLifecycleCallbacksImpl());

        ModuleManager.getInstance().loadModule();
        mAppDelegateList = ModuleManager.getInstance().getAppDelegateList();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate();
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        Log.d(TAG, "attachBaseContext()");
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTerminate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onConfigurationChanged(configuration);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTrimMemory(level);
        }
    }

    public Activity getCurrentActivity() {
        return this.mCurrentActivity != null ? (Activity)this.mCurrentActivity.get(): null;
    }

    public boolean isAppRunningBackground() {
        boolean result = false;
        if(this.mCurrentCount == 0) {
            result = true;
        }
        return result;
    }

    public void killAllActivity() {
        try {
            Iterator activityIterator = this.mActivityList.iterator();

            while(activityIterator.hasNext()) {
                Activity activity = (Activity) activityIterator.next();
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void killAllActivityExcept(Class<Activity> execptActivity) {
        try {
            Iterator activityIterator = this.mActivityList.iterator();

            while(activityIterator.hasNext()) {
                Activity activity = (Activity) activityIterator.next();
                if(!activity.getClass().equals(execptActivity)) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ActivityLifecycleCallbacksImpl implements ActivityLifecycleCallbacks {
        private ActivityLifecycleCallbacksImpl() {}

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            EFLog.d(TAG, "onActivityCreated --> " + activity.getClass().getName());
            BaseApplication.this.mActivityList.add(0, activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            EFLog.d(TAG, "onActivityStarted --> " + activity.getClass().getName());
            if(BaseApplication.this.mCurrentCount == 0 && !BaseApplication.this.isCurrent) {
                BaseApplication.this.isCurrent = true;
                for (IApplicationDelegate delegate : ModuleManager.getInstance().getAppDelegateList()) {
                    delegate.enterForeground();
                }
                EFLog.d("BaseApplication", "The App go to foreground");
            }
            BaseApplication.this.mCurrentCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            EFLog.d(TAG, "onActivityResumed --> " + activity.getClass().getName());
            if(!BaseApplication.this.mResumeActivity.contains(activity)) {
                BaseApplication.this.mResumeActivity.add(activity);
            }
            BaseApplication.this.mCurrentActivity = new WeakReference(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            EFLog.d(TAG, "onActivityPaused --> " + activity.getClass().getName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            EFLog.d(TAG, "onActivityStopped --> " + activity.getClass().getName());
            BaseApplication.this.mResumeActivity.remove(activity);
            BaseApplication.this.mCurrentCount--;
            if(BaseApplication.this.mCurrentCount == 0 && BaseApplication.this.isCurrent) {
                BaseApplication.this.isCurrent = false;
                for (IApplicationDelegate delegate : ModuleManager.getInstance().getAppDelegateList()) {
                    delegate.enterBackground();
                }
                EFLog.d(TAG, "The App go to background");
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            EFLog.d(TAG, "onActivitySaveInstanceState --> " + activity.getClass().getName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            EFLog.d(TAG, "onActivityDestroyed --> " + activity.getClass().getName());
            BaseApplication.this.mActivityList.remove(activity);
        }
    }
}
