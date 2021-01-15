package com.github.advra.roxas;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.gateway.GatewayClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private GatewayDiscordClient client;
    private static final Map<String, Command> commands = new HashMap<>();

    static{
        commands.put("ping", event -> event.getMessage()
                .getChannel().block()
                .createMessage(
                        "Latency: " +
                        event.getMessage().getClient().getGatewayClient(0).map(GatewayClient::getResponseTime).get().toMillis() +
                        "ms")
                .block());
    }

    static{
        commands.put("menu", event -> event.getMessage()
                .getChannel().block()
                .createMessage("generate image to show menu!").block());
    }

    public Client(ServerConfig cfg){
        client = DiscordClientBuilder
                .create(cfg.token()).build().login().block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        // event handler to look up commands
        client.getEventDispatcher().on(MessageCreateEvent.class)
                // subscribe is like block, in that it will *request* for action
                // to be done, but instead of blocking the thread, waiting for it
                // to finish, it will just execute the results asynchronously.
                .subscribe(event -> {
                    final String content = event.getMessage().getContent(); // 3.1 Message.getContent() is a String
                    for (final Map.Entry<String, Command> entry : commands.entrySet()) {
                        // We will be using ! as our "prefix" to any command in the system.
                        if (content.startsWith(cfg.botPrefix() + entry.getKey())) {
                            entry.getValue().execute(event);
                            break;
                        }
                    }
                });

        client.onDisconnect().block();
    }

    public GatewayDiscordClient getInstance(){
        return client;
    }

    public Map<String, Command> Commands(){
        return commands;
    }

}
