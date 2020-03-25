package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-19 16:04
 */
public class CompletableFutureTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        FutureTask<Integer> futureTask = new FutureTask<>(new Task());

        executorService.submit(futureTask);
        System.out.println("FutureTask...");
        System.out.println(futureTask.get());
        System.out.println("FutureTask done.");

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            futures.add(executorService.submit(new Task()));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }

        for (Future<Integer> future : futures) {
            if (future.isDone()) {
                System.out.println(future.get());
            }
        }
        CompletableFuture.runAsync(() -> System.out.println(""));
        System.out.println("All tasks were done.");
    }

    public static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int second = 0;
            try {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                second = random.nextInt(10000);
                Thread.sleep(second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return second;
        }
    }

}
