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
	}
	
	static class WsvDocumentLineIterator implements WsvLineIterator {
		WsvDocument wsvDocument;
		String endKeyword;
		
		int index;
		
		public WsvDocumentLineIterator(WsvDocument wsvDocument, String endKeyword) {
			Objects.requireNonNull(endKeyword);
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
		return document;
	}
	
	public static SmlElement readRootElement(WsvLineIterator iterator, 
			ArrayList<SmlEmptyNode> emptyNodesBefore) throws IOException {
		readEmptyNodes(emptyNodesBefore, iterator);
		
		if (!iterator.hasLine()) {
			throw new SmlParserException("Root element expected");
		}
		WsvLine rootStartLine = iterator.getLine();
		if (!rootStartLine.hasValues() || rootStartLine.Values.length != 1 
				|| rootStartLine.Values[0].equalsIgnoreCase(iterator.getEndKeyword())) {
			throw new SmlParserException("Invalid root element start");
		}
		String rootElementName = rootStartLine.Values[0];
		SmlElement rootElement = new SmlElement(rootElementName);
		rootElement.setWhitespacesAndComment(rootStartLine.whitespaces, rootStartLine.comment);
		return rootElement;
	}
	
	public static SmlNode readNode(WsvLineIterator iterator, SmlElement parentElement) throws IOException {
		SmlNode node;
		WsvLine line = iterator.getLine();
		if (line.hasValues()) {
			String name = line.Values[0];
			if (name.equalsIgnoreCase(iterator.getEndKeyword())) {
				if (line.Values.length > 1) {
					throw new SmlParserException("Attribute with end keyword name is not allowed");
				}
				parentElement.setEndWhitespacesAndComment(line.whitespaces, line.comment);
				return null;
			}
			if (line.Values.length == 1) {
				SmlElement childElement = new SmlElement(name);
				childElement.setWhitespacesAndComment(line.whitespaces, line.comment);

				readElementContent(iterator, childElement);

				node = childElement;
			} else {
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
				throw new SmlParserException("Element not closed");
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
		throw new SmlParserException("End keyword could not be detected");
	}
}