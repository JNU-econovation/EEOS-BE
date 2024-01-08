package com.blackcompany.eeos.auth.application.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.auth.application.exception.InvalidNameFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OauthMemberModelTest {

	@Test
	@DisplayName("올바르지 않은 이름 형식일 경우 에러가 발생한다")
	void fail_valid_name_format() {
		// given
		OauthMemberModel invalidName = OauthMemberModel.builder().name("김수민 22기").build();

		// when & then
		assertThrows(InvalidNameFormatException.class, invalidName::validateNameFormat);
	}

	@Test
	@DisplayName("올바른 형식의 이름은 경우 에러가 발생하지 않는다.")
	void success_valid_name_format() {
		// given
		OauthMemberModel invalidName = OauthMemberModel.builder().name("22기 김수민").build();

		// when & then
		assertDoesNotThrow(invalidName::validateNameFormat);
	}
}
