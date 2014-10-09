package com.codepath.apps.basictwitter.helper;


public class TwitterHelper {

	public static String convertNumbers(int num) {
		int i = 0;
		StringBuilder convNum = new StringBuilder();
		while (num >= 1000) {
			num = num / 1000;
			i++;
		}
		if (i == 0) {
			convNum.append(num);
		} else if (i == 1) {
			convNum.append(num).append("K");
		} else if (i > 1) {
			convNum.append(num).append("M");
		}
		return convNum.toString();
	}
	
	
}
