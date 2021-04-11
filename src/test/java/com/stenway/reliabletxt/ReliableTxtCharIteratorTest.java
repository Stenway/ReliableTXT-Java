package com.stenway.reliabletxt;

import org.junit.Test;

public class ReliableTxtCharIteratorTest {

	@Test(expected=NullPointerException.class)
	public void constructor_NullGiven_ShouldThrowException() {
		new ReliableTxtCharIterator(null);
	}
	
	@Test
	public void getText_ShouldEqualConstructorArgument() {
		getText_ShouldEqualConstructorArgument("");
		getText_ShouldEqualConstructorArgument("abc");
	}
	
	private void getText_ShouldEqualConstructorArgument(String text) {
		Assert.equals(new ReliableTxtCharIterator(text).getText(), text);
	}
	
	@Test
	public void getLineInfo() {
		Assert.equals(newIterator("").getLineInfo(),							intArray(0, 0));
		
		Assert.equals(newIterator("abc").getLineInfo(),							intArray(0, 0));
		Assert.equals(newIterator("abc", 'a').getLineInfo(),					intArray(0, 1));
		Assert.equals(newIterator("abc", 'a', 'b').getLineInfo(),				intArray(0, 2));
		Assert.equals(newIterator("abc", 'a', 'b', 'c').getLineInfo(),			intArray(0, 3));
		
		Assert.equals(newIterator("a\nb").getLineInfo(),						intArray(0, 0));
		Assert.equals(newIterator("a\nb", 'a').getLineInfo(),					intArray(0, 1));
		Assert.equals(newIterator("a\nb", 'a', '\n').getLineInfo(),				intArray(1, 0));
		Assert.equals(newIterator("a\nb", 'a', '\n', 'b').getLineInfo(),		intArray(1, 1));
		
		Assert.equals(newIterator("\n\n\n").getLineInfo(),						intArray(0, 0));
		Assert.equals(newIterator("\n\n\n", '\n').getLineInfo(),				intArray(1, 0));
		Assert.equals(newIterator("\n\n\n", '\n', '\n').getLineInfo(),			intArray(2, 0));
		Assert.equals(newIterator("\n\n\n", '\n', '\n', '\n').getLineInfo(),	intArray(3, 0));
		
		Assert.equals(newIterator("a\uD840\uDC07\nb").getLineInfo(),						intArray(0, 0));
		Assert.equals(newIterator("a\uD840\uDC07\nb", 'a').getLineInfo(),					intArray(0, 1));
		Assert.equals(newIterator("a\uD840\uDC07\nb", 'a', 0x20007).getLineInfo(),			intArray(0, 2));
		Assert.equals(newIterator("a\uD840\uDC07\nb", 'a', 0x20007, '\n').getLineInfo(),		intArray(1, 0));
	}
		
	private static int[] intArray(int... values) {
		return values;
	}
	
	@Test
	public void isEndOfText() {
		Assert.equals(newIterator("").isEndOfText(),						true);
		
		Assert.equals(newIterator("abc").isEndOfText(),						false);
		Assert.equals(newIterator("abc", 'a').isEndOfText(),				false);
		Assert.equals(newIterator("abc", 'a', 'b').isEndOfText(),			false);
		Assert.equals(newIterator("abc", 'a', 'b', 'c').isEndOfText(),		true);
		
		Assert.equals(newIterator("a\nb").isEndOfText(),					false);
		Assert.equals(newIterator("a\nb", 'a').isEndOfText(),				false);
		Assert.equals(newIterator("a\nb", 'a', '\n').isEndOfText(),			false);
		Assert.equals(newIterator("a\nb", 'a', '\n', 'b').isEndOfText(),	true);
	}
	
	@Test
	public void isChar() {
		Assert.equals(newIterator("").isChar('a'),						false);
		
		Assert.equals(newIterator("abc").isChar('a'),					true);
		Assert.equals(newIterator("abc").isChar('b'),					false);
		Assert.equals(newIterator("abc", 'a').isChar('a'),				false);
		Assert.equals(newIterator("abc", 'a').isChar('b'),				true);
		Assert.equals(newIterator("abc", 'a', 'b').isChar('c'),			true);
		Assert.equals(newIterator("abc", 'a', 'b', 'c').isChar('c'),	false);
	}
	
	@Test
	public void tryReadChar() {
		ReliableTxtCharIterator iterator = new ReliableTxtCharIterator("abc");
		
		Assert.equals(iterator.tryReadChar('b'),	false);
		Assert.equals(iterator.getLineInfo(),		intArray(0, 0));
		
		Assert.equals(iterator.tryReadChar('a'),	true);
		Assert.equals(iterator.getLineInfo(),		intArray(0, 1));
	}
	
	private ReliableTxtCharIterator newIterator(String text, int... chars) {
		ReliableTxtCharIterator charIterator = new ReliableTxtCharIterator(text);
		for (int c : chars) {
			if (!charIterator.tryReadChar(c)) throw new RuntimeException();
		}
		return charIterator;
	}
}