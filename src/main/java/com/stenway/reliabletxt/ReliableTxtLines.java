package com.stenway.reliabletxt;

public class ReliableTxtLines {
	public static String join(CharSequence... lines) {
		return String.join("\n", lines);
	}
	
	public static String join(Iterable<? extends CharSequence> lines) {
		return String.join("\n", lines);
	}
	
	public static String[] split(String text) {
		return text.split("\\n");
	}
}
