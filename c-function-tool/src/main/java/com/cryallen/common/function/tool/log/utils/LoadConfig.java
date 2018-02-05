package com.cryallen.common.function.tool.log.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

import com.cryallen.common.function.tool.log.ecs.WriteFile;
import com.cryallen.common.function.tool.log.listener.ConfigListener;

/**
 * Created by chenran3 on 2018/1/3.
 */

public class LoadConfig {

    private static final String TAG = "LoadConfig";
    private static final String FILE_PATH = "/assets/";
    private static final String FILE_NAME = "config.properties";
    private Properties properties = new Properties();
    private Config config = new Config();
    private static LoadConfig instance;
    private String filePath = "";
    private List<ConfigListener> listeners = new ArrayList();
    private long lastModified;
    private Thread modifyListener;

    private LoadConfig() {
    }

    public static LoadConfig getInstance() {
        if(instance == null) {
            instance = new LoadConfig();
        }

        return instance;
    }

    public boolean loadConfigFile(String file) {
        boolean result = false;
        boolean isSystem = true;
        if(file != null && !"".equals(file)) {
            if(!file.endsWith("config/config.properties")) {
                if(!file.endsWith("/")) {
                    file = file + "/";
                }

                if(file.equals("/assets/")) {
                    file = file + "config.properties";
                } else {
                    file = file + "config/config.properties";
                    isSystem = false;
                }

                this.filePath = file;
            } else {
                isSystem = false;
            }

            Object is = null;

            label143: {
                try {
                    if(isSystem) {
                        is = LoadConfig.class.getResourceAsStream(file);
                    } else {
                        is = new FileInputStream(file);
                    }

                    if(is != null) {
                        this.properties.load((InputStream)is);
                        this.parseProperty(this.properties);
                        this.properties.clear();
                        result = true;
                        break label143;
                    }

                    is = LoadConfig.class.getResourceAsStream("/assets/config.properties");
                    final Object finalIs = is;
                    final String finalFile = file;
                    Executors.newCachedThreadPool().submit(new Runnable() {
                        public void run() {
                            WriteFile.inputStreamToFile((InputStream) finalIs, WriteFile.createFile(finalFile));
                            LoadConfig.this.listenerEntyFile();
                        }
                    });
                } catch (IOException var16) {
                    var16.printStackTrace();
                    result = false;
                    break label143;
                } finally {
                    if(is != null) {
                        try {
                            ((InputStream)is).close();
                        } catch (IOException var15) {
                            var15.printStackTrace();
                        }
                    }

                }

                return false;
            }

            this.listenerEntyFile();
            return result;
        } else {
            return false;
        }
    }

    private void listenerEntyFile() {
        if(!this.filePath.equals("/assets/config.properties")) {
            this.lastModified = (new File(this.filePath)).lastModified();
            if(this.modifyListener == null) {
                this.modifyListener = new Thread(new LoadConfig.WatchFileModifyListener());
                this.modifyListener.start();
                Log.d("LoadConfig", "file listener start");
            }
        }

    }

    private void parseProperty(Properties properties) {
        String debugStr = properties.getProperty("debug", "false").trim().toLowerCase();
        if(debugStr.equals("true") || debugStr.equals("t")) {
            this.config.debug = true;
        }

        this.config.registServer = properties.getProperty("regist_ip_addr", "");
        this.config.registServer = properties.getProperty("send_log_ip", "");
    }

    public Config getConfig() {
        return this.config;
    }

    public void regConfigChange(ConfigListener listener) {
        if(this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void unRegConfigChange(ConfigListener listener) {
        if(this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }

    }

    private void notifyConfigChange() {
        if(!this.listeners.isEmpty()) {
            Iterator var2 = this.listeners.iterator();

            while(var2.hasNext()) {
                ConfigListener listener = (ConfigListener)var2.next();
                listener.configChange(this.config);
            }

        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        }

    }

    protected class WatchFileModifyListener implements Runnable {
        protected WatchFileModifyListener() {
        }

        public void run() {
            while(true) {
                LoadConfig.this.sleep(5000L);
                if(LoadConfig.this.filePath == null || "".equals(LoadConfig.this.filePath)) {
                    return;
                }

                File checkFile = new File(LoadConfig.this.filePath);
                if(LoadConfig.this.lastModified != checkFile.lastModified()) {
                    LoadConfig.this.loadConfigFile(LoadConfig.this.filePath);
                    WriteFile.copyFile(checkFile, new File("/assets/config.properties"));
                    LoadConfig.this.lastModified = checkFile.lastModified();
                    Log.d("LoadConfig", "file has modified");
                    LoadConfig.this.notifyConfigChange();
                }
            }
        }
    }
}
