package com.PD.Tool;

public class DataUnit {

	public static Number getNumber(String str) {
		if (str.contains(".")) {
			return Double.parseDouble(str);
		} else {
			return Integer.parseInt(str);
		}
	}

}
