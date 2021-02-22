package com.stenway.reliabletxt;

import java.util.ArrayList;

public class BasicWsvParser {
	static class CharIterator {	
		public final int[] Chars;
		public int Index;
		public final int LineIndex;
		
		public CharIterator(String str, int lineIndex) {
			Chars = str.codePoints().toArray();
			LineIndex = lineIndex;
		}
		
		public boolean isEnd() {
			return Index >= Chars.length;
		}
		
		public boolean is(int c) {
			return Chars[Index] == c;
		}
		
		public boolean isWhitespace() {
			return WsvChar.isWhitespace(Chars[Index]);
		}
		
		public boolean next() {
			Index++;
			return !isEnd();
		}
		
		public int get() {
			return Chars[Index];
		}
		
		public String get(int startIndex) {
			int length = Index - startIndex;
			return new String(Chars, startIndex, length);
		}
		
		public BasicWsvParserException getException(String message) {
			return new BasicWsvParserException(LineIndex, Index, message);
		}
	}
	
	public static String[][] parseDocument(String content) {
		String[] lines = content.split("\\n");
		String[][] result = new String[lines.length][];
		for (int i=0; i<lines.length; i++) {
			String line = lines[i];
			result[i] = parseLine(line, i);
		}
		return result;
	}

	private static String[] parseLine(String str, int lineIndex) {
		CharIterator iterator = new CharIterator(str, lineIndex);
		ArrayList<String> values = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		while(true) {
			skipWhitespace(iterator);
			if (iterator.isEnd()) {
				break;
			}
			if (iterator.is('#')) {
				break;
			}
			String curValue;
			if (iterator.is('"')) {
				curValue = parseDoubleQuoteValue(iterator, sb);
			} else {
				curValue = parseValue(iterator);
				if (curValue.equals("-")) {
					curValue = null;
				}
			}
			values.add(curValue);
		}
		
		String[] result = new String[values.size()];
		values.toArray(result);
		return result;
	}
	
	private static String parseValue(CharIterator iterator) {
		int startIndex = iterator.Index;
		while(true) {
			if (!iterator.next()) {
				break;
			}
			if (iterator.isWhitespace() || iterator.is('#')) {
				break;
			} else if (iterator.is('"') ) {
				throw iterator.getException("Invalid double quote");
			}
		}
		return iterator.get(startIndex);
	}
	
	private static String parseDoubleQuoteValue(CharIterator iterator, StringBuilder sb) {
		sb.setLength(0);
		while(true) {
			if (!iterator.next()) {
				throw iterator.getException("String not closed");
			}
			if (iterator.is('"')) { 
				if (!iterator.next()) {
					break;
				}
				if (iterator.is('"')) { 
					sb.append('"');
				} else if (iterator.is('/')) { 
					if (!(iterator.next() && iterator.is('"'))) {
						throw iterator.getException("Invalid line break");
					}
					sb.append('\n');
				} else if (iterator.isWhitespace() || iterator.is('#')) {
					break;
				} else {
					throw iterator.getException("Invalid character after string");
				}
			} else {
				sb.appendCodePoint(iterator.get());
			}
		}
		return sb.toString();
	}
	
	private static void skipWhitespace(CharIterator iterator) {
		if (iterator.isEnd()) {
			return;
		}
		do {
			if (!iterator.isWhitespace()) {
				break;
			}
		} while(iterator.next());
	}
}
