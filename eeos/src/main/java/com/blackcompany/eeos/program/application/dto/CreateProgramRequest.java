package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import com.blackcompany.eeos.program.presentation.annotation.OverDate;
import java.sql.Timestamp;
import java.util.List;
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
	private @NotNull @OverDate Timestamp deadLine;
	private @NotNull String content;
	private @NotNull String category;
	private @NotNull String type;
	private List<ProgramMembers> members;
}
