package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.exception.DeniedProgramEditException;
import com.blackcompany.eeos.program.application.exception.NotAllowedUpdatedProgramAttendException;
import com.blackcompany.eeos.program.application.exception.NotAllowedUpdatedProgramTypeException;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramCategoryException;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramType;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
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
	private Timestamp programDate;
	private String eventStatus;
	private ProgramCategory programCategory;
	private ProgramType programType;
	private Long writer;

	public ProgramStatus findProgramStatus() {
		LocalDate now = DateConverter.toLocalDate(Instant.now().toEpochMilli());
		LocalDate programDate = DateConverter.toLocalDate(this.programDate.getTime());

		if (programDate.isBefore(now)) {
			return ProgramStatus.END;
		}
		return ProgramStatus.ACTIVE;
	}

	public void validateEditAttend(Long memberId) {
		canEdit(memberId);
		if (findProgramStatus() == ProgramStatus.ACTIVE) {
			throw new NotAllowedUpdatedProgramAttendException(this.id);
		}
	}

	public void validateDelete(Long memberId) {
		canEdit(memberId);
	}

	public String getAccessRight(Long memberId) {
		if (isWriter(memberId)) {
			return AccessRights.EDIT.getAccessRight();
		}
		return AccessRights.READ_ONLY.getAccessRight();
	}

	public ProgramModel update(ProgramModel requestModel) {
		canEdit(requestModel.getWriter());
		canUpdate(requestModel);

		title = requestModel.getTitle();
		content = requestModel.getContent();
		programDate = requestModel.getProgramDate();
		programCategory = requestModel.getProgramCategory();

		return this;
	}

	private boolean canEdit(Long memberId) {
		if (isWriter(memberId)) {
			return true;
		}
		throw new DeniedProgramEditException(id);
	}

	private boolean isWriter(Long memberId) {
		return writer.equals(memberId);
	}

	private void canUpdate(ProgramModel requestModel) {
		validateUpdateType(requestModel.getProgramType());
		validateUpdateCategory(requestModel.getProgramCategory());
	}

	private void validateUpdateType(ProgramType requestType) {
		if (programType.equals(requestType)) {
			return;
		}
		throw new NotAllowedUpdatedProgramTypeException();
	}

	private void validateUpdateCategory(ProgramCategory requestCategory) {
		if (requestCategory.isAll()) {
			throw new NotFoundProgramCategoryException(requestCategory.getCategory());
		}
	}
}
