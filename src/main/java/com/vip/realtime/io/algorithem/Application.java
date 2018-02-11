package com.vip.realtime.io.algorithem;

import com.vip.realtime.io.algorithem.utils.CommandOptions;
import com.vip.realtime.io.algorithem.utils.CommandOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args){
    CommandOptionsParser optionsParser = new CommandOptionsParser();
    CommandOptions commandOptions = new CommandOptions();
    optionsParser.process(args, commandOptions);

    String database = commandOptions.getDatabase();
    String tableName = commandOptions.getTableName();


  }

}
