package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComparatorTest {
  public static class User {
    public User(String name, int age, int sex) {
      this.name = name;
      this.age = age;
      this.sex = sex;
    }

    String name;
    int age;

    int sex;

    @Override
    public String toString() {
      return "User{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", sex=" + sex +
        '}';
    }
  }

  @Test
  public void test() {
    List<User> users = Arrays.asList(
      new User("u4", 4, 1),
      new User("u2", 2, 1),
      new User("u1", 1, 0),
      new User("u3", 2, 0));
    List<User> result = users.stream()
      .sorted((a, b) -> {
        if (0 == a.sex) {
          return -1;
        }
        if (0 == b.sex) {
          return 1;
        }
        return 0;
      })
      .collect(Collectors.toList());
    System.out.println(Arrays.toString(result.toArray()));
  }
}
