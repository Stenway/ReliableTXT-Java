package com.stenway.reliabletxt;

import org.junit.Test;

public class ReliableTxtStreamReaderTest {
	@Test
	public void readLine() throws Exception {
		readLine(ReliableTxtEncoding.UTF_8);
		readLine(ReliableTxtEncoding.UTF_16);
		readLine(ReliableTxtEncoding.UTF_16_REVERSE);
		readLine(ReliableTxtEncoding.UTF_32);
	}
	
	private void readLine(ReliableTxtEncoding encoding) throws Exception {
		String filePath = "Test.txt";
		ReliableTxtDocument.save("Line 1\nLine 2\nLine 3", encoding, filePath);
		try (ReliableTxtStreamReader reader = 
				new ReliableTxtStreamReader(filePath)) {
			Assert.equals(reader.Encoding, encoding);
			String line = null;
			int lineCount = 0;
			while ((line = reader.readLine()) != null) {
				lineCount++;
				Assert.equals(line, "Line "+lineCount);
			}
			Assert.equals(lineCount, 3);
		}
	}
}
