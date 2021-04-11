package com.stenway.reliabletxt;

import java.util.Objects;

public class ReliableTxtCharIterator {
	protected final StringBuilder sb = new StringBuilder();
	protected final int[] chars;
	protected int index;
	
	public ReliableTxtCharIterator(String text) {
		Objects.requireNonNull(text);
		chars = text.codePoints().toArray();
	}
	
	public String getText() {
		return new String(chars, 0, chars.length);
	}

	public int[] getLineInfo() {
		int lineIndex = 0;
		int linePosition = 0;
		for (int i=0; i<index; i++) {
			if (chars[i] == '\n') {
				lineIndex++;
				linePosition = 0;
			} else {
				linePosition++;
			}
		}
		return new int[] {lineIndex, linePosition};
	}
	
	public boolean isEndOfText() {
		return index >= chars.length;
	}

	public boolean isChar(int c) {
		if (isEndOfText()) return false;
		return chars[index] == c;
	}
	
	public boolean tryReadChar(int c) {
		if (!isChar(c)) return false;
		index++;
		return true;
	}
}
