package com.blackcompany.eeos.auth.application.dto.request;

import com.blackcompany.eeos.common.support.dto.AbstractApplicationDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfoApplicationCommand implements AbstractApplicationDto {
	private UUID verificationId;
	private String name;
	private Integer generation;
	private String activeStatus;
}
