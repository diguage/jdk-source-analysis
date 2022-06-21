package com.diguage.truman.regex;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
  @Test
  public void test() {
    Map<Pattern, DateTimeFormatter> p2fMap = new TreeMap<>();

    Properties properties = new Properties();
//    timeFormatter=[patter=format,patter=format,patter=format,patter=format,]

    p2fMap.put(Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d"),
      DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));

    Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
    String input = "2022-03-03 23:59:59";
    Matcher matcher = pattern.matcher(input);
    if (matcher.find()) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
      TemporalAccessor parse = formatter.parse(input);
    }

    Pattern p = Pattern.compile("(\\d\\d\\d\\d)[-|/](\\d\\d)[-|/](\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
    Matcher m = p.matcher("2022-03-03 23:59:59");
  }
}
