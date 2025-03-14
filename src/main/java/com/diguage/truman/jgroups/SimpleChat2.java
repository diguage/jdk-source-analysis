package com.diguage.truman.jgroups;

import org.jgroups.*;
import org.jgroups.util.Util;

import java.io.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class SimpleChat2 implements Receiver {
  JChannel channel;
  String user_name = System.getProperty("user.name", LocalTime.now().toString());
  final List<String> state = new LinkedList<>();

  public void viewAccepted(View new_view) {
    System.out.println("** view: " + new_view);
  }

  public void receive(Message msg) {
    String line = msg.getSrc() + ": " + msg.getObject();
    System.out.println(line);
    synchronized (state) {
      state.add(line);
    }
  }

  public void getState(OutputStream output) throws Exception {
    synchronized (state) {
      Util.objectToStream(state, new DataOutputStream(output));
    }
  }

  public void setState(InputStream input) throws Exception {
    List<String> list = Util.objectFromStream(new DataInputStream(input));
    synchronized (state) {
      state.clear();
      state.addAll(list);
    }
    System.out.println("received state (" + list.size() + " messages in chat history):");
    list.forEach(System.out::println);
  }


  private void start() throws Exception {
//    channel = new JChannel(Thread.currentThread().getContextClassLoader().getResourceAsStream("jgroups-chat-udp.xml"));
    channel = new JChannel("jgroups-chat-tcp.xml");
    channel.setReceiver(this);
    channel.connect("ChatCluster");
    channel.getState(null, 10000);
    eventLoop();
    channel.close();
  }

  private void eventLoop() {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      try {
        System.out.print("> ");
        System.out.flush();
        String line = in.readLine().toLowerCase();
        if (line.startsWith("quit") || line.startsWith("exit")) {
          break;
        }
        line = "[" + user_name + "] " + line;
        Message msg = new ObjectMessage(null, line);
        channel.send(msg);
      } catch (Exception e) {
      }
    }
  }


  public static void main(String[] args) throws Exception {
    new SimpleChat2().start();
  }
}
