package cotato.hack.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	// Common
	USER_INPUT_EXCEPTION(HttpStatus.BAD_REQUEST, "C-001", "사용자 입력 오류"),
	USER_ROLE_EXCEPTION(HttpStatus.FORBIDDEN, "C-002", "유저 권한 오류"),
	AUTHENTICATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-003", "공통 권한 에러(필터)"),
	JWT_AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-004", "JWT 인증 에러"),
	TOKEN_BLACKLISTED_EXCEPTION(HttpStatus.BAD_REQUEST, "C-005", "차단된 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "C-006", "해당 유저에게 발급된 리프레시 토큰이 존재하지 않습니다."),
	REFRESH_TOKEN_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-007", "리프레시 토큰이 일치하지 않습니다."),
	USER_NOT_AUTHENTICATED_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-008", "사용자가 인증되지 않았습니다."),
	// ContextHolder에서 인증 주체 타입이 잘못된 경우
	INVALID_PRINCIPAL_TYPE_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-009", "잘못된 인증 주체 타입입니다."),
	// 토큰에서 파싱한 유저 타입이 잘못된 경우
	INVALID_USER_TYPE_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-010", "잘못된 사용자 타입입니다."),
	INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "C-011", "잘못된 토큰 타입입니다."),
	MALFORMED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "C-012", "잘못된 형식의 토큰입니다."),

	// Server
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 내부에서 에러가 발생하였습니다."),
	S3_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-002", "S3 서버에서 에러가 발생하였습니다.");


	private final HttpStatus status;
	private final String code;
	private final String message;
}