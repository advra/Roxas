package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.stores.Database;
import com.github.advra.roxas.utils.EmojiUtils;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class StartCommand implements Command{

    private enum GenderType{
        Unselected,
        Male,
        Female
    };

    @Override
    public String getCommand() {
        return "start";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public CommandInfo getCommandInfo() {
        return new CommandInfo("start",
                "To begin setup your character before starting your adventure", "!start");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {
        Message msg =  MessageUtils.sendMessage(event, "Welcome " + event.getMember().get().getNicknameMention()
                + "! It looks like you're new around here.\n No worries! "
                + "Lets get you started. What do you identify as?");

        Mono<Void> maleReaction = msg.addReaction(EmojiUtils.EMOJI_MALE);
        Mono<Void> femaleReaction = msg.addReaction(EmojiUtils.EMOJI_FEMALE);

        Mono<Void> reactorEvent = event.getClient().on(ReactionAddEvent.class)
                .filter(e -> e.getUserId().equals(event.getMessage().getAuthor().get().getId()))
                .log()
                .next()
                .doOnNext(data -> Database.setUserGender(data.getUser().block(Duration.ofSeconds(5)), data.getEmoji()))
                .then();

//        String IMAGE_URL = "https://cdn.betterttv.net/emote/55028cd2135896936880fdd7/3x";
//        String ANY_URL = "https://www.youtube.com/watch?v=5zwY50-necw";
//        MessageChannel channel = event.getMessage().getChannel()
//                .then()
//                .createEmbed(spec ->
//                spec.setColor(Color.RED)
//                        .setAuthor("setAuthor", ANY_URL, IMAGE_URL)
//                        .setImage(IMAGE_URL)
//                        .setTitle("setTitle/setUrl")
//                        .setUrl(ANY_URL)
//                        .setDescription("setDescription\n" +
//                                "big D: is setImage\n" +
//                                "small D: is setThumbnail\n" +
//                                "<-- setColor")
//                        .addField("addField", "inline = true", true)
//                        .addField("addFIeld", "inline = true", true)
//                        .addField("addFile", "inline = false", false)
//                        .setThumbnail(IMAGE_URL)
//                        .setFooter("setFooter --> setTimestamp", IMAGE_URL)
//                        .setTimestamp(Instant.now())
//        ).block();

        return Mono.when(maleReaction, femaleReaction, reactorEvent);
    }
}
