package com.diguage.truman;

public class StringUtils {
  public static String switchFormat(int cur, int length) {
    String str = "" + cur;
    int q = length - str.length();
    switch (q) {
      case 0:
        break;
      case 1:
        str = "0" + str;
        break;
      case 2:
        str = "00" + str;
        break;
      case 3:
        str = "000" + str;
        break;
      case 4:
        str = "0000" + str;
        break;
      case 5:
        str = "00000" + str;
        break;
      case 6:
        str = "000000" + str;
        break;
      default:
        break;
    }
    return str;
  }

  public static String format(int cur, int len) {
    return String.format("%0" + len + "d", cur);
  }
}
