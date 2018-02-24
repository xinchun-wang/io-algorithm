package com.vip.realtime.io.algorithem.data;

/**
 * @author xinchun
 */
public class BrandBO {
  private Integer brandId;
  private String brandName;

  public Integer getBrandId() {
    return brandId;
  }

  public void setBrandId(Integer brandId) {
    this.brandId = brandId;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BrandBO{");
    sb.append("brandId=").append(brandId);
    sb.append(", brandName='").append(brandName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
