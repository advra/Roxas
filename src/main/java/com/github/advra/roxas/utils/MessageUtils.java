package com.github.advra.roxas.utils;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;

public class MessageUtils {

    public static Message sendMessage(MessageCreateEvent event, String message){
        return event.getMessage().getChannel().block().createMessage(message).block();
    }

//    public static Message sendEmbedMessage(MessageCreateEvent event, String message){
//        return event.getMessage().getChannel().block()
//            .createEmbed(spec -> spec.setColor(Color.BLACK)
//            .setAuthor(event.getMessage().getUserData().))
//    }
}
