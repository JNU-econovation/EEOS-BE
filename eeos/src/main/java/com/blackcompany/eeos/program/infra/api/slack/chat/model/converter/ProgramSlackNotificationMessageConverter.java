package com.blackcompany.eeos.program.infra.api.slack.chat.model.converter;

import com.blackcompany.eeos.program.application.model.ProgramNotificationModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.*;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Block;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Text;

public class ProgramSlackNotificationMessageConverter
{
    public ChatPostModel from(ProgramNotificationModel model){
        return ChatPostModel.builder()
                .token(BotTokens.BLACK_COMPANY_EEOS.getToken())
                .channel(SlackChannels.NOTIFICATION.getChannel())
                .message(getMessage(model))
                .build();
    }

    private Block[] getMessage(final ProgramNotificationModel model){
        return getBlocks(BlockTypes.SECTION.getType(),
                TextTypes.MARKDOWN.getType(),
                model);
    }

    private Block[] getBlocks(final String blockType,
                              final String textType,
                              final ProgramNotificationModel model){
        return new Block[]{
                getBlock(blockType, textType, ProgramMessageAnnouncements.HEADER_TOP.getAnnouncement()),
                getBlock(blockType, textType, ProgramMessageAnnouncements.HEADER_MID.getAnnouncement()),
                getBlock(blockType, textType, ProgramMessageAnnouncements.PROGRAM_NAME.getAnnouncement()+model.getTitle()),
                getBlock(blockType, textType, ProgramMessageAnnouncements.PROGRAM_DATE.getAnnouncement()+model.getProgramDate()),
                getBlock(blockType, textType, ProgramMessageAnnouncements.PROGRAM_URL.getAnnouncement()+model.getProgramUrl()),
                getBlock(blockType, textType, ProgramMessageAnnouncements.BOTTOM.getAnnouncement())
        };
    }

    private Block getBlock(final String blockType,
                           final String textType,
                           final String text){
        return Block.builder()
                .type(blockType)
                .text(
                        getText(textType, text)
                )
                .build();
    }

    private Text getText(final String type,
                         final String text){
        return Text.builder()
                .type(type)
                .text(text)
                .build();
    }
}
