package com.stenway.reliabletxt;

public class WsvParserException extends RuntimeException {
	public final String Text;
	public final int Index;
	public final int LineIndex;
	public final int LinePosition;
	
	WsvParserException(WsvCharIterator charIterator, String message) {
		super(message + " " + charIterator.getLineInfoString());
		int[] lineInfo = charIterator.getLineInfo();
		Index = lineInfo[0];
		LineIndex = lineInfo[1];
		LinePosition = lineInfo[2];
		Text = charIterator.getText();
	}
}