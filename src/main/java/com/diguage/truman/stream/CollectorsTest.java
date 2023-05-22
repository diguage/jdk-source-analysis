package com.diguage.truman.stream;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CollectorsTest {
  @Test
  public void testToList() {
    List<Integer> ints = Arrays.asList(1, 2, 3);
    List<Integer> filtered = ints.stream()
      .filter(i -> i > 5)
      .collect(Collectors.toList());
    System.out.println(Objects.isNull(filtered));
  }
}
