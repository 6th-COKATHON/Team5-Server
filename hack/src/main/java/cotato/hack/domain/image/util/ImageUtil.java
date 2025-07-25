package cotato.hack.domain.image.util;

import java.util.List;

import cotato.hack.domain.image.domain.ImagePrefix;
import cotato.hack.domain.image.dto.CreateImageSaveUrlDto;

public interface ImageUtil {

	// 이미지 삭제
	void deleteImage(String imagePath);

	// 여러 이미지 삭제
	void deleteImages(List<String> imagePaths);

	// 이미지 URL 생성
	String createImageUrl(ImagePrefix imagePrefix, String imagePath);

	// 이미지 저장 URL 생성
	CreateImageSaveUrlDto createImageSaveUrl(ImagePrefix imagePrefix, String fullFileName);

	// 이미지 조회 URL 생성
	String createImageGetUrl(String imagePath);

	// 이미지 저장 확인
	boolean isImageSaved(ImagePrefix imagePrefix, String imagePath);
}
