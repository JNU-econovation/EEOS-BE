package com.blackcompany.eeos.comment.application.model;

import com.blackcompany.eeos.comment.application.exception.DeniedCommentEditException;
import com.blackcompany.eeos.comment.application.exception.ExceedContentLimitLengthException;
import com.blackcompany.eeos.comment.application.exception.UnExpectedNPException;
import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.program.application.model.AccessRights;
import java.sql.Timestamp;
import lombok.*;

@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Getter
public class CommentModel implements AbstractModel {

	private static final long contentLimitLength = 500L;

	private Long id;
	private Long programId;
	private Long presentingTeam;
	private Long superCommentId;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private String content;
	private Long writer;

	public String getAccessRight(Long memberId) {
		if (isEdit(memberId)) return AccessRights.EDIT.getAccessRight();
		return AccessRights.READ_ONLY.getAccessRight();
	}

	public void validateCreate() {
		if (isExceedLengthLimit()) throw new ExceedContentLimitLengthException();
	}

	public void validateUpdate(Long memberId) {
		if (!isEdit(memberId)) throw new DeniedCommentEditException(this.getId());
	}

	public void validateDelete(Long memberId) {
		if (!isEdit(memberId)) throw new DeniedCommentEditException(this.getId());
	}

	public boolean isSuperComment() {
		return superCommentId.equals(Long.valueOf(-1L));
	}

	public void changeSuperComment(Long newSuper){
		this.superCommentId = newSuper;
	}

	private boolean isEdit(Long memberId) {
		if (this.writer.equals(memberId)) return true;
		return false;
	}

	private boolean isExceedLengthLimit() {
		return getContentLength() > contentLimitLength;
	}

	private long getContentLength() {
		try {
			return this.content.length();
		} catch (NullPointerException e) {
			throw new UnExpectedNPException();
		}
	}
}
