package com.vip.realtime.io.algorithem.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
  private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);


  public String createFile(String path, String fileName) throws IOException {
    File filePath = new File(path);
    if(!filePath.exists()) {
      filePath.mkdirs();
    }
    File file = new File(path + File.separator + fileName);
    if (!file.exists()) {
      file.createNewFile();
    }
    return file.getAbsolutePath();
  }

  public BufferedWriter getWriter(File file) throws IOException{
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
    return bufferedWriter;
  }

  public void write(BufferedWriter writer, String content) throws IOException{
    writer.write(content);
    writer.newLine();
  }

  public void flushAndClose(Writer writer) throws IOException{
    writer.flush();
    writer.close();
  }

  private void read(String fileName) throws IOException {
    File file = new File(fileName);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      System.out.println(line);
    }
    bufferedReader.close();
  }



}
