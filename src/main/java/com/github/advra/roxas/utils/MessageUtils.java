package com.github.advra.roxas.utils;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

public class MessageUtils {

    public static Message SendMessage(MessageCreateEvent event, String message){
        return event.getMessage().getChannel().block().createMessage(message).block();
    }
}
