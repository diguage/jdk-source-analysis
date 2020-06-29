package com.diguage.truman.net;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.LockSupport;

/**
 * Socket 全双工演示示例
 *
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-25 23:03
 */
public class SocketFullDuplexTest {
    private int port = 11233;

    @Test
    public void testServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();

        new Thread(() -> {
            InputStreamReader reader = new InputStreamReader(new BufferedInputStream(inputStream));
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }).start();

        OutputStream outputStream = socket.getOutputStream();
        new Thread(() -> {
            try {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    String msg = "S-" + i + "\n";
                    System.out.println("msg = " + msg);
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }).start();

        LockSupport.park();
    }

    @Test
    public void testClient() throws IOException {
        Socket socket = new Socket("localhost", port);

        InputStream inputStream = socket.getInputStream();
        new Thread(() -> {
            InputStreamReader reader = new InputStreamReader(new BufferedInputStream(inputStream));
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }).start();

        OutputStream outputStream = socket.getOutputStream();
        new Thread(() -> {
            try {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    String msg = "C-" + i + "\n";
                    System.out.println("msg = " + msg);
                    outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }).start();

        LockSupport.park();
    }
}
