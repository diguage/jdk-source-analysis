package com.diguage.truman.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 10:42
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private int count;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    byte[] bytes = new byte[msg.readableBytes()];
    msg.readBytes(bytes);

    // 将 bytes 转成字符串
    String message = new String(bytes, UTF_8);
    System.out.println("服务器接收到数据=" + message);
    System.out.println("服务器接收到消息量=" + (++this.count));

    // 服务器回送数据给客户端，回送一个随机ID
    ByteBuf buffer = Unpooled.copiedBuffer(UUID.randomUUID().toString() + "  ", UTF_8);
    ctx.writeAndFlush(buffer);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
