package cotato.hack.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	USER_INPUT_EXCEPTION(HttpStatus.BAD_REQUEST, "C-001", "사용자 입력 오류"),
	// 토큰에서 파싱한 유저 타입이 잘못된 경우
	INVALID_USER_TYPE_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-010", "잘못된 사용자 타입입니다."),
	INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "C-011", "잘못된 토큰 타입입니다."),
	MALFORMED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-012", "잘못된 형식의 토큰입니다."),

	// Server
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부에서 에러가 발생하였습니다."),
	S3_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-002", "S3 서버에서 에러가 발생하였습니다."),

	// Image
	IMAGE_NOT_STORE_EXCEPTION(HttpStatus.BAD_REQUEST, "I-001", "이미지가 저장되지 않았습니다."),
	IMAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "I-002", "해당 이미지를 찾을 수 없습니다."),
	INVALID_IMAGE_FILE_NAME_EXCEPTION(HttpStatus.BAD_REQUEST, "I-003", "잘못된 이미지 파일 이름입니다."),
	INVALID_IMAGE_FILE_PATH_EXCEPTION(HttpStatus.BAD_REQUEST, "I-004", "잘못된 이미지 파일 경로입니다."),
	IMAGE_PATH_DUPLICATED_EXCEPTION(HttpStatus.CONFLICT, "I-005", "이미지 경로가 중복되었습니다."),
	IMAGE_CAN_NOT_BE_STOLEN_EXCEPTION(HttpStatus.BAD_REQUEST, "I-006", "이미지를 훔칠 수 없습니다."),

	// Celebrity
	CELEBRITY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "CE-001", "해당 연예인을 찾을 수 없습니다."),

	// Personality
	NEED_MORE_ANSWERS_EXCEPTION(HttpStatus.BAD_REQUEST, "P-001", "성향 테스트를 완료하기 위해 5개 이상의 답변이 필요합니다.");


	private final HttpStatus status;
	private final String code;
	private final String message;
}