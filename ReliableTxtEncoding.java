package com.stenway.reliabletxt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public enum ReliableTxtEncoding {
	UTF_8 {
		@Override
		public Charset getCharset() {
			return StandardCharsets.UTF_8;
		}
		
		@Override
		public byte getPreambleLength() {
			return 3;
		}
	},
	UTF_16 {
		@Override
		public Charset getCharset() {
			return StandardCharsets.UTF_16BE;
		}
		
		@Override
		public byte getPreambleLength() {
			return 2;
		}
	},
	UTF_16_REVERSE {
		@Override
		public Charset getCharset() {
			return StandardCharsets.UTF_16LE;
		}
		
		@Override
		public byte getPreambleLength() {
			return 2;
		}
	},
	UTF_32 {
		@Override
		public Charset getCharset() {
			return CHARSET_UTF_32;
		}
		
		@Override
		public byte getPreambleLength() {
			return 4;
		}
	};
	
	private static final Charset CHARSET_UTF_32 = Charset.forName("UTF-32BE");
  
	public abstract Charset getCharset();
	public abstract byte getPreambleLength();
	
	public static ReliableTxtEncoding fromFile(String filePath) throws IOException {
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
		throw new ReliableTxtException("Document does not have a ReliableTXT preamble");
	}
	
	public static ReliableTxtEncoding fromBytes(byte[] bytes) {
		Objects.requireNonNull(bytes);

		if (bytes.length >= 3
				&& bytes[0] == (byte)0xEF && bytes[1] == (byte)0xBB
				&& bytes[2] == (byte)0xBF) {
			return ReliableTxtEncoding.UTF_8;
		} else if (bytes.length >= 2
				&& bytes[0] == (byte)0xFE && bytes[1] == (byte)0xFF) {
			return ReliableTxtEncoding.UTF_16;
		} else if (bytes.length >= 2
				&& bytes[0] == (byte)0xFF && bytes[1] == (byte)0xFE) {
			return ReliableTxtEncoding.UTF_16_REVERSE;
		} else if (bytes.length >= 4
				&& bytes[0] == 0 && bytes[1] == 0
				&& bytes[2] == (byte)0xFE && bytes[3] == (byte)0xFF) {
			return ReliableTxtEncoding.UTF_32;
		} else {
			throw new ReliableTxtException("Document does not have a ReliableTXT preamble");
		}
	}
}