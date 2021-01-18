package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.stores.Database;
import com.github.advra.roxas.utils.EmojiUtils;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.time.Duration;
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

//        Mono<Void> responseMesage = event.getClient().on(MessageCreateEvent)

        return Mono.when(maleReaction, femaleReaction, reactorEvent);
    }
}
