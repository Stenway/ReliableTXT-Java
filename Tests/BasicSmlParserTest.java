package com.stenway.reliabletxt;

import org.junit.Test;

public class BasicSmlParserTest {
	@Test
	public void test_parseDocument() {
		parseDocument_equals("MyRootElement\nEnd");
		parseDocument_equals("myrootelement\nend");
		parseDocument_equals("MYROOTELEMENT\nEND");
		
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute 123\nEnd");
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parseDocument_equals("MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute \"Hello \"\"world\"\"!\"\n\tMySecondAttribute c:\\Temp\\Readme.txt\nEnd");
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute \"# This is not a comment\"\nEnd");
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute \"-\"\n\tMySecondAttribute -\n\tMyThirdAttribute \"\"\n\tMyFourthAttribute My-Value-123\nEnd");
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute \"Line1\"/\"Line2\"/\"Line3\"\nEnd");
		
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute 123\n\tMyFirstAttribute 3456\n\tMyFirstAttribute 67\n\tElement1\n\tEnd\n\tElement1\n\tEnd\nEnd");
		parseDocument_equals("RecentFiles\n\tFile c:\\Temp\\Readme.txt\n\tFile \"c:\\My Files\\Todo.txt\"\n\tFile c:\\Games\\Racer\\Config.sml\n\tFile d:\\Untitled.txt\nEnd");
		
		parseDocument_equals("Root\n-");
		parseDocument_equals("Root\n\tEnd 12 13\nEnd");
		
		parseDocument_equals("契約\n\t個人情報\n\t\t名字 田中\n\t\t名前 蓮\n\tエンド\n\t日付 ２０２１－０１－０２\nエンド");
		parseDocument_equals("Vertragsdaten\n\tPersonendaten\n\t\tNachname Meier\n\t\tVorname Hans\n\tEnde\n\tDatum 2021-01-02\nEnde");
		
		parseDocument_equals("\"My Root Element\"\n\t\"My First Attribute\" 123\nEnd");
		
		parseDocument_equals("Actors\n\tName Age PlaceOfBirth FavoriteColor JobTitle\n\t\"John Smith\" 33 Vancouver -\n\t\"Mary Smith\" 27 Toronto Green Lawyer\nEnd");
		
		parseDocument_equals("MyRootElement\n\tMyFirstAttribute \"123\"\n\tMySecondAttribute \"10\" \"20\" \"30\" \"40\" \"50\"\nEnd", "MyRootElement\n\tMyFirstAttribute 123\n\tMySecondAttribute 10 20 30 40 50\nEnd");
		parseDocument_equals("MyRootElement\nGroup1\nMyFirstAttribute 123\nMySecondAttribute 10 20 30 40 50\nEnd\nMyThirdAttribute \"Hello world\"\nEnd", "MyRootElement\n\tGroup1\n\t\tMyFirstAttribute 123\n\t\tMySecondAttribute 10 20 30 40 50\n\tEnd\n\tMyThirdAttribute \"Hello world\"\nEnd");
		parseDocument_equals("# My first SML document\nMyRootElement\n\t#Group1\n\t#\tMyFirstAttribute 123\n\t#\tMySecondAttribute 10 20 30 40 50\n\t#End\n\tMyThirdAttribute \"Hello world\" # Comment\nEnd", "MyRootElement\n\tMyThirdAttribute \"Hello world\"\nEnd");
	}
	
	private void parseDocument_equals(String textAndExpected) {
		parseDocument_equals(textAndExpected, textAndExpected);
	}
	
	private void parseDocument_equals(String text, String expected) {
		Assert.equals(BasicSmlSerializer.serializeDocument(BasicSmlParser.parseDocument(text)), expected);
	}
	
	@Test
	public void test_parseDocument_exceptions() {
		parseDocument_throws_wsvException("Root\n  FirstAttribute \"hello world\nEnd",			"String not closed (2, 30)");
		parseDocument_throws_wsvException("Root\n  FirstAttribute ab\"c\nEnd",					"Invalid double quote in value (2, 20)");
		parseDocument_throws_wsvException("Root\n  FirstAttribute \"hello world\"a b c\nEnd",	"Invalid character after string (2, 31)");
		parseDocument_throws_wsvException("Root\n  FirstAttribute \"Line1\"/ \"Line2\"\nEnd",	"Invalid string line break (2, 26)");
		
		parseDocument_throws_smlException("# Only\n# Comments",		"End keyword could not be detected (2)");
		parseDocument_throws_smlException("Root abc\nEnd",			"Invalid root element start (1)");
		parseDocument_throws_smlException("-\nEnd",					"Null value as element name is not allowed (1)");
		parseDocument_throws_smlException("Root\n  -\n  End\nEnd",	"Null value as element name is not allowed (2)");
		parseDocument_throws_smlException("Root\n  - 123\nEnd",		"Null value as attribute name is not allowed (2)");
		parseDocument_throws_smlException("Root\n  Element\n  End",	"Element \"Root\" not closed (3)");
		parseDocument_throws_smlException("Root\nEnd\nRoot2\nEnd",	"Only one root element allowed (3)");
		
	}
	
	private void parseDocument_throws_smlException(String text, String expectedExceptionMessage) {
		try {
			BasicSmlParser.parseDocument(text);
		} catch (BasicSmlParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Text is valid");
	}
	
	private void parseDocument_throws_wsvException(String text, String expectedExceptionMessage) {
		try {
			BasicSmlParser.parseDocument(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Text is valid");
	}
}
