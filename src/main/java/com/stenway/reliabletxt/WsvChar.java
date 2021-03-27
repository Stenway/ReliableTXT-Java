package com.stenway.reliabletxt;

public class WsvChar {
	public static boolean isWhitespace(int c) {
		if (Character.isSpaceChar(c)) {
			return true;
		}
		return (c >= 0x09 && c <= 0x0D) || c == 0x85;
	}
	
	public static int[] getWhitespaceCodePoints() {
		return new int[] {
			0x0009,
			0x000A,
			0x000B,
			0x000C,
			0x000D,
			0x0020,
			0x0085,
			0x00A0,
			0x1680,
			0x2000,
			0x2001,
			0x2002,
			0x2003,
			0x2004,
			0x2005,
			0x2006,
			0x2007,
			0x2008,
			0x2009,
			0x200A,
			0x2028,
			0x2029,
			0x202F,
			0x205F,
			0x3000
		};
	}
}