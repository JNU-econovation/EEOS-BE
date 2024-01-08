package com.blackcompany.eeos.common.presentation.respnose;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
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

	public static ApiResponse<ApiResponseBody.FailureBody> fail(
			final String message, final String code, final HttpStatus status) {
		return new ApiResponse<>(
				new ApiResponseBody.FailureBody(String.valueOf(status.value()), code, message), status);
	}

	public static ApiResponse<ApiResponseBody.FailureBody> fail(
			BindingResult bindingResult, final String code, final HttpStatus status) {
		return new ApiResponse<>(
				new ApiResponseBody.FailureBody(
						String.valueOf(status.value()), code, createErrorMessage(bindingResult)),
				status);
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
}
