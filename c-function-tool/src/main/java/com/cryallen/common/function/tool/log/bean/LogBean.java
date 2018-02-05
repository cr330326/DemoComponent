package com.cryallen.common.function.tool.log.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenran3 on 2018/1/3.
 */

public class LogBean {
    public static String SESSION = "";
    public static int BEANCOUNT;
    public static SharedPreferences SP = null;
    public int seq;
    public float time;
    public String timeStr = "";
    public String msg = "";
    public String className = "";
    public int line;
    public int level = 0;
    public String func = "";
    public String tag = "";

    public LogBean() {
    }

    public void increment() {
    }

    public static void INIT_SEQ(Context context) {
        SP = context.getSharedPreferences("log_seq", 0);
        BEANCOUNT = SP.getInt("log_seq", 0);
    }

    public static void SET_SEQ_SP(int val) {
        if(SP != null) {
            SharedPreferences.Editor editor = SP.edit();
            editor.putInt("log_seq", val);
            editor.commit();
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(this.timeStr);
        sb.append(' ').append('{').append(this.className).append('.').append(this.func).append('}').append('[').append(this.getLevel(this.level)).append(']').append('[').append(this.tag).append(']').append(' ').append(this.msg).append("\n");
        return sb.toString();
    }

    private String getLevel(int level) {
        String ret = "D";
        switch(level) {
            case 2:
                ret = "V";
                break;
            case 3:
                ret = "D";
                break;
            case 4:
                ret = "I";
                break;
            case 5:
                ret = "W";
                break;
            case 6:
                ret = "E";
                break;
            case 7:
                ret = "A";
                break;
            default:
                ret = "D";
        }

        return ret;
    }
}
