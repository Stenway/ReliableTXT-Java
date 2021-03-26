package com.stenway.reliabletxt;

public class BasicSmlSerializer {
	public static String serializeDocument(SmlDocument document) {
		return serializeDocument(document, false);
	}
	
	public static String serializeDocument(SmlDocument document, boolean minified) {
		StringBuilder sb = new StringBuilder();
		String defaultIndentation = document.getDefaultIndentation();
		if (defaultIndentation == null) {
			defaultIndentation = "\t";
		}
		String endKeyword = document.getEndKeyword();
		if (minified) {
			defaultIndentation = "";
			endKeyword = null;
		}
		serializeElement(sb, document.getRoot(), 0, defaultIndentation, endKeyword);
		sb.setLength(sb.length()-1);
		return sb.toString();
	}

	private static void serializeElement(StringBuilder sb, SmlElement element,
			int level, String defaultIndentation, String endKeyword) {
		serializeIndentation(sb, level, defaultIndentation);
		WsvSerializer.serializeValue(sb, element.getName());
		sb.append('\n'); 

		int childLevel = level + 1;
		for (SmlNode child : element.Nodes) {
			if (child instanceof SmlElement) {
				serializeElement(sb, (SmlElement)child, childLevel, defaultIndentation, endKeyword);
			} else if (child instanceof SmlAttribute) {
				serializeAttribute(sb, (SmlAttribute)child, childLevel, defaultIndentation);
			}
		}
		
		serializeIndentation(sb, level, defaultIndentation);
		WsvSerializer.serializeValue(sb, endKeyword);
		sb.append('\n'); 
	}
	
	private static void serializeAttribute(StringBuilder sb, SmlAttribute attribute,
			int level, String defaultIndentation) {
		serializeIndentation(sb, level, defaultIndentation);
		WsvSerializer.serializeValue(sb, attribute.getName());
		sb.append(' '); 
		WsvSerializer.serializeLine(sb, attribute.getValues());
		sb.append('\n'); 
	}
	
	private static void serializeIndentation(StringBuilder sb, int level, String defaultIndentation) {
		String indentStr = defaultIndentation.repeat(level);
		sb.append(indentStr);
	}
}
