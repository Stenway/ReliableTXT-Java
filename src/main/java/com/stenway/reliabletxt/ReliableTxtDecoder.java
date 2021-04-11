package com.stenway.reliabletxt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;

public class ReliableTxtDecoder {
	private static final String NO_RELIABLETXT_PREAMBLE = "Document does not have a ReliableTXT preamble";
	
	public static ReliableTxtEncoding getEncoding(byte[] bytes) {
		Objects.requireNonNull(bytes);

		if (bytes.length >= 3
				&& bytes[0] == (byte)0xEF 
				&& bytes[1] == (byte)0xBB
				&& bytes[2] == (byte)0xBF) {
			return ReliableTxtEncoding.UTF_8;
		} else if (bytes.length >= 2
				&& bytes[0] == (byte)0xFE 
				&& bytes[1] == (byte)0xFF) {
			return ReliableTxtEncoding.UTF_16;
		} else if (bytes.length >= 2
				&& bytes[0] == (byte)0xFF 
				&& bytes[1] == (byte)0xFE) {
			return ReliableTxtEncoding.UTF_16_REVERSE;
		} else if (bytes.length >= 4
				&& bytes[0] == 0 
				&& bytes[1] == 0
				&& bytes[2] == (byte)0xFE 
				&& bytes[3] == (byte)0xFF) {
			return ReliableTxtEncoding.UTF_32;
		} else {
			throw new ReliableTxtException(NO_RELIABLETXT_PREAMBLE);
		}
	}
	
	public static ReliableTxtEncoding getEncodingFromFile(String filePath) throws IOException {
		byte[] bytes = new byte[4];
		try (InputStream inputStream = new FileInputStream(filePath)) {
			if (inputStream.read(bytes, 0, 2) == 2) {
				if (bytes[0] == (byte)0xEF && bytes[1] == (byte)0xBB) {
					if (inputStream.read(bytes, 2, 1) == 1 
							&& bytes[2] == (byte)0xBF) {
						return ReliableTxtEncoding.UTF_8;
					}
				} else if (bytes[0] == (byte)0xFE && bytes[1] == (byte)0xFF) {
					return ReliableTxtEncoding.UTF_16;
				} else if (bytes[0] == (byte)0xFF && bytes[1] == (byte)0xFE) {
					return ReliableTxtEncoding.UTF_16_REVERSE;
				} else if (bytes[0] == 0 && bytes[1] == 0) {
					if (inputStream.read(bytes, 2, 2) == 2 
							&& bytes[2] == (byte)0xFE && bytes[3] == (byte)0xFF) {
						return ReliableTxtEncoding.UTF_32;
					}
				}
			}
		}
		throw new ReliableTxtException(NO_RELIABLETXT_PREAMBLE);
	}
	
	public static Object[] decode(byte[] bytes) {
		ReliableTxtEncoding detectedEncoding = getEncoding(bytes);
		Charset charset = detectedEncoding.getCharset();
		byte preambleLength = detectedEncoding.getPreambleLength();
		
		
		CharsetDecoder decoder = charset.newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPORT);
		decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, preambleLength,
				bytes.length-preambleLength);
		String decodedText = null;
		try {
			CharBuffer charBuffer = decoder.decode(byteBuffer);
			decodedText = charBuffer.toString();
		} catch (Exception e) {
			throw new ReliableTxtException("The "+detectedEncoding.name()+" encoded text contains invalid data.");
		}
		
		return new Object[] {detectedEncoding, decodedText};
	}
}
