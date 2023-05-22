package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringTest {

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
