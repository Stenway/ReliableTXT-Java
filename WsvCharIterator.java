package com.stenway.reliabletxt;

class WsvCharIterator {
	private final StringBuilder sb = new StringBuilder();
	private final int[] chars;
	private int index;
	
	public WsvCharIterator(String text) {
		chars = text.codePoints().toArray();
	}
	
	public String getText() {
		return new String(chars, 0, chars.length);
	}
	
	public String getLineInfoString() {
		int[] lineInfo = getLineInfo();
		return String.format("(%d, %d)", lineInfo[1] + 1, lineInfo[2] + 1);
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
		return new int[] {index, lineIndex, linePosition};
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
	
	public String readCommentText() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) break;
			if (chars[index] == '\n') break;
			index++;
		}
		return new String(chars,startIndex,index-startIndex);
	}

	public String readWhitespaceOrNull() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) break;
			int c = chars[index];
			if (c == '\n') break;
			if (!WsvChar.isWhitespace(c)) break;
			index++;
		}
		if (index == startIndex) return null;
		return new String(chars,startIndex,index-startIndex);
	}

	public String readString() {
		sb.setLength(0);
		while (true) {
			if (isEndOfText()) {
				throw new WsvParserException(this, "String not closed");
			}
			int c = chars[index];
			if (c == '\n') {
				throw new WsvParserException(this, "String not closed in starting line");
			} else if (c == '"') {
				index++;
				if (tryReadChar('"')) {
					sb.append('"');
				} else if(tryReadChar('/')) {
					if (!tryReadChar('"')) {
						throw new WsvParserException(this, "String expected after linebreak slash");
					}
					sb.append('\n');
				} else {
					break;
				}
			} else {
				sb.appendCodePoint(c);
				index++;
			}
		}
		return sb.toString();
	}

	public String readValue() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) {
				break;
			}
			int c = chars[index];
			if (WsvChar.isWhitespace(c) || c == '#') {
				break;
			}
			if (c == '\"') {
				throw new WsvParserException(this, "String starting in value");
			}
			index++;
		}
		if (index == startIndex) {
			throw new WsvParserException(this, "Invalid value");
		}
		return new String(chars,startIndex,index-startIndex);
	}
}