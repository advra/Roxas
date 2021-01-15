package com.github.advra.roxas;

import com.github.advra.roxas.CommandHandler;
import com.github.advra.roxas.Command;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.DiscordClient;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("ping", event -> event.getMessage()
                .getChannel().block()
                .createMessage("Pong!").block());
    }

    public static void main(String[] args) {

        // load token from properties
        ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
        try{
            System.out.println("Roxas Server booted " + cfg.hostname() + ":" + cfg.port() +
                    " will run " + cfg.maxThreads() + " threads with key: " + cfg.token());
        }catch(Exception e){
            e.printStackTrace();
        }

        // Initiate main client
        final GatewayDiscordClient client = DiscordClientBuilder
                .create(cfg.token())
                .build()
                .login()
                .block();

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

}
