package com.cryallen.common.framework.manager;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.cryallen.common.framework.base.BaseApplication;
import com.cryallen.common.framework.module.entity.ModuleInfo;
import com.cryallen.common.framework.module.interfaces.IApplicationDelegate;
import com.cryallen.common.framework.module.utils.ModuleClassUtils;
import com.cryallen.common.function.tool.log.EFLog;

/**
 * 组件管理工具类
 * Created by chenran3 on 2017/12/6.
 */

public class ModuleManager {

    private static final String TAG = "ModuleManager";
    public static final String MODULE_PREFIX = "CC_Module_";

    private List<ModuleInfo> moduleInfoList;
    private List<String> delegateNameList;
    private List<IApplicationDelegate> appDelegateList;

    private static class ModuleManagerHolder {  //内部静态类单例模式
        private static final ModuleManager sInstance  = new ModuleManager();
    }

    public static ModuleManager getInstance() {
        return ModuleManagerHolder.sInstance;
    }

    private ModuleManager() {
        this.moduleInfoList = new ArrayList();
    }

    public Context getApplicationContext() {
        return BaseApplication.getInstance().getApplicationContext();
    }

    public List<ModuleInfo> getModuleInfoList() {
        return moduleInfoList;
    }

    public List<String> getDelegateNameList() {
        return delegateNameList;
    }

    public List<IApplicationDelegate> getAppDelegateList() {
        return appDelegateList;
    }

    /**
     *
     * 加载模块信息
     */
    public void loadModule() {
        Context context = getApplicationContext();
        appDelegateList = new ArrayList();
        delegateNameList = new ArrayList();
        try {
            AssetManager assetManager = context.getResources().getAssets();
            String[] fileList = assetManager.list("");
            int fileLength = fileList.length;

            for(int i = 0; i < fileLength; ++i) {
                String fileName = fileList[i];
                if(fileName.startsWith(MODULE_PREFIX)) {
                    //解析json配置文件
                    ModuleInfo moduleInfo = parse(assetManager.open(fileName));
                    if(moduleInfo == null){
                        continue;
                    }
                    moduleInfoList.add(moduleInfo);
                    delegateNameList.add(moduleInfo.getPackageName());
                    EFLog.d(TAG, "load Module: " + moduleInfo.getModuleName());
                }
            }
            appDelegateList.addAll(ModuleClassUtils.getObjectsWithClassName(context, IApplicationDelegate.class, delegateNameList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析ModuleInfo对象
     *
     * @param inputStream
     * @return
     */
    private ModuleInfo parse(InputStream inputStream) {
        ModuleInfo fromJson = null;
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            fromJson = parseModuleInfoByJson(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            EFLog.e(TAG,"解析Module.json出错");
        }
        catch (JSONException e) {
            e.printStackTrace();
            EFLog.e(TAG,"解析Module.json出错");
        }
        catch (ParseException e) {
            e.printStackTrace();
            EFLog.e(TAG,"解析Module.json出错");
        }
        return fromJson;
    }

    /**
     * 解析Json数据
     *
     * @param jsonString Json数据字符串
     * @throws JSONException
     * @throws ParseException
     */
    private ModuleInfo parseModuleInfoByJson(String jsonString) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(jsonString);
        if(jsonObject == null){
            return null;
        }
        ModuleInfo moduleInfo =new ModuleInfo();
        moduleInfo.setModuleName(jsonObject.optString("moduleName"));
        moduleInfo.setPackageName(jsonObject.optString("packageName"));
        moduleInfo.setDelegateName(jsonObject.optString("delegateName"));
        return moduleInfo;
    }
}
