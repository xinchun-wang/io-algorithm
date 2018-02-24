package com.vip.realtime.io.algorithem.data;

import com.vip.realtime.io.algorithem.utils.FileUtils;
import java.io.File;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xinchun
 */
public class FileWriter implements Runnable{
  protected static final Logger LOG = LoggerFactory.getLogger(FileWriter.class);

  private BlockingQueue<String> dataQueue;
  private String path;
  private int files;

  public FileWriter(BlockingQueue<String> dataQueue, String path, int files){
    this.dataQueue = dataQueue;
    this.path = path;
    this.files = files;
  }

  @Override
  public void run() {
    try {
      out();
    }
    catch (Exception e){
      LOG.error("", e);
    }
  }

  public void out() throws Exception{
    Writer[] writers = new Writer[files];
    for(int i = 0; i < files; i++) {
      File file = FileUtils.createFile(path, i + ".txt");
      Writer writer = FileUtils.getWriter(file);
      writers[i] = writer;
    }

    int lineCounter = 0;
    while (true) {
      try {
        String content = dataQueue.poll(2, TimeUnit.SECONDS);
        if (content == null) {
          LOG.info("write finish. lines = " + lineCounter);
          for(Writer writer : writers){
            FileUtils.flushAndClose(writer);
          }
          return;
        }
        int writerIndex = lineCounter % files;
        Writer writer = writers[writerIndex];
        FileUtils.write(writer, content);
        lineCounter++;
        if(lineCounter % 100000 == 0){
          LOG.info("write lines = " + lineCounter);
        }
      } catch (InterruptedException ingore) {
      }
    }
  }

}
