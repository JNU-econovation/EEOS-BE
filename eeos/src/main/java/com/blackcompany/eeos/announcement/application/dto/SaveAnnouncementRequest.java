package com.blackcompany.eeos.announcement.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveAnnouncementRequest {

	@Schema(description = "Slack 이벤트 ID (중복 방지용)", example = "Ev08LXXXXX")
	private String eventId;

	@Schema(description = "Slack 워크스페이스 팀 ID", example = "T08LXXXXX")
	private String teamId;

	@Schema(description = "Slack 채널 ID", example = "C08LXXXXX")
	private String channelId;

	@Schema(description = "메시지를 발송한 Slack 사용자 ID", example = "U08LXXXXX")
	private String userId;

	@Schema(description = "공지 메시지 본문")
	private String text;

	@Schema(description = "메시지 타임스탬프 (Slack ts 형식, epoch 초)", example = "1774187000.123456")
	private String messageTs;

	@Schema(description = "스레드 답글인 경우 부모 메시지의 ts. 최상위 메시지이면 null", nullable = true)
	private String threadTs;
}
