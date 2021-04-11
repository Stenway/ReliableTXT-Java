package com.stenway.reliabletxt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
}
