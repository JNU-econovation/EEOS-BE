package com.blackcompany.eeos.member.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import com.blackcompany.eeos.member.application.support.ActiveStatusSupplier;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangeActiveStatusRequest implements AbstractResponseDto, ActiveStatusSupplier {
	@NotNull @NotEmpty private String activeStatus;
}
