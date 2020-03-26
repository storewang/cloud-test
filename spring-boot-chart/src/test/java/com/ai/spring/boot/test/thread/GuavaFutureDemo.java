package com.ai.spring.boot.test.thread;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 实现异步回调，Guava实现测试
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class GuavaFutureDemo {
    public static final int SLEEP_GAP = 500;

    public static void main(String[] args) {
        //新起一个线程，作为泡茶主线程
        MainJob mainJob = new MainJob();
        Thread mainThread = new Thread(mainJob);
        mainThread.setName("主线程");
        mainThread.start();

        //烧水的业务逻辑
        Callable<Boolean> hotJob = new HotWaterJob();
        //清洗的业务逻辑
        Callable<Boolean> washJob = new WashJob();

        //创建java 线程池
        ExecutorService jPool = Executors.newFixedThreadPool(10);
        //包装java线程池，构造guava 线程池
        ListeningExecutorService gPool = MoreExecutors.listeningDecorator(jPool);


        //提交烧水的业务逻辑，取到异步任务
        ListenableFuture<Boolean> hotFuture = gPool.submit(hotJob);
        //绑定任务执行完成后的回调，到异步任务
        Futures.addCallback(hotFuture, new FutureCallback<Boolean>() {
            public void onSuccess(Boolean r) {
                if (r) {
                    mainJob.waterOk = true;
                }
            }
            public void onFailure(Throwable t) {
                log.info("烧水失败，没有茶喝了");
            }
        });

        //提交清洗的业务逻辑，取到异步任务
        ListenableFuture<Boolean> washFuture = gPool.submit(washJob);
        //绑定任务执行完成后的回调，到异步任务
        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            public void onSuccess(Boolean r) {
                if (r) {
                    mainJob.cupOk = true;
                }
            }
            public void onFailure(Throwable t) {
                log.info("杯子洗不了，没有茶喝了");
            }
        });
    }

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    //泡茶线程
    static class MainJob implements Runnable {
        boolean waterOk = false;
        boolean cupOk = false;
        int gap = SLEEP_GAP / 10;

        @Override
        public void run() {
            while (true){
                try{
                    Thread.sleep(gap);
                    log.info("读书中......");
                }catch (InterruptedException e){
                }
                if (waterOk && cupOk) {
                    drinkTea(waterOk, cupOk);
                }
            }
        }

        public void drinkTea(Boolean wOk, Boolean cOK) {
            if (wOk && cOK) {
                log.info("泡茶喝，茶喝完");
                this.waterOk = false;
                this.gap = SLEEP_GAP * 100;
            } else if (!wOk) {
                log.info("烧水失败，没有茶喝了");
            } else if (!cOK) {
                log.info("杯子洗不了，没有茶喝了");
            }
        }
    }

    static class HotWaterJob implements Callable<Boolean> {  //①
        @Override
        public Boolean call() throws Exception { //②

            try {
                log.info(" 洗好水壶");
                log.info(" 灌上凉水");
                log.info(" 放在火上");

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                log.info(" 水开了");

            } catch (InterruptedException e) {
                log.info(" 发生异常被中断.");
                return false;
            }
            log.info(" 烧水工作，运行结束.");

            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                log.info(" 洗茶壶 ");
                log.info(" 洗茶杯 ");
                log.info(" 拿茶叶 ");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                log.info(" 洗完了 ");

            } catch (InterruptedException e) {
                log.info("清洗工作 发生异常被中断.");
                return false;
            }
            log.info("清洗工作  运行结束.");
            return true;
        }
    }
}
