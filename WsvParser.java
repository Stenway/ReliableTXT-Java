package com.stenway.reliabletxt;

import java.util.ArrayList;

class WsvParser {
	private static WsvLine parseLine(WsvCharIterator iterator, 
			ArrayList<String> values, ArrayList<String> whitespaces) {
		values.clear();
		whitespaces.clear();
		
		String whitespace = iterator.readWhitespaceOrNull();
		whitespaces.add(whitespace);

		while (!iterator.isChar('\n') && !iterator.isEndOfText()) {
			String value;
			if(iterator.isChar('#')) {
				break;
			} else if(iterator.tryReadChar('"')) {
				value = iterator.readString();
			} else {
				value = iterator.readValue();
				if (value.equals("-")) {
					value = null;
				}
			}
			values.add(value);

			whitespace = iterator.readWhitespaceOrNull();
			if (whitespace == null) {
				break;
			}
			whitespaces.add(whitespace);
		}
		
		String comment = null;
		if(iterator.tryReadChar('#')) {
			comment = iterator.readCommentText();
			if (whitespace == null) {
				whitespaces.add(null);
			}
		}

		String[] valueArray = new String[values.size()];
		String[] whitespaceArray = new String[whitespaces.size()];
		values.toArray(valueArray);
		whitespaces.toArray(whitespaceArray);

		WsvLine newLine = new WsvLine();
		newLine.set(valueArray, whitespaceArray, comment);
		return newLine;
	}
	
	public static WsvLine parseLine(String content) {
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> whitespaces = new ArrayList<>();
		
		WsvLine newLine = parseLine(iterator, values, whitespaces);
		if (iterator.tryReadChar('\n')) {
			throw new WsvParserException(iterator, "Multiple WSV lines not allowed");
		} else if (!iterator.isEndOfText()) {
			throw new WsvParserException(iterator, "WSV line not parsed completely");
		}
		
		return newLine;
	}
	
	public static WsvDocument parseDocument(String content) {
		WsvDocument document = new WsvDocument();
		
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> whitespaces = new ArrayList<>();
		
		while (true) {
			WsvLine newLine = parseLine(iterator, values, whitespaces);
			document.addLine(newLine);
			
			if (iterator.isEndOfText()) {
				break;
			} else if(!iterator.tryReadChar('\n')) {
				throw new WsvParserException(iterator, "Invalid WSV document");
			}
		}
		
		if (!iterator.isEndOfText()) {
			throw new WsvParserException(iterator, "WSV document not parsed completely");
		}

		return document;
	}
}
