package com.diguage.truman;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static com.diguage.truman.StringUtils.format;
import static com.diguage.truman.StringUtils.switchFormat;

@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 10, time = 30)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 5)
@State(Scope.Thread)
public class StringUtilTest {

  // TODO 怎么书写测试用例？
  @Param({"1", "2"})
  int num;

  private static final int[] lens = {1, 2, 3, 4, 5, 6, 7};

  @Benchmark
  public String testStringFormat() {
    return format(num, num);
  }

  @Benchmark
  public String testSwitchFormat() {
    return switchFormat(num, num);
  }
}
