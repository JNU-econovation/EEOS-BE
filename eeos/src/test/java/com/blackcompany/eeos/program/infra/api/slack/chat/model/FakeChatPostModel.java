package com.blackcompany.eeos.program.infra.api.slack.chat.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/env.properties")
public class FakeChatPostModel {

    @Value("${slack.bot.black-company.eeos-test")
    private static String BLACK_COMPANY_EEOS_BOT_TOKEN;
    @Value("${slack.channel.econovation.notification")
    private static String BLACK_COMPANY_CHANNEL;
    public static ChatPostModel get(){
        return ChatPostModel.builder()
                .token(BLACK_COMPANY_EEOS_BOT_TOKEN)
                .channel(BLACK_COMPANY_CHANNEL)
                .message(getBlocks())
                .build();
    }


    private static ChatPostModel.Block[] getBlocks(){
        return new ChatPostModel.Block[]{
                getBlock(),
                getBlock(),
                getBlock(),
                getBlock(),
                getBlock(),
                getBlock()
        };
    }

    private static ChatPostModel.Block getBlock(){
        return new ChatPostModel.Block(BlockTypes.SECTION.getType(), getText());
    }

    private static ChatPostModel.Text getText(){
        return new ChatPostModel.Text(TextTypes.MARKDOWN.getType(), "*Test 메세지 입니다.*");
    }
}
