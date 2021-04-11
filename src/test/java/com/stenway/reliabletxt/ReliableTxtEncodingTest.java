package com.stenway.reliabletxt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class ReliableTxtEncodingTest {

	@Test
	public void values() {
		ReliableTxtEncoding[] expResult = new ReliableTxtEncoding[] {
			ReliableTxtEncoding.UTF_8,
			ReliableTxtEncoding.UTF_16,
			ReliableTxtEncoding.UTF_16_REVERSE,
			ReliableTxtEncoding.UTF_32,
		};
		Assert.equals(ReliableTxtEncoding.values(), expResult);
	}

	@Test
	public void name() {
		Assert.equals(ReliableTxtEncoding.UTF_8.name(),				"UTF_8");
		Assert.equals(ReliableTxtEncoding.UTF_16.name(),			"UTF_16");
		Assert.equals(ReliableTxtEncoding.UTF_16_REVERSE.name(),	"UTF_16_REVERSE");
		Assert.equals(ReliableTxtEncoding.UTF_32.name(),			"UTF_32");
	}
	
	@Test
	public void valueOf() {
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_8"),				ReliableTxtEncoding.UTF_8);
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_16"),			ReliableTxtEncoding.UTF_16);
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_16_REVERSE"),	ReliableTxtEncoding.UTF_16_REVERSE);
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_32"),			ReliableTxtEncoding.UTF_32);
	}
	
	@Test
	public void getCharset() {
		Assert.equals(ReliableTxtEncoding.UTF_8.getCharset(),			StandardCharsets.UTF_8);
		Assert.equals(ReliableTxtEncoding.UTF_16.getCharset(),			StandardCharsets.UTF_16BE);
		Assert.equals(ReliableTxtEncoding.UTF_16_REVERSE.getCharset(),	StandardCharsets.UTF_16LE);
		Assert.equals(ReliableTxtEncoding.UTF_32.getCharset(),			Charset.forName("UTF-32BE"));
	}
	
	@Test
	public void getPreambleLength() {
		Assert.equals(ReliableTxtEncoding.UTF_8.getPreambleLength(),			3);
		Assert.equals(ReliableTxtEncoding.UTF_16.getPreambleLength(),			2);
		Assert.equals(ReliableTxtEncoding.UTF_16_REVERSE.getPreambleLength(),	2);
		Assert.equals(ReliableTxtEncoding.UTF_32.getPreambleLength(),			4);
	}
}