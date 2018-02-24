package com.vip.realtime.io.algorithem;

import com.vip.realtime.io.algorithem.data.BasicGenerator;
import com.vip.realtime.io.algorithem.data.FileWriter;
import com.vip.realtime.io.algorithem.utils.CommandOptions;
import com.vip.realtime.io.algorithem.utils.CommandOptionsParser;
import com.vip.realtime.io.algorithem.utils.Constants;
import com.vip.realtime.io.algorithem.utils.FileUtils;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xinchun
 */
public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  private CommandOptions commandOptions = new CommandOptions();
  private BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>(1024);


  public void generatorOrderData() throws Exception{
    int orderUserFactor = 4;
    int userIdBound = Constants.FIRST_ORDER_DEFAULT_VALUE * orderUserFactor * commandOptions.getFactor();
    int orders = Constants.FIRST_ORDER_DEFAULT_VALUE * commandOptions.getFactor();

    BasicGenerator generator = new BasicGenerator(dataQueue, userIdBound, orders, 2);
    Thread generatorThread = new Thread(generator);
    generatorThread.start();

    createWriter(commandOptions.getBasedir() + File.separator + Constants.FIRST_ORDER_DIR);

  }

  public void generatorWarmupAndBrandData() throws Exception{
    BasicGenerator generator = new BasicGenerator(dataQueue, 10000, 2);
    Thread generatorThread = new Thread(generator);
    generatorThread.start();

    createWriter(commandOptions.getBasedir() + File.separator + Constants.WARMUP_BRAND_DIR);
  }

  protected void createWriter(String path){
    FileWriter writer = new FileWriter(dataQueue, path, 3);
    Thread writerThread = new Thread(writer);
    writerThread.start();
  }


  protected void parseArgs(String[] args){
    CommandOptionsParser optionsParser = new CommandOptionsParser();
    optionsParser.process(args, commandOptions);

    LOG.info(commandOptions.toString());
  }

  public static void main(String[] args) {
    try {
      Application application = new Application();
      application.parseArgs(args);
      application.generatorWarmupAndBrandData();
    }
    catch (Exception e){
      LOG.error("", e);
    }
  }
}
