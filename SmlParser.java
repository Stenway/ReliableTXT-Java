package com.stenway.reliabletxt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class SmlParser {
	static interface WsvLineIterator {	
		boolean hasLine();
		boolean isEmptyLine();
		WsvLine getLine() throws IOException;
		String getEndKeyword();
		int getLineIndex();
	}
	
	public static class WsvStreamLineIterator implements WsvLineIterator {
		WsvStreamReader reader;
		String endKeyword;
		WsvLine currentLine;
		
		int index;
		
		public WsvStreamLineIterator(WsvStreamReader reader, String endKeyword) throws IOException {
			Objects.requireNonNull(endKeyword);
			this.reader = reader;
			this.endKeyword = endKeyword;
			
			currentLine = reader.readLine();
		}
		
		@Override
		public boolean hasLine() {
			return currentLine != null;
		}

		@Override
		public boolean isEmptyLine() {
			return hasLine() && !currentLine.hasValues();
		}

		@Override
		public WsvLine getLine() throws IOException {
			WsvLine result = currentLine;
			currentLine = reader.readLine();
			index++;
			return result;
		}

		@Override
		public String getEndKeyword() {
			return endKeyword;
		}
		
		@Override
		public String toString() {
			String result = "(" + index + "): ";
			if (hasLine()) {
				result += currentLine.toString();
			}
			return result;
		}
		
		@Override
		public int getLineIndex() {
			return index;
		}
	}
	
	static class WsvDocumentLineIterator implements WsvLineIterator {
		WsvDocument wsvDocument;
		String endKeyword;
		
		int index;
		
		public WsvDocumentLineIterator(WsvDocument wsvDocument, String endKeyword) {
			this.wsvDocument = wsvDocument;
			this.endKeyword = endKeyword;
		}
		
		@Override
		public String getEndKeyword() {
			return endKeyword;
		}
		
		@Override
		public boolean hasLine() {
			return index < wsvDocument.Lines.size();
		}
		
		@Override
		public boolean isEmptyLine() {
			return hasLine() && !wsvDocument.Lines.get(index).hasValues();
		}
		
		@Override
		public WsvLine getLine() {
			WsvLine line = wsvDocument.Lines.get(index);
			index++;
			return line;
		}
		
		@Override
		public String toString() {
			String result = "(" + index + "): ";
			if (hasLine()) {
				result += wsvDocument.Lines.get(index).toString();
			}
			return result;
		}
		
		@Override
		public int getLineIndex() {
			return index;
		}
	}
		
	public static SmlDocument parseDocument(String content) throws IOException {
		WsvDocument wsvDocument = WsvDocument.parse(content);
		String endKeyword = getEndKeyword(wsvDocument);
		WsvLineIterator iterator = new WsvDocumentLineIterator(wsvDocument, endKeyword);
		
		SmlDocument document = new SmlDocument();
		document.setEndKeyword(endKeyword);
		
		SmlElement rootElement = readRootElement(iterator, document.EmptyNodesBefore);
		document.setRoot(rootElement);
		
		readElementContent(iterator, rootElement);
		
		readEmptyNodes(document.EmptyNodesAfter, iterator);
		if (iterator.hasLine()) {
			throw new SmlParserException(iterator.getLineIndex(), "Only one root element allowed");
		}
		return document;
	}
	
	private static boolean equalIgnoreCase(String name1, String name2) {
		if (name1 == null) {
			return name1 == name2;
		}
		return name1.equalsIgnoreCase(name2);
	}
	
	public static SmlElement readRootElement(WsvLineIterator iterator, 
			ArrayList<SmlEmptyNode> emptyNodesBefore) throws IOException {
		readEmptyNodes(emptyNodesBefore, iterator);
		
		if (!iterator.hasLine()) {
			throw new SmlParserException(iterator.getLineIndex(), "Root element expected");
		}
		WsvLine rootStartLine = iterator.getLine();
		if (!rootStartLine.hasValues() || rootStartLine.Values.length != 1 
				|| equalIgnoreCase(iterator.getEndKeyword(), rootStartLine.Values[0])) {
			throw new SmlParserException(iterator.getLineIndex()-1, "Invalid root element start");
		}
		String rootElementName = rootStartLine.Values[0];
		if (rootElementName == null) {
			throw new SmlParserException(iterator.getLineIndex()-1, "Null value as element name is not allowed");
		}
		SmlElement rootElement = new SmlElement(rootElementName);
		rootElement.setWhitespacesAndComment(rootStartLine.whitespaces, rootStartLine.comment);
		return rootElement;
	}
	
	public static SmlNode readNode(WsvLineIterator iterator, SmlElement parentElement) throws IOException {
		SmlNode node;
		WsvLine line = iterator.getLine();
		if (line.hasValues()) {
			String name = line.Values[0];
			if (line.Values.length == 1) {
				if (equalIgnoreCase(iterator.getEndKeyword(),name)) {
					parentElement.setEndWhitespacesAndComment(line.whitespaces, line.comment);
					return null;
				}
				if (name == null) {
					throw new SmlParserException(iterator.getLineIndex()-1, "Null value as element name is not allowed");
				}
				SmlElement childElement = new SmlElement(name);
				childElement.setWhitespacesAndComment(line.whitespaces, line.comment);

				readElementContent(iterator, childElement);

				node = childElement;
			} else {
				if (name == null) {
					throw new SmlParserException(iterator.getLineIndex()-1, "Null value as attribute name is not allowed");
				}
				String[] values = Arrays.copyOfRange(line.Values, 1, line.Values.length);
				SmlAttribute childAttribute = new SmlAttribute(name, values);
				childAttribute.setWhitespacesAndComment(line.whitespaces, line.comment);

				node = childAttribute;
			}
		} else {
			SmlEmptyNode emptyNode = new SmlEmptyNode();
			emptyNode.setWhitespacesAndComment(line.whitespaces, line.comment);

			node = emptyNode;
		}
		return node;
	}
	
	private static void readElementContent(WsvLineIterator iterator, SmlElement element) throws IOException {
		while (true) {
			if (!iterator.hasLine()) {
				throw new SmlParserException(iterator.getLineIndex()-1, "Element \""+element.getName()+"\" not closed");
			}
			SmlNode node = readNode(iterator, element);
			if (node == null) {
				break;
			}
			element.add(node);
		}
	}
	
	private static void readEmptyNodes(ArrayList<SmlEmptyNode> nodes, WsvLineIterator iterator) throws IOException {
		while (iterator.isEmptyLine()) {
			SmlEmptyNode emptyNode = readEmptyNode(iterator);
			nodes.add(emptyNode);
		}
	}
	
	private static SmlEmptyNode readEmptyNode(WsvLineIterator iterator) throws IOException {
		WsvLine line = iterator.getLine();
		SmlEmptyNode emptyNode = new SmlEmptyNode();
		emptyNode.setWhitespacesAndComment(line.whitespaces, line.comment);
		return emptyNode;
	}
	
	private static String getEndKeyword(WsvDocument wsvDocument) {
		for (int i=wsvDocument.Lines.size()-1; i>=0; i--) {
			String[] values = wsvDocument.Lines.get(i).Values;
			if (values != null) {
				if (values.length == 1) {
					return values[0];
				} else if (values.length > 1) {
					break;
				}
			}
		}
		throw new SmlParserException(wsvDocument.Lines.size()-1, "End keyword could not be detected");
	}
}