package debug;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.xutils.x;

/**
 * Created by chenran3 on 2017/12/7.
 */

public class LoginApplication extends Application {
    public static LoginApplication loginApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        loginApplication = this;
        init();
    }

    public void init(){
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
        x.Ext.init(loginApplication);
        // 设置是否输出debug
        x.Ext.setDebug(false);
    }
}
