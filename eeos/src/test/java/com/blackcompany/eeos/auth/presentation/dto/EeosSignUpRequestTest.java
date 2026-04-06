package com.blackcompany.eeos.auth.presentation.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EeosSignUpRequestTest {

	private ValidatorFactory factory;
	private Validator validator;

	@BeforeEach
	void setUp() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterEach
	void tearDown() {
		factory.close();
	}

	@ParameterizedTest
	@ValueSource(
			strings = {
				"password1!", // 소문자+숫자+특수기호
				"PASSWORD1!", // 대문자+숫자+특수기호
				"Password1!", // 대소문자+숫자+특수기호
				"abc1234@", // 소문자+숫자+특수기호 8자
				"ABC1234@", // 대문자+숫자+특수기호 8자
			})
	@DisplayName("영문+숫자+특수기호 조합 8~20자 비밀번호는 유효하다.")
	void valid_password(String password) {
		EeosSignUpRequest request = new EeosSignUpRequest("testId", password, 15, "홍길동", "am");

		Set<ConstraintViolation<EeosSignUpRequest>> violations = validator.validate(request);

		assertThat(violations).isEmpty();
	}

	@ParameterizedTest
	@ValueSource(
			strings = {
				"Ab1!xyz", // 7자 (8자 미만)
				"onlyletters", // 숫자/특수기호 없음
				"12345678", // 영문/특수기호 없음
				"Password1", // 특수기호 없음
				"password!!", // 숫자 없음
				"1234567!", // 영문 없음
				"Pass1!Pass1!Pass1!Pass1!", // 20자 초과
			})
	@DisplayName("조건을 만족하지 않는 비밀번호는 유효하지 않다.")
	void invalid_password(String password) {
		EeosSignUpRequest request = new EeosSignUpRequest("testId", password, 15, "홍길동", "am");

		Set<ConstraintViolation<EeosSignUpRequest>> violations = validator.validate(request);

		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
	}
}
