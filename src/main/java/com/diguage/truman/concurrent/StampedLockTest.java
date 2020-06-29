package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.StampedLock;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-16 23:15
 */
public class StampedLockTest {
    @Test
    public void test() {
        StampedLock lock = new StampedLock();
        Point point = new Point(lock);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                point.move(random.nextDouble(100), random.nextDouble(100));
                System.out.println("move point...");
            });

            executorService.execute(() -> {
                double distance = point.distanceFromOrigin();
                System.out.println("current distance = " + distance);
            });
        }
    }

    static class Point {
        private volatile double x, y;
        private final StampedLock lock;

        public Point(StampedLock lock) {
            this.lock = lock;
        }

        void move(double deltaX, double deltaxY) {
            long stamp = lock.writeLock();
            try {
                x += deltaX;
                y += deltaxY;
            } finally {
                lock.unlock(stamp);
            }
        }

        double distanceFromOrigin() {
            long stamp = lock.tryOptimisticRead();
            double currentX = x, currentY = y;
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    lock.unlock(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
    }
}
