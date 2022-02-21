package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.*;

public class ListSortTest {
  @Test
  public void testSort() {
    List<Date> list = new ArrayList<>(Arrays.asList(new Date(1000), new Date(2000), new Date(3000)));
    list.sort(Comparator.comparingInt(Date::getSeconds));
    list.forEach(d -> System.out.println(d.getSeconds()));
  }
}
