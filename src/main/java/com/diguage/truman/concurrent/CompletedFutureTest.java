package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class CompletedFutureTest {


  /**
   * 入门
   */
  @Test
  public void testIntro() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = new CompletableFuture<>();
    new Thread(() -> {
      System.out.println("CompletableFuture 可以监控任务执行情况");
      future.complete("Result： https://www.diguage.com");
    }).start();
    System.out.println(future.get());
  }


  @Test
  public void test3() {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//      try {
//        TimeUnit.SECONDS.sleep(5);
//      } catch (InterruptedException e) {
//      }
      return 0;
    });
    try {
      future.get(1, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      System.out.println("timeout");
    } catch (Throwable e) {
    }
    try {
      System.out.println("get task1: " + future.get());
    } catch (Throwable e) {
    }
    future.thenComposeAsync(r -> CompletableFuture.supplyAsync(() -> {
      System.out.println("get task1 result=" + r);
      return 1;
    }));
  }


  @Test
  public void test2() {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
      monteCarloMethod(Long.MAX_VALUE);
      return 0;
    });

    try {
      future.get(1, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      while (true) {
        System.out.println("timeout, and then throw an error.");
      }
    }
    LockSupport.park();
  }


  @Test
  public void test() throws ExecutionException, InterruptedException {
    AtomicInteger cnt = new AtomicInteger(0);

    CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> monteCarloMethod(10000));
    CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> monteCarloMethod(100000));
    CompletableFuture<Void> all = CompletableFuture.allOf(cf1, cf2);


    System.out.println("start to get result");
    System.out.println(LocalTime.now() + " : " + cf1.get());
    System.out.println("got the result");
//    LockSupport.park();
  }

  /**
   * 蒙特卡罗算法
   */
  private static double monteCarloMethod(long count) {
    double pi = 0;
    int circles = 0;
    int squares = 0;

    // Total Random numbers generated = possible x
    // values * possible y values
    while (squares < count) {

      // Randomly generated x and y values in the range [-1,1]
      double x = Math.random() * 2 - 1;
      double y = Math.random() * 2 - 1;

      // Distance between (x, y) from the origin
      double dist = x * x + y * y;

      // Checking if (x, y) lies inside the define
      // circle with R=1
      if (dist <= 1) {
        circles++;
      }

      // Total number of points generated
      squares++;

      // estimated pi after this iteration
      pi = (4.0 * circles) / squares;
      if (squares % 10 == 0) {
        System.out.println(circles + " " + squares + " - " + pi);
      }
    }

    return pi;
  }
}
