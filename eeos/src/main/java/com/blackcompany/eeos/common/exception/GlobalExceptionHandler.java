package com.blackcompany.eeos.common.exception;

import com.blackcompany.eeos.auth.application.exception.RequiredSignupInfoException;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.FailureBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.support.AuthorizationScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/** jakarta.validation.Valid 또는 @Validated binding error가 발생할 경우 */
	@ExceptionHandler(BindException.class)
	protected ApiResponse<FailureBody> handleBindException(BindException e) {
		log.warn("handleBindException", e);
		String code = String.valueOf(HttpStatus.BAD_REQUEST.value());
		return ApiResponseGenerator.fail(e.getBindingResult(), code, HttpStatus.BAD_REQUEST);
	}

	/** 주로 @RequestParam enum으로 binding 못했을 경우 발생 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ApiResponse<FailureBody> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e) {
		log.warn("handleMethodArgumentTypeMismatchException", e);
		String code = String.valueOf(HttpStatus.BAD_REQUEST.value());
		return ApiResponseGenerator.fail(e.getMessage(), code, HttpStatus.BAD_REQUEST);
	}

	/** 지원하지 않은 HTTP method 호출 할 경우 발생 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ApiResponse<FailureBody> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		log.warn("handleHttpRequestMethodNotSupportedException", e);
		String code = String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value());
		return ApiResponseGenerator.fail(e.getMessage(), code, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/** 비즈니스 로직 실행 중 오류 발생 */
	@ExceptionHandler(value = {BusinessException.class})
	protected ApiResponse<FailureBody> handleConflict(BusinessException e) {
		log.warn("BusinessException", e);
		return ApiResponseGenerator.fail(e.getMessage(), e.getCode(), e.getHttpStatus());
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	protected ApiResponse<FailureBody> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {
		log.warn("MethodArgumentNotValidException", e);

		if (e.getBindingResult().getFieldErrors().isEmpty()) {
			String code = String.valueOf(HttpStatus.BAD_REQUEST.value());
			return ApiResponseGenerator.fail("잘못된 요청입니다", code, HttpStatus.BAD_REQUEST);
		}

		String defaultMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		String code = String.valueOf(HttpStatus.BAD_REQUEST.value());
		String message = defaultMessage;

		if (defaultMessage != null && defaultMessage.contains(":")) {
			String[] parts = defaultMessage.split(":", 2);
			code = parts[0];
			message = parts[1];
		}

		return ApiResponseGenerator.fail(message, code, HttpStatus.BAD_REQUEST);
	}

	/** OAuth 로그인 후 추가 정보 필요한 경우 */
	@ExceptionHandler(RequiredSignupInfoException.class)
	protected ApiResponse<FailureBody> handleRequiredSignupInfo(RequiredSignupInfoException e) {
		log.warn("RequiredSignupInfoException", e);

		HttpHeaders headers = new HttpHeaders();
		headers.add(
				HttpHeaders.AUTHORIZATION, AuthorizationScheme.VERIFICATION + e.getVerificationId());

		return ApiResponseGenerator.fail(e.getMessage(), e.getCode(), e.getHttpStatus(), headers);
	}

	/** 나머지 예외 발생 */
	@ExceptionHandler(Exception.class)
	protected ApiResponse<FailureBody> handleException(Exception e) {
		log.error("Exception", e);
		String code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ApiResponseGenerator.fail(e.getMessage(), code, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
