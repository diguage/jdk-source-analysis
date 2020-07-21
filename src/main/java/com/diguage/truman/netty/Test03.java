package com.diguage.truman.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.locks.LockSupport;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-20 22:19
 */
public class Test03 {
  private static final int PORT = 11911;

  @Test
  public void testServer() throws InterruptedException {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childAttr(AttributeKey.newInstance("childAttr"), null)
        .handler(new ServerHandler())
        .childHandler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel ch) throws Exception {

          }
        });
      ChannelFuture f = b.bind(PORT).sync();
      f.addListener(future -> {
        if (future.isSuccess()) {
          System.out.println(LocalDateTime.now() + ": 端口[" + PORT + "]绑定成功！");
        } else {
          System.out.println(LocalDateTime.now() + ": 端口[" + PORT + "]绑定失败！");
        }
      });
      f.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
    LockSupport.park();
  }

  public static class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      System.out.println("channelActive");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      System.out.println("handlerAdded");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            ((ByteBuf) msg).release();
//            ReferenceCountUtil.release(msg);
      ctx.write(msg);
      ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }

  @Test
  public void testClient() throws InterruptedException {
    NioEventLoopGroup executors = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(executors)
        .channel(NioSocketChannel.class)
        .remoteAddress(new InetSocketAddress("localhost", PORT))
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ClientHandler());
          }
        });
    } finally {
      executors.shutdownGracefully().sync();
    }
  }

  public static class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      System.out.println("Handl");
    }
  }
}
