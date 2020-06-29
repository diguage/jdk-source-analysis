package com.diguage.truman.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-02 09:37
 */
public class ProxyTest {
    public static class LogProxy implements InvocationHandler {
        private Object realObject;

        public LogProxy(Object realObject) {
            this.realObject = realObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println("Proxy: " + proxy.getClass().getName());
            System.out.println("start to invoke: "
                    + realObject.getClass().getName() + "#" + method.getName()
                    + " args =" + Arrays.toString(args));
            return method.invoke(realObject, args);
        }
    }

    public static interface UserGetService {
        String getById(Integer id);
    }

    static interface UserPostService {
        String postUser(String name);
    }

    public static class UserGetServiceImpl implements UserGetService, UserPostService {
        @Override
        public String getById(Integer id) {
            return "D瓜哥-" + id;
        }

        @Override
        public String postUser(String name) {
            return "119-" + name;
        }
    }

    public static void main(String[] args) {
        // 注意：这里不能使用 JUnit 来运行，JUnit 也是通过代理启动的，
        // 先于我们的测试运行，导致设置无效。
        System.getProperties()
                .put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

        UserGetServiceImpl userService = new UserGetServiceImpl();
        ClassLoader classLoader = UserGetService.class.getClassLoader();
        Class<?>[] interfaces = UserGetServiceImpl.class.getInterfaces();
        Object proxy = Proxy.newProxyInstance(classLoader,
                interfaces, new LogProxy(userService));
        System.out.println("UserName = "
                + ((UserGetService) proxy).getById(119));

        System.out.println("UserCode = "
                + ((UserPostService) proxy).postUser("diguage"));

        Object proxy2 = Proxy.newProxyInstance(classLoader,
                interfaces, new LogProxy(userService));
        System.out.println("UserName = "
                + ((UserGetService) proxy2).getById(119));

        System.out.println("UserCode = "
                + ((UserPostService) proxy2).postUser("diguage"));
    }

    @Test
    public void testGetCallerMethodName() {
        System.out.println(getCallerMethod());

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
    }

    public String getCallerMethod() {
        String methodName = Thread.currentThread()
                .getStackTrace()[2] // 注意下标值
                .getMethodName();
        return methodName;
    }
}
