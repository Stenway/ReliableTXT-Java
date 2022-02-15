package com.stenway.reliabletxt;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReliableTxtStreamReader implements AutoCloseable {
	public final ReliableTxtEncoding encoding;
	protected BufferedReader reader;
	protected StringBuilder sb;
	protected boolean endReached;
	
	public ReliableTxtStreamReader(String filePath) throws IOException {
		encoding = ReliableTxtDecoder.getEncodingFromFile(filePath);
		Charset charset = encoding.getCharset();
		Path path = Paths.get(filePath);
		reader = Files.newBufferedReader(path, charset);
		
		if (encoding != ReliableTxtEncoding.UTF_32) {
			int preamble = reader.read();
		}
		sb = new StringBuilder();
	}
	
	public String readLine() throws IOException {
		if (endReached) {
			return null;
		}
		int c;
		sb.setLength(0);
		while (true) {
			c = reader.read();
			if (c == '\n') {
				break;
			} else if(c < 0) {
				endReached = true;
				break;
			}
			sb.append((char)c);
		}
		return sb.toString();
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}
}
