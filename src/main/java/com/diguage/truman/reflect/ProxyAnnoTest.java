package com.diguage.truman.reflect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-08 23:34
 */
public class ProxyAnnoTest {
    @Diguage
    public static class AnnoTest {
    }

    @Diguage("https://github.com/diguage")
    public static class AnnoTest2 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Target(ElementType.TYPE)
    static @interface Diguage {
        String value() default "https://www.diguage.com";

        String name() default "D瓜哥";
    }

    public static void main(String[] args) {
        System.getProperties()
                .put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

        Class<AnnoTest> clazz = AnnoTest.class;
        Diguage annotation = clazz.getAnnotation(Diguage.class);
        System.out.println(annotation + " : " + annotation.hashCode());
        System.out.println("Name: " + annotation.name());
        System.out.println("Value: " + annotation.value());

        Class<? extends Diguage> annoClass = annotation.getClass();


        System.out.println("\n----Class----");
        String className = annoClass.getName();

        System.out.println("\n----SuperClass----");
        System.out.println(annoClass.getSuperclass().getName());

        System.out.println("\n----Interfaces----");
        System.out.println(Arrays.toString(annoClass.getInterfaces()));

        System.out.println("\n----Methods----");
        System.out.println(Arrays.toString(annoClass.getDeclaredMethods())
                .replaceAll(", p", ",\n p"));

        System.out.println("\n\n==============");
        Diguage anno2 = AnnoTest2.class.getAnnotation(Diguage.class);
        System.out.println(anno2 + " : " + anno2.hashCode());
    }
}
