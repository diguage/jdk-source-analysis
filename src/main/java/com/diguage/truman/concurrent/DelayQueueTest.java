package com.diguage.truman.concurrent;

import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-22 16:38
 */
public class DelayQueueTest {
    @Test
    public void test() throws InterruptedException {
        DelayQueue<IntDelay> delayQueue = new DelayQueue<>();
        for (int i = 0; i < 10; i++) {
            delayQueue.add(new IntDelay(i));
        }
        while (!delayQueue.isEmpty()) {
            IntDelay delay = delayQueue.take();
            if (Objects.nonNull(delay)) {
                System.out.println(delay.num);
            }
        }
    }

    public static class IntDelay implements Delayed {

        private int num;
        private long deadline;

        public IntDelay(int num) {
            this.num = num;
            deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(num);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return deadline - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            IntDelay param = (IntDelay) o;
            return Integer.compare(this.num, param.num);
        }
    }

}
