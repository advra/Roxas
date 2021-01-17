package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.GatewayClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class PingCommand implements Command {

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public CommandInfo getCommandInfo() {
        return new CommandInfo("ping",
                "Tests your connection latency to the bot", "!ping");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {
        event.getMessage().getChannel().block()
            .createMessage(
                "Latency: " + event.getMessage().getClient().getGatewayClient(event.getShardInfo().getIndex())
                .map(GatewayClient::getResponseTime).get().toMillis() + "ms")
            .block();

            return Mono.empty();
    }
}
