package com.blackcompany.eeos.program.infra.api.slack.chat.model.converter;

import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.model.ProgramNotificationModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.*;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Block;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Text;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatPostModelConverter {
	private final MemberRepository memberRepository;

	public ChatPostModel from(ProgramNotificationModel model) {
		return ChatPostModel.builder().message(getMessage(model)).userName(getUsername(model)).build();
	}

	private Block[] getMessage(final ProgramNotificationModel model) {
		return getBlocks(
				BlockTypes.SECTION.getType(),
				/** 수정 필요 */
				TextTypes.MARKDOWN.getType(),
				/** 수정 필요 */
				model);
	}

	private Block[] getBlocks(
			final String blockType, final String textType, final ProgramNotificationModel model) {
		return new Block[] {
			getBlock(blockType, textType, ProgramMessageAnnouncements.HEADER_TOP.getAnnouncement()),
			getBlock(blockType, textType, ProgramMessageAnnouncements.HEADER_MID.getAnnouncement()),
			getBlock(
					blockType,
					textType,
					ProgramMessageAnnouncements.PROGRAM_NAME.getAnnouncement() + model.getTitle()),
			getBlock(
					blockType,
					textType,
					ProgramMessageAnnouncements.PROGRAM_DATE.getAnnouncement() + getStringDate(model)),
			getBlock(
					blockType,
					textType,
					ProgramMessageAnnouncements.PROGRAM_URL.getAnnouncement() + model.getUrl()),
			getBlock(blockType, textType, ProgramMessageAnnouncements.BOTTOM.getAnnouncement())
		};
	}

	private Block getBlock(final String blockType, final String textType, final String text) {
		return Block.builder().type(blockType).text(getText(textType, text)).build();
	}

	private Text getText(final String type, final String text) {
		return Text.builder().type(type).text(text).build();
	}

	private String getStringDate(ProgramNotificationModel model) {
		return model
				.getProgramDate()
				.toLocalDateTime()
				.format(DateTimeFormatter.ofPattern("YYYY년 MM월 dd일(E)"));
	}

	private String getUsername(ProgramNotificationModel model) {
		return memberRepository
				.findById(model.getWriter())
				.map(m -> m.getName())
				.orElseThrow(() -> new NotFoundMemberException());
	}
}
