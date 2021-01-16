package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.GatewayClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PingCommand implements Command {

    //    static{
//        commands.put("ping", event -> event.getMessage()
//                .getChannel().block()
//                .createMessage(
//                    "Latency: " + event.getMessage().getClient().getGatewayClient(0)
//                    .map(GatewayClient::getResponseTime).get().toMillis() + "ms")
//                .block());
//    }

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
                "Tests your connection latency to the bot",
                "!ping (command) (sub-command)");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {

        event.getMessage()
                .getChannel().block()
                .createMessage(
                    "Latency: " + event.getMessage().getClient().getGatewayClient(0)
                    .map(GatewayClient::getResponseTime).get().toMillis() + "ms")
                .block();

//        return Mono.just(args).flatMap(ignore -> {
//            System.out.println("issueCommand::ping::execute");
            return Mono.empty();
//        }).then();
    }

}
