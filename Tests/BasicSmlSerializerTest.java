package com.stenway.reliabletxt;

import org.junit.Test;

public class BasicSmlSerializerTest {
	@Test
	public void test_serializeDocument() {
		serializeDocument_equals("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", true, "MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50 60\n-\nMyThirdAttribute \"Hello world\"\n-");
		serializeDocument_equals("# My SML document\nMyRootElement\n  Group1\n    MyFirstAttribute  123\n    MySecondAttribute 10 20  30 40  50 60\n  End\n  \n  MyThirdAttribute \"Hello world\"\n  #MyForthAttribute Test\nEnd\n# End of document", false, "MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50 60\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
	}
	
	private void serializeDocument_equals(String text, boolean minify, String expected) {
		Assert.equals(BasicSmlSerializer.serializeDocument(BasicSmlParser.parseDocument(text), minify), expected);
	}
}
