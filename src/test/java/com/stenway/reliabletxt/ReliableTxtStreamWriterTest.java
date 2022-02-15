package com.stenway.reliabletxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class ReliableTxtStreamWriterTest {

	@Test
	public void constructor() throws Exception {
		String filePath = "Test.txt";
		try (ReliableTxtStreamWriter writer = new ReliableTxtStreamWriter(filePath)) {

		}
		try (ReliableTxtStreamWriter writer = new ReliableTxtStreamWriter(filePath, true)) {

		}
	}
	
	@Test
	public void constructor_append() throws IOException, Exception {
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_32);
		
	}
	
	private void constructor_append(ReliableTxtEncoding firstEncoding, ReliableTxtEncoding secondEncoding) throws IOException, Exception {
		String filePath = "Append.txt";
		deleteAppendFile(filePath);
		try (ReliableTxtStreamWriter writer = new ReliableTxtStreamWriter(filePath, firstEncoding, true)) {
			writer.writeLine("Line 1");
			Assert.equals(writer.encoding, firstEncoding);
		}
		load(filePath, "Line 1", firstEncoding);
		try (ReliableTxtStreamWriter writer = new ReliableTxtStreamWriter(filePath, secondEncoding, true)) {
			writer.writeLine("Line 2");
			Assert.equals(writer.encoding, firstEncoding);
		}
		load(filePath, "Line 1\nLine 2", firstEncoding);
	}
	
	private void deleteAppendFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	@Test
	public void constructor_ZeroByteFileGiven() throws IOException, Exception {
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_8);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_32);
	}
	
	private void constructor_ZeroByteFileGiven(ReliableTxtEncoding encoding) throws IOException, Exception {
		String filePath = "Append.txt";
		deleteAppendFile(filePath);
		Files.write(Paths.get(filePath), new byte[] {});
		try (ReliableTxtStreamWriter writer = new ReliableTxtStreamWriter(filePath, encoding, true)) {
			writer.writeLine("Line 1");
		}
		load(filePath, "Line 1", encoding);
	}
	
	@Test
	public void writeLine() throws Exception {
		writeLine(ReliableTxtEncoding.UTF_8);
		writeLine(ReliableTxtEncoding.UTF_16);
		writeLine(ReliableTxtEncoding.UTF_16_REVERSE);
		writeLine(ReliableTxtEncoding.UTF_32);
	}
	
	private void writeLine(ReliableTxtEncoding encoding) throws Exception {
		String filePath = "Test.txt";
		try (ReliableTxtStreamWriter writer = 
			new ReliableTxtStreamWriter(filePath, encoding)) {
			for (int i=1; i<=3; i++) {
				writer.writeLine("Line "+i);
			}
		}
		load(filePath, "Line 1\nLine 2\nLine 3", encoding);
	}
	
	@Test
	public void writeLines() throws Exception {
		String filePath = "Test.txt";
		try (ReliableTxtStreamWriter writer = 
			new ReliableTxtStreamWriter(filePath)) {
			writer.writeLines("Line 1", "Line 2", "Line 3");
		}
		load(filePath, "Line 1\nLine 2\nLine 3", ReliableTxtEncoding.UTF_8);
	}
	
	private void load(String filePath, String text, ReliableTxtEncoding encoding) {
		try {
			ReliableTxtDocument loaded = ReliableTxtDocument.load(filePath);
			Assert.equals(loaded.getEncoding(), encoding);
			Assert.equals(loaded.getText(), text);
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
}
