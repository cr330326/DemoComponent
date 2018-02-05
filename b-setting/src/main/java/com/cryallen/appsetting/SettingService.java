package com.cryallen.appsetting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cryallen.appsetting.setting.views.SettingActivity;
import com.cryallen.common.router.RouterPath;
import com.cryallen.common.router.provider.ISettingProvider;

/**
 * Created by chenran3 on 2017/11/21.
 */

@Route(path = RouterPath.ROUTER_PATH_TO_SETTING_SERVICE, name = "设置页面")
public class SettingService implements ISettingProvider {
    @Override
    public void init(Context context) {}
    

    @Override
    public void goToSetting(Activity activity) {
        Intent loginIntent = new Intent(activity, SettingActivity.class);
        activity.startActivity(loginIntent);
    }
}
