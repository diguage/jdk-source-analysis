package com.diguage.truman;

import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.format.DateTimeFormat;
import org.openjdk.jmh.annotations.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.diguage.truman.DateFormatTest.DateFormatUtils.yyyyMMdd;


@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 5)
@Fork(1)
@Threads(8)
public class DateFormatTest {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yyyyMMdd);
  private static final FastDateFormat fastFormat = FastDateFormat.getInstance(yyyyMMdd);
  private static final org.joda.time.format.DateTimeFormatter
    jodaFormat = DateTimeFormat.forPattern(yyyyMMdd);

  @Benchmark
  public String testSimpleDateFormat() {
    SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);
    return format.format(new Date());
  }

  @Benchmark
  public String testLocalSimpleDateFormat() {
    return DateFormatUtils.formatDate(new Date());
  }

  @Benchmark
  public String testVariaFormatter() {
    LocalDateTime now = LocalDateTime.now();
    return now.format(DateTimeFormatter.ofPattern(yyyyMMdd));
  }

  @Benchmark
  public String testConstFormatter() {
    LocalDateTime now = LocalDateTime.now();
    return now.format(formatter);
  }

  @Benchmark
  public String testConstFormatterDate() {
    Date date = new Date();
    LocalDateTime now = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    return now.format(formatter);
  }

  @Benchmark
  public String testConstFormatterDateZ() {
    Date date = new Date();
    ZonedDateTime now = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    return now.format(formatter);
  }

  @Benchmark
  public String testVariaFastDateFormat() {
    FastDateFormat format = FastDateFormat.getInstance(yyyyMMdd);
    return format.format(new Date());
  }

  @Benchmark
  public String testConstFastDateFormat() {
    return fastFormat.format(new Date());
  }

  @Benchmark
  public String testJodaFormat() {
    org.joda.time.LocalDateTime now = org.joda.time.LocalDateTime.fromDateFields(new Date());
    return jodaFormat.print(now);
  }

  public static class DateFormatUtils {
    public static final String yyyyMMdd = "yyyyMMdd";
    private static ThreadLocal<DateFormat> dateFormatThreadLocal
      = ThreadLocal.withInitial(() -> new SimpleDateFormat(yyyyMMdd));

    public static String formatDate(Date date) {
      return dateFormatThreadLocal.get().format(date);
    }
  }
}

