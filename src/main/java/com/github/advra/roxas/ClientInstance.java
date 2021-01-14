package com.github.advra.roxas;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

public class ClientInstance {
    //Nzk5MDMxOTIyMTAwNTM1MzQ2.X_9qdw.KLrWnQtILuKVGG8cWvKY3YBx4JI
    public static void main(String[] args) {
        final String token = "Nzk5MDMxOTIyMTAwNTM1MzQ2.X_9qdw.KLrWnQtILuKVGG8cWvKY3YBx4JI";

        ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
        try{
            System.out.println("Roxas Server booted " + cfg.hostname() + ":" + cfg.port() +
                    " will run " + cfg.maxThreads() + " with key: " + cfg.token());
        }catch(Exception e){
            e.printStackTrace();
        }

        final GatewayDiscordClient client = DiscordClientBuilder
                .create(token)
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        client.onDisconnect().block();
    }

}
