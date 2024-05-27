package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentMapTest {
  @Test
  public void test() {
    ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<>();
//    AtomicInteger cnt = map.putIfAbsent("abc", new AtomicInteger(10));
    AtomicInteger cnt = map.computeIfAbsent("abc", (k) -> new AtomicInteger(10));
    System.out.println(cnt);
  }
}
