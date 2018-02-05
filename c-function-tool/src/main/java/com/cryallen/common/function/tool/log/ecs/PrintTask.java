package com.cryallen.common.function.tool.log.ecs;

import android.util.Log;

import com.cryallen.common.function.tool.log.bean.LogBean;

/**
 * Created by chenran3 on 2018/1/3.
 */

public class PrintTask extends BasePrint {

    public PrintTask(String filePath, int max_size) {
        super(filePath, max_size);
    }

    public void addTask(int level, String tag, String msg) {
        Log.println(level, tag, msg);
        super.addTask(level, tag, msg);
    }

    public void doTask(LogBean bean) {}
}
