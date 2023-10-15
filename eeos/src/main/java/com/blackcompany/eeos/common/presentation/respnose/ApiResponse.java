package com.blackcompany.eeos.common.presentation.respnose;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@Getter
public class ApiResponse<B> extends ResponseEntity<B> {

	public ApiResponse(final HttpStatus status) {
		super(status);
	}

	public ApiResponse(final B body, final HttpStatus status) {
		super(body, status);
	}

	public ApiResponse(final B body, MultiValueMap<String, String> headers, HttpStatus status) {
		super(body, headers, status);
	}
}
