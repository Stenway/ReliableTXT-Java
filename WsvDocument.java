package com.stenway.reliabletxt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class WsvDocument {
	public final ArrayList<WsvLine> Lines = new ArrayList<WsvLine>();
	
	private ReliableTxtEncoding encoding;
	
	public WsvDocument() {
		this(ReliableTxtEncoding.UTF_8);
	}
	
	public WsvDocument(ReliableTxtEncoding encoding) {
		setEncoding(encoding);
	}
	
	public final void setEncoding(ReliableTxtEncoding encoding) {
		Objects.requireNonNull(encoding);
		this.encoding = encoding;
	}
	
	public ReliableTxtEncoding getEncoding() {
		return encoding;
	}
	
	public void addLine(String... values) {
		addLine(new WsvLine(values));
	}
	
	public void addLine(String[] values, String[] whitespaces, String comment) {
		addLine(new WsvLine(values, whitespaces, comment));
	}

	public void addLine(WsvLine line) {
		Lines.add(line);
	}
	
	public WsvLine getLine(int index) {
		return Lines.get(index);
	}
	
	public String[][] toArray() {
		String[][] array = new String[Lines.size()][];
		for (int i=0; i<Lines.size(); i++) {
			array[i] = Lines.get(i).Values;
		}
		return array;
	}

	@Override
	public String toString() {
		return WsvSerializer.serializeDocument(this);
	}

	public void save(String filePath) throws IOException {
		String content = toString();
		ReliableTxtDocument.save(content, encoding, filePath);
	}

	public static WsvDocument load(String filePath) throws IOException {
		ReliableTxtDocument txt = ReliableTxtDocument.load(filePath);
		WsvDocument document = parse(txt.getText());
		document.encoding = txt.getEncoding();
		return document;
	}

	public static WsvDocument parse(String content) {
		return WsvParser.parseDocument(content);
	}
}