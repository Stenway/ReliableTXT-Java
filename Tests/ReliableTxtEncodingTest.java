package com.stenway.reliabletxt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class ReliableTxtEncodingTest {

	@Test
	public void test_values() {
		ReliableTxtEncoding[] expResult = new ReliableTxtEncoding[] {
			ReliableTxtEncoding.UTF_8,
			ReliableTxtEncoding.UTF_16,
			ReliableTxtEncoding.UTF_16_REVERSE,
			ReliableTxtEncoding.UTF_32,
		};
		Assert.array_equals(ReliableTxtEncoding.values(), expResult);
	}

	@Test
	public void test_name() {
		Assert.equals(ReliableTxtEncoding.UTF_8.name(),				"UTF_8");
		Assert.equals(ReliableTxtEncoding.UTF_16.name(),			"UTF_16");
		Assert.equals(ReliableTxtEncoding.UTF_16_REVERSE.name(),	"UTF_16_REVERSE");
		Assert.equals(ReliableTxtEncoding.UTF_32.name(),			"UTF_32");
	}
	
	@Test
	public void test_valueOf() {
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_8"),				ReliableTxtEncoding.UTF_8);
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_16"),			ReliableTxtEncoding.UTF_16);
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_16_REVERSE"),	ReliableTxtEncoding.UTF_16_REVERSE);
		Assert.equals(ReliableTxtEncoding.valueOf("UTF_32"),			ReliableTxtEncoding.UTF_32);
	}
	
	@Test
	public void test_getCharset() {
		Assert.equals(ReliableTxtEncoding.UTF_8.getCharset(),			StandardCharsets.UTF_8);
		Assert.equals(ReliableTxtEncoding.UTF_16.getCharset(),			StandardCharsets.UTF_16BE);
		Assert.equals(ReliableTxtEncoding.UTF_16_REVERSE.getCharset(),	StandardCharsets.UTF_16LE);
		Assert.equals(ReliableTxtEncoding.UTF_32.getCharset(),			Charset.forName("UTF-32BE"));
	}
	
	@Test
	public void test_getPreambleLength() {
		Assert.equals(ReliableTxtEncoding.UTF_8.getPreambleLength(),			3);
		Assert.equals(ReliableTxtEncoding.UTF_16.getPreambleLength(),			2);
		Assert.equals(ReliableTxtEncoding.UTF_16_REVERSE.getPreambleLength(),	2);
		Assert.equals(ReliableTxtEncoding.UTF_32.getPreambleLength(),			4);
	}
	
	@Test
	public void test_fromBytes() {
		Assert.equals(ReliableTxtEncoding.fromBytes(Utils.byteArray(0xEF,0xBB,0xBF)),		ReliableTxtEncoding.UTF_8);
		Assert.equals(ReliableTxtEncoding.fromBytes(Utils.byteArray(0xFE,0xFF)),			ReliableTxtEncoding.UTF_16);
		Assert.equals(ReliableTxtEncoding.fromBytes(Utils.byteArray(0xFF,0xFE)),			ReliableTxtEncoding.UTF_16_REVERSE);
		Assert.equals(ReliableTxtEncoding.fromBytes(Utils.byteArray(0x00,0x00,0xFE,0xFF)),	ReliableTxtEncoding.UTF_32);
	}
	
	@Test
	public void test_fromBytes_invalidPreamble() {
		test_fromBytes_invalidPreamble(Utils.byteArray());
		test_fromBytes_invalidPreamble(Utils.byteArray(0xEF));
		test_fromBytes_invalidPreamble(Utils.byteArray(0xEF,0xBB));
		test_fromBytes_invalidPreamble(Utils.byteArray(0xFE));
		test_fromBytes_invalidPreamble(Utils.byteArray(0xFF));
		test_fromBytes_invalidPreamble(Utils.byteArray(0x00));
		test_fromBytes_invalidPreamble(Utils.byteArray(0x00,0x00));
		test_fromBytes_invalidPreamble(Utils.byteArray(0x00,0x00,0xFE));
	}
	
	private void test_fromBytes_invalidPreamble(byte[] bytes) {
		try {
			ReliableTxtEncoding.fromBytes(bytes);
		} catch (ReliableTxtException e) {
			return;
		}
		throw new RuntimeException("Preamble is valid");
	}
	
	@Test(expected=NullPointerException.class)
	public void test_fromBytes_nullArgument() {
		ReliableTxtEncoding.fromBytes(null);
	}
}