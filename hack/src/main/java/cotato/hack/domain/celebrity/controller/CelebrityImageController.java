package cotato.hack.domain.celebrity.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.hack.common.error.ErrorCode;
import cotato.hack.common.error.exception.AppException;
import cotato.hack.common.response.AppResponse;
import cotato.hack.domain.celebrity.dto.request.CallbackImageSaveUrlRequest;
import cotato.hack.domain.celebrity.dto.request.CreateImageSaveUrlRequest;
import cotato.hack.domain.celebrity.dto.response.CallbackImageSaveUrlResponse;
import cotato.hack.domain.celebrity.dto.response.CreateImageGetUrlResponse;
import cotato.hack.domain.celebrity.dto.response.CreateImageSaveUrlResponse;
import cotato.hack.domain.celebrity.service.CelebrityImageService;
import cotato.hack.domain.image.dto.CreateImageSaveUrlDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/celebrities/images")
public class CelebrityImageController {

	private final CelebrityImageService celebrityImageService;

	@PostMapping("/save-url")
	@Operation(
		summary = "사용자 이미지 저장 URL 생성 API",
		description = "사용자 이미지 저장을 위한 URL을 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "400", description = "사용자 입력 오류[C-001]"),
			@ApiResponse(responseCode = "400", description = "잘못된 이미지 파일 이름입니다.[I-003]")
		}
	)
	public ResponseEntity<AppResponse<CreateImageSaveUrlResponse>> createImageSaveUrl(
		@RequestBody @Valid CreateImageSaveUrlRequest request
	) {
		CreateImageSaveUrlDto dto = celebrityImageService.createImageSaveUrl(
			request.imageUsagePurpose(),
			request.fileName()
		);
		return ResponseEntity.status(CREATED)
			.body(AppResponse.created(new CreateImageSaveUrlResponse(dto.imageSaveUrl(), dto.imagePath())));
	}

	@PostMapping("/save-url/callback")
	@Operation(
		summary = "사용자 이미지 저장 콜백 API",
		description = "사용자 이미지 저장 완료 후 호출되는 콜백 API입니다.",
		responses = {
			@ApiResponse(responseCode = "400", description = "사용자 입력 오류[C-001]"),
			@ApiResponse(responseCode = "400", description = "이미지가 저장되지 않았습니다.[I-001]"),
			@ApiResponse(responseCode = "400", description = "잘못된 이미지 파일 이름입니다.[I-003]"),
			@ApiResponse(responseCode = "409", description = "이미지 경로가 중복되었습니다.[I-005]")
		}
	)
	public ResponseEntity<AppResponse<CallbackImageSaveUrlResponse>> callbackImageSaveUrl(
		@RequestBody @Valid CallbackImageSaveUrlRequest request
	) {
		Long id = celebrityImageService.saveImage(
			request.celebrityId(),
			request.imageUsagePurpose(),
			request.fileName(),
			request.imagePath()
		).getId();

		return ResponseEntity.status(CREATED)
			.body(AppResponse.created(new CallbackImageSaveUrlResponse(id)));
	}

	@GetMapping("/get-url")
	@Operation(
		summary = "사용자 이미지 조회 URL 생성 API",
		description = "사용자 이미지는 public이므로 URL을 직접 반환합니다.",
		responses = {
			@ApiResponse(responseCode = "404", description = "해당 이미지를 찾을 수 없습니다.[I-002]")
		}
	)
	public ResponseEntity<AppResponse<CreateImageGetUrlResponse>> createImageGetUrl(
		@RequestParam Long celebrityId
	) {
		String imageGetUrl = celebrityImageService.createImageGetUrl(celebrityId)
			.orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND_EXCEPTION));

		return ResponseEntity.ok(AppResponse.ok(new CreateImageGetUrlResponse(imageGetUrl)));
	}
}
