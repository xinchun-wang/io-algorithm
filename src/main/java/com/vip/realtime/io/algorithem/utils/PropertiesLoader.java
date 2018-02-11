package com.vip.realtime.io.algorithem.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

  private Properties properties = new Properties();

  private  PropertiesLoader(){
    load();
  }

  private void load(){
    final InputStream stream = PropertiesLoader.class.getResourceAsStream("/application.properties");
    if (stream == null) {
      throw new RuntimeException("No properties!!!");
    }
    try {
      properties.load(stream);
    } catch (final IOException e) {
      throw new RuntimeException("Configuration could not be loaded!");
    }
  }

  public String getString(String key){
    return properties.getProperty(key);
  }

  public Integer getInt(String key){
    return Integer.parseInt(properties.getProperty(key));
  }

  public Long getLong(String key){
    return Long.parseLong(properties.getProperty(key));
  }


}
