package com.vip.realtime.io.algorithem.utils;

public class CommandOptions {
	private String basedir;

	private boolean warmup;

	private boolean goodslike;

	private boolean firstorder;

	/**
	 * 数据的倍数
	 */
	private int factor = 1;

	private String help;

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public boolean isWarmup() {
		return warmup;
	}

	public void setWarmup(boolean warmup) {
		this.warmup = warmup;
	}

	public boolean isGoodslike() {
		return goodslike;
	}

	public void setGoodslike(boolean goodslike) {
		this.goodslike = goodslike;
	}

	public boolean isFirstorder() {
		return firstorder;
	}

	public void setFirstorder(boolean firstorder) {
		this.firstorder = firstorder;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	@Override
	public String toString() {
		return "input: " +
				"basedir='" + basedir + '\'' +
				", warmup=" + warmup +
				", goodslike=" + goodslike +
				", firstorder=" + firstorder +
				", factor=" + factor;
	}
}
