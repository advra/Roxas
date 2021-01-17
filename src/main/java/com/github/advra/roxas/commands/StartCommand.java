package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.utils.EmojiUtils;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.gateway.GatewayClient;
import reactor.core.publisher.Flux;
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
        GenderType gender = GenderType.Unselected;

        // todo: check if user is in db
        // if already then do nothing otherwise setup character
        Message msg = MessageUtils.SendMessage(event, "Welcome " + event.getMember().get().getNicknameMention()
                + "! It looks like you're new around here.\n No worries! "
                + "Lets get you started. Are you a boy or a girl?");

        msg.addReaction(EmojiUtils.EMOJI_MALE).block();
        msg.addReaction(EmojiUtils.EMOJI_FEMALE).block();

        msg.getReactors(EmojiUtils.EMOJI_MALE).filter(user -> !user.equals(event.getMessage().getAuthor())).then().block();

//        MessageUtils.SendMessage(event, "You selected Male, confirm?");
//        MessageUtils.SendMessage(event, "You selected Female, confirm?");

        return Mono.empty();
    }
}
