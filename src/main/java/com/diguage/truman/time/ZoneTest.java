package com.diguage.truman.time;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ZoneTest {
  @Test
  public void test() {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    ZonedDateTime tomorrow = now.plusDays(1L).truncatedTo(ChronoUnit.DAYS).plusHours(5L);
    System.out.println(now);
    System.out.println(tomorrow);
  }
}
