package com.cryallen.common.function.comlib.rxbus;

/**
 * RxBus data
 * Created by chenran3 on 2018/1/15.
 */
public class BusData {
    String id;
    String status;

    public BusData() {
    }

    public BusData(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}