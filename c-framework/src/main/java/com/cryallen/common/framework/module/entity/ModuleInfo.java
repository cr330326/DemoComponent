package com.cryallen.common.framework.module.entity;

/**
 * 组件配置信息类
 * Created by chenran3 on 2017/12/6.
 */

public class ModuleInfo {
    private String moduleName;
    private String packageName;
    private String delegateName;

    public ModuleInfo() {}

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }
}
