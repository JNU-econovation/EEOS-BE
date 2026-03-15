package com.blackcompany.eeos.announcement.application.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class SlackMessageParser {

	private static final Pattern BRACKET_PATTERN = Pattern.compile("^\\[(.+)]$");

	public ParsedMessage parse(String text) {
		if (text == null || text.isEmpty()) {
			return new ParsedMessage(null, text == null ? "" : text);
		}

		String[] lines = text.split("\n", 2);
		String firstLine = lines[0].trim();

		Matcher matcher = BRACKET_PATTERN.matcher(firstLine);
		if (matcher.matches()) {
			String title = matcher.group(1).trim();
			String body = lines.length > 1 ? lines[1].trim() : "";
			return new ParsedMessage(title, body);
		}

		return new ParsedMessage(null, text.trim());
	}

	@Getter
	public static class ParsedMessage {

		private final String title;
		private final String body;

		public ParsedMessage(String title, String body) {
			this.title = title;
			this.body = body;
		}
	}
}
