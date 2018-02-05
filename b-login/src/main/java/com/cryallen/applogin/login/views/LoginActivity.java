package com.cryallen.applogin.login.views;

import android.content.Intent;
import android.os.Bundle;

import com.cryallen.applogin.R;
import com.cryallen.common.function.comlib.base.AppActivity;

/**
 * 登录视图类
 *
 */
public class LoginActivity extends AppActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
