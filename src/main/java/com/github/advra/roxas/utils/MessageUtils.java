package com.github.advra.roxas.utils;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.Color;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

public class MessageUtils {

    public static Color colorBot = Color.DARK_GOLDENROD;
    public static Color colorUser = Color.BLACK;
    public static String thumbNailUnknown = "https://i.imgur.com/v7L004T.png";

    /*
        Messages that are disposable are intended for the bot and do no "consume"  the subscription
     */

    public static Mono<Message> sendDisposableEmbedMessage(MessageCreateEvent event, String message, String imgBody, String imgFooter, String description){
        return event.getMessage().getChannel()
                .flatMap(c -> c.createEmbed(spec -> spec
                    .setColor(Color.DARK_GOLDENROD)
                    .setThumbnail(thumbNailUnknown)
                    .setDescription(message)
                    .setImage(imgBody)
                    .setFooter(description, imgFooter)));
    }

    public static Mono<Message> sendStoryboardMessage(MessageCreateEvent event, String message, String imgBody, String imgFooter, String description){
        return event.getMessage().getChannel()
                .flatMap(c -> c.createEmbed(spec -> spec
                        .setColor(Color.DARK_GOLDENROD)
                        .setThumbnail(thumbNailUnknown)
                        .setDescription(message)
                        .setImage(imgBody)
                        .setFooter(description, imgFooter)));
//                .onErrorResume(ClientException.class, e -> Mono.empty());
    }

    public static Mono<Message> sendUserActionMessage(MessageCreateEvent event, User user, String message){
        return event.getMessage().getChannel()
                .flatMap(c -> c
                    .createEmbed(spec -> spec
                    .setColor(colorUser)
                    .setAuthor(user.getUsername(), null, user.getAvatarUrl())
                    .setDescription(message)));
//                .onErrorResume(ClientException.class, e -> Mono.empty());
    }
}
