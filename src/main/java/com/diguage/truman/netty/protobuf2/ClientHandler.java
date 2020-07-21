package com.diguage.truman.netty.protobuf2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

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

    // 随机发送 Student 或 Worker
    int random = new Random().nextInt(3);
    MyDataInfo.MyMessage message = null;
    if (0 == random) {
      // 发送学生
      message = MyDataInfo.MyMessage.newBuilder()
        .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
        .setStudent(
          MyDataInfo.Student
            .newBuilder()
            .setId(119)
            .setName("瓜哥")
            .build())
        .build();
    } else {
      // 发送 worker
      message = MyDataInfo.MyMessage.newBuilder()
        .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
        .setWorker(
          MyDataInfo.Worker
            .newBuilder()
            .setName("瓜哥")
            .setAge(119)
            .build())
        .build();
    }

    ctx.writeAndFlush(message);
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
