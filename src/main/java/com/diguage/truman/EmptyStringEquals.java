package com.diguage.truman;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * [Micro optimizations in Java. String.equals() | Medium^]
 * <p>
 * https://medium.com/javarevisited/micro-optimizations-in-java-string-equals-22be19fd8416
 *
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-08-10 17:16
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 10, time = 1)
public class EmptyStringEquals {
  @Param({"", "nonEmptyString"})
  private String strParams;

  @Benchmark
  public boolean nonNullAndIsEmpty() {
    return strParams != null && strParams.isEmpty();
  }

  @Benchmark
  public boolean equalsPost() {
    return strParams != null && strParams.equals("");
  }

  @Benchmark
  public boolean preEquals() {
    return "".equals(strParams);
  }
}
