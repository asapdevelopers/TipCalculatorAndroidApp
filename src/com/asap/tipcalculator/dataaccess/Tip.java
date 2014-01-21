package com.asap.tipcalculator.dataaccess;

public class Tip {
	
	private long id;
	private String tip;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	
	@Override
	public String toString() {
		return tip;
	}	
	
}
