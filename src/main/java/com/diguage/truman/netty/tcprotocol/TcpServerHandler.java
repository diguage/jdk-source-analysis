package com.diguage.truman.netty.tcprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 10:42
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
  private int count;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
    // 接收到数据，并处理
    int len = msg.getLen();
    byte[] bytes = msg.getContent();
    System.out.println("服务器接收到信息如下：");
    System.out.println("长度=" + len);
    System.out.println("消息=" + new String(bytes, UTF_8));
    System.out.println("服务器接收到消息包数量=" + (++this.count));

    String responseContent = UUID.randomUUID().toString();
    byte[] responseContentBytes = responseContent.getBytes(UTF_8);
    MessageProtocol messageProtocol = new MessageProtocol();
    messageProtocol.setContent(responseContentBytes);
    messageProtocol.setLen(responseContentBytes.length);

    ctx.writeAndFlush(messageProtocol);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
