package com.blackcompany.eeos.announcement.application.support;
import com.blackcompany.eeos.announcement.application.model.ParsedAnnouncement;

public interface AnnouncementParser {
	ParsedAnnouncement parse(String text);
}
