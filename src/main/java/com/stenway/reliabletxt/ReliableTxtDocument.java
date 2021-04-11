package com.stenway.reliabletxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ReliableTxtDocument {
	protected String text;
	protected ReliableTxtEncoding encoding;
	
	public ReliableTxtDocument() {
		this("");
	}

	public ReliableTxtDocument(String text) {
		this(text, ReliableTxtEncoding.UTF_8);
	}
	
	public ReliableTxtDocument(int... codePoints) {
		this(codePoints, ReliableTxtEncoding.UTF_8);
	}
	
	public ReliableTxtDocument(CharSequence... lines) {
		this(lines, ReliableTxtEncoding.UTF_8);
	}
	
	public ReliableTxtDocument(Iterable<? extends CharSequence> lines) {
		this(lines, ReliableTxtEncoding.UTF_8);
	}

	public ReliableTxtDocument(String text, ReliableTxtEncoding encoding) {
		setText(text);
		setEncoding(encoding);
	}
	
	public ReliableTxtDocument(int[] codePoints, ReliableTxtEncoding encoding) {
		setText(codePoints);
		setEncoding(encoding);
	}
	
	public ReliableTxtDocument(CharSequence[] lines, ReliableTxtEncoding encoding) {
		setLines(lines);
		setEncoding(encoding);
	}
	
	public ReliableTxtDocument(Iterable<? extends CharSequence> lines, ReliableTxtEncoding encoding) {
		setLines(lines);
		setEncoding(encoding);
	}

	public ReliableTxtDocument(byte[] bytes) {
		Object[] decoderResult = ReliableTxtDecoder.decode(bytes);
		
		setEncoding((ReliableTxtEncoding)decoderResult[0]);
		setText((String)decoderResult[1]);
	}
	
	public final void setText(String text) {
		Objects.requireNonNull(text);
		this.text = text;
	}
	
	public void setText(int[] codePoints) {
		Objects.requireNonNull(codePoints);
		this.text = new String(codePoints,0,codePoints.length);
	}
	
	public String getText() {
		return text;
	}
	
	public int[] getCodePoints() {
		return text.codePoints().toArray();
	}
	
	public final void setEncoding(ReliableTxtEncoding encoding) {
		Objects.requireNonNull(encoding);
		this.encoding = encoding;
	}
	
	public ReliableTxtEncoding getEncoding() {
		return encoding;
	}
	
	public final void setLines(CharSequence... lines) {
		text = ReliableTxtLines.join(lines);
	}
	
	public final void setLines(Iterable<? extends CharSequence> lines) {
		text = ReliableTxtLines.join(lines);
	}

	public String[] getLines() {
		return ReliableTxtLines.split(text);
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public byte[] getBytes() {
		return ReliableTxtEncoder.encode(text, encoding);
	}
	
	public void save(String filePath) throws IOException {
		Objects.requireNonNull(filePath);
		
		byte[] bytes = getBytes();
		Files.write(Paths.get(filePath),bytes);
	}
	
	public static ReliableTxtDocument load(String filePath) throws IOException {
		Objects.requireNonNull(filePath);
		
		byte[] bytes = Files.readAllBytes(Paths.get(filePath));
		return new ReliableTxtDocument(bytes);
	}

	public static void save(String text, String filePath) throws IOException {
		new ReliableTxtDocument(text).save(filePath);
	}
	
	public static void save(String text, ReliableTxtEncoding encoding,
			String filePath) throws IOException {
		new ReliableTxtDocument(text,encoding).save(filePath);
	}
	
	public static void save(int[] codepoints, String filePath) throws IOException {
		new ReliableTxtDocument(codepoints).save(filePath);
	}
	
	public static void save(int[] codepoints, ReliableTxtEncoding encoding,
			String filePath) throws IOException {
		new ReliableTxtDocument(codepoints,encoding).save(filePath);
	}
}
