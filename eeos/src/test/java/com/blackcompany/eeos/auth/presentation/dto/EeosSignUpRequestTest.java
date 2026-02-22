package com.blackcompany.eeos.auth.presentation.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EeosSignUpRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@ParameterizedTest
	@ValueSource(strings = {"password1", "Password1", "abc12345", "ABCD1234", "abcd1234efgh5678"})
	@DisplayName("영문+숫자 조합 8~20자 비밀번호는 유효하다.")
	void valid_password(String password) {
		EeosSignUpRequest request = new EeosSignUpRequest("testId", password, 15, "홍길동");

		Set<ConstraintViolation<EeosSignUpRequest>> violations = validator.validate(request);

		assertThat(violations).isEmpty();
	}

	@ParameterizedTest
	@ValueSource(strings = {"short1", "onlyletters", "12345678", "!special1", "toolongpassword12345678"})
	@DisplayName("조건을 만족하지 않는 비밀번호는 유효하지 않다.")
	void invalid_password(String password) {
		EeosSignUpRequest request = new EeosSignUpRequest("testId", password, 15, "홍길동");

		Set<ConstraintViolation<EeosSignUpRequest>> violations = validator.validate(request);

		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
	}
}
