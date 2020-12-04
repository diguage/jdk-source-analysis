package com.diguage.truman;

import java.io.*;
import java.util.StringJoiner;

public class OuterClass implements Serializable {
  private int age = 119;
  private String name = "D瓜哥";

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", OuterClass.class.getSimpleName() + "[", "]")
      .add("age=" + age)
      .add("name='" + name + "'")
      .toString();
  }

  public static class InnerClass {
    private int iage = 120;
    private String iname = "https://www.diguage.com";

    public int getIage() {
      return iage;
    }

    public void setIage(int iage) {
      this.iage = iage;
    }

    public String getIname() {
      return iname;
    }

    public void setIname(String iname) {
      this.iname = iname;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", InnerClass.class.getSimpleName() + "[", "]")
        .add("iage=" + iage)
        .add("iname='" + iname + "'")
        .toString();
    }

    public static void main(String[] args) throws Throwable {
      test(new OuterClass());
      test(new InnerClass());
    }

    private static void test(Object param) throws Exception {
      System.out.println("param = " + param);
      ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(param);

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      Object object = ois.readObject();
      System.out.println("deser = " + object);
    }
  }
}
