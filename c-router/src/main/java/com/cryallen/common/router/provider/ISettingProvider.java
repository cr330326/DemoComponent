package com.cryallen.common.router.provider;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by chenran3 on 2017/12/8.
 */

public interface ISettingProvider extends IProvider {
    void goToSetting(Activity activity);
}
