package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-09 14:23
 */
public class LongAdderTest {
    @Test
    public void test() {
        LongAdder adder = new LongAdder();
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(processors);
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        for (int i = 0; i < processors - 1; i++) {
            executor.execute(() -> {
                for (int j = 0; j < Integer.MAX_VALUE; j++) {
                    adder.increment();
                }
            });
        }
        executor.execute(() -> {
            while (true) {
                try {
                    System.out.println(adder.sum());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();
        LockSupport.park();
    }
}
