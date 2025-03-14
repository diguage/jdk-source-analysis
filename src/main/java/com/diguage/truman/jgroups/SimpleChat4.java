package com.diguage.truman.jgroups;

import java.io.InputStream;
import java.nio.charset.Charset;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
//import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

/**
 * jGroups
 */
public class SimpleChat4  {
//  private static final String DEFULT_CONFIG_XML = "jgroups-chat-udp.xml";
//  /**
//   * 配置文件.
//   */
//  private String confXml;
//
//  /**
//   * 集群名称.
//   */
//  private static final String CLUSTER_NAME = "WTCLOSYN-SIMPLE-CHAT";
//  /**
//   * 字符编码
//   */
//  private static final Charset CHARSET = Charset.defaultCharset();
//  /**
//   * 节点通道.
//   */
//  private JChannel channel = null;
//
//  public SimpleChat4() {
//    this.confXml = DEFULT_CONFIG_XML;
//  }
//
//  public SimpleChat4(String confXml) {
//    this.confXml = confXml;
//  }
//
//  /**
//   * 发送消息
//   */
//  public void start() {
//    try {
//      InputStream cfg = SimpleChat4.class.getClassLoader().getResourceAsStream(confXml);
//      channel = new JChannel(cfg);
//      //连接到集群
//      channel.connect(CLUSTER_NAME);
//      channel.setDiscardOwnMessages(true);
//      //指定Receiver用来收消息和得到View改变的通知
//      channel.setReceiver(this);
//    }catch (Exception e){
//      System.out.println("启动Chat失败！");
//    }
//  }
//
//  public void sendMessage(Address dst, String text){
//    try {
//      //Message构造函数的第一个参数代表目的地地址，这里传null代表要发消息给集群内的所有地址
//      //第二个参数表示源地址，传null即可，框架会自动赋值
//      //第三个参数line会被序列化成byte[]然后发送，推荐自己序列化而不是用java自带的序列化
//      Message msg = new Message(dst, null, text.getBytes(CHARSET));
//      //发消息到集群
//      channel.send(msg);
//    } catch (Exception e) {
//      System.out.println("Chat发送消息失败！");
//    }
//  }
//
//  @Override
//  public void receive(Message msg) {
//    String line = msg.getSrc() + ":" + new String(msg.getBuffer(), CHARSET);
//    System.out.println(line);
//  }
//
//  @Override
//  public void viewAccepted(View view) {
//    System.out.println("A client has changed！" + view.toString());
//  }
}
