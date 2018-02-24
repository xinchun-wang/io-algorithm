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

  public static File createFile(String path, String fileName) throws IOException {
    File filePath = new File(path);
    if(!filePath.exists()) {
      filePath.mkdirs();
    }
    File file = new File(path + File.separator + fileName);
    if(file.exists()){
      file.delete();
    }
    file.createNewFile();
    return file;
  }

  public static Writer getWriter(File file) throws IOException{
    Writer writer = new BufferedWriter(new FileWriter(file));
    return writer;
  }

  public static void write(Writer writer, String content) throws IOException{
    writer.write(content);
  }

  public static void flushAndClose(Writer writer) throws IOException{
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

  public static boolean delete(String fileName){
    File filePath = new File(fileName);
    if(!filePath.exists()){
      LOG.info(" file path [{}] is not exist. ", fileName);
      return false;
    }
    if(!filePath.isDirectory()){
       filePath.deleteOnExit();
      LOG.info("delete file : " + filePath);
    }
    File[] files = filePath.listFiles();
    if(files == null){
      return true;
    }
    for(File file : files){
      delete(file.getAbsolutePath());
    }
    return true;
  }



}
