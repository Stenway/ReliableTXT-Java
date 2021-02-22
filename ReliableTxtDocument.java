package com.stenway.reliabletxt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ReliableTxtDocument {
	protected String text;
	protected ReliableTxtEncoding encoding;
	
	public ReliableTxtDocument() {
		this("");
	}

	public ReliableTxtDocument(String text) {
		this(text,ReliableTxtEncoding.UTF_8);
	}
	
	public ReliableTxtDocument(int... codePoints) {
		this(codePoints,ReliableTxtEncoding.UTF_8);
	}
	
	public ReliableTxtDocument(String... lines) {
		this(lines,ReliableTxtEncoding.UTF_8);
	}

	public ReliableTxtDocument(String text, ReliableTxtEncoding encoding) {
		setText(text);
		setEncoding(encoding);
	}
	
	public ReliableTxtDocument(int[] codePoints, ReliableTxtEncoding encoding) {
		setText(codePoints);
		setEncoding(encoding);
	}
	
	public ReliableTxtDocument(String[] lines, ReliableTxtEncoding encoding) {
		setLines(lines);
		setEncoding(encoding);
	}
	
	public ReliableTxtDocument(byte[] bytes) {
		ReliableTxtEncoding detectedEncoding = ReliableTxtEncoding.fromBytes(bytes);
		Charset charset = detectedEncoding.getCharset();
		byte preambleLength = detectedEncoding.getPreambleLength();
		
		String decodedText = new String(bytes, preambleLength,
				bytes.length-preambleLength, charset);

		setText(decodedText);
		setEncoding(detectedEncoding);
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
	
	public final void setLines(String... lines) {
		text = String.join("\n", lines);
	}
	
	public String[] getLines() {
		return text.split("\n");
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public byte[] getBytes() {
		Charset charset = encoding.getCharset();
		String textWithPreamble = ((char)65279) + text;
		return textWithPreamble.getBytes(charset);
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
	
	public static String join(String... lines) {
		return new ReliableTxtDocument(lines).toString();
	}
	
	public static String join(List<String> lines) {
		String[] linesArray = new String[lines.size()];
		lines.toArray(linesArray);
		return new ReliableTxtDocument(linesArray).toString();
	}
}