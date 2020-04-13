package com.diguage.truman.net;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
public class SocketHalfCloseTest {
    private int port = 11233;

    // TODO 示例还有问题，代码还需要再修改。

    @Test
    public void testServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        new Thread(() -> {
            try (InputStreamReader reader = new InputStreamReader(
                    new BufferedInputStream(socket.getInputStream()))) {

                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }).start();


        new Thread(() -> {
            try (OutputStream outputStream = socket.getOutputStream()) {
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
    public void testClient() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", port);

        InputStream inputStream = socket.getInputStream();
        new Thread(() -> {
            try {
                InputStreamReader reader = new InputStreamReader(new BufferedInputStream(inputStream));
                BufferedReader bufferedReader = new BufferedReader(reader);
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
                for (int i = 0; i < 10; i++) {
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
        Thread.sleep(2000);
        outputStream.flush();
        outputStream.close();
        socket.shutdownOutput();

        LockSupport.park();
    }
}
