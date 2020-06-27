package com.diguage.truman.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 说明：
 * 我们自定义一个 Handler 需要继承 netty 规定好的某个 HandlerAdapter
 *
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 18:54
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
  /**
   * 读取数据实际（这里我们可以读取客户端发送的消息）
   *
   * @param ctx ChannelHandlerContext 上下文对象，含有管道 pipeline，通道 channel，地址
   * @param msg 客户端发送的数据，默认是 Object
   * @throws Exception
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//    // 第一：正常情况
//    System.out.println("服务器读取线程 " + Thread.currentThread().getName());
//    System.out.println("server ctx = " + ctx);
//    System.out.println("看看 channel 和 pipeline 的关系");
//    // 从 ctx 可以拿到非常非常多的信息
//    Channel channel = ctx.channel();
//    ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链接
//
//    // 将 msg 转成一个 ByteBuf
//    // ByteBuf 是 Netty 提供的，不是 NIO 提供的 ByteBuffer
//    ByteBuf buf = (ByteBuf) msg;
//    System.out.println("客户端发送消息是：" + buf.toString(UTF_8));
//    System.out.println("客户端地址：" + channel.remoteAddress());


    // 第二种情况：
    // 比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该 channel 对应的
    // NIOEventLoop 的 taskQueue 中,
    // 从 ctx -> pipeline -> eventLoop -> taskQueue 可以看到提交的任务
    ctx.channel().eventLoop().execute(() -> {
      try {
        Thread.sleep(20 * 1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵 2", UTF_8));
        System.out.println("channel code=" + ctx.channel().hashCode());
      } catch (Exception ex) {
        System.out.println("发生异常" + ex.getMessage());
      }
    });

    // 定时任务
    ctx.channel().eventLoop().schedule(() -> {
      try {
        Thread.sleep(20 * 1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵 2", UTF_8));
        System.out.println("channel code=" + ctx.channel().hashCode());
      } catch (Exception ex) {
        System.out.println("发生异常" + ex.getMessage());
      }
    }, 10, TimeUnit.SECONDS);

    System.out.println(ctx);
  }

  /**
   * 数据读取完毕
   */
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    // 将数据写入到缓存，并刷新
    // 一般讲，我们对这个发送的数据进行编码
    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, D瓜哥~, pong -> O(∩_∩)O哈哈~", UTF_8));
  }

  /**
   * 处理异常，一般需要关闭通道
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.close();
  }
}
