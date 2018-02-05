package com.cryallen.appsetting.setting.views;

import android.content.Intent;
import android.os.Bundle;

import com.cryallen.appsetting.R;
import com.cryallen.common.function.comlib.base.AppActivity;

public class SettingActivity extends AppActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
