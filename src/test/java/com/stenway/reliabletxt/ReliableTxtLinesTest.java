package com.stenway.reliabletxt;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ReliableTxtLinesTest {
	
	@Test
	public void split() {
		split("",						stringArray(""));
		split("Line1",					stringArray("Line1"));
		split("Line1\nLine2",			stringArray("Line1", "Line2"));
		split("Line1\nLine2\nLine3",	stringArray("Line1", "Line2", "Line3"));
	}
	
	private void split(String text, String[] expectedResult) {
		Assert.equals(ReliableTxtLines.split(text), expectedResult);
	}
	
	@Test
	public void join() {
		join(stringArray(),								"");
		join(stringArray("Line1"),						"Line1");
		join(stringArray("Line1", "Line2"),				"Line1\nLine2");
		join(stringArray("Line1", "Line2", "Line3"),	"Line1\nLine2\nLine3");
	}
	
	private void join(String[] lines, String expectedResult) {
		Assert.equals(ReliableTxtLines.join(lines), expectedResult);
				
		List<String> linesList = Arrays.asList(lines);
		Assert.equals(ReliableTxtLines.join(linesList), expectedResult);
	}
	
	private static String[] stringArray(String... values) {
		return values;
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		ReliableTxtLines encoder = new ReliableTxtLines();
	}
}
