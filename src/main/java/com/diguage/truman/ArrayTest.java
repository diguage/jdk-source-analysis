package com.diguage.truman;

import java.util.Arrays;
import java.util.Objects;

public class ArrayTest {
  public static void main(String[] args) {
    int[] i1 = new int[0];
    int[] i2 = new int[0];
    System.out.println(i1.hashCode());
    System.out.println(i2.hashCode());
    System.out.println(Objects.equals(i1, i2));
    System.out.println(Arrays.equals(i1, i2));
  }
}
