package com.diguage.truman.netty.tcprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 20:05
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
  private int count;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
    // 接收到数据，并处理
    int len = msg.getLen();
    byte[] bytes = msg.getContent();
    System.out.println("客户端接收到信息如下：");
    System.out.println("长度=" + len);
    System.out.println("消息=" + new String(bytes, UTF_8));
    System.out.println("客户端接收到消息包数量=" + (++this.count));
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 使用客户端发送10条数据
    for (int i = 0; i < 10; i++) {
      String msg = "Hello，D瓜哥！~~" + i;
      byte[] content = msg.getBytes(UTF_8);
      // 创建协议包
      MessageProtocol msp = new MessageProtocol();
      msp.setContent(content);
      msp.setLen(content.length);

      ctx.writeAndFlush(msp);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
