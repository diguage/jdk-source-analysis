package com.diguage.truman.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 20:05
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private int count;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    byte[] bytes = new byte[msg.readableBytes()];
    msg.readBytes(bytes);
    String message = new String(bytes, UTF_8);
    System.out.println("客户端接收到消息=" + message);
    System.out.println("客户端接收到消息量=" + (++this.count));
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 使用客户端发送10条数据
    for (int i = 0; i < 10; i++) {
      ByteBuf buffer = Unpooled.copiedBuffer("Hello，D瓜哥！~~" + i, UTF_8);
      ctx.writeAndFlush(buffer);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
