package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringTest {


  @Test
  public void testDedup() {
    List<String> lists = new ArrayList<>(1);
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      String is = String.valueOf(i);
      String s1 = "D瓜哥 · https://www.digauge.com".repeat(i % 10) + is;
      lists.add(new String(s1.substring(0, s1.length() - is.length())));
      String s2 = i + "D瓜哥 · https://www.digauge.com";
      lists.add(new String(s2.substring(is.length())));
      System.out.println(lists.size());
      if (i % 1000 == 0) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2L));
      }
    }
  }

  @Test
  public void testSplit() {
    String s = "abc";
    System.out.println(Arrays.toString(s.split("\\|")));


    // 调用 split 方法，如果参数为 null 时，则抛异常
    assertThatThrownBy(() -> s.split(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testReplaceAll() {
    String value = "${abc} 是一个占位符，${abc}";
    Set<String> placeholders = getAllPlaceholders(value);

    for (String placeholder : placeholders) {
      System.out.println(value.replaceAll(placeholder, "ABC"));
    }
  }

  /**
   * 占位符正则表达式：${\w*}
   */
  private static final Pattern PH_PATTERN = Pattern.compile("(\\u0024\\{\\w*\\})+");

  private static Set<String> getAllPlaceholders(String value) {
    Matcher matcher = PH_PATTERN.matcher(value);
    Set<String> placeholders = new HashSet<>();
    int matcherStart = 0;
    while (matcher.find(matcherStart)) {
      String group = matcher.group();
      placeholders.add(group);
      matcherStart = matcher.end();
    }
    System.out.println(Arrays.toString(placeholders.toArray(new String[0])));
    return placeholders;
  }

}
