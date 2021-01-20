package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.ArrayList;

public class StartCommand implements Command{

    long durationTimeout = 30;

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
        MessageUtils.sendStoryboardMessage(
                event,
                "Hey! I haven't seen you around here before- Oh right, sorry " + event.getMember().get().getNicknameMention()
                    + " my memory's hazy lately.",
                "https://i.imgur.com/eFczQTu.png",
                "https://i.imgur.com/tnZTxt7.png",
                "SELECT: male or female"
                );

        Mono<Void> messageEvent = event.getClient().on(MessageCreateEvent.class)
                .timeout(Duration.ofSeconds(durationTimeout))
                .doOnError(e-> MessageUtils.sendUserActionMessage(event, event.getMember().get(),
                        "Timed out. Use !start to begin again."))
                .filter(e -> {
                   return e.getMessage().getContent().equalsIgnoreCase("male") ||
                           e.getMessage().getContent().equalsIgnoreCase("female");
                })
                .next()
                .doOnNext(data -> {
                    String response = "";
                    if(data.getMessage().getContent().equalsIgnoreCase("male")){
                        response = "Selected Male.";
                    }else if(data.getMessage().getContent().equalsIgnoreCase("female")){
                        response = "Selected Female.";
                    }
                    System.out.println(response);
                    MessageUtils.sendUserActionMessage(event, event.getMember().get(), response);
                })
                .then();
            return Mono.when(messageEvent);

    }
}
