package com.ai.spring.boot.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/6
 * @Version 1.0
 **/
@Slf4j
public class FutureTaskScheduler extends Thread{
    private ConcurrentLinkedQueue<ExecuteTask> executeTaskQueue = new ConcurrentLinkedQueue<ExecuteTask>();// 任务队列
    private long sleepTime = 200;// 线程休眠时间
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    private static FutureTaskScheduler inst = new FutureTaskScheduler();
    private FutureTaskScheduler(){
        this.start();
    }

    public static void add(ExecuteTask executeTask){
        inst.executeTaskQueue.add(executeTask);
    }
    @Override
    public void run(){
        while (true){
            handleTask();// 处理任务
            threadSleep(sleepTime);
        }
    }
    /**
     * 处理任务队列，检查其中是否有任务
     */
    private void handleTask(){
        try {
            ExecuteTask executeTask;
            while (executeTaskQueue.peek() != null) {
                executeTask = executeTaskQueue.poll();
                handleTask(executeTask);
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }
    /**
     * 执行任务操作
     *
     * @param executeTask
     */
    private void handleTask(ExecuteTask executeTask){
        pool.execute(new ExecuteRunnable(executeTask));
    }
    private void threadSleep(long time){
        try {
            sleep(time);
        } catch (InterruptedException e) {
            log.error("",e);
        }
    }

    class ExecuteRunnable implements Runnable {
        ExecuteTask executeTask;

        ExecuteRunnable(ExecuteTask executeTask) {
            this.executeTask = executeTask;
        }

        public void run() {
            try {
                executeTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
