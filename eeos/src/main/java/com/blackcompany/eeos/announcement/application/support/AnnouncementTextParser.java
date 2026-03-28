package com.blackcompany.eeos.announcement.application.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementTextParser implements AnnouncementParser {

	// [제목]\n내용 패턴
	private static final Pattern BRACKET_PATTERN = Pattern.compile("^\\[(.+)]$");

	@Override
	public ParsedAnnouncement parse(String text) {
		if (text == null || text.isEmpty()) {
			return new ParsedAnnouncement(null, text == null ? "" : text, null);
		}

		String[] lines = text.split("\n", 2);
		String firstLine = lines[0].trim();

		Matcher matcher = BRACKET_PATTERN.matcher(firstLine);
		if (matcher.matches()) {
			String title = matcher.group(1).trim();
			String body = lines.length > 1 ? lines[1].trim() : "";
			return new ParsedAnnouncement(title, body, null);
		}

		return new ParsedAnnouncement(null, text.trim(), null);
	}
}
