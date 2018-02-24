package com.vip.realtime.io.algorithem.data;

/**
 * @author xinchun
 */
public class WarmupBrandBO {
  private String platform;
  private String actName;
  private String actStartTime;
  private String actEndTime;
  private String brandName;
  private Integer brandId;

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getActName() {
    return actName;
  }

  public void setActName(String actName) {
    this.actName = actName;
  }

  public String getActStartTime() {
    return actStartTime;
  }

  public void setActStartTime(String actStartTime) {
    this.actStartTime = actStartTime;
  }

  public String getActEndTime() {
    return actEndTime;
  }

  public void setActEndTime(String actEndTime) {
    this.actEndTime = actEndTime;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public Integer getBrandId() {
    return brandId;
  }

  public void setBrandId(Integer brandId) {
    this.brandId = brandId;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(platform).append(',');
    sb.append(actName).append(',');
    sb.append(actStartTime).append(',');
    sb.append(actEndTime).append(',');
    sb.append(brandName).append(',');
    sb.append(brandId).append("\n");
    return sb.toString();
  }
}
