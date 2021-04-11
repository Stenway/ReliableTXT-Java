package com.stenway.reliabletxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class ReliableTxtDecoderTest {

	@Test
	public void getEncoding() {
		Assert.equals(ReliableTxtDecoder.getEncoding(byteArray(0xEF,0xBB,0xBF)),		ReliableTxtEncoding.UTF_8);
		Assert.equals(ReliableTxtDecoder.getEncoding(byteArray(0xFE,0xFF)),				ReliableTxtEncoding.UTF_16);
		Assert.equals(ReliableTxtDecoder.getEncoding(byteArray(0xFF,0xFE)),				ReliableTxtEncoding.UTF_16_REVERSE);
		Assert.equals(ReliableTxtDecoder.getEncoding(byteArray(0x00,0x00,0xFE,0xFF)),	ReliableTxtEncoding.UTF_32);
	}
	
	@Test
	public void getEncoding_InvalidPreambleGiven_ShouldThrowException() {
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray());
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEE));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBC));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBB));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBB,0xBE));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBC,0xBF));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE,0xFE));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF,0xFF));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00,0xFE));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00,0xFE,0xFE));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x01,0x00,0xFE,0xFF));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x01,0xFE,0xFF));
		getEncoding_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00,0xFF,0xFE));
	}
	
	private void getEncoding_InvalidPreambleGiven_ShouldThrowException(byte[] bytes) {
		try {
			ReliableTxtDecoder.getEncoding(bytes);
		} catch (ReliableTxtException e) {
			Assert.equals(e.getMessage(), "Document does not have a ReliableTXT preamble");
			return;
		}
		throw new RuntimeException("Preamble is valid");
	}
	
	@Test(expected=NullPointerException.class)
	public void getEncoding_NullGiven_ShouldThrowException() {
		ReliableTxtDecoder.getEncoding(null);
	}
	
	@Test
	public void getEncodingFromFile() throws IOException {
		Assert.equals(getEncodingFromFile(byteArray(0xEF,0xBB,0xBF)),		ReliableTxtEncoding.UTF_8);
		Assert.equals(getEncodingFromFile(byteArray(0xFE,0xFF)),			ReliableTxtEncoding.UTF_16);
		Assert.equals(getEncodingFromFile(byteArray(0xFF,0xFE)),			ReliableTxtEncoding.UTF_16_REVERSE);
		Assert.equals(getEncodingFromFile(byteArray(0x00,0x00,0xFE,0xFF)),	ReliableTxtEncoding.UTF_32);
	}
	
	private ReliableTxtEncoding getEncodingFromFile(byte[] bytes) throws IOException {
		String filePath = "Test.txt";
		Files.write(Paths.get(filePath), bytes);
		return ReliableTxtDecoder.getEncodingFromFile(filePath);
	}
	
	@Test
	public void getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException() throws IOException {
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray());
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEE));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBC));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBB));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBB,0xBE));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xEF,0xBC,0xBF));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFE,0xFE));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0xFF,0xFF));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00,0xFE));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00,0xFE,0xFE));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x01,0x00,0xFE,0xFF));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x01,0xFE,0xFF));
		getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byteArray(0x00,0x00,0xFF,0xFE));
	}
	
	private void getEncodingFromFile_InvalidPreambleGiven_ShouldThrowException(byte[] bytes) throws IOException {
		try {
			String filePath = "Test.txt";
			Files.write(Paths.get(filePath), bytes);
			ReliableTxtDecoder.getEncodingFromFile(filePath);
		} catch (ReliableTxtException e) {
			Assert.equals(e.getMessage(), "Document does not have a ReliableTXT preamble");
			return;
		}
		throw new RuntimeException("Preamble is valid");
	}
	
	@Test
	public void decode() {
		decode(byteArray(0xEF,0xBB,0xBF),							ReliableTxtEncoding.UTF_8, "");
		decode(byteArray(0xEF,0xBB,0xBF,0x61),						ReliableTxtEncoding.UTF_8, "a");
		decode(byteArray(0xEF,0xBB,0xBF,0x00),						ReliableTxtEncoding.UTF_8, "\u0000");
		decode(byteArray(0xEF,0xBB,0xBF,0xC3,0x9F),					ReliableTxtEncoding.UTF_8, "\u00DF");
		decode(byteArray(0xEF,0xBB,0xBF,0xE6,0x9D,0xB1),			ReliableTxtEncoding.UTF_8, "\u6771");
		decode(byteArray(0xEF,0xBB,0xBF,0xF0,0xA0,0x80,0x87),		ReliableTxtEncoding.UTF_8, "\uD840\uDC07");
		
		decode(byteArray(0xFE,0xFF),								ReliableTxtEncoding.UTF_16, "");
		decode(byteArray(0xFE,0xFF,0x00,0x61),						ReliableTxtEncoding.UTF_16, "a");
		decode(byteArray(0xFE,0xFF,0x00,0x00),						ReliableTxtEncoding.UTF_16, "\u0000");
		decode(byteArray(0xFE,0xFF,0x00,0xDF),						ReliableTxtEncoding.UTF_16, "\u00DF");
		decode(byteArray(0xFE,0xFF,0x67,0x71),						ReliableTxtEncoding.UTF_16, "\u6771");
		decode(byteArray(0xFE,0xFF,0xD8,0x40,0xDC,0x07),			ReliableTxtEncoding.UTF_16, "\uD840\uDC07");
		
		decode(byteArray(0xFF,0xFE),								ReliableTxtEncoding.UTF_16_REVERSE, "");
		decode(byteArray(0xFF,0xFE,0x61,0x00),						ReliableTxtEncoding.UTF_16_REVERSE, "a");
		decode(byteArray(0xFF,0xFE,0x00,0x00),						ReliableTxtEncoding.UTF_16_REVERSE, "\u0000");
		decode(byteArray(0xFF,0xFE,0xDF,0x00),						ReliableTxtEncoding.UTF_16_REVERSE, "\u00DF");
		decode(byteArray(0xFF,0xFE,0x71,0x67),						ReliableTxtEncoding.UTF_16_REVERSE, "\u6771");
		decode(byteArray(0xFF,0xFE,0x40,0xD8,0x07,0xDC),			ReliableTxtEncoding.UTF_16_REVERSE, "\uD840\uDC07");
		
		decode(byteArray(0x00,0x00,0xFE,0xFF),						ReliableTxtEncoding.UTF_32, "");
		decode(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x61),	ReliableTxtEncoding.UTF_32, "a");
		decode(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00),	ReliableTxtEncoding.UTF_32, "\u0000");
		decode(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x00,0xDF),	ReliableTxtEncoding.UTF_32, "\u00DF");
		decode(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x00,0x67,0x71),	ReliableTxtEncoding.UTF_32, "\u6771");
		decode(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x02,0x00,0x07),	ReliableTxtEncoding.UTF_32, "\uD840\uDC07");
	}
	
	@Test
	public void decode_UTF32RGiven_ShouldBeMisinterpretedAsUTF16R() {
		decode(byteArray(0xFF,0xFE,0x00,0x00,0x61,0x00,0x00,0x00),	ReliableTxtEncoding.UTF_16_REVERSE, "\u0000a\u0000");
	}
	
	private void decode(byte[] bytes, ReliableTxtEncoding expectedEncoding, String expectedStr) {
		Object[] decoderResult = ReliableTxtDecoder.decode(bytes);
		Assert.equals((ReliableTxtEncoding)decoderResult[0], expectedEncoding);
		Assert.equals((String)decoderResult[1], expectedStr);
	}
	
	@Test
	public void decode_InvalidEncodedDataGiven_ShouldThrowException() {
		decode_InvalidEncodedDataGiven_ShouldThrowException(byteArray(0xEF,0xBB,0xBF,0xFF), "The UTF_8 encoded text contains invalid data.");
		decode_InvalidEncodedDataGiven_ShouldThrowException(byteArray(0xFE,0xFF,0xD8,0x40,0x00,0x61), "The UTF_16 encoded text contains invalid data.");
		decode_InvalidEncodedDataGiven_ShouldThrowException(byteArray(0xFF,0xFE,0x40,0xD8,0x61,0x00), "The UTF_16_REVERSE encoded text contains invalid data.");
		decode_InvalidEncodedDataGiven_ShouldThrowException(byteArray(0x00,0x00,0xFE,0xFF,0x00,0x11,0x00,0x00), "The UTF_32 encoded text contains invalid data.");
	}
	
	private void decode_InvalidEncodedDataGiven_ShouldThrowException(byte[] bytes, String expectedMessage) {
		try {
			Object[] decoderResult = ReliableTxtDecoder.decode(bytes);
		} catch (ReliableTxtException e) {
			Assert.equals(e.getMessage(), expectedMessage);
			return;
		}
		throw new RuntimeException("Encoded data was valid");
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
		ReliableTxtDecoder decoder = new ReliableTxtDecoder();
	}
}