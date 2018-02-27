package com.vip.realtime.io.algorithem.data;

import com.vip.realtime.io.algorithem.utils.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
  private  BlockingQueue<String> goodsLikeDataQueue;

  private int userIdBound;
  private int orders;
  private int warmupBrands;
  private int largeWarmupCount;
  private int randomFactor = 2;
  private int hugeGoodsLikeCount;
  private int largeGoodsLikeCount;

  private boolean isOrder;
  private int dataFactor;

  /**
   * 活动的最小时间区间
   */
  private int act_min_day_range = 3;
  /**
   * 活动的最大时间区间
   */
  private int act_max_day_range = 20;

  private Map<String, List<WarmupBrandBO>> warmupBrandMap;

  private String[] plantforms = new String[]{"app", "wap", "pc", "weixin"};


  public BasicGenerator(BlockingQueue<String> dataQueue,  int userIdBound, int orders, int dataFactor){
    this.dataQueue = dataQueue;
    this.userIdBound = userIdBound * dataFactor;
    this.orders = orders * dataFactor;
    this.dataFactor = dataFactor;
    this.isOrder = true;
  }

  public BasicGenerator(
      BlockingQueue<String> dataQueue,
      BlockingQueue<String> goodsLikeDataQueue,
      int userIdBound,
      int warmupBrands,
      int hugeGoodsLikeCount,
      int largeWarmupCount,
      int largGoodsLikeCount,
      int dataFactor){
    this.dataQueue = dataQueue;
    this.goodsLikeDataQueue = goodsLikeDataQueue;
    this.warmupBrands = warmupBrands * dataFactor;
    this.userIdBound = userIdBound * dataFactor;
    this.hugeGoodsLikeCount = hugeGoodsLikeCount * Constants.GOODS_LIKE_PER_ACT_DEFAULT_AVARGE_VALUES * dataFactor;
    this.largeWarmupCount = largeWarmupCount;
    this.largeGoodsLikeCount = largGoodsLikeCount * Constants.GOODS_LIKE_PER_ACT_DEFAULT_AVARGE_VALUES * dataFactor;
    this.dataFactor = dataFactor;
    this.isOrder = false;
    this.warmupBrandMap = new HashMap<>(warmupBrands);
  }

  @Override
  public void run() {
    if(isOrder) {
      createOrder();
    }
    else{
      createWarmupBrand();
      createGoodsLike();
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

  public void createGoodsLike(){
    //巨量收藏的活动index
    int hugeWarmupIndex = generalRandom.nextInt(warmupBrands);
    Set<Integer> largeWarmupIndexSet = getWarmupLargeGoodsLikeIndexs();

    int warmupIndex = 0;
    for(Map.Entry<String, List<WarmupBrandBO>> warmupBrandInfo : warmupBrandMap.entrySet()){
      List<WarmupBrandBO> brandList = warmupBrandInfo.getValue();

      int goodsLikeNumber = getGoodsLikeNumber();

      if(warmupIndex == hugeWarmupIndex){
        goodsLikeNumber = hugeGoodsLikeCount;
        LOG.info("huge goods like number: " + goodsLikeNumber);
      }
      else if(largeWarmupIndexSet.contains(warmupIndex)){
        goodsLikeNumber = largeGoodsLikeCount;
        LOG.info("large goods like number: " + goodsLikeNumber);
      }

      generalRandom.longs(goodsLikeNumber)
          //并行执行
          .parallel()
          .forEach((randomNumber) -> {
            int brandIndex = generalRandom.nextInt(brandList.size());
            WarmupBrandBO brandBO = brandList.get(brandIndex);

            String goodsLike = getGoodsLike(brandBO, randomNumber);
            try {
              goodsLikeDataQueue.put(goodsLike);
            }
            catch (InterruptedException ingore){
              LOG.info(ingore.getMessage());
            }
          }
      );

      warmupIndex++;
    }
  }

  protected Set<Integer> getWarmupLargeGoodsLikeIndexs(){
    //大量收藏的活动的index列表
    Set<Integer> largeWarmupIndexSet = new HashSet<>();

    generalRandom.ints(largeWarmupCount * 2,0, warmupBrands)
        .filter((warupIndex) -> {
          if (largeWarmupIndexSet.contains(warupIndex)) {
            return false;
          }
          return true;
        }).limit(largeWarmupCount)
        .forEach((warupIndex) -> largeWarmupIndexSet.add(warupIndex));
    return largeWarmupIndexSet;
  }

  protected int getGoodsLikeNumber(){
    int goodsLikeNumber = getRandomInt(Constants.GOODS_LIKE_PER_ACT_DEFAULT_LOW_VALUES * dataFactor,
        Constants.GOODS_LIKE_PER_ACT_DEFAULT_UP_VALUES * dataFactor);
    return goodsLikeNumber;
  }

  protected String getGoodsLike(WarmupBrandBO brandBO, long randomNumber) {
    int userId = generalRandom.nextInt(userIdBound);

    StringBuilder goodsLikeBuilder = new StringBuilder();
    goodsLikeBuilder.append(userId).append(",");

    //在活动产生的时间范围内，加个随机数，使得收藏时间可能超过活动时间
    long goodsAddTime = brandBO.getActTime() + generalRandom.nextInt(
        act_max_day_range * Constants.DAY_OF_MS.intValue());

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String addTime = dateFormat.format(new Date(goodsAddTime));

    goodsLikeBuilder.append(addTime).append(",");
    goodsLikeBuilder.append(randomNumber).append(",");
    goodsLikeBuilder.append(brandBO.getPlatform()).append(",");
    goodsLikeBuilder.append(brandBO.getBrandId()).append(",");
    //截取时间
    goodsLikeBuilder.append(addTime.substring(0, 11)).append("\n");
    return goodsLikeBuilder.toString();
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

    for(int i = 0; i <= remainder; i++){
      //每个活动的品牌数量
      int brandNumber = getRandomInt(4, 40);
      for(int j= 0; j < brandNumber; j++) {
        WarmupBrandBO warmupBrandBO = new WarmupBrandBO();
        warmupBrandBO.setActName(actName);
        warmupBrandBO.setActTime(warmupTimestamp);
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
    //为产生收藏数据准备
    warmupBrandMap.put(actName, warmupBrandBOS);
    return warmupBrandBOS;
  }

  protected int getRandomInt(int min, int max){
    int random = generalRandom.nextInt(max)
        % (max - min  + 1 ) + min;
    return random;
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

}
