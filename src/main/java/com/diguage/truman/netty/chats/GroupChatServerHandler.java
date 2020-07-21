package com.diguage.truman.netty.chats;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-06-28 11:04
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
  // 定义一个 channel 组，管理所有的 channel
  // GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
  private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  /**
   * 表示连接建立，一旦建立，第一个执行
   * <p>
   * 将当前 channel 加入到 channelGroup
   */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    /**
     * 将该客户加入聊天的信息推送给其他在线的客户端
     *
     * 该方法会将 channelGroup 中所有的 channel 遍历，并发送消息。
     *
     * 我们不需要自己遍历。
     */
    channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入群聊 at" + LocalDateTime.now() + " \n");
    channelGroup.add(channel);

    super.handlerAdded(ctx);
  }

  /**
   * 表示 channel 处于活动状态，表示 xx 上线
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println(ctx.channel().remoteAddress() + " 上线了~");
  }

  /**
   * 表示 channel 处于不活动状态，表示 xx 离线
   */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    System.out.println(channel.remoteAddress() + " 下线了");
  }

  /**
   * 断开连接，将 xx客户离开信息推送给当前在线客户
   */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
    // 触发这个方法后，不需要手动删除，Netty 会自动删除的
    //  channelGroup.remove(channel);
    System.out.println("channelGroup size=" + channelGroup.size());
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    // 获取当前 channel
    Channel channel = ctx.channel();
    // 这时我们遍历 channelGroup，根据不同的情况，回送不同的消息
    channelGroup.forEach(ch -> {
      if (channel != ch) {
        ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送消息：" + msg + "\n");

      } else {
        ch.writeAndFlush("[自己]发送了消息：" + msg + "\n");
      }
    });

  }
}
