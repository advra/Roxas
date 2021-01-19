package com.github.advra.roxas.utils;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Color;

import java.time.Instant;

public class MessageUtils {

    public static Message sendMessage(MessageCreateEvent event, String message){
        return event.getMessage().getChannel().block().createMessage(message).block();
    }

    public static Message sendEmbedMessage(MessageCreateEvent event, String message){
         String IMAGE_URL = "https://cdn.betterttv.net/emote/55028cd2135896936880fdd7/3x";
         String ANY_URL = "https://www.youtube.com/watch?v=5zwY50-necw";

        return event.getMessage().getChannel().block().createEmbed(spec ->
                spec.setColor(Color.DARK_GOLDENROD)
                        .setAuthor("setAuthor", ANY_URL, IMAGE_URL)
                        .setImage(IMAGE_URL)
                        .setTitle("setTitle/setUrl")
                        .setUrl(ANY_URL)
                        .setDescription("setDescription\n" +
                                "big D: is setImage\n" +
                                "small D: is setThumbnail\n" +
                                "<-- setColor")
                        .addField("addField", "inline = true", true)
                        .addField("addFIeld", "inline = true", true)
                        .addField("addFile", "inline = false", false)
                        .setThumbnail(IMAGE_URL)
                        .setFooter("setFooter --> setTimestamp", IMAGE_URL)
                        .setTimestamp(Instant.now())
        ).block();

    }

    public static Message sendResponseMessage(MessageCreateEvent event, String message){
        String IMAGE_URL = "https://cdn.betterttv.net/emote/55028cd2135896936880fdd7/3x";
        String ANY_URL = "https://www.youtube.com/watch?v=5zwY50-necw";

        return event.getMessage().getChannel().block().createEmbed(spec ->
                spec.setColor(Color.DARK_GOLDENROD)
                        .setAuthor("setAuthor", ANY_URL, IMAGE_URL)
                        .setImage(IMAGE_URL)
                        .setTitle("setTitle/setUrl")
                        .setUrl(ANY_URL)
                        .setDescription("setDescription\n" +
                                "big D: is setImage\n" +
                                "small D: is setThumbnail\n" +
                                "<-- setColor")
                        .addField("addField", "inline = true", true)
                        .addField("addFIeld", "inline = true", true)
                        .addField("addFile", "inline = false", false)
                        .setThumbnail(IMAGE_URL)
                        .setFooter("setFooter --> setTimestamp", IMAGE_URL)
                        .setTimestamp(Instant.now())
        ).block();

    }

    public static Message sendStoryboardMessage(String message, MessageCreateEvent event, String imgBody, String imgFooter, String description){
        return event.getMessage().getChannel().block().createEmbed(spec ->
                spec.setColor(Color.DARK_GOLDENROD)
                        .setDescription(message)
                        .setImage(imgBody)
                        .setFooter(description, imgFooter)
        ).block();

    }

    public static Message sendUserActionMessage(MessageCreateEvent event, User user, String message){
        return event.getMessage().getChannel().block().createEmbed(spec ->
                spec.setColor(Color.BLACK)
                        .setAuthor(user.getUsername(), null, user.getAvatarUrl())
                        .setDescription(message)
        ).block();

    }

    public static Message sendItemPrompt(MessageCreateEvent event, String message){
        String ITEM_IMG = "https://i.imgur.com/R95qZ7k.png";

        return event.getMessage().getChannel().block().createEmbed(spec ->
                spec.setColor(Color.DARK_GOLDENROD)
                        .setAuthor("setAuthor", null, ITEM_IMG)
                        .setImage("https://i.imgur.com/8ke1AGB.png")
                        .setTitle("setTitle/setUrl")
                        .setDescription("setDescription\n" +
                                "big D: is setImage\n" +
                                "small D: is setThumbnail\n" +
                                "<-- setColor")
                        .addField("addField", "inline = true", true)
                        .addField("addFIeld", "inline = true", true)
                        .addField("addFile", "inline = false", false)
                        .setThumbnail("https://i.pinimg.com/originals/e7/0d/73/e70d735261bd122c1a1bb548fc620d1d.png")
                        .setFooter("setFooter --> setTimestamp", ITEM_IMG)
                        .setTimestamp(Instant.now())
        ).block();

    }
}
