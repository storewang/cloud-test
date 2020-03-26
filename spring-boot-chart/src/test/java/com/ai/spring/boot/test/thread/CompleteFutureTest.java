package com.ai.spring.boot.test.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CompleteFuture Test
 *
 *
 * @author 石头
 * @Date 2020/3/23
 * @Version 1.0
 **/
@Slf4j
public class CompleteFutureTest {
    public static void main(String[] args) {
        // CompletableFuture 异步串行执行
        //serialExcuteTest();
        //
        supplyAsyncTest();
    }
    private static void   supplyAsyncTest(){
        long begin = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture.supplyAsync(() ->{
            long start = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start));
            return "[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start);
        },executorService).thenApplyAsync((result) -> {
            long start = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start));
            return "[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start);
        }).thenApplyAsync((result) -> {
            long start = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start));
            return "[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start);
        }).whenComplete((result ,e) -> {
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 加载完成");
        }).join();

        System.out.println("[线程:"+Thread.currentThread().getName() + "] 总耗时：" + (System.currentTimeMillis() - begin));
    }
    private static void   serialExcuteTest(){
        long begin = System.currentTimeMillis();
        CompletableFuture.runAsync(() ->{
            long start = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start));
        }).thenRun(() -> {
            long start = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start));
        }).thenRun(() -> {
            long start = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 耗时：" + (System.currentTimeMillis() - start));
        }).whenComplete((result ,e) -> {
            System.out.println("[线程:"+Thread.currentThread().getName() + "] 加载完成");
        }).join();

        System.out.println("[线程:"+Thread.currentThread().getName() + "] 总耗时：" + (System.currentTimeMillis() - begin));
    }

}
