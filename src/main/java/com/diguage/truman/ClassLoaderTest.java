package com.diguage.truman;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-10 17:23
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        System.out.println("ClassLodarDemo's ClassLoader is " + ClassLoaderTest.class.getClassLoader());
        System.out.println("The Parent of ClassLodarDemo's ClassLoader is " + ClassLoaderTest.class.getClassLoader().getParent());
        System.out.println("The GrandParent of ClassLodarDemo's ClassLoader is " + ClassLoaderTest.class.getClassLoader().getParent().getParent());
    }
}
