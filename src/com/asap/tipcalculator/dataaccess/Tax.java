package com.asap.tipcalculator.dataaccess;

public class Tax {
	
	private long id;
	private String tax;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	
	@Override
	public String toString() {
		return tax;
	}	
}
