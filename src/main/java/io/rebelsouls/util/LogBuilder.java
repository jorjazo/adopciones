package io.rebelsouls.util;

import java.util.HashMap;
import java.util.Map;

public class LogBuilder {

	private Map<String, String> fields = new HashMap<>();
	
	public LogBuilder with(String field, String value) {
		fields.put(field, value);
		return this;
	}
	
	public String build() {
		final StringBuilder log = new StringBuilder();
		fields.keySet().stream().sorted().forEach(k -> log.append(k + "=\"" + fields.get(k).replaceAll("\"", "'") + "\","));
		log.deleteCharAt(log.length()-1);
		return log.toString();
	}
	
}
