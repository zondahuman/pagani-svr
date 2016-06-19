package com.abin.lee.pagani.concurrency.guava;

import com.google.common.util.concurrent.*;
import com.sun.istack.internal.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: abin
 * Date: 16-6-19
 * Time: 下午6:16
 * To change this template use File | Settings | File Templates.
 */
public class RateLimitFlow {

    public static void main(String[] args) {
//        limitingFlowTask();
        limitingFlow();
//        listenableFutureFlow();
    }

    public static void limitingFlowTask() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        RateLimiter rateLimiter = RateLimiter.create(5, 1, TimeUnit.MICROSECONDS);

        for (int i = 0; i < 30; i++) {
//            boolean flag = rateLimiter.tryAcquire();
//            System.out.println("limitingFlow--flag=" + flag);
//            double rateResult = rateLimiter.acquire();
//            System.out.println("limitingFlow--rateResult=" + rateResult);
            final ListenableFuture<Integer> listenableFuture = executorService.submit(new MyTask(i, "limitingFlow", rateLimiter));
            try {
                Integer syncResult = listenableFuture.get();
                System.out.println("limitingFlow--syncResult=" + syncResult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void limitingFlow() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
//        RateLimiter rateLimiter = RateLimiter.create(5, 1, TimeUnit.SECONDS);
        RateLimiter rateLimiter = RateLimiter.create(5.0);
        for (int i = 0; i < 30; i++) {
            double rateResult = rateLimiter.acquire();
            System.out.println("limitingFlow--rateResult=" + rateResult);
            boolean flag = rateLimiter.tryAcquire();
            System.out.println("limitingFlow--flag=" + flag);
            final ListenableFuture<Integer> listenableFuture = executorService.submit(new Task(i, "limitingFlow"));
            try {
                Integer syncResult = listenableFuture.get();
                System.out.println("limitingFlow--syncResult=" + syncResult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void listenableFutureFlow() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        final ListenableFuture<Integer> listenableFuture = executorService.submit(new Task(-1, "listenableFutureFlow"));

        try {
            Integer syncResult = listenableFuture.get();
            System.out.println("listenableFutureFlow--syncResult=" + syncResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //第一种方式
        listenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("get listenable future's result " + listenableFuture.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, executorService);

        //第二种方式
        Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(@Nullable Integer result) {
                System.out
                        .println("get listenable future's result with callback "
                                + result);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });


    }


}

class MyTask implements Callable<Integer> {
    Integer param;
    String desc;
    RateLimiter rateLimiter;

    MyTask(Integer param, String desc, RateLimiter rateLimiter) {
        this.param = param;
        this.desc = desc;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("call execute ...param=" + param + " , desc=" + desc);
        double rateResult = rateLimiter.acquire();
        System.out.println("limitingFlow--rateResult=" + rateResult);
        TimeUnit.SECONDS.sleep(1);
        if (null != param)
            return param;
        else
            return -1;
    }
}

class Task implements Callable<Integer> {
    Integer param;
    String desc;

    Task(Integer param, String desc) {
        this.param = param;
        this.desc = desc;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("call execute ...param=" + param + " , desc=" + desc);
        TimeUnit.SECONDS.sleep(1);
        if (null != param)
            return param;
        else
            return -1;
    }
}

