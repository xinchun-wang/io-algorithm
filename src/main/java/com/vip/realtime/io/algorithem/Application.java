package com.vip.realtime.io.algorithem;

import com.vip.realtime.io.algorithem.data.BasicGenerator;
import com.vip.realtime.io.algorithem.data.FileWriter;
import com.vip.realtime.io.algorithem.utils.CommandOptions;
import com.vip.realtime.io.algorithem.utils.CommandOptionsParser;
import com.vip.realtime.io.algorithem.utils.Constants;
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

  public void generatorOrderData(){
    LOG.info("begin generator order data.");
    BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>(1024);
    BasicGenerator generator = new BasicGenerator(dataQueue,
        Constants.FIRST_ORDER_DEFAULT_VALUE * Constants.ORDER_USER_FACTOR ,
        Constants.FIRST_ORDER_DEFAULT_VALUE ,
        commandOptions.getFactor());
    Thread generatorThread = new Thread(generator);
    generatorThread.setName("Thread-Order-Generator");
    generatorThread.start();

    Thread orderWriterThread = createWriter(
        "Thread-Order-Writer",
        dataQueue,
        commandOptions.getBasedir() + File.separator + Constants.FIRST_ORDER_DIR,
        commandOptions.getFactor());
    try {
      orderWriterThread.join();
    }
    catch (InterruptedException ignore){
    }
    LOG.info("finsh generator order data.");
  }

  public void generatorWarmupBrandAndGoodsLikeData(){
    LOG.info("begin warmup brand and goods like data.");

    BlockingQueue<String> warmupBrandDataQueue = new LinkedBlockingQueue<>(1024);
    BlockingQueue<String> goodsLikeDataQueue = new LinkedBlockingQueue<>(1024);

    BasicGenerator generator = new BasicGenerator(
        warmupBrandDataQueue,
        goodsLikeDataQueue,
        Constants.FIRST_ORDER_DEFAULT_VALUE * Constants.ORDER_USER_FACTOR ,
        Constants.WARMUP_BRAND_NUMBER_DEFAULT_VALUE,
        Constants.GOODS_LIKE_HUGE_PER_ACT_FACTOR,
        10,
        Constants.GOODS_LIKE_LARGE_PER_ACT_FACTOR,
        commandOptions.getFactor());

    Thread generatorThread = new Thread(generator);
    generatorThread.setName("Thread-WarmupBrandAndGoods-Generator");

    generatorThread.start();

    Thread warmupBrandThread = createWriter(
        "Thread-WarmupBrand-Writer",
        warmupBrandDataQueue,
        commandOptions.getBasedir() + File.separator + Constants.WARMUP_BRAND_DIR,
        1);
    try {
      warmupBrandThread.join();
    }
    catch (InterruptedException ignore){
    }

    createWriter("Thread-GoodsLike-Writer",
        goodsLikeDataQueue,
        commandOptions.getBasedir() + File.separator + Constants.GOODS_LIKE_DIR,
        commandOptions.getFactor());
    LOG.info("finish warmup brand and goods like data.");
  }

  protected Thread createWriter(String name, BlockingQueue<String> dataQueue, String path, int files){
    FileWriter writer = new FileWriter(dataQueue, path, files);
    Thread writerThread = new Thread(writer);
    writerThread.setName(name);
    writerThread.start();
    return writerThread;
  }

  protected CommandOptions parseArgs(String[] args){
    CommandOptionsParser optionsParser = new CommandOptionsParser();
    optionsParser.process(args, commandOptions);

    LOG.info(commandOptions.toString());
    return commandOptions;
  }

  /**
   * 运行参数： -basedir /workspace/data -firstorder false -warmup true -factor 100
   * 其中比赛的时候-factor 为100，可以将factor设置为一个较小的值，用于调试程序
   * @param args
   */
  public static void main(String[] args) {
    try {
      Application application = new Application();
      CommandOptions commandOptions = application.parseArgs(args);

      if(commandOptions.isFirstorder()) {
        application.generatorOrderData();
      }
      if(commandOptions.isWarmup()) {
        application.generatorWarmupBrandAndGoodsLikeData();
      }
    }
    catch (Exception e){
      LOG.error("", e);
    }
  }
}
