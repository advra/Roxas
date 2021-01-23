package com.github.advra.roxas;

import com.github.advra.roxas.listeners.MessageCreateListener;
import com.github.advra.roxas.listeners.ReadyEventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.shard.ShardingStrategy;
import reactor.core.publisher.Mono;

public class Client {

    /**
     * Creates the bot client.
     *
     * @param token The Bot Token
     * @return The client if successful, otherwise <code>null</code>.
     */
    public static GatewayDiscordClient create(String token){
        try {
            DiscordClientBuilder.create(token).build().gateway()
                .setSharding(getShardingStrategy(true))
                .setInitialStatus(shard -> Presence.idle(Activity.playing("Booting up...")))
                .withGateway(client -> {

                    //Register listeners
                    final Mono<Void> onReady = client.on(ReadyEvent.class)
                            .flatMap(ReadyEventListener::handle)
                            .then();
                    final Mono<Void> onCommand = client.on(MessageCreateEvent.class, MessageCreateListener::handle)
                            .then();
                    return Mono.when(onReady, onCommand);

                }).block();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ShardingStrategy getShardingStrategy(boolean simple){
        if (simple)
            return ShardingStrategy.recommended();

        return ShardingStrategy.builder().count(2).indices(10).build();
    }

}
