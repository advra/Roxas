package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.gateway.GatewayClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

import static discord4j.core.event.EventDispatcher.log;

public class ClearCommand implements Command {
    @Override
    public String getCommand() {
        return "clear";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public CommandInfo getCommandInfo() {
        return new CommandInfo("clear",
                "Deletes all messages in the current channel", "!clear");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {
        Mono<Void> startMessage = event.getMessage().getChannel()
                .flatMap(channel -> channel
                        .createMessage("Deleting messages..."))
                .then();

        Mono<Void> deleteEvent = event.getClient().on(MessageBulkDeleteEvent.class)
            .filter(e -> e.getChannel().equals(event.getMessage().getChannel()))
            .then();

//       Mono<Void> deleteEvent =  event.getMessage().getChannel().cast(TextChannel.class)
//           .subscribe(channel -> {
//               channel.bulkDeleteMessages(channel.getMessagesBefore(event.getMessage().getId()).take(2)).subscribe();
//        });

        Mono<Void> responseMessage = event.getMessage().getChannel().block().createMessage("Removed all messages done!")
            .then();

        return Mono.when(startMessage, deleteEvent, responseMessage);
    }
}
