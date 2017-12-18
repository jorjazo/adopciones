package io.rebelsouls.photos;

import java.util.stream.Stream;

import lombok.Getter;

public enum PhotoSize {
	ORIGINAL (null, null, "original"),
	FIXED_HEIGHT (null, 200, "fixed_height"),
	FIXED_WIDTH (200, null, "fixed_width");
	
	@Getter
	private Integer maxWidth;
	
	@Getter
	private Integer maxHeight;
	
	@Getter
	private String name;
	
	private PhotoSize(Integer maxWidth, Integer maxHeight, String name) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.name = name;
	}
	
	public static PhotoSize fromName(String name) {
		return Stream.of(values())
				.filter(ps -> ps.getName().equals(name))
				.findFirst()
				.orElse(null);
	}
}
