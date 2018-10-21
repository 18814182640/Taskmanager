package com.FD.model.DataModel;

import com.FP.frame.Config;

public class FixdtType extends DataType {

	private String shortName;
	private String symbol; // ·ûºÅ
	private Number slope; // Òò×Ó
	private Number offset; // Æ«ÒÆ
	private int length;
	private String description = "";

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Number getSlope() {
		return slope;
	}

	public void setSlope(Number slope) {
		this.slope = slope;
	}

	public Number getOffset() {
		return offset;
	}

	public void setOffset(Number offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getType() {
		return Config.FixdtType;
	}

}
