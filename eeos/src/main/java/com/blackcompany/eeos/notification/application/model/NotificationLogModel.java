package com.blackcompany.eeos.notification.application.model;

import java.time.LocalDateTime;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.program.persistence.ProgramCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationLogModel implements AbstractModel {

	private Long id;
	private Long programId;
	private String pushToken;
	private ProgramCategory programCategory;
	private String messageTitle;
	private String messageBody;
	private NotificationStatus status;
	private String errorCode;
	private int retryCount;
	private LocalDateTime scheduledAt;
	private LocalDateTime sentAt;
	private NotificationProvider provider;
}
