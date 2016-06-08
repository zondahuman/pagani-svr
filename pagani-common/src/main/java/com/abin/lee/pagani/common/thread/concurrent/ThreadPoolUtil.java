package com.abin.lee.pagani.common.thread.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: tinkpad
 * Date: 16-6-8
 * Time: 下午11:05
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPoolUtil {
    public static void main(String[] args) {
//        executeFixedPool();
//        executeCachedPool();
//        executeScheduledPool();
//        executeSinglePool();
//        executeThreadGroup();
        executeThreadPoolExecutor();
    }

    public static void executeThreadPoolExecutor() {
        ExecutorService executorService = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new MyThreadFactory("ThreadPoolExecutor"), new ThreadPoolExecutor.AbortPolicy());
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int param = i;
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable(param));
            executorService.submit(futureTask);
            try {
                Integer result = futureTask.get();
                total.getAndAdd(result);
            } catch (Exception e) {
                futureTask.cancel(true);
                e.printStackTrace();
            }
        }
        System.out.println("total=" + total);
    }

    public static void executeThreadGroup() {
        ThreadGroup threadGroup = new ThreadGroup("ThreadGroup");
        MyThread myThread = new MyThread();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(threadGroup, myThread);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Number of Threads: %d\n"+threadGroup.activeCount());
        threadGroup.list();
        Thread threads[] = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for (int i = 0; i < threadGroup.activeCount(); i++) {
            System.out.printf("Thread %s: %s\n", threads[i].getName(),
                    threads[i].getState());
        }
        threadGroup.checkAccess();
    }

    public static void executeSinglePool() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new MyThreadFactory("newSingleThreadExecutor"));
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int param = i;
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable(param));
            executorService.submit(futureTask);
            try {
                Integer result = futureTask.get();
                total.getAndAdd(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("total=" + total);
    }

    public static void executeScheduledPool() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new MyThreadFactory("newScheduledThreadPool"));
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int param = i;
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable(param));
            executorService.scheduleAtFixedRate(futureTask, 3, 3, TimeUnit.SECONDS);
//            executorService.schedule(futureTask, 3, TimeUnit.SECONDS);
//            executorService.scheduleWithFixedDelay(futureTask, 3, 3, TimeUnit.SECONDS);
            try {
                Integer result = futureTask.get();
                total.getAndAdd(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("total=" + total);
    }

    public static void executeCachedPool() {
        ExecutorService executorService = Executors.newCachedThreadPool(new MyThreadFactory("newCachedThreadPool"));
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int param = i;
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable(param));
            executorService.submit(futureTask);
            try {
                Integer result = futureTask.get();
                total.getAndAdd(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("total=" + total);
    }

    public static void executeFixedPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new MyThreadFactory("newFixedThreadPool"));
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int param = i;
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable(param));
            executorService.submit(futureTask);
            try {
                Integer result = futureTask.get();
                total.getAndAdd(result);
            } catch (Exception e) {
                futureTask.cancel(true);
                e.printStackTrace();
            }
        }
        System.out.println("total=" + total);
    }

    static class MyThreadFactory implements ThreadFactory {
        private AtomicInteger increase = new AtomicInteger(0);
        private String prefix = "";

        MyThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, prefix + "-" + increase.getAndIncrement());
        }
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 正在执行..");
        }
    }

    static class MyCallable implements Callable<Integer> {
        private Integer param;

        MyCallable(Integer param) {
            this.param = param;
        }

        @Override
        public Integer call() throws Exception {
            param++;
            if (param == 3)
                throw new RuntimeException();
            System.out.println(Thread.currentThread().getName() + " 正在执行.. param=" + param);
            return param;
        }
    }
}
