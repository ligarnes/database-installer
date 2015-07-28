package net.alteiar.db.installer.exception;

import org.junit.Assert;
import org.junit.Test;

public class ParsingExceptionTest {

	@Test
	public void testExceptionWithCause() {

		final NullPointerException root = new NullPointerException();
		ParsingException ex = new ParsingException(root);

		Assert.assertEquals(NullPointerException.class.toString(), "class " + ex.getMessage());
		Assert.assertEquals(root, ex.getCause());
	}

	@Test
	public void testExceptionWithMessage() {

		final String message = "test";
		ParsingException ex = new ParsingException(message);

		Assert.assertEquals(message, ex.getMessage());
		Assert.assertEquals(null, ex.getCause());
	}

	@Test
	public void testExceptionWithCauseAndMessage() {

		final NullPointerException root = new NullPointerException();
		final String message = "test";
		ParsingException ex = new ParsingException(message, root);

		Assert.assertEquals(message, ex.getMessage());
		Assert.assertEquals(root, ex.getCause());
	}
}
