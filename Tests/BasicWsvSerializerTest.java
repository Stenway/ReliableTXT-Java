package com.stenway.reliabletxt;

import org.junit.Test;

public class BasicWsvSerializerTest {
	@Test
	public void test_serialize() {
		serialize_equals("  a  b  c  #c", "a b c");
	}
	
	private void serialize_equals(String content, String expectedResult) {
		Assert.equals(BasicWsvSerializer.serialize(BasicWsvParser.parseDocument(content)), expectedResult);
	}
}
