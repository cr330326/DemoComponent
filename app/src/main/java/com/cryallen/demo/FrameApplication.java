package com.cryallen.demo;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cryallen.common.framework.base.BaseApplication;
import com.cryallen.common.function.tool.log.EFLog;


public class FrameApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        //日志显示开关
        EFLog.enableLog2Console(BuildConfig.DEBUG);

        if (BuildConfig.DEBUG) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(BaseApplication.getInstance());
    }
}
