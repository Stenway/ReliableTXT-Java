package com.stenway.reliabletxt;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ReliableTxtDocumentTest {
	
	@Test
	public void test_getCodePoints() {
		getCodePoints_equals(Utils.intArray(0x61, 0xDF, 0x6771, 0x20007));
		
		getCodePoints_equals("aß東\uD840\uDC07", Utils.intArray(0x61, 0xDF, 0x6771, 0x20007));
	}
	
	private void getCodePoints_equals(int[] inputAndExpected) {
		Assert.array_equals(new ReliableTxtDocument(inputAndExpected).getCodePoints(), inputAndExpected);
	}
	
	private void getCodePoints_equals(String text, int[] expected) {
		Assert.array_equals(new ReliableTxtDocument(text).getCodePoints(), expected);
	}
	
	@Test
	public void test_split() {
		split_equals("",					Utils.stringArray(""));
		split_equals("Line1",				Utils.stringArray("Line1"));
		split_equals("Line1\nLine2",		Utils.stringArray("Line1", "Line2"));
		split_equals("Line1\nLine2\nLine3",	Utils.stringArray("Line1", "Line2", "Line3"));
	}
	
	private void split_equals(String text, String[] expectedResult) {
		Assert.array_equals(ReliableTxtDocument.split(text), expectedResult);
		Assert.array_equals(new ReliableTxtDocument(text).getLines(), expectedResult);
	}
	
	@Test
	public void test_join() {
		join_equals(Utils.stringArray(),							"");
		join_equals(Utils.stringArray("Line1"),						"Line1");
		join_equals(Utils.stringArray("Line1", "Line2"),			"Line1\nLine2");
		join_equals(Utils.stringArray("Line1", "Line2", "Line3"),	"Line1\nLine2\nLine3");
	}
	
	private void join_equals(String[] lines, String expectedResult) {
		
		Assert.equals(ReliableTxtDocument.join(lines), expectedResult);
		Assert.equals(new ReliableTxtDocument(lines).toString(), expectedResult);
		
		List<String> linesList = Arrays.asList(lines);
		Assert.equals(ReliableTxtDocument.join(linesList), expectedResult);
		Assert.equals(new ReliableTxtDocument(linesList).toString(), expectedResult);
	}
	
	@Test
	public void test_getBytes() {
		getBytes_equals(new ReliableTxtDocument(),							Utils.byteArray(0xEF,0xBB,0xBF));
		
		getBytes_equals("",													Utils.byteArray(0xEF,0xBB,0xBF));
		getBytes_equals("a",												Utils.byteArray(0xEF,0xBB,0xBF,0x61));
		
		getBytes_equals("", ReliableTxtEncoding.UTF_8,						Utils.byteArray(0xEF,0xBB,0xBF));
		getBytes_equals("a", ReliableTxtEncoding.UTF_8,						Utils.byteArray(0xEF,0xBB,0xBF,0x61));
		getBytes_equals("\u0000", ReliableTxtEncoding.UTF_8,				Utils.byteArray(0xEF,0xBB,0xBF,0x00));
		getBytes_equals("\u00DF", ReliableTxtEncoding.UTF_8,				Utils.byteArray(0xEF,0xBB,0xBF,0xC3,0x9F));
		getBytes_equals("\u6771", ReliableTxtEncoding.UTF_8,				Utils.byteArray(0xEF,0xBB,0xBF,0xE6,0x9D,0xB1));
		getBytes_equals("\uD840\uDC07", ReliableTxtEncoding.UTF_8,			Utils.byteArray(0xEF,0xBB,0xBF,0xF0,0xA0,0x80,0x87));
		
		getBytes_equals("", ReliableTxtEncoding.UTF_16,						Utils.byteArray(0xFE,0xFF));
		getBytes_equals("a", ReliableTxtEncoding.UTF_16,					Utils.byteArray(0xFE,0xFF,0x00,0x61));
		getBytes_equals("\u0000", ReliableTxtEncoding.UTF_16,				Utils.byteArray(0xFE,0xFF,0x00,0x00));
		getBytes_equals("\u00DF", ReliableTxtEncoding.UTF_16,				Utils.byteArray(0xFE,0xFF,0x00,0xDF));
		getBytes_equals("\u6771", ReliableTxtEncoding.UTF_16,				Utils.byteArray(0xFE,0xFF,0x67,0x71));
		getBytes_equals("\uD840\uDC07", ReliableTxtEncoding.UTF_16,			Utils.byteArray(0xFE,0xFF,0xD8,0x40,0xDC,0x07));
		
		getBytes_equals("", ReliableTxtEncoding.UTF_16_REVERSE,				Utils.byteArray(0xFF,0xFE));
		getBytes_equals("a", ReliableTxtEncoding.UTF_16_REVERSE,			Utils.byteArray(0xFF,0xFE,0x61,0x00));
		getBytes_equals("\u0000", ReliableTxtEncoding.UTF_16_REVERSE,		Utils.byteArray(0xFF,0xFE,0x00,0x00));
		getBytes_equals("\u00DF", ReliableTxtEncoding.UTF_16_REVERSE,		Utils.byteArray(0xFF,0xFE,0xDF,0x00));
		getBytes_equals("\u6771", ReliableTxtEncoding.UTF_16_REVERSE,		Utils.byteArray(0xFF,0xFE,0x71,0x67));
		getBytes_equals("\uD840\uDC07", ReliableTxtEncoding.UTF_16_REVERSE,	Utils.byteArray(0xFF,0xFE,0x40,0xD8,0x07,0xDC));
		
		getBytes_equals("", ReliableTxtEncoding.UTF_32,						Utils.byteArray(0x00,0x00,0xFE,0xFF));
		getBytes_equals("a", ReliableTxtEncoding.UTF_32,					Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x61));
		getBytes_equals("\u0000", ReliableTxtEncoding.UTF_32,				Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00));
		getBytes_equals("\u00DF", ReliableTxtEncoding.UTF_32,				Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0xDF));
		getBytes_equals("\u6771", ReliableTxtEncoding.UTF_32,				Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x67,0x71));
		getBytes_equals("\uD840\uDC07", ReliableTxtEncoding.UTF_32,			Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x02,0x00,0x07));
		
		getBytes_equals(Utils.byteArray(0xEF,0xBB,0xBF));
		getBytes_equals(Utils.byteArray(0xEF,0xBB,0xBF,0x61));
		getBytes_equals(Utils.byteArray(0xEF,0xBB,0xBF,0x00));
		getBytes_equals(Utils.byteArray(0xEF,0xBB,0xBF,0xC3,0x9F));
		getBytes_equals(Utils.byteArray(0xEF,0xBB,0xBF,0xE6,0x9D,0xB1));
		getBytes_equals(Utils.byteArray(0xEF,0xBB,0xBF,0xF0,0x90,0x90,0x80));
		
		getBytes_equals(Utils.byteArray(0xFE,0xFF));
		getBytes_equals(Utils.byteArray(0xFE,0xFF,0x00,0x61));
		getBytes_equals(Utils.byteArray(0xFE,0xFF,0x00,0x00));
		getBytes_equals(Utils.byteArray(0xFE,0xFF,0x00,0xDF));
		getBytes_equals(Utils.byteArray(0xFE,0xFF,0x67,0x71));
		getBytes_equals(Utils.byteArray(0xFE,0xFF,0xD8,0x40,0xDC,0x07));
		
		getBytes_equals(Utils.byteArray(0xFF,0xFE));
		getBytes_equals(Utils.byteArray(0xFF,0xFE,0x61,0x00));
		getBytes_equals(Utils.byteArray(0xFF,0xFE,0x00,0x00));
		getBytes_equals(Utils.byteArray(0xFF,0xFE,0xDF,0x00));
		getBytes_equals(Utils.byteArray(0xFF,0xFE,0x71,0x67));
		getBytes_equals(Utils.byteArray(0xFF,0xFE,0x40,0xD8,0x07,0xDC));
		
		getBytes_equals(Utils.byteArray(0x00,0x00,0xFE,0xFF));
		getBytes_equals(Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x61));
		getBytes_equals(Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00));
		getBytes_equals(Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0xDF));
		getBytes_equals(Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x67,0x71));
		getBytes_equals(Utils.byteArray(0x00,0x00,0xFE,0xFF,0x00,0x02,0x00,0x07));
	}
	
	private void getBytes_equals(ReliableTxtDocument document, byte[] expectedBytes) {
		Assert.array_equals(document.getBytes(), expectedBytes);
	}
	
	private void getBytes_equals(String str, byte[] expectedBytes) {
		Assert.array_equals(new ReliableTxtDocument(str).getBytes(), expectedBytes);
	}
	
	private void getBytes_equals(String str, ReliableTxtEncoding encoding, byte[] expectedBytes) {
		Assert.array_equals(new ReliableTxtDocument(str,encoding).getBytes(), expectedBytes);
	}
	
	private void getBytes_equals(byte[] inputAndExpected) {
		Assert.array_equals(new ReliableTxtDocument(inputAndExpected).getBytes(), inputAndExpected);
	}
}
