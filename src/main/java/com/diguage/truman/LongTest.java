package com.diguage.truman;

import org.junit.jupiter.api.Test;

public class LongTest {
  @Test
  public void test() {
    System.out.println(Long.toHexString(Long.MAX_VALUE));
    System.out.println(Long.toHexString(System.nanoTime()));
    System.out.println(Long.toHexString(System.currentTimeMillis()));
  }
}
