package com.diguage.truman.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ConnPoolTest {
  public static void main(String[] args) {
    // 创建自定义的连接池
    ConnectionPool connectionPool = new ConnectionPool(10, 5, TimeUnit.MINUTES);

    OkHttpClient client = new OkHttpClient.Builder()
      .connectionPool(connectionPool)
      .build();

// 创建请求
    Request request = new Request.Builder()
      .url("https://www.baidu.com")
      .build();

// 发送请求并获取响应
    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        System.out.println(response.body().string());
      } else {
        System.err.println("Request failed: " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
