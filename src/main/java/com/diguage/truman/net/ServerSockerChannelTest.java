package com.diguage.truman.net;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-03 16:08
 */
public class ServerSockerChannelTest {

  @Test
  public void bindMultiTimes() {
    int port = 11911;
    List<ServerSocketChannel> serverSockets = new ArrayList<>(2);

    assertThatExceptionOfType(BindException.class)
      .isThrownBy(() -> {
        for (int i = 0; i < 2; i++) {
          ServerSocketChannel serverSocket = ServerSocketChannel.open();
          serverSockets.add(serverSocket);
          serverSocket.socket().bind(new InetSocketAddress(port));
        }
      });
    
    System.out.println("done.");
  }

  @Test
  public void multiReactor() {

  }

  public static class Reactor implements Runnable {
    final Selector selector;
    ServerSocketChannel serverSocket = null;
    int port;
    boolean isMaster;

    public Reactor(int port, boolean isMaster) throws IOException {
      this.port = port;
      this.isMaster = isMaster;
      this.selector = Selector.open();
      if (isMaster) {
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey selectionKey = this.serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Object());
        System.out.println("start on " + port + " ...");
      } else {
        // TODO 如何
      }
    }

    private void init() {

    }

    @Override
    public void run() {
      try {
        while (!Thread.interrupted()) {
          selector.select();
          Set<SelectionKey> keys = selector.selectedKeys();
          for (SelectionKey key : keys) {
            dispatch(key);
          }
          keys.clear();
        }
      } catch (IOException e) {

      }
    }

    private void dispatch(SelectionKey key) {
      Runnable run = (Runnable) key.attachment();
      if (Objects.nonNull(run)) {
        run.run();
      }
    }

    class Acceptor implements Runnable {
      @Override
      public void run() {
        try {
          SocketChannel channel = serverSocket.accept();
          if (Objects.nonNull(channel)) {
            new Handler(selector, channel);
          }
        } catch (IOException e) {

        }
      }
    }

    class Handler implements Runnable {
      final int MAXIN = 1024;
      final int MAXOUT = 1024;
      static final int READING = 0, SENDING = 1;
      int state = READING;

      final SocketChannel socket;
      final SelectionKey selectionKey;
      ByteBuffer input = ByteBuffer.allocate(MAXIN);
      ByteBuffer output = ByteBuffer.allocate(MAXOUT);

      public Handler(Selector selector, SocketChannel channel) throws IOException {
        socket = channel;
        channel.configureBlocking(false);
        selectionKey = socket.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
      }

      @Override
      public void run() {

      }
    }
  }

}
