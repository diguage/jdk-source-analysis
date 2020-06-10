package com.diguage.truman.netty.nia.ch02;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-05 19:17
 */
public class EchoTest {

  private static final int PORT = 11911;

  @Test
  public void testServer() throws InterruptedException {
    EchoServerHandler serverHandler = new EchoServerHandler();
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup wokerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, wokerGroup)
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress(PORT))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(serverHandler);
          }
        });
      ChannelFuture f = b.bind().sync();
      f.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully().sync();
      wokerGroup.shutdownGracefully().sync();
    }
  }

  @Test
  public void testClient() throws InterruptedException {
    NioEventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
        .channel(NioSocketChannel.class)
        .remoteAddress(new InetSocketAddress("localhost", PORT))
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new EchoClientHandler());
          }
        });
      ChannelFuture f = b.connect().sync();
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      ctx.channel().eventLoop().execute(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(10 * 1000);
            System.out.println("Task-1: " + LocalDateTime.now());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });

      ctx.channel().eventLoop().execute(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(20 * 1000);
            System.out.println("Task-2: " + LocalDateTime.now());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });

      ByteBuf in = (ByteBuf) msg;
      System.out.println("Server received: " + in.toString(StandardCharsets.UTF_8));
      ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
        .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }

  public static class EchoServer {
    private final int port;

    public EchoServer(int port) {
      this.port = port;
    }

  }

  public static class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", StandardCharsets.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
      System.out.println("Client received: " + in.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }

}
