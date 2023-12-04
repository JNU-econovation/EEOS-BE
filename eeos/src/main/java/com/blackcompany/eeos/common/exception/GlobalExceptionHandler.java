package com.blackcompany.eeos.common.exception;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.FailureBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import lombok.extern.slf4j.Slf4j;
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
	private static final String FAIL_CODE = "fail";

	/** javax.validation.Valid 또는 @Validated binding error가 발생할 경우 */
	@ExceptionHandler(BindException.class)
	protected ApiResponse<FailureBody> handleBindException(BindException e) {
		log.error("handleBindException", e);
		return ApiResponseGenerator.fail(e.getBindingResult(), FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	/** 주로 @RequestParam enum으로 binding 못했을 경우 발생 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ApiResponse<FailureBody> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e) {
		log.error("handleMethodArgumentTypeMismatchException", e);
		return ApiResponseGenerator.fail(e.getMessage(), FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	/** 지원하지 않은 HTTP method 호출 할 경우 발생 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ApiResponse<FailureBody> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		log.error("handleHttpRequestMethodNotSupportedException", e);
		return ApiResponseGenerator.fail(e.getMessage(), FAIL_CODE, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/** 비즈니스 로직 실행 중 오류 발생 */
	@ExceptionHandler(value = {BusinessException.class})
	protected ApiResponse<FailureBody> handleConflict(BusinessException e) {
		log.error("BusinessException", e);
		return ApiResponseGenerator.fail(e.getMessage(), e.getCode(), e.getHttpStatus());
	}

	/** 나머지 예외 발생 */
	@ExceptionHandler(Exception.class)
	protected ApiResponse<FailureBody> handleException(Exception e) {
		log.error("Exception", e);
		return ApiResponseGenerator.fail(e.getMessage(), FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	protected ApiResponse<FailureBody> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e) {
		return ApiResponseGenerator.fail(
				e.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
				FAIL_CODE,
				HttpStatus.BAD_REQUEST);
	}
}
