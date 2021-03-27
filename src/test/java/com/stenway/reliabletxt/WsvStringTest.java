package com.stenway.reliabletxt;

import org.junit.Test;

public class WsvStringTest {
	
	@Test
	public void test_isWhitespace() {
		var wsCodePoints = WsvChar.getWhitespaceCodePoints();
		Assert.isTrue(WsvString.isWhitespace(new String(wsCodePoints, 0, wsCodePoints.length)));
	}
	
	@Test
	public void test_isWhitespace_false() {
		Assert.isFalse(WsvString.isWhitespace(""));
		Assert.isFalse(WsvString.isWhitespace(" a "));
	}
}
