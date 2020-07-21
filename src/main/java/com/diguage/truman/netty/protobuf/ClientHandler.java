package com.diguage.truman.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-27 19:41
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
  /**
   * 当通道就绪就会触发该方法
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("client " + ctx);

    StudentPOJO.Student student = StudentPOJO.Student
      .newBuilder()
      .setId(119)
      .setName("D瓜哥")
      .build();

    ctx.writeAndFlush(student);
  }

  /**
   * 当通道有读取事件时，会触发
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("");
    ByteBuf buf = (ByteBuf) msg;
    System.out.println("服务器回复的消息：" + buf.toString(UTF_8));
    System.out.println("服务器的地址：" + ctx.channel().remoteAddress());
  }
}
