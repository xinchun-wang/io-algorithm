package com.vip.realtime.io.algorithem.utils;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandOptionsParser {

  private static final Logger LOG = LoggerFactory.getLogger(CommandOptionsParser.class);

  private final Options options = new Options();
  private CommandLine cmd;

  public CommandOptionsParser() {
    Option baseDir = new Option(Constants.BASE_DIR_KEY, true, "basedir");
    baseDir.setRequired(true);
    options.addOption(baseDir);

    Option warmup = new Option(Constants.WARMUP_KEY, false, "generate warmup data");
    options.addOption(warmup);

    Option goodslike = new Option(Constants.GOODS_LIKE_KEY, false, "generate goods like data");
    options.addOption(goodslike);

    Option firstorder = new Option(Constants.FIRST_ORDER_KEY, false, "generate first order data");
    options.addOption(firstorder);

    Option factor = new Option(Constants.FACTOR_KEY, false, "data factor");
    options.addOption(factor);

    Option h = new Option("h", false, "help info");
    options.addOption(h);
  }

  private void printUsage() {
    new HelpFormatter().printHelp("md", options);
  }

  public void process(String[] args, CommandOptions commandOptions) {
    try {
      cmd = new DefaultParser().parse(options, args);
      if (cmd.hasOption("h")) {
        printUsage();
        System.exit(1);
      }
      commandOptions.setBasedir(cmd.getOptionValue(Constants.BASE_DIR_KEY));
      commandOptions.setWarmup(getValue(Constants.WARMUP_KEY,true));
      commandOptions.setGoodslike(getValue(Constants.GOODS_LIKE_KEY, true));
      commandOptions.setFirstorder(getValue(Constants.FIRST_ORDER_KEY,true));
      commandOptions.setFactor(getValue(Constants.FACTOR_KEY, 1));
    } catch (ParseException e) {
      LOG.error(e.getMessage(), e);
      System.exit(1);
    }
  }

  public boolean getValue(String key, boolean defaultValue){
    if(cmd.hasOption(key)){
      return Boolean.valueOf(cmd.getOptionValue(key));
    }
    return defaultValue;

  }

  public int getValue(String key, int defaultValue){
    if(cmd.hasOption(key)){
      return Integer.valueOf(cmd.getOptionValue(key));
    }
    return defaultValue;

  }
}
