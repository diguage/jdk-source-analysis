package com.diguage.truman.time;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FormatTest {

  @Test
  public void test() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
//    ZonedDateTime time = ZonedDateTime.parse("20230815114735092", formatter);
    LocalDateTime t3 = LocalDateTime.parse("20230609143033001", formatter);
    LocalDateTime t1 = LocalDateTime.parse("20230815114735092", formatter);
    LocalDateTime t2 = LocalDateTime.parse("20230815114735099", formatter);
    System.out.println();
  }
}
