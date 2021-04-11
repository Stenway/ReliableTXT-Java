package com.stenway.reliabletxt;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReliableTxtStreamWriter implements AutoCloseable {
	public final ReliableTxtEncoding Encoding;
	BufferedWriter writer;
	boolean isFirstLine;
	
	public final boolean AppendMode;
	
	public ReliableTxtStreamWriter(String filePath) throws IOException {
		this(filePath, null, false);
	}
	
	public ReliableTxtStreamWriter(String filePath, boolean append) throws IOException {
		this(filePath, null, append);
	}
	
	public ReliableTxtStreamWriter(String filePath, ReliableTxtEncoding encoding) throws IOException {
		this(filePath, encoding, false);
	}
			
	public ReliableTxtStreamWriter(String filePath, ReliableTxtEncoding encoding,
			boolean append) throws IOException {
		if (encoding == null) {
			encoding = ReliableTxtEncoding.UTF_8;
		}

		Path path = Paths.get(filePath);
		
		isFirstLine = true;
		OpenOption[] options = new OpenOption[0];
		if (append && Files.exists(path) && Files.size(path) > 0) {
			encoding = ReliableTxtDecoder.getEncodingFromFile(filePath);
			isFirstLine = false;
			
			options = new OpenOption[] {StandardOpenOption.APPEND};
		}
		AppendMode = !isFirstLine;
		
		Encoding = encoding;
		
		Charset charset = Encoding.getCharset();

		writer = Files.newBufferedWriter(path, charset, options);
		if (isFirstLine) {
			writer.write((int)0xFEFF);
		}
	}
	
	public void writeLine(String line) throws IOException {
		if (!isFirstLine) {
			writer.append('\n');
		} else {
			isFirstLine = false;
		}
		writer.write(line);
	}
	
	public void writeLines(String... lines) throws IOException {
		for (String line:lines) {
			writeLine(line);
		}
	}

	@Override
	public void close() throws Exception {
		writer.close();
	}
}
