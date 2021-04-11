package com.stenway.reliabletxt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ReliableTxtDocumentTest {
	
	@Test
	public void getText() {
		getText("");
		getText("abc");
		getText("aß東\uD840\uDC07");
	}
	
	private void getText(String text) {
		Assert.equals(new ReliableTxtDocument(text).getText(), text);
	}
	
	@Test
	public void getEncoding() {
		Assert.equals(new ReliableTxtDocument("abc").getEncoding(), ReliableTxtEncoding.UTF_8);
		
		getEncoding(ReliableTxtEncoding.UTF_8);
		getEncoding(ReliableTxtEncoding.UTF_16);
		getEncoding(ReliableTxtEncoding.UTF_16_REVERSE);
		getEncoding(ReliableTxtEncoding.UTF_32);
	}
		
	private void getEncoding(ReliableTxtEncoding encoding) {
		Assert.equals(new ReliableTxtDocument("abc", encoding).getEncoding(), encoding);
	}
	
	@Test
	public void getCodePoints() {
		getCodePoints(intArray(0x61, 0xDF, 0x6771, 0x20007));
		
		getCodePoints("aß東\uD840\uDC07", intArray(0x61, 0xDF, 0x6771, 0x20007));
	}
	
	private void getCodePoints(int[] inputAndExpected) {
		Assert.equals(new ReliableTxtDocument(inputAndExpected).getCodePoints(), inputAndExpected);
	}
	
	private void getCodePoints(String text, int[] expected) {
		Assert.equals(new ReliableTxtDocument(text).getCodePoints(), expected);
	}
	
	@Test
	public void getLines() {
		getLines("",					stringArray(""));
		getLines("Line1",				stringArray("Line1"));
		getLines("Line1\nLine2",		stringArray("Line1", "Line2"));
		getLines("Line1\nLine2\nLine3",	stringArray("Line1", "Line2", "Line3"));
	}
	
	private void getLines(String text, String[] expectedResult) {
		Assert.equals(new ReliableTxtDocument(text).getLines(), expectedResult);
	}
	
	@Test
	public void constructor_LinesGiven() {
		constructor_LinesGiven(stringArray(),							"");
		constructor_LinesGiven(stringArray("Line1"),					"Line1");
		constructor_LinesGiven(stringArray("Line1", "Line2"),			"Line1\nLine2");
		constructor_LinesGiven(stringArray("Line1", "Line2", "Line3"),	"Line1\nLine2\nLine3");
	}
	
	private void constructor_LinesGiven(String[] lines, String expectedResult) {
		Assert.equals(new ReliableTxtDocument(lines).toString(), expectedResult);
		
		List<String> linesList = Arrays.asList(lines);
		Assert.equals(new ReliableTxtDocument(linesList).toString(), expectedResult);
	}
	
	@Test
	public void getBytes() {
		getBytes(new ReliableTxtDocument(),								byteArray(0xEF,0xBB,0xBF));
		
		getBytes("",													byteArray(0xEF,0xBB,0xBF));
		getBytes("a",													byteArray(0xEF,0xBB,0xBF,0x61));
		
		getBytes("", ReliableTxtEncoding.UTF_8,							byteArray(0xEF,0xBB,0xBF));
		getBytes("a", ReliableTxtEncoding.UTF_8,						byteArray(0xEF,0xBB,0xBF,0x61));
		getBytes("\u0000", ReliableTxtEncoding.UTF_8,					byteArray(0xEF,0xBB,0xBF,0x00));
		getBytes("\u00DF", ReliableTxtEncoding.UTF_8,					byteArray(0xEF,0xBB,0xBF,0xC3,0x9F));
		getBytes("\u6771", ReliableTxtEncoding.UTF_8,					byteArray(0xEF,0xBB,0xBF,0xE6,0x9D,0xB1));
		getBytes("\uD840\uDC07", ReliableTxtEncoding.UTF_8,				byteArray(0xEF,0xBB,0xBF,0xF0,0xA0,0x80,0x87));
		
		getBytes("", ReliableTxtEncoding.UTF_16,						byteArray(0xFE,0xFF));
		getBytes("a", ReliableTxtEncoding.UTF_16,						byteArray(0xFE,0xFF,0x00,0x61));
		getBytes("\u0000", ReliableTxtEncoding.UTF_16,					byteArray(0xFE,0xFF,0x00,0x00));
		getBytes("\u00DF", ReliableTxtEncoding.UTF_16,					byteArray(0xFE,0xFF,0x00,0xDF));
		getBytes("\u6771", ReliableTxtEncoding.UTF_16,					byteArray(0xFE,0xFF,0x67,0x71));
		getBytes("\uD840\uDC07", ReliableTxtEncoding.UTF_16,			byteArray(0xFE,0xFF,0xD8,0x40,0xDC,0x07));
		
		getBytes("", ReliableTxtEncoding.UTF_16_REVERSE,				byteArray(0xFF,0xFE));
		getBytes("a", ReliableTxtEncoding.UTF_16_REVERSE,				byteArray(0xFF,0xFE,0x61,0x00));
		getBytes("\u0000", ReliableTxtEncoding.UTF_16_REVERSE,			byteArray(0xFF,0xFE,0x00,0x00));
		getBytes("\u00DF", ReliableTxtEncoding.UTF_16_REVERSE,			byteArray(0xFF,0xFE,0xDF,0x00));
		getBytes("\u6771", ReliableTxtEncoding.UTF_16_REVERSE,			byteArray(0xFF,0xFE,0x71,0x67));
		getBytes("\uD840\uDC07", ReliableTxtEncoding.UTF_16_REVERSE,	byteArray(0xFF,0xFE,0x40,0xD8,0x07,0xDC));
		
		getBytes("", ReliableTxtEncoding.UTF_32,						byteArray(0x00,0x00,0xFE,0xFF));
		getBytes("a", ReliableTxtEncoding.UTF_32,						byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x61));
		getBytes("\u0000", ReliableTxtEncoding.UTF_32,					byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00));
		getBytes("\u00DF", ReliableTxtEncoding.UTF_32,					byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0xDF));
		getBytes("\u6771", ReliableTxtEncoding.UTF_32,					byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x67,0x71));
		getBytes("\uD840\uDC07", ReliableTxtEncoding.UTF_32,			byteArray(0x00,0x00,0xFE,0xFF,0x00,0x02,0x00,0x07));
		
		getBytes(byteArray(0xEF,0xBB,0xBF));
		getBytes(byteArray(0xEF,0xBB,0xBF,0x61));
		getBytes(byteArray(0xEF,0xBB,0xBF,0x00));
		getBytes(byteArray(0xEF,0xBB,0xBF,0xC3,0x9F));
		getBytes(byteArray(0xEF,0xBB,0xBF,0xE6,0x9D,0xB1));
		getBytes(byteArray(0xEF,0xBB,0xBF,0xF0,0x90,0x90,0x80));
		
		getBytes(byteArray(0xFE,0xFF));
		getBytes(byteArray(0xFE,0xFF,0x00,0x61));
		getBytes(byteArray(0xFE,0xFF,0x00,0x00));
		getBytes(byteArray(0xFE,0xFF,0x00,0xDF));
		getBytes(byteArray(0xFE,0xFF,0x67,0x71));
		getBytes(byteArray(0xFE,0xFF,0xD8,0x40,0xDC,0x07));
		
		getBytes(byteArray(0xFF,0xFE));
		getBytes(byteArray(0xFF,0xFE,0x61,0x00));
		getBytes(byteArray(0xFF,0xFE,0x00,0x00));
		getBytes(byteArray(0xFF,0xFE,0xDF,0x00));
		getBytes(byteArray(0xFF,0xFE,0x71,0x67));
		getBytes(byteArray(0xFF,0xFE,0x40,0xD8,0x07,0xDC));
		
		getBytes(byteArray(0x00,0x00,0xFE,0xFF));
		getBytes(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x61));
		getBytes(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00));
		getBytes(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0xDF));
		getBytes(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x67,0x71));
		getBytes(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x02,0x00,0x07));
	}
	
	private void getBytes(ReliableTxtDocument document, byte[] expectedBytes) {
		Assert.equals(document.getBytes(), expectedBytes);
	}
	
	private void getBytes(String str, byte[] expectedBytes) {
		Assert.equals(new ReliableTxtDocument(str).getBytes(), expectedBytes);
	}
	
	private void getBytes(String str, ReliableTxtEncoding encoding, byte[] expectedBytes) {
		Assert.equals(new ReliableTxtDocument(str,encoding).getBytes(), expectedBytes);
	}
	
	private void getBytes(byte[] inputAndExpected) {
		Assert.equals(new ReliableTxtDocument(inputAndExpected).getBytes(), inputAndExpected);
	}
	
	@Test
	public void save() {
		save("");
		save("abc");
		save("aß東\uD840\uDC07");
		
		save("", ReliableTxtEncoding.UTF_8);
		save("", ReliableTxtEncoding.UTF_16);
		save("", ReliableTxtEncoding.UTF_16_REVERSE);
		save("", ReliableTxtEncoding.UTF_32);
		
		save("abc", ReliableTxtEncoding.UTF_8);
		save("abc", ReliableTxtEncoding.UTF_16);
		save("abc", ReliableTxtEncoding.UTF_16_REVERSE);
		save("abc", ReliableTxtEncoding.UTF_32);
		
		save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_8);
		save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_16);
		save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_16_REVERSE);
		save("aß東\uD840\uDC07", ReliableTxtEncoding.UTF_32);
	}
	
	private void save(String text, ReliableTxtEncoding encoding) {
		try {
			String filePath = "Test.txt";
			ReliableTxtDocument loaded = null;
			
			new ReliableTxtDocument(text, encoding).save(filePath);
			load(filePath, text, encoding);
			
			ReliableTxtDocument.save(text, encoding, filePath);
			load(filePath, text, encoding);
			
			ReliableTxtDocument.save(text.codePoints().toArray(), encoding, filePath);
			load(filePath, text, encoding);
			
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	private void save(String text) {
		try {
			String filePath = "Test.txt";
			ReliableTxtDocument loaded = null;
			
			new ReliableTxtDocument(text).save(filePath);
			load(filePath, text, ReliableTxtEncoding.UTF_8);
			
			ReliableTxtDocument.save(text, filePath);
			load(filePath, text, ReliableTxtEncoding.UTF_8);
			
			ReliableTxtDocument.save(text.codePoints().toArray(), filePath);
			load(filePath, text, ReliableTxtEncoding.UTF_8);
			
		} catch (IOException ex) {
			throw new RuntimeException();
		}
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
	
	private static int[] intArray(int... values) {
		return values;
	}
	
	private static String[] stringArray(String... values) {
		return values;
	}
	
	private static byte[] byteArray(int... values) {
		byte[] bytes = new byte[values.length];
		for (int i=0; i<values.length; i++) {
			bytes[i] = (byte)values[i];
		}
		return bytes;
	}
}
