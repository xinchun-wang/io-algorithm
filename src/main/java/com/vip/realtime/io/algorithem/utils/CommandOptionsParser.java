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
        Option database = new Option("db", false, "database");
        database.setRequired(false);
        options.addOption(database);

        Option tableName = new Option("table", true, "process table");
        tableName.setRequired(true);
        options.addOption(tableName);

        Option startParition = new Option("startParition", true, "start partiton");
        startParition.setRequired(false);
        options.addOption(startParition);

        Option endPartition = new Option("endPartition", true, "end partition");
        endPartition.setRequired(false);
        options.addOption(endPartition);

        Option batchNum = new Option("batchNum", true, "batchNum, default is 30");
        batchNum.setRequired(false);
        options.addOption(batchNum);

        Option h = new Option("h", false, "help info");
        h.setRequired(false);
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
            if(cmd.hasOption("database")){
            	commandOptions.setDatabase(cmd.getOptionValue("database"));
            }
            commandOptions.setTableName(cmd.getOptionValue("table"));
            
        } catch (ParseException e) {
          LOG.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
