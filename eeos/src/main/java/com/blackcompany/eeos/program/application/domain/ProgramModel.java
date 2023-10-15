package com.blackcompany.eeos.program.application.domain;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramModel implements AbstractModel {
	private Long id;
	private String title;

	private String content;

	private Long userId;

	private Long programDate;
	private String eventStatus;
}
