package com.github.advra.roxas;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.DiscordClient;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, Command> commands = new HashMap<>();

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

//        client.getEventDispatcher().on(MessageCreateEvent.class)
//                .map(MessageCreateEvent::getMessage)
//                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
//                .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
//                .flatMap(Message::getChannel)
//                .flatMap(channel -> channel.createMessage("Pong!"))
//                .subscribe();

        client.onDisconnect().block();
    }

}
