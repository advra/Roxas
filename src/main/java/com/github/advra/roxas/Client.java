package com.github.advra.roxas;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;

import java.util.HashMap;
import java.util.Map;

public class Client {

    private GatewayDiscordClient client;
    private static final Map<String, Command> commands = new HashMap<>();

    public Client(ServerConfig cfg){
        client = DiscordClientBuilder
                .create(cfg.token()).build().login().block();
        RegisterCommands();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

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

    public void RegisterCommands(){
        commands.put("ping", event -> event.getMessage()
                .getChannel().block()
                .createMessage("Pong!").block());
    }
}
