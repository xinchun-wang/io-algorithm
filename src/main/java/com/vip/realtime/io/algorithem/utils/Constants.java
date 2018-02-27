package com.vip.realtime.io.algorithem.utils;

/**
 * @author xinchun
 */
public class Constants {
  public static String GOODS_LIKE_DIR = "trd_brand_goods_like_hm";
  public static String FIRST_ORDER_DIR = "dm_vip_user_first_ord_dt";
  public static String WARMUP_BRAND_DIR = "dty_act_warmup_brand_total_hm";

  public static String BASE_DIR_KEY = "basedir";

  public static String WARMUP_KEY = "warmup";

  public static String GOODS_LIKE_KEY = "goodslike";

  public static String FIRST_ORDER_KEY = "firstorder";

  public static String FACTOR_KEY = "factor";

  /**
   * 用户收藏的user id产生随机数区间 = 订单数量 * ORDER_USER_FACTOR
   */
  public static int ORDER_USER_FACTOR = 4;

  /**
   * 预热的活动数量
   */
  public static Integer WARMUP_BRAND_NUMBER_DEFAULT_VALUE = 100;

  /**
   * 每个活动的base收藏数量，{下限，平均，上限}，实际数量会*data factor
   */
  public static Integer GOODS_LIKE_PER_ACT_DEFAULT_LOW_VALUES = 100;

  public static Integer GOODS_LIKE_PER_ACT_DEFAULT_UP_VALUES = 600;

  public static Integer GOODS_LIKE_PER_ACT_DEFAULT_AVARGE_VALUES = 500;

  /**
   * 收藏的超大活动的数量倍数
   */
  public static Integer GOODS_LIKE_HUGE_PER_ACT_FACTOR = 1000;

  /**
   * 收藏的大型活动的数量倍数
   */
  public static Integer GOODS_LIKE_LARGE_PER_ACT_FACTOR = 100;

  /**
   * 首单的用户的数量基数
   */
  public static Integer FIRST_ORDER_DEFAULT_VALUE = 300*10000;

  public static Long DAY_OF_2017_01_01_000000_MS = 1483200000000L;

  public static Long DAY_OF_2017_12_31_235959_MS = 1514735999000L;

  public static Long DAY_OF_2008_12_08_000000_MS = 1228665600000L;

  public static Long DAY_OF_MS = 86400000L;

}
