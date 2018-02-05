package com.cryallen.common.function.comlib.utils;

import android.os.Environment;

import com.cryallen.common.function.tool.log.EFLog;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作工具类
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 获取网络缓存目录
     * @since V1.0
     */
    public static File getNetCacheDirPath() {
        File SDFile;
        File SDFolder;
        try {

            SDFile = Environment.getExternalStorageDirectory();

            SDFolder = new File(SDFile.getAbsolutePath() + File.separator + "cryallen" + File.separator + "data" + File.separator + "NetCache");
            if (!SDFolder.exists()) {
                boolean ret = SDFolder.mkdirs();
                if (ret) {
                    EFLog.d(TAG, "SDFolder.mkdir success");
                }
                ret = SDFolder.createNewFile();
                if (ret) {
                    EFLog.d(TAG, "SDFolder.createNewFile success");
                }
            }
            return SDFolder;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }




}
