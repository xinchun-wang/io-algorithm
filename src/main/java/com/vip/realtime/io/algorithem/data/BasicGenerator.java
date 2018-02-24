package com.vip.realtime.io.algorithem.data;

import com.vip.realtime.io.algorithem.utils.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalLong;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xinchun
 */
public class BasicGenerator implements  Runnable{
  protected static final Logger LOG = LoggerFactory.getLogger(BasicGenerator.class);

  protected Random timestampRandom = new Random();
  protected Random generalRandom = new Random();

  private  BlockingQueue<String> dataQueue;

  private int userIdBound;
  private int orders;
  private int warmupBrands;
  private int randomFactor;
  private boolean isOrder;

  /**
   * 活动的最小时间区间
   */
  private int act_min_day_range = 3;
  /**
   * 活动的最大时间区间
   */
  private int act_max_day_range = 20;

  private List<WarmupBrandBO> warmupBrandList;

  private String[] plantforms = new String[]{"app", "wap", "pc", "weixin"};


  public BasicGenerator(BlockingQueue<String> dataQueue,  int userIdBound, int orders, int randomFactor){
    this.dataQueue = dataQueue;
    this.userIdBound = userIdBound;
    this.orders = orders;
    this.randomFactor = randomFactor;
    this.isOrder = true;
  }

  public BasicGenerator(BlockingQueue<String> dataQueue, int warmupBrands, int randomFactor){
    this.dataQueue = dataQueue;
    this.warmupBrands = warmupBrands;
    this.randomFactor = randomFactor;
    this.warmupBrandList = new ArrayList<>(warmupBrands);
    this.isOrder = false;
  }

  @Override
  public void run() {
    if(isOrder) {
      createOrder();
    }
    else{
      createWarmupBrand();
    }
  }

  /**
   * 产生活动配置信息
   */
  public void createWarmupBrand(){
    List<BrandBO> brandBOList = createBrands();
    Random randomWarmupBrand = new Random();

    int generateWarmups = warmupBrands * randomFactor;

    Set<Long> warmupFilter = new HashSet<>(warmupBrands);
    randomWarmupBrand.longs(generateWarmups,
        Constants.DAY_OF_2017_01_01_000000_MS,
        Constants.DAY_OF_2017_12_31_235959_MS)
        .filter(
            (warmupTimestamp) -> {
              if (warmupFilter.contains(warmupTimestamp)) {
                return false;
              } else {
                warmupFilter.add(warmupTimestamp);
                return true;
              }
            })
        .limit(warmupBrands)
        .forEach((warmupTimestamp) -> {
          List<WarmupBrandBO> brandBOs = getWarmupBrandInfo(brandBOList, warmupTimestamp);
          warmupBrandList.addAll(brandBOs);
          try {
            for(WarmupBrandBO warmupBrandBO : brandBOs) {
              dataQueue.put(warmupBrandBO.toString());
            }
          }
          catch (InterruptedException ingore){
            LOG.info(ingore.getMessage());
          }
        });
  }

  protected List<WarmupBrandBO> getWarmupBrandInfo(List<BrandBO> brandBOList, long warmupTimestamp) {
    Date configDate = new Date(warmupTimestamp);
    SimpleDateFormat actNameFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String actName = actNameFormat.format(configDate);

    SimpleDateFormat actTime = new SimpleDateFormat("yyyy-MM-dd");
    String actStartTime = actTime.format(configDate) + " 10:00:00";
    Date actEndDate = new Date(warmupTimestamp
        + getRandomInt(act_min_day_range, act_max_day_range) * Constants.DAY_OF_MS);
    String actEndTime = actTime.format(actEndDate) + " 09:59:59";


    List<WarmupBrandBO> warmupBrandBOS = new ArrayList<>();

    long remainder = warmupTimestamp % plantforms.length;
    for(int i = 0; i < remainder; i++){
      //每个活动的品牌数量
      int brandNumber = getRandomInt(4, 40);
      for(int j= 0; j < brandNumber; j++) {
        WarmupBrandBO warmupBrandBO = new WarmupBrandBO();
        warmupBrandBO.setActName(actName);
        warmupBrandBO.setActStartTime(actStartTime);
        warmupBrandBO.setActEndTime(actEndTime);
        warmupBrandBO.setPlatform(plantforms[i]);

        int brandIndex = generalRandom.nextInt(warmupBrands);
        BrandBO brandBO = brandBOList.get(brandIndex);
        warmupBrandBO.setBrandId(brandBO.getBrandId());
        warmupBrandBO.setBrandName(brandBO.getBrandName());
        warmupBrandBOS.add(warmupBrandBO);
      }
    }
    return warmupBrandBOS;
  }

