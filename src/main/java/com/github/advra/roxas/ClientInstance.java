package com.github.advra.roxas;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

import java.io.File;
import java.util.logging.Logger;

public class Bot {


    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create("Nzk5MDMxOTIyMTAwNTM1MzQ2.X_9qdw.KLrWnQtILuKVGG8cWvKY3YBx4JI").build().login().block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });

        client.onDisconnect().block();
    }

}
