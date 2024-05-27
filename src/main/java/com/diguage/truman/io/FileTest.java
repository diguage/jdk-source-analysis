package com.diguage.truman.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class FileTest {

  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

  @Test
  public void test() {
    File file = new File("/Users/lijun695/vm.log");
    long mod = file.lastModified();
    System.out.println(new Date(mod).toInstant().atZone(ZoneId.systemDefault()));
    System.out.println(file.getName());
  }

  public static void main(String[] args) throws IOException {
    for (int i = 0; i < 10; i++) {
      Files.write(Paths.get("/tmp/abc123/456/20230609183256983"),
        ("" + i + " -- D瓜哥 · https://www.diguage.com").getBytes(StandardCharsets.UTF_8), TRUNCATE_EXISTING);
    }
  }

  public static void createDirectory(String path) {
    File file = new File(path);
    if (file.exists() && !file.isDirectory()) {
      file.delete();
    }
    if (!file.exists()) {
      try {
        Files.createDirectories(Paths.get(path));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
