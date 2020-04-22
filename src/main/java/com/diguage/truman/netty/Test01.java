package com.diguage.truman.netty;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-22 09:09
 */
public class Test01 {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 11911;

    @Test
    public void testServer() {
        try {
            TCPReader reader = new TCPReader(PORT);
            reader.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LockSupport.park();

    }

    @Test
    public void testClient() {

    }
    @Test
    public void testClient2() {
        try {
            Socket socket = new Socket(HOST, PORT);
            new Thread(() -> {
                while (true) {

                    try {
                        byte[] bytes = new byte[1024];
                        int read = socket.getInputStream().read(bytes);
                        System.out.println(new String(bytes, 0, read));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            new Thread(() -> {
                try {
                    while (true) {
                        String s = "diguage:" + LocalDateTime.now().toString();
                        socket.getOutputStream().write(s.getBytes(UTF_8));
                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LockSupport.park();
    }

    private static class TCPReader implements Runnable {
        private final ServerSocketChannel serverSocketChannel;
        private final Selector selector;

        public TCPReader(int port) throws IOException {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress addr = new InetSocketAddress(port);
            serverSocketChannel.socket().bind(addr);
            serverSocketChannel.configureBlocking(false);
            SelectionKey selectionKey = serverSocketChannel.register(
                    selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor(selector, serverSocketChannel));
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                System.out.println("Waiting for new event on port: "
                        + serverSocketChannel.socket().getLocalPort());
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                    iterator.remove();
                }
            }

        }

        private void dispatch(SelectionKey selectionKey) {
            // 这里相等于获取了上面构造函数中附加的 Acceptor 对象
            Runnable r = (Runnable) selectionKey.attachment();
            if (Objects.nonNull(r)) {
                r.run();
            }
        }
    }

    private static class Acceptor implements Runnable {
        private final ServerSocketChannel serverSocketChannel;
        private final Selector selector;

        public Acceptor(Selector selector, ServerSocketChannel ssc) {
            this.serverSocketChannel = ssc;
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println(
                        socketChannel.socket().getRemoteSocketAddress()
                                .toString() + " is connected.");
                if (Objects.nonNull(socketChannel)) {
                    socketChannel.configureBlocking(false);
                    SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    selector.wakeup();
                    selectionKey.attach(new TCPHandler(selectionKey, socketChannel));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TCPHandler implements Runnable {

        private final SelectionKey selectionKey;
        private final SocketChannel socketChannel;

        private int state;

        public TCPHandler(SelectionKey selectionKey, SocketChannel socketChannel) {
            this.selectionKey = selectionKey;
            this.socketChannel = socketChannel;
            this.state = 0;
        }

        @Override
        public void run() {
            try {
                if (state == 0) {
                    read();
                } else {
                    send();
                }
            } catch (IOException e) {
                System.out.println("[Warning!] A client has been closed.");
                closeChannel();
            }
        }

        private void closeChannel() {
            try {
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void read() throws IOException {
            byte[] bytes = new byte[1024];
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            int numBytes = socketChannel.read(byteBuffer);
            if (numBytes == -1) {
                System.out.println("[Warning!] A client has bean closed.");
                closeChannel();
                return;
            }
            String str = new String(bytes, 0, numBytes);
            if (!"".equals(str)) {
                process(str);
                System.out.println(
                        socketChannel.socket().getRemoteSocketAddress()
                                .toString() + " > " + str);
                state = 1;
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                selectionKey.selector().wakeup();
            }
        }

        private void send() throws IOException {
        }

        private void process(String str) {

        }
    }


    private void readClientDate(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        int read = socketChannel.read(byteBuffer);
        if (read > 0) {
            String s = new String(byteBuffer.array());
            writeClientData(socketChannel, s);
            System.out.printf("%8d, %s", byteBuffer.position(), s);
        }
    }

    private void writeClientData(SocketChannel socketChannel, String s) {
    }
}
