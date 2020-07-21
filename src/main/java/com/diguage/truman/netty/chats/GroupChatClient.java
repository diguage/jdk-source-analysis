package com.diguage.truman.netty.chats;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 11:24
 */
public class GroupChatClient {
  // 属性
  private final String host;
  private final int port;

  public GroupChatClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void run() throws InterruptedException {
    NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(eventExecutors)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            // 加入自定义的 handler TODO
            pipeline.addLast(new GroupChatClientHandler());
          }
        });
      ChannelFuture future = bootstrap.connect(host, port).sync();
      Channel channel = future.channel();
      System.out.println("-----" + channel.localAddress() + "------");
      // 客户端需要输入信息，创建一个扫描器
      // TODO 还不能输入，调试一下，看看怎么回事？
      Scanner scanner = new Scanner(System.in);
      while (scanner.hasNextLine()) {
        String msg = scanner.nextLine();
        channel.writeAndFlush(msg + "\r\n");
      }
    } finally {
      eventExecutors.shutdownGracefully();
    }
  }

  public static class Clients {
    @Test
    public void client1() throws InterruptedException {
      new GroupChatClient("127.0.0.1", 11911).run();
    }

    @Test
    public void client2() throws InterruptedException {
      new GroupChatClient("127.0.0.1", 11911).run();
    }

    @Test
    public void client3() throws InterruptedException {
      new GroupChatClient("127.0.0.1", 11911).run();
    }
  }
}
