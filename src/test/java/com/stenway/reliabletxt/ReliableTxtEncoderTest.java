package com.stenway.reliabletxt;

import org.junit.Test;

public class ReliableTxtEncoderTest {
	@Test
	public void encode() {
		encode("", ReliableTxtEncoding.UTF_8,						byteArray(0xEF,0xBB,0xBF));
		encode("a", ReliableTxtEncoding.UTF_8,						byteArray(0xEF,0xBB,0xBF,0x61));
		encode("\u0000", ReliableTxtEncoding.UTF_8,					byteArray(0xEF,0xBB,0xBF,0x00));
		encode("\u00DF", ReliableTxtEncoding.UTF_8,					byteArray(0xEF,0xBB,0xBF,0xC3,0x9F));
		encode("\u6771", ReliableTxtEncoding.UTF_8,					byteArray(0xEF,0xBB,0xBF,0xE6,0x9D,0xB1));
		encode("\uD840\uDC07", ReliableTxtEncoding.UTF_8,			byteArray(0xEF,0xBB,0xBF,0xF0,0xA0,0x80,0x87));
		
		encode("", ReliableTxtEncoding.UTF_16,						byteArray(0xFE,0xFF));
		encode("a", ReliableTxtEncoding.UTF_16,						byteArray(0xFE,0xFF,0x00,0x61));
		encode("\u0000", ReliableTxtEncoding.UTF_16,				byteArray(0xFE,0xFF,0x00,0x00));
		encode("\u00DF", ReliableTxtEncoding.UTF_16,				byteArray(0xFE,0xFF,0x00,0xDF));
		encode("\u6771", ReliableTxtEncoding.UTF_16,				byteArray(0xFE,0xFF,0x67,0x71));
		encode("\uD840\uDC07", ReliableTxtEncoding.UTF_16,			byteArray(0xFE,0xFF,0xD8,0x40,0xDC,0x07));
		
		encode("", ReliableTxtEncoding.UTF_16_REVERSE,				byteArray(0xFF,0xFE));
		encode("a", ReliableTxtEncoding.UTF_16_REVERSE,				byteArray(0xFF,0xFE,0x61,0x00));
		encode("\u0000", ReliableTxtEncoding.UTF_16_REVERSE,		byteArray(0xFF,0xFE,0x00,0x00));
		encode("\u00DF", ReliableTxtEncoding.UTF_16_REVERSE,		byteArray(0xFF,0xFE,0xDF,0x00));
		encode("\u6771", ReliableTxtEncoding.UTF_16_REVERSE,		byteArray(0xFF,0xFE,0x71,0x67));
		encode("\uD840\uDC07", ReliableTxtEncoding.UTF_16_REVERSE,	byteArray(0xFF,0xFE,0x40,0xD8,0x07,0xDC));
		
		encode("", ReliableTxtEncoding.UTF_32,						byteArray(0x00,0x00,0xFE,0xFF));
		encode("a", ReliableTxtEncoding.UTF_32,						byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x61));
		encode("\u0000", ReliableTxtEncoding.UTF_32,				byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00));
		encode("\u00DF", ReliableTxtEncoding.UTF_32,				byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0xDF));
		encode("\u6771", ReliableTxtEncoding.UTF_32,				byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x67,0x71));
		encode("\uD840\uDC07", ReliableTxtEncoding.UTF_32,			byteArray(0x00,0x00,0xFE,0xFF,0x00,0x02,0x00,0x07));
	}
	
	private void encode(String str, ReliableTxtEncoding encoding, byte[] expectedBytes) {
		Assert.equals(ReliableTxtEncoder.encode(str, encoding), expectedBytes);
	}
	
	@Test
	public void encode_InvalidSurrogateGiven_ShouldThrowException() {
		encode_InvalidSurrogateGiven_ShouldThrowException("\uD840\u0061", ReliableTxtEncoding.UTF_8);
		encode_InvalidSurrogateGiven_ShouldThrowException("\uD840\u0061", ReliableTxtEncoding.UTF_16);
		encode_InvalidSurrogateGiven_ShouldThrowException("\uD840\u0061", ReliableTxtEncoding.UTF_16_REVERSE);
		encode_InvalidSurrogateGiven_ShouldThrowException("\uD840\u0061", ReliableTxtEncoding.UTF_32);
		
		encode_InvalidSurrogateGiven_ShouldThrowException("\uDC07\uD840", ReliableTxtEncoding.UTF_8);
		encode_InvalidSurrogateGiven_ShouldThrowException("\uDC07\uD840", ReliableTxtEncoding.UTF_16);
		encode_InvalidSurrogateGiven_ShouldThrowException("\uDC07\uD840", ReliableTxtEncoding.UTF_16_REVERSE);
		encode_InvalidSurrogateGiven_ShouldThrowException("\uDC07\uD840", ReliableTxtEncoding.UTF_32);
	}
	
	private void encode_InvalidSurrogateGiven_ShouldThrowException(String str, ReliableTxtEncoding encoding) {
		try {
			ReliableTxtEncoder.encode(str, encoding);
		} catch (Exception e) {
			return;
		}
		throw new RuntimeException("String is valid");
	}
	
	private static byte[] byteArray(int... values) {
		byte[] bytes = new byte[values.length];
		for (int i=0; i<values.length; i++) {
			bytes[i] = (byte)values[i];
		}
		return bytes;
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		ReliableTxtEncoder encoder = new ReliableTxtEncoder();
	}
}
