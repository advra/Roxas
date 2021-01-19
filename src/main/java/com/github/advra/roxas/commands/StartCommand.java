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

import javax.xml.crypto.Data;
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
                "To begin setup your character.", "!start");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {
        Message msg0 = MessageUtils.sendStoryboardMessage(
                event,
                "Hey! I haven't seen you around- oh right, sorry " + event.getMember().get().getNicknameMention()
                    + " my memory has been hazy lately. What do you identify yourself as?",
                "https://i.imgur.com/eFczQTu.png",
                "https://i.imgur.com/tnZTxt7.png",
                "Enter: male or female. Or React to the Emoji below to select your gender"
                );

        Mono<Void> maleReaction = msg0.addReaction(EmojiUtils.EMOJI_MALE);
        Mono<Void> femaleReaction = msg0.addReaction(EmojiUtils.EMOJI_FEMALE);

        Mono<Void> reactorEvent = event.getClient().on(ReactionAddEvent.class)
                .filter(e -> e.getUserId().equals(event.getMessage().getAuthor().get().getId()))
                .log()
                .next()
                .doOnNext(data -> Database.setUserGender(data.getUser().block(Duration.ofSeconds(5)), data.getEmoji()))
                .doOnNext(data -> {
                    String response = "Timed out (no response made in time). Create character again with !start";
                    if(data.getEmoji().equals(EmojiUtils.EMOJI_MALE)){
                        response = "Selected Male.";
                    }else{
                        response = "Selected Female.";
                    }
                    MessageUtils.sendUserActionMessage(event, event.getMember().get(), response);
                })
                .then();

        return Mono.when(maleReaction, femaleReaction, reactorEvent);
    }
}
