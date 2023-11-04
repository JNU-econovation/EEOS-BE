package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import com.blackcompany.eeos.program.presentation.annotation.OverDate;
import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateProgramRequest implements AbstractRequestDto {

	private @NotNull String title;
	private @NotNull String content;
	private @NotNull @OverDate Timestamp programDate;
}
