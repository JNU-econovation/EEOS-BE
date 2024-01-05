package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.program.application.model.ProgramModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramsResponse {
	private Page page;
	private List<ProgramModel> programs;
}
