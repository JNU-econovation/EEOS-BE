package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.exception.*;
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
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Slf4j
public class ProgramModel implements AbstractModel {
	private Long id;
	private String title;
	private String content;
	private Timestamp programDate;
	private String eventStatus;
	private ProgramCategory programCategory;
	private ProgramType programType;
	private Long writer;

	public ProgramModel(ProgramModel model) {
		this.id = model.id;
		this.title = model.title;
		this.content = model.content;
		this.programDate = model.programDate;
		this.eventStatus = model.eventStatus;
		this.programCategory = model.programCategory;
		this.programType = model.programType;
		this.writer = model.writer;
	}

	public void validateCreate() {
		if (findProgramStatus().equals(ProgramStatus.ACTIVE)) {
			return;
		}
		throw new OverDateException();
	}

	public void validateEditAttend(Long memberId) {
		canEdit(memberId);
		if (findProgramStatus() == ProgramStatus.ACTIVE) {
			throw new NotAllowedUpdatedProgramAttendException(id);
		}
	}

	public void validateDelete(Long memberId) {
		canEdit(memberId);
	}

	public void validateNotify(Long memberId) {
		if (!isWriter(memberId)) throw new DeniedProgramNotificationException(memberId);
		if (!isWeeklyProgram(this)) throw new NotWeeklyProgramException();
	}

	public String getAccessRight(Long memberId) {
		if (isWriter(memberId)) {
			return AccessRights.EDIT.getAccessRight();
		}
		return AccessRights.READ_ONLY.getAccessRight();
	}

	public String getProgramStatus() {
		return findProgramStatus().getStatus();
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

	private ProgramStatus findProgramStatus() {
		LocalDate now = DateConverter.toLocalDate(Instant.now().toEpochMilli());
		LocalDate programDate = DateConverter.toLocalDate(this.programDate.getTime());

		if (programDate.isBefore(now)) {
			return ProgramStatus.END;
		}
		return ProgramStatus.ACTIVE;
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

	private boolean isWeeklyProgram(ProgramModel model){
		return model.programCategory.equals(ProgramCategory.find("weekly"));
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
