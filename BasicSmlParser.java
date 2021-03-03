package com.stenway.reliabletxt;

import java.util.Arrays;

public class BasicSmlParser {
	static class BasicWsvLineIterator {
		private final String[][] lines;
		private String endKeyword;
		private int index;
		
		public BasicWsvLineIterator(String content) {
			lines = BasicWsvParser.parseDocument(content);	
			detectEndKeyword();
		}
		
		private void detectEndKeyword() {
			int i;
			for (i=lines.length-1; i>=0; i--) {
				String[] values = lines[i];
				if (values.length == 1) {
					endKeyword = values[0];
					if (endKeyword == null) {
						throw new BasicSmlParserException(i, "End keyword cannot be null");
					}
					return;
				} else if (values.length > 1) {
					break;
				}
			}
			throw new BasicSmlParserException(i, "End keyword could not be detected");
		}
		
		public String getEndKeyword() {
			return endKeyword;
		}
		
		public boolean hasLine() {
			return index < lines.length;
		}

		public boolean isEmptyLine() {
			return hasLine() && (lines[index] == null || lines[index].length == 0);
		}

		public String[] getLine() {
			String[] line = lines[index];
			index++;
			return line;
		}
		
		public BasicSmlParserException getException(String message) {
			return new BasicSmlParserException(index, message);
		}
	}
	
	public static SmlDocument parseDocument(String content) {
		BasicWsvLineIterator iterator = new BasicWsvLineIterator(content);

		skipEmptyLines(iterator);
		if (!iterator.hasLine()) {
			throw iterator.getException("Root element expected");
		}
		
		SmlNode node = readNode(iterator);
		if (!(node instanceof SmlElement)) {
			throw iterator.getException("Invalid root element start");
		}
		
		skipEmptyLines(iterator);
		if (iterator.hasLine()) {
			throw iterator.getException("Only one root element allowed");
		}
		
		SmlDocument document = new SmlDocument((SmlElement)node);
		document.setEndKeyword(iterator.getEndKeyword());
		return document;
	}
	
	private static void skipEmptyLines(BasicWsvLineIterator iterator) {
		while (iterator.isEmptyLine()) {
			iterator.getLine();
		}
	}
	
	private static SmlNode readNode(BasicWsvLineIterator iterator) {
		String[] line = iterator.getLine();
		
		String name = line[0];
		if (iterator.getEndKeyword().equalsIgnoreCase(name)) {
			if (line.length > 1) {
				throw iterator.getException("Attribute with end keyword name is not allowed");
			}
			return null;
		}
		if (line.length == 1) {
			if (name == null) {
				throw iterator.getException("Element name cannot be null");
			}
			SmlElement element = new SmlElement(name);
			readElementContent(iterator, element);
			return element;
		} else {
			if (name == null) {
				throw iterator.getException("Attribute name cannot be null");
			}
			String[] values = Arrays.copyOfRange(line, 1, line.length);
			SmlAttribute attribute = new SmlAttribute(name, values);
			return attribute;
		}
	}
	
	private static void readElementContent(BasicWsvLineIterator iterator, SmlElement element) {
		while (true) {
			if (!iterator.hasLine()) {
				throw iterator.getException("Element not closed");
			}
			skipEmptyLines(iterator);
			SmlNode node = readNode(iterator);
			if (node == null) {
				break;
			}
			element.add(node);
		}
	}
}
