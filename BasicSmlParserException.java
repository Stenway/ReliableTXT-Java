package com.stenway.reliabletxt;

public class BasicSmlParserException extends RuntimeException {
	public final int LineIndex;
	
	public BasicSmlParserException(int lineIndex, String message) {
		super(String.format("%s (%d)", message, lineIndex + 1));
		LineIndex = lineIndex;
	}
}
