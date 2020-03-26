package com.ai.spring.boot.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * FutureTask类的实现比join线程合并操作更加高明，能取得异 步线程的结果。但是，也就未必高明到哪里去了。为啥呢？
 * 因为通过FutureTask类的get方法，获取异步结果时，主线程也会被 阻塞的。这一点，FutureTask和join也是一样的，它们俩都是异步阻塞模 式。
 * 异步阻塞的效率往往是比较低的，被阻塞的主线程不能干任何事情，唯一能干的，就是在傻傻地等待
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class JavaFutureDemo {
    public static final int SLEEP_GAP = 500;

    public static void main(String[] args) {
        Callable<Boolean> hJob = new HotWaterJob();//③
        FutureTask<Boolean> hTask = new FutureTask<>(hJob);//④
        Thread hThread = new Thread(hTask, "** 烧水-Thread");//⑤

        Callable<Boolean> wJob = new WashJob();//③
        FutureTask<Boolean> wTask = new FutureTask<>(wJob);//④
        Thread wThread = new Thread(wTask, "$$ 清洗-Thread");//⑤
        hThread.start();
        wThread.start();
        Thread.currentThread().setName("主线程");

        try {
            boolean  waterOk = hTask.get();
            boolean  cupOk = wTask.get();
            drinkTea(waterOk, cupOk);
        }catch (Exception e){
        }
        log.info(getCurThreadName() + " 运行结束.");
    }

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }
    public static void drinkTea(boolean waterOk, boolean cupOk) {
        if (waterOk && cupOk) {
            log.info("泡茶喝");
        } else if (!waterOk) {
            log.info("烧水失败，没有茶喝了");
        } else if (!cupOk) {
            log.info("杯子洗不了，没有茶喝了");
        }
    }

    static class HotWaterJob implements Callable<Boolean> {  //①
        @Override
        public Boolean call() throws Exception { //②

            try {
                log.info("洗好水壶");
                log.info("灌上凉水");
                log.info("放在火上");

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                log.info("水开了");

            } catch (InterruptedException e) {
                log.info(" 发生异常被中断.");
                return false;
            }
            log.info(" 运行结束.");

            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                log.info("洗茶壶");
                log.info("洗茶杯");
                log.info("拿茶叶");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                log.info("洗完了");

            } catch (InterruptedException e) {
                log.info(" 清洗工作 发生异常被中断.");
                return false;
            }
            log.info(" 清洗工作  运行结束.");
            return true;
        }
    }
}
