package com.diguage.truman.netty.dubbo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-29 17:18
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

  private ChannelHandlerContext context;
  private String result;
  private String param;

  /**
   * 与服务器的连接创建后，就会被调用
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    this.context = ctx;
  }

  /**
   * 收到服务器的数据后，调用方法
   */
  @Override
  public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    this.result = msg.toString();
    notify(); // 唤醒等待的线程
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.close();
  }

  /**
   * 被代理对象调用，发送数据给服务器，-> wait -> 等待被唤醒(channel read) -> 返回结果
   */
  @Override
  public synchronized Object call() throws Exception {
    context.writeAndFlush(param);
    // 进行 wait
    wait(); // 等待 channel read 方法获取的到服务器的结果后，唤醒
    return result;
  }

  public void setParam(String param) {
    this.param = param;
  }
}
