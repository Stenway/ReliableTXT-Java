package com.stenway.reliabletxt;

public class BasicWsvParserException extends RuntimeException {
	public final int LineIndex;
	public final int LinePosition;
	
	BasicWsvParserException(int lineIndex, int linePosition, String message) {
		super(String.format("%s (%d, %d)", message, lineIndex + 1, linePosition));
		LineIndex = lineIndex;
		LinePosition = linePosition;
	}
}
