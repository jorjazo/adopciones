package io.rebelsouls.util;

import java.util.regex.Pattern;

public class Patterns {

	public static final String EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+[.][a-zA-Z._-]+$";
	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
}
