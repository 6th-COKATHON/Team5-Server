package cotato.hack.domain.celebrity.service;


import static cotato.hack.common.error.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.hack.common.error.ErrorCode;
import cotato.hack.common.error.exception.AppException;
import cotato.hack.domain.celebrity.entity.Celebrity;
import cotato.hack.domain.celebrity.entity.CelebrityImage;
import cotato.hack.domain.celebrity.repository.CelebrityRepository;
import cotato.hack.domain.celebrity.repository.CelebrityImageRepository;
import cotato.hack.domain.image.domain.ImageExtension;
import cotato.hack.domain.image.domain.ImagePrefix;
import cotato.hack.domain.image.dto.CreateImageSaveUrlDto;
import cotato.hack.domain.image.service.ImageValidationService;
import cotato.hack.domain.image.util.ImageUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CelebrityImageService {

	private final CelebrityRepository celebrityRepository;
	private final CelebrityImageRepository celebrityImageRepository;
	private final ImageValidationService imageValidationService;
	private final ImageUtil imageUtil;

	// 이미지 저장 URL 생성
	public CreateImageSaveUrlDto createImageSaveUrl(ImagePrefix imagePrefix, String fullFileName) {
		imageValidationService.validateFullFileName(fullFileName);

		return imageUtil.createImageSaveUrl(imagePrefix, fullFileName);
	}

	// 이미지 URL 조회
	public Optional<String> createImageGetUrl(final long celebrityId) {
		return celebrityImageRepository.findByCelebrityId(celebrityId)
			.map(CelebrityImage::getImageUrl);
	}

	// 이미지 저장
	@Transactional
	public CelebrityImage saveImage(
		final long celebrityId,
		final ImagePrefix imagePrefix,
		final String fullFileName,
		final String imagePath
	) {
		imageValidationService.validateImageSaved(imagePath, imagePrefix);
		imageValidationService.validateFullFileName(fullFileName);
		validateDuplicatedImagePath(imagePath);

		final String[] fileNameParts = fullFileName.split("\\.");
		final String fileName = fileNameParts[0];
		final String fileExtension = fileNameParts[1];
		final String imageUrl = imageUtil.createImageUrl(imagePrefix, imagePath);

		Celebrity celebrity = celebrityRepository.findById(celebrityId)
			.orElseThrow(() -> new AppException(ErrorCode.CELEBRITY_NOT_FOUND_EXCEPTION));
		celebrity.updateCelebrityImageUrl(imageUrl);

		return celebrityImageRepository.save(
			CelebrityImage.builder()
				.celebrityId(celebrityId)
				.imagePurpose(imagePrefix)
				.imageUrl(imageUrl)
				.fullImageName(fullFileName)
				.imageName(fileName)
				.extension(ImageExtension.from(fileExtension))
				.imagePath(imagePath)
				.build()
		);
	}

	// 중복된 이미지 경로 검증
	private void validateDuplicatedImagePath(final String imagePath) {
		if (celebrityImageRepository.existsByImagePath(imagePath)) {
			throw new AppException(IMAGE_PATH_DUPLICATED_EXCEPTION);
		}
	}
}
