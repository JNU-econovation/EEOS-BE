package com.blackcompany.eeos.announcement.application.dto;

import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.common.utils.DateConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementResponse {

	@Schema(description = "공지사항 ID", example = "1")
	private Long id;

	@Schema(description = "공지 제목 (없으면 null)", example = "에코노베이션 31기 신입모집 면접 도우미 모집", nullable = true)
	private String title;

	@Schema(description = "공지 본문", example = "안녕하세요! 면접 도우미를 모집합니다.")
	private String body;

	@Schema(description = "Slack 메시지 발송 시각 (epoch milliseconds, KST)", example = "1774187000000")
	private long announcedAt;

	@Schema(
			description = "마감기한 (epoch milliseconds, KST 자정 기준). 마감기한이 없으면 null",
			example = "1774396800000",
			nullable = true)
	private Long deadline;

	@Schema(description = "공지사항 저장 시각 (epoch milliseconds, KST)", example = "1774187457249")
	private long createdDate;

	public static AnnouncementResponse from(AnnouncementModel model) {
		return AnnouncementResponse.builder()
				.id(model.getId())
				.title(model.getTitle())
				.body(model.getBody())
				.announcedAt(DateConverter.toMillis(model.getAnnouncedAt()))
				.deadline(DateConverter.toMillis(model.getDeadline()))
				.createdDate(DateConverter.toMillis(model.getCreatedDate()))
				.build();
	}
}
