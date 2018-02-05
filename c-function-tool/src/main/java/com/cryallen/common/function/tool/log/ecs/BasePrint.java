package com.cryallen.common.function.tool.log.ecs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import com.cryallen.common.function.tool.log.bean.LogBean;

/**
 * Created by chenran3 on 2018/1/3.
 */

public abstract class BasePrint extends WriteFile {

    private ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });
    private BlockingQueue<LogBean> printQueue = new LinkedBlockingQueue();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public BasePrint(String filePath, int max_size) {
        super(filePath, max_size);
        this.pool.execute(new BasePrint.WriteThread());
    }

    public synchronized void addTask(int level, String tag, String msg) {
        StackTraceElement t = Thread.currentThread().getStackTrace()[7];
        Date date = new Date();
        LogBean bean = new LogBean();
        bean.timeStr = this.sdf.format(date);
        bean.level = level;
        bean.tag = tag;
        bean.className = this.getClassName(t);
        bean.msg = msg;
        bean.func = this.getFunName(t);
        bean.line = this.getLine(t);
        this.printQueue.add(bean);
    }

    private String getClassName(StackTraceElement t) {
        String cls = t.getClassName();
        int clsInd = cls.lastIndexOf(".");
        cls = cls.substring(clsInd == -1?0:clsInd + 1);
        return cls;
    }

    private String getFunName(StackTraceElement t) {
        String fun = t.getMethodName();
        return fun;
    }

    private int getLine(StackTraceElement t) {
        int line = t.getLineNumber();
        return line;
    }

    public abstract void doTask(LogBean var1);

    private class WriteThread implements Runnable {
        private WriteThread() {
        }

        public void run() {
            while(true) {
                try {
                    LogBean logBean = (LogBean)BasePrint.this.printQueue.take();
                    BasePrint.this.doTask(logBean);
                    BasePrint.this.write(logBean.toString());
                    Thread.yield();
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            }
        }
    }
}
