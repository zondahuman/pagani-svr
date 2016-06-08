package com.abin.lee.pagani.common.thread.pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: tinkpad
 * Date: 16-6-9
 * Time: 上午12:32
 * To change this template use File | Settings | File Templates.
 */
public class ConcurrencyUtil {
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++){
            executeCountSemaphore();
        }

    }

    public static void executeCountSemaphore() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final Semaphore semaphore = new Semaphore(2);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new MyThreadFactory("newFixedThreadPool"));
        AtomicInteger total = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int param = i;
            FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable(countDownLatch, semaphore, param));
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
        countDownLatch.await();
    }

    static class MyThread extends Thread {
        private CountDownLatch countDownLatch;
        private Semaphore semaphore;

        MyThread(CountDownLatch countDownLatch, Semaphore semaphore) {
            this.countDownLatch = countDownLatch;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "before-countDown-正在执行...");
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + "after-countDown-正在执行...");
        }
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

    static class MyCallable implements Callable<Integer> {
        private Integer param;
        private CountDownLatch countDownLatch;
        private Semaphore semaphore;

        MyCallable(CountDownLatch countDownLatch, Semaphore semaphore, Integer param) {
            this.countDownLatch = countDownLatch;
            this.semaphore = semaphore;
            this.param = param;
        }

        @Override
        public Integer call() throws Exception {
            semaphore.acquire();
            param++;
//            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() + " before-countDown-正在执行...");
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + " after-countDown-正在执行...");
            semaphore.release();
            return param;
        }
    }

}
