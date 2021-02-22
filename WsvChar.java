package com.stenway.reliabletxt;

public class WsvChar {
	public static boolean isWhitespace(int c) {
		if (Character.isSpaceChar(c)) {
			return true;
		}
		return (c >= 0x09 && c <= 0x0D) || c == 0x85;
	}
}
