package com.stenway.reliabletxt;

public class WsvLine {
	public String[] Values;
	
	String[] whitespaces;
	String comment;

	public WsvLine() {
		
	}
	
	public WsvLine(String... values) {
		Values = values;
		
		whitespaces = null;
		comment = null;
	}

	public WsvLine(String[] values, String[] whitespaces, String comment) {
		Values = values;
		
		setWhitespaces(whitespaces);
		setComment(comment);
	}
	
	public boolean hasValues() {
		return Values != null && Values.length > 0;
	}
	
	public void setValues(String... values) {
		Values = values;
	}
	
	public final void setWhitespaces(String... whitespaces) {
		validateWhitespaces(whitespaces);
		this.whitespaces = whitespaces;
	}
	
	static void validateWhitespaces(String... whitespaces) {
		if (whitespaces != null) {
			for (String whitespace : whitespaces) {
				if (whitespace != null && whitespace.length() > 0 && !WsvString.isWhitespace(whitespace)) {
					throw new IllegalArgumentException(
							"Whitespace value contains non whitespace character");
				}
			}
		}
	}
	
	public String[] getWhitespaces() {
		return whitespaces.clone();
	}
	
	public final void setComment(String comment) {
		validateComment(comment);
		this.comment = comment;
	}
	
	static void validateComment(String comment) {
		if (comment != null && comment.indexOf('\n') >= 0) {
			throw new IllegalArgumentException(
					"Line break in comment is not allowed");
		}
	}
	
	public String getComment() {
		return comment;
	}
	
	void set(String[] values, String[] whitespaces, String comment) {
		Values = values;
		this.whitespaces = whitespaces;
		this.comment = comment;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		WsvSerializer.serializeLine(sb, this);
		return sb.toString();
	}
	
	public static WsvLine parse(String content) {
		return WsvParser.parseLine(content);
	}
}