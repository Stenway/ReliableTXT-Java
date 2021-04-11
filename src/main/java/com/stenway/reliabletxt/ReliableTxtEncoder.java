package com.stenway.reliabletxt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class ReliableTxtEncoder {
	public static byte[] encode(String text, ReliableTxtEncoding encoding) {
		Charset charset = encoding.getCharset();
		String textWithPreamble = ((char)65279) + text;
		CharsetEncoder encoder = charset.newEncoder();
		CharBuffer charBuffer = CharBuffer.wrap(textWithPreamble);
		try {
			ByteBuffer byteBuffer = encoder.encode(charBuffer);
			int numBytes = byteBuffer.limit();
			byte[] bytes = new byte[numBytes];
			byteBuffer.get(bytes);
			return bytes;
		} catch (Exception e) {
			throw new ReliableTxtException("Text contains invalid characters");
		}
	}
}
