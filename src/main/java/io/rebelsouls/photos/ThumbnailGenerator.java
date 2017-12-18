package io.rebelsouls.photos;

import java.io.File;
import java.io.IOException;

import cl.adopciones.pets.ThumbGenerationException;
import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

@Data
public class ThumbnailGenerator {

	private String tempFilePathPrefix = "/tmp/";

	private String tempFilePathSuffix = ".png";

	
	public File generateThumb(File original, PhotoSize size) {
		File tmpFile = new File(getThumbTempFilePath(size));
		Builder<File> thumb = Thumbnails.of(original);
		try {
			if(size.getMaxHeight() != null)
				thumb.height(size.getMaxHeight());
			if(size.getMaxWidth() != null)
				thumb.width(size.getMaxWidth());
			thumb.toFile(tmpFile);
			return tmpFile;
		} catch (IOException e) {
			throw new ThumbGenerationException(original.getAbsolutePath(), size, tmpFile, e);
		}
	}

	private String getThumbTempFilePath(PhotoSize size) {
		return getTempFilePathPrefix() + size.name() + getTempFilePathSuffix();
	}

}
