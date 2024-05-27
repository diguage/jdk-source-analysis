package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ScheduledThreadPoolExecutorTest {
  @Test
  public void test() {
    ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(0);
    executor.scheduleAtFixedRate(() -> System.out.println("OKKK"), 0, 10, TimeUnit.MINUTES);
    LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
    System.out.println(executor);
  }

  @Test
  public void testSchedule() {
    ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(0);
    for (int i = 0; i < 100; i++) {
      executor.schedule(() -> System.out.println(LocalDateTime.now() + " OKKK"), 0, TimeUnit.MINUTES);
    }
    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
    System.out.println(executor);
  }
}
