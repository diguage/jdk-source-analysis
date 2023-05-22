package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class HashMapTest {
  @Test
  public void test() {
    Map<String, Object> map = new HashMap<>();
    map.put("123", null);
    assertThat(map.getOrDefault("123", "456")).isNull();
  }
}
