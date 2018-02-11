package com.vip.realtime.io.algorithem.utils;

public class CommandOptions {
	public String folder;

    public String database = "bi";
    public String tableName;
    public String startParition;
    public String endPartition;
    public String help;
    
    public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStartParition() {
		return startParition;
	}

	public void setStartParition(String startParition) {
		this.startParition = startParition;
	}

	public String getEndPartition() {
		return endPartition;
	}

	public void setEndPartition(String endPartition) {
		this.endPartition = endPartition;
	}

	public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

}
