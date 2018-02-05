package com.cryallen.common.function.tool.log.ecs;

import com.cryallen.common.function.tool.log.utils.Config;
import com.cryallen.common.function.tool.log.utils.LoadConfig;

/**
 * Created by chenran3 on 2018/1/3.
 */

public final class ECS {

    public static String LOG_FILE_NAME = "ECS.txt";
    private PrintTask printTask;
    private Config config;

    public ECS(String filePath, int max_size) {
        this.printTask = null;
        this.config = LoadConfig.getInstance().getConfig();
        if(this.printTask == null) {
            this.printTask = new PrintTask(filePath, max_size);
            StringBuffer sb = new StringBuffer("\n");
            sb.append("**********************************************************************\n").append("*********************  ECS version 2.0.3  ****************************\n").append("**********************************************************************\n");
            this.print(4, "ECS", sb.toString());
        }
    }

    public ECS(String filePath) {
        this(filePath, 10485760);
    }

    public void d(String tag, String msg) {
        this.print(3, tag, msg);
    }

    public void e(String tag, String msg) {
        this.print(6, tag, msg);
    }

    public void i(String tag, String msg) {
        this.print(4, tag, msg);
    }

    public void w(String tag, String msg) {
        this.print(5, tag, msg);
    }

    public void v(String tag, String msg) {
        this.print(2, tag, msg);
    }

    private void print(int level, String tag, String msg) {
        this.printTask.addTask(level, tag, msg);
    }
}