  protected int getRandomInt(int min, int max){
    int dayRange = generalRandom.nextInt(max)
        % (max - min  + 1 ) + min;
    return dayRange;
  }

  protected List<BrandBO> createBrands(){
    Set<Integer> brandFilter = new HashSet<>(warmupBrands);
    List<BrandBO> brandBOList = new ArrayList<>(warmupBrands);
    int generateBrands = warmupBrands * randomFactor;
    Random randomBrands = new Random();
    randomBrands.ints(generateBrands, Integer.MAX_VALUE)
        .filter((generateBrandId) -> {
              if (brandFilter.contains(generateBrandId)) {
                return false;
              } else {
                brandFilter.add(generateBrandId);
                return true;
              }
            }
          ).limit(warmupBrands)
        .forEach((generateBrandId) -> {
          String brandName = RandomStringUtils.randomAlphanumeric(10, 40);
          BrandBO brandBO = new BrandBO();
          brandBO.setBrandId(generateBrandId);
          brandBO.setBrandName(brandName);
          brandBOList.add(brandBO);
        });

    return brandBOList;
  }

  /**
   * 产生订单相关的数据，会对产生的随机userId使用BitSet进行去重
   */
  public void createOrder(){
    Random randomUser = new Random();
    BitSet bitSet = new BitSet(userIdBound);

    int generateOrders = orders * randomFactor;
    randomUser.ints(generateOrders, 1, userIdBound)
        .filter((userId) -> {
          if (bitSet.get(userId)) {
            return false;
          } else {
            bitSet.set(userId);
            return true;
          }
        })
        .parallel() //并行执行
        .limit(orders)
        .forEach((userId) -> {
          String orderTime = createOrderTimestamp(userId);

          String content = new StringBuilder()
              .append(userId)
              .append(",")
              .append(orderTime)
              .append("\n").toString();
          try {
            dataQueue.put(content);
          }
          catch (InterruptedException ingore){
            LOG.info(ingore.getMessage());
          }
        });
  }

  protected String createOrderTimestamp(int userId){
    int remainder = userId % 10;
    //30%的订单在2017年
    if(remainder >= 7){
      return getRandomTimeStampByRange(Constants.DAY_OF_2017_01_01_000000_MS, Constants.DAY_OF_2017_12_31_235959_MS);
    }
    return getRandomTimeStampByRange(Constants.DAY_OF_2008_12_08_000000_MS, Constants.DAY_OF_2017_01_01_000000_MS);
  }

  protected String getRandomTimeStampByRange(long lowBound, long upBound){
    OptionalLong orderTimestamp = timestampRandom.longs(
        1,
        lowBound,
        upBound)
        .findFirst();
    if(orderTimestamp.isPresent()){
      Date orderDate = new Date();
      orderDate.setTime(orderTimestamp.getAsLong());
      SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return time.format(orderDate);
    }
    else {
      return "2017-01-01 00:00:00";
    }
  }

  public static  void main(String[] args){
    BasicGenerator generator = new BasicGenerator(null, 10000, 2);
//    List<BrandBO> brandBOS = generator.createBrands();
//    for(BrandBO brandBO : brandBOS){
//      System.out.println(brandBO);
//    }
//    System.out.println(brandBOS.size());

    for(int i = 0; i < 100; i++){
      System.out.println(generator.createBrands());
    }
  }

}
