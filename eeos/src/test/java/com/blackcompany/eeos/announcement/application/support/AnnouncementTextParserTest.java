package com.blackcompany.eeos.announcement.application.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnouncementTextParserTest {

	private AnnouncementTextParser parser;

	@BeforeEach
	void setUp() {
		parser = new AnnouncementTextParser();
	}

	@Test
	@DisplayName("[제목] 본문 형태의 텍스트를 파싱하면 title과 body가 분리된다")
	void parse_with_bracket_title_and_body() {
		// given
		String text = "[제목]\n본문 내용입니다.";

		// when
		AnnouncementTextParser.ParsedMessage result = parser.parse(text);

		// then
		assertThat(result.getTitle()).isEqualTo("제목");
		assertThat(result.getBody()).isEqualTo("본문 내용입니다.");
	}

	@Test
	@DisplayName("이모지가 포함된 [ 🔔 제목 🔔 ] 형태를 파싱하면 title이 trim된다")
	void parse_with_emoji_and_spaces_in_bracket() {
		// given
		String text = "[ 🔔 제목 🔔 ]\n본문 내용입니다.";

		// when
		AnnouncementTextParser.ParsedMessage result = parser.parse(text);

		// then
		assertThat(result.getTitle()).isEqualTo("🔔 제목 🔔");
		assertThat(result.getBody()).isEqualTo("본문 내용입니다.");
	}

	@Test
	@DisplayName("브라켓이 없는 텍스트를 파싱하면 title은 null이고 body는 전체 텍스트다")
	void parse_without_bracket_returns_null_title() {
		// given
		String text = "그냥 공지 텍스트입니다.";

		// when
		AnnouncementTextParser.ParsedMessage result = parser.parse(text);

		// then
		assertThat(result.getTitle()).isNull();
		assertThat(result.getBody()).isEqualTo("그냥 공지 텍스트입니다.");
	}

	@Test
	@DisplayName("빈 문자열을 파싱하면 title은 null이고 body는 빈 문자열이다")
	void parse_empty_string_returns_null_title_and_empty_body() {
		// given
		String text = "";

		// when
		AnnouncementTextParser.ParsedMessage result = parser.parse(text);

		// then
		assertThat(result.getTitle()).isNull();
		assertThat(result.getBody()).isEqualTo("");
	}

	@Test
	@DisplayName("첫 줄이 브라켓이지만 나머지 줄이 없으면 title은 파싱되고 body는 빈 문자열이다")
	void parse_only_bracket_title_returns_empty_body() {
		// given
		String text = "[제목만 있음]";

		// when
		AnnouncementTextParser.ParsedMessage result = parser.parse(text);

		// then
		assertThat(result.getTitle()).isEqualTo("제목만 있음");
		assertThat(result.getBody()).isEqualTo("");
	}
}
