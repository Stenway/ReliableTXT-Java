package com.stenway.reliabletxt;

import org.junit.Test;

public class ReliableTxtExceptionTest {

	@Test
	public void getMessage() {
		Assert.equals(new ReliableTxtException("Test").getMessage(), "Test");
	}
	
}