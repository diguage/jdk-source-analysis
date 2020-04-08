package com.diguage.truman.concurrent;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-08 16:10
 */
public class JolTest {
    /**
     * Java Object Layout
     */
    public static void main(String[] args) {

        System.out.println(VM.current().details());
        System.out.println("--o = 12--------------");
        Object o = new Object() {};
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        System.out.println("--o2 = 12--------------");
        Object o2 = new Object() {
            private String name = "";
            private long age = 0;
        };
        System.out.println(ClassLayout.parseInstance(o2).toPrintable());
        System.out.println("--\"119\"--------------");
        String s = "119";
        System.out.println(s.hashCode());
        System.out.println(ClassLayout.parseInstance(s).toPrintable());
        System.out.println("--119L--------------");
        System.out.println(ClassLayout.parseInstance(119L).toPrintable());

        System.out.println("--o[] = 16--------------");
        System.out.println(ClassLayout.parseInstance(new Object[0]).toPrintable());

        System.out.println("--o[1]--------------");
        System.out.println(ClassLayout.parseInstance(new Object[]{new Object()}).toPrintable());
    }
}
