package com.github.advra.roxas.utils;

import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

public class EmbedUtils
{
    public class EmbedContainer
    {
        String title;
        String description;

    }

    public static Mono<Message> updateEmbed(Message target, EmbedContainer embedContainer, EmbedCreateSpec embedSpecMod){
        MessageChannel channel = target.getChannel().block();
        Embed oldEmbed = target.getEmbeds().get(0);
        target.getEmbeds().clear();

        return Mono.empty();
    }
}
