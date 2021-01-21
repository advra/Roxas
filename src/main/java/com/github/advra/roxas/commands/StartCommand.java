package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.database.DatabaseManager;
import com.github.advra.roxas.database.models.PlayerDataModel;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

import static com.github.advra.roxas.database.DatabaseManager.PLAYER_COLLECTION;

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
        return new CommandInfo("start", "To begin setup your character.", "!start");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {
        MessageUtils.sendStoryboardMessage(
            event,
            "Hey! I haven't seen you around here before- Oh right, sorry " + event.getMember().get()
                    .getNicknameMention() + " my memory's hazy lately.",
            "https://i.imgur.com/eFczQTu.png",
            "https://i.imgur.com/tnZTxt7.png",
            "ENTER: male or female"
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
                String genderResponse = "";
                if(data.getMessage().getContent().equalsIgnoreCase("male")){
                    genderResponse = "male";
                }else if(data.getMessage().getContent().equalsIgnoreCase("female")){
                    genderResponse = "female";
                }
                DatabaseManager.getInstance().getCollection(PLAYER_COLLECTION)
                    .insertOne(new PlayerDataModel(
                        event.getMember().get().getId().asLong(),
                        genderResponse)
                    .toDocument());
                MessageUtils.sendUserActionMessage(event, event.getMember().get(), "Selected " +
                    genderResponse + ".");
            })
            .then();

        return Mono.when(messageEvent);
    }
}
