package com.stenway.reliabletxt;

public class BasicWsvSerializer {
	private static boolean containsSpecialChar(int[] chars) {
		for (int c : chars) {
			if (WsvChar.isWhitespace(c) || c == '"' || c == '#') {
				return true;
			}
		}
		return false;
	}

	public static void serializeValue(StringBuilder sb, String value) {
		if (value == null) {
			sb.append('-');
		} else if (value.length() == 0) {
			sb.append("\"\"");
		} else if (value.equals("-")) {
			sb.append("\"-\"");
		} else {
			int[] chars = value.codePoints().toArray();
			if (containsSpecialChar(chars)) {
				sb.append('"');
				for (int c : chars) {
					if (c == '\n') {
						sb.append("\"/\"");
					} else if (c == '"') {
						sb.append("\"\"");
					} else {
						sb.appendCodePoint(c);
					}
				}
				sb.append('"');
			} else {
				sb.append(value);
			}
		}
	}
	
	public static void serializeLine(StringBuilder sb, String[] line) {
		boolean isFirstValue = true;
		for (String value : line) {
			if (!isFirstValue) {
				sb.append(' ');
			} else {
				isFirstValue = false;
			}
			serializeValue(sb, value);
		}
	}
	
	public static String serialize(String... line) {
		StringBuilder sb = new StringBuilder();
		serializeLine(sb, line);
		return sb.toString();
	}
		
	public static String serialize(String[][] lines) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstLine = true;
		for (String[] line : lines) {
			if (!isFirstLine) {
				sb.append('\n');
			} else {
				isFirstLine = false;
			}
			serializeLine(sb, line);
		}
		return sb.toString();
	}
}
