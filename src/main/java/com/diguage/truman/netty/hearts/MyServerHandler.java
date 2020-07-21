package com.diguage.truman.netty.hearts;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 14:45
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      String eventType = null;
      switch (event.state()) {
        case READER_IDLE:
          eventType = "读空闲";
          break;
        case WRITER_IDLE:
          eventType = "写空闲";
          break;
        case ALL_IDLE:
          eventType = "读写空闲";
          break;
      }
      System.out.println(ctx.channel().remoteAddress() + " --超时时间--" + eventType);
      System.out.println("服务器做相应处理");
      //比如：如果发生空闲，我们关闭通道
      ctx.channel().close();
    }
  }
}
