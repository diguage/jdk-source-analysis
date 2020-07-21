package com.diguage.truman.netty.protobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 说明：
 * 我们自定义一个 Handler 需要继承 netty 规定好的某个 HandlerAdapter
 *
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 18:54
 */
public class ServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {
  /**
   * 读取数据实际（这里我们可以读取客户端发送的消息）
   */
  @Override
  public void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student student) throws Exception {
    System.out.println("客户端发送的数据 id=" + student.getId() + "，name=" + student.getName());
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
