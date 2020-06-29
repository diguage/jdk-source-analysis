package com.diguage.truman;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Objects;

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

    @Test
    public void test() throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {
        DiguageClassLoader loader = new DiguageClassLoader();
        // 如何识别内部类？
        // 如何获取内部类的正确类名？
        Class<?> clazz = loader.loadClass(
                "com.diguage.truman.ClassLoaderTest.HelloWorld");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        System.out.println(instance.toString());
    }

    public static class HelloWorld {
        @Override
        public String toString() {
            return "Hello, https://www.diguage.com/";
        }
    }

    public static class DiguageClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (Objects.isNull(name)) {
                throw new IllegalArgumentException("class name is null.");
            }
            String fileName = name.replaceAll("\\.", "/") + ".class";
            int index = fileName.lastIndexOf("/");
            fileName = fileName.substring(0, index) + "$"
                    + fileName.substring(index + 1);
            InputStream inputStream = getResourceAsStream(fileName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                int size = 0;
                byte[] bytes = new byte[1024];
                while ((size = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytecodes = outputStream.toByteArray();
            if (bytecodes.length == 0) {
                throw new ClassNotFoundException(name);
            }
            int i = name.lastIndexOf(".");
            String className = name.substring(0, i) + "$" + name.substring(i + 1);
            return defineClass(className, bytecodes, 0, bytecodes.length);
        }
    }
}
