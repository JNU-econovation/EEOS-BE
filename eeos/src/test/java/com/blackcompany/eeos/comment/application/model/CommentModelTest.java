package com.blackcompany.eeos.comment.application.model;

import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentModelTest {

	@Test
	@DisplayName("내용 길이가 500자 이상일 경우 예외가 발생하지 않는다.")
	void createTest() {
		// given
		String length_501_word =
				new Random()
						.ints(32, 127)
						.limit(501)
						.mapToObj(number -> String.valueOf((char) number))
						.collect(Collectors.joining());

		CommentModel comment = CommentModel.builder().content(length_501_word).build();

		// when & then
		Assertions.assertDoesNotThrow(comment::validateCreate);
	}
}
