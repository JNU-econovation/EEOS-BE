package com.blackcompany.eeos.notification.application.dto;

import com.blackcompany.eeos.program.persistence.ProgramCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationRequest {
	private Long programId;
	private ProgramCategory category;
	private String title;
	private String body;
}
