package com.cryallen.applogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cryallen.applogin.login.views.LoginActivity;
import com.cryallen.common.router.RouterPath;
import com.cryallen.common.router.provider.ILoginProvider;

/**
 * Created by chenran3 on 2017/11/21.
 */

@Route(path = RouterPath.ROUTER_PATH_TO_LOGIN_SERVICE, name = "登陆页面")
public class LoginService implements ILoginProvider {
    @Override
    public void init(Context context) {}


    @Override
    public void goToLogin(Activity activity) {
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(loginIntent);
    }
}
