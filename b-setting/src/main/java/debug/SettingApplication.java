package debug;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.xutils.x;

/**
 * Created by chenran3 on 2017/12/7.
 */

public class SettingApplication extends Application {
    public static SettingApplication settingApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        settingApplication = this;
        init();
    }

    public void init(){
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
        x.Ext.init(settingApplication);
        // 设置是否输出debug
        x.Ext.setDebug(false);
    }
}
