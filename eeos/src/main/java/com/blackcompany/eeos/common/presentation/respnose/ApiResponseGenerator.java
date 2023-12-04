package com.blackcompany.eeos.common.presentation.respnose;

import com.blackcompany.eeos.program.application.dto.PageResponse;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@UtilityClass
public class ApiResponseGenerator {

	public static ApiResponse<ApiResponseBody.SuccessBody<Void>> success(
			final HttpStatus status, MessageCode code) {
		return new ApiResponse<>(
				new ApiResponseBody.SuccessBody<>(null, code.getMessage(), code.getCode()), status);
	}

	public static <D> ApiResponse<ApiResponseBody.SuccessBody<D>> success(
			final D data, final HttpStatus status, MessageCode code) {
		return new ApiResponse<>(
				new ApiResponseBody.SuccessBody<>(data, code.getMessage(), code.getCode()), status);
	}

	public static <D> ApiResponse<ApiResponseBody.SuccessBody<D>> success(
			final D data, final HttpStatus status, MessageCode code, String cookieValue) {
		return new ApiResponse<>(
				new ApiResponseBody.SuccessBody<>(data, code.getMessage(), code.getCode()),
				setCookie(cookieValue),
				status);
	}

	public static <D> ApiResponse<ApiResponseBody.SuccessBody<PageResponse<D>>> success(
			final Page<D> data, final HttpStatus status, final MessageCode code) {
		return new ApiResponse<>(
				new ApiResponseBody.SuccessBody<>(
						new PageResponse<>(data), code.getMessage(), code.getCode()),
				status);
	}

	public static ApiResponse<ApiResponseBody.FailureBody> fail(
			final ApiResponseBody.FailureBody body, final HttpStatus status) {
		return new ApiResponse<>(body, status);
	}

	public static ApiResponse<ApiResponseBody.FailureBody> fail(
			final String message, final String code, final HttpStatus httpStatus) {
		return new ApiResponse<>(
				new ApiResponseBody.FailureBody(httpStatus.name(), code, message), httpStatus);
	}

	public static ApiResponse<ApiResponseBody.FailureBody> fail(
			final String status, final String code, final String message, final HttpStatus httpStatus) {
		return new ApiResponse<>(new ApiResponseBody.FailureBody(status, code, message), httpStatus);
	}

	public static ApiResponse<ApiResponseBody.FailureBody> fail(
			BindingResult bindingResult, final String code, final HttpStatus httpStatus) {
		return new ApiResponse<>(
				new ApiResponseBody.FailureBody(httpStatus.name(), code, createErrorMessage(bindingResult)),
				httpStatus);
	}

	private static String createErrorMessage(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			if (!isFirst) {
				sb.append(", ");
			} else {
				isFirst = false;
			}
			sb.append("[");
			sb.append(fieldError.getField());
			sb.append("] ");
			sb.append(fieldError.getDefaultMessage());
		}

		return sb.toString();
	}

	private MultiValueMap<String, String> setCookie(String cookieValue) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("Set-Cookie", cookieValue);

		return map;
	}
}
