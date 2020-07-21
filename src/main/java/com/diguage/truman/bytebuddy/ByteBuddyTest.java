package com.diguage.truman.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.implementation.DefaultMethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.named;


/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-07-10 09:04
 */
public class ByteBuddyTest {
  @Test
  public void test() {
    System.out.println("--subclass-----------------------");
    saveToFile(subclass(User.class), "gen.Subclass");
    System.out.println("--rebase-----------------------");
    saveToFile(rebase(User.class), "gen.Rebase");
    System.out.println("--redefine-----------------------");
    saveToFile(redefine(User.class), "gen.Redefine");
  }

  public void saveToFile(byte[] bytes, String name) {
    int i = name.lastIndexOf('.');
    Path dir = Path.of(name.substring(0, i).replace('.', File.separatorChar));
    try {
      Files.createDirectories(dir);
      Path path = dir.resolve(name.substring(i + 1, name.length()) + ".class");
      Files.write(path, bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static class LoggerInterceptor {
    public static String log(@SuperCall Callable<String> zuper) throws Exception {
      System.out.println("Calling database");
      try {
        return zuper.call();
      } finally {
        System.out.println("Returned from database");
      }
    }
  }

  public byte[] subclass(Class<?> clazz) {
    return new ByteBuddy()
      .subclass(clazz)
//      .method(named("getName")).intercept(FixedValue.value("Hello World!"))
      .method(named("getInfo")).intercept(MethodDelegation.to(LoggerInterceptor.class))
      .make()
      .getBytes();
  }

  public byte[] redefine(Class<?> clazz) {
    return new ByteBuddy()
      .redefine(clazz)
//      .method(named("getName")).intercept(FixedValue.value("Hello World!"))
      .method(named("getInfo")).intercept(MethodDelegation.to(LoggerInterceptor.class))
      .make()
      .getBytes();
  }

  public byte[] rebase(Class<?> clazz) {
    return new ByteBuddy()
      .rebase(clazz)
//      .method(named("getName")).intercept(FixedValue.value("Hello World!"))
      .method(named("getInfo")).intercept(MethodDelegation.to(LoggerInterceptor.class))
      .make()
      .getBytes();
  }

  public static class User {
    private int age;
    private String name;

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

    public String getInfo(String info) {
      return String.format("%s-%d-%s", getName(), getAge(), info);
    }
  }


  public void xray(Class<?> clazz) {
    System.out.println("className = " + clazz.getName());
    Field[] fields = clazz.getFields();
    System.out.println("fields: ");
    for (Field field : fields) {
      System.out.printf(" %s %s%n", field.getDeclaringClass().getName(), field.getName());
    }
    Method[] methods = clazz.getMethods();
    System.out.println("methodName: ");
    Method[] baseMethods = Object.class.getMethods();
    Set<String> methodNames = Arrays.stream(baseMethods)
      .map(Method::getName)
      .collect(Collectors.toSet());
    for (Method method : methods) {
      if (methodNames.contains(method.getName())) {
        continue;
      }
      Class<?>[] classes = method.getParameterTypes();
      String params = Arrays.stream(classes)
        .map(Class::getName)
        .collect(Collectors.joining(", "));
      System.out.printf("  %s(%s)%n", method.getName(), params);
    }
  }

  public static interface First {
    default String qux() {
      return "FOO";
    }
  }

  public static interface Second {
    default String qux() {
      return "BAR";
    }
  }

  @Test
  public void test2() {
    byte[] bytes = new ByteBuddy(ClassFileVersion.JAVA_V11)
      .subclass(Object.class)
      .implement(First.class)
      .implement(Second.class)
      .method(named("qux")).intercept(DefaultMethodCall.prioritize(First.class))
      .make()
      .getBytes();
    saveToFile(bytes, "gen.NewClass");
  }

}
