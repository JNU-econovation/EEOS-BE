package com.blackcompany.eeos.comment.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.program.application.model.AccessRights;
import java.sql.Timestamp;
import lombok.*;

@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Getter
public class CommentModel implements AbstractModel {

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

	public boolean isSuperComment() {
		return superCommentId.equals(Long.valueOf(-1L));
	}

	private boolean isEdit(Long memberId) {
		if (this.writer.equals(memberId)) return true;
		return false;
	}
}
