package io.rebelsouls.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PatternsTest {

	@Test
	public void emailPatternShouldMatchEmailExpressions() {
		assertTrue(Patterns.EMAIL_PATTERN.matcher("test@rebelsouls.io").matches());
		assertTrue(Patterns.EMAIL_PATTERN.matcher("test.prueba@rebelsouls.io").matches());
		assertTrue(Patterns.EMAIL_PATTERN.matcher("test123@rebelsouls123.io").matches());
		assertTrue(Patterns.EMAIL_PATTERN.matcher("test123@dev.rebelsouls123.io").matches());
		assertTrue(Patterns.EMAIL_PATTERN.matcher("test123@rebelsouls123.info").matches());
	}
	
	@Test
	public void emailPatterShouldRejectNonEmailExpressions() {
		assertFalse(Patterns.EMAIL_PATTERN.matcher("@rebelsouls.io").matches());
		assertFalse(Patterns.EMAIL_PATTERN.matcher("test@io").matches());
		assertFalse(Patterns.EMAIL_PATTERN.matcher("test@.io").matches());
		assertFalse(Patterns.EMAIL_PATTERN.matcher("test@rebelsouls.").matches());
		assertFalse(Patterns.EMAIL_PATTERN.matcher("test@rebelsouls").matches());
	}
}
