package com.blackcompany.eeos.calendar.application.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CalendarValidatorTest {

	@Test
	@DisplayName("유효한 URL은 검증을 통과한다")
	void valid_url_passes_validation() {
		// given
		CalendarValidator validator =
				new CalendarValidator(null); // memberRepository is not needed for URL validation
		CalendarModel calendar = mock(CalendarModel.class);
		when(calendar.getUrl()).thenReturn("https://example.com/path");

		// when & then
		assertDoesNotThrow(() -> validator.urlValidator(calendar));
	}

	@ParameterizedTest
	@ValueSource(
			strings = {
				"https://google.com",
				"http://example.org",
				"https://sub.domain.com/path?query=value",
				"http://localhost.com/test"
			})
	@DisplayName("다양한 유효한 URL 형식은 검증을 통과한다")
	void various_valid_urls_pass_validation(String url) {
		// given
		CalendarValidator validator = new CalendarValidator(null);
		CalendarModel calendar = mock(CalendarModel.class);
		when(calendar.getUrl()).thenReturn(url);

		// when & then
		assertDoesNotThrow(() -> validator.urlValidator(calendar));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "\t", "\n"})
	@DisplayName("null 또는 빈 URL은 검증을 통과한다")
	void null_or_empty_url_passes_validation(String url) {
		// given
		CalendarValidator validator = new CalendarValidator(null);
		CalendarModel calendar = mock(CalendarModel.class);
		when(calendar.getUrl()).thenReturn(url);

		// when & then
		assertDoesNotThrow(() -> validator.urlValidator(calendar));
	}

	@ParameterizedTest
	@ValueSource(strings = {"not-a-url", "ftp://invalid", "javascript:alert(1)"})
	@DisplayName("잘못된 URL 형식은 예외를 발생시킨다")
	void invalid_url_throws_exception(String url) {
		// given
		CalendarValidator validator = new CalendarValidator(null);
		CalendarModel calendar = mock(CalendarModel.class);
		when(calendar.getUrl()).thenReturn(url);

		// when & then
		assertThrows(IllegalArgumentException.class, () -> validator.urlValidator(calendar));
	}
}
