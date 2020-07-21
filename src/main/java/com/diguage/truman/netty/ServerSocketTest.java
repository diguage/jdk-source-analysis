package com.diguage.truman.netty;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.LockSupport;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-21 14:31
 */
public class ServerSocketTest {
  public static final String HOST = "127.0.0.1";
  public static final int PORT = 11911;

  @Test
  public void testServer() {
    Server server = new Server(PORT);
    server.start();
    LockSupport.park();
  }

  @Test
  public void testClient() throws IOException {
    Socket socket = new Socket(HOST, PORT);
    new Thread(() -> {
      System.out.println("客户端启动成功");
      while (true) {
        try {
          String message = "Hello, D瓜哥！";
          System.out.println("客户端发送数据：" + message);
          socket.getOutputStream().write(message.getBytes(UTF_8));
        } catch (IOException e) {
          System.out.println("写入数据报错！");
          e.printStackTrace();
        }
        sleep();
      }
    }).start();
    LockSupport.park();
  }

  private void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
      try {
        this.serverSocket = new ServerSocket(port);
        System.out.println("服务端启动成功，端口号：" + port);
      } catch (IOException e) {
        System.out.println("服务端启动失败");
        e.printStackTrace();
      }
    }

    public void start() {
      new Thread(() -> doStart()).start();
    }

    private void doStart() {
      while (true) {
        try {
          Socket socket = serverSocket.accept();
          new ClientHandler(socket).start();
        } catch (IOException e) {
          System.out.println("服务端异常");
          e.printStackTrace();
        }
      }
    }
  }

  private static class ClientHandler {
    public static final int MAX_DATA_LEN = 1024;
    private final Socket socket;

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    public void start() {
      System.out.println("新客户端接入");
      new Thread(() -> doStart()).start();
    }

    private void doStart() {
      try {
        InputStream inputStream = socket.getInputStream();
        while (true) {
          byte[] data = new byte[MAX_DATA_LEN];
          int len;
          while ((len = inputStream.read(data)) != -1) {
            String message = new String(data, 0, len);
            System.out.println("客户端传来消息：" + message);
            socket.getOutputStream().write(data);
          }
        }
      } catch (IOException e) {
        System.out.println("服务端读取错误失败");
        e.printStackTrace();
      }
    }
  }
}
