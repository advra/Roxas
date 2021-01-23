package com.github.advra.roxas;

import com.github.advra.roxas.commands.Command;
import com.github.advra.roxas.commands.CommandExecutor;
import com.github.advra.roxas.commands.PingCommand;
import com.github.advra.roxas.listeners.MessageCreateListener;
import com.github.advra.roxas.listeners.ReadyEventListener;
import com.github.advra.roxas.utils.GeneralUtils;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.shard.ShardingStrategy;
import reactor.core.publisher.Mono;


// ref: https://github.com/DreamExposure/DisCal-Discord-Bot/tree/1c0bbff377676d046c5242ce7f965ce58dd79ddd

public class Client {

    private static GatewayDiscordClient client;
    GuildSettings settings;

    /**
     * Creates the bot client.
     *
     * @param token The Bot Token
     * @return The client if successful, otherwise <code>null</code>.
     */
    public static GatewayDiscordClient create(String token){
        DiscordClientBuilder.create(token)
                .build().gateway()
                .setSharding(getShardingStrategy(true))
                .setInitialStatus(shard -> Presence.idle(Activity.playing("Booting up...")))
                .withGateway(client -> {

                    //Register listeners
                    final Mono<Void> onReady = client.on(ReadyEvent.class)
                            .flatMap(ReadyEventListener::handle)
                            .then();

//                    final Mono<Void> onTextChannelDelete = client
//                            .on(TextChannelDeleteEvent.class, ChannelDeleteListener::handle)
//                            .then();

                    final Mono<Void> onCommand = client
                            .on(MessageCreateEvent.class, MessageCreateListener::handle)
                            .then();

                    return Mono.when(onReady, onCommand);
                }).block();

        try {
            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Creates the bot client.
     *
     * @param token The Bot Token.
     * @return The client if successful, otherwise <code>null</code>.
     */
    public static GatewayDiscordClient createClient(String token) {
        GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
        try {
            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GatewayDiscordClient getInstance(){
        return client;
    }

    public static ShardingStrategy getShardingStrategy(boolean simple){
        if (simple)
            return ShardingStrategy.recommended();

        return ShardingStrategy.builder().count(2).indices(10).build();
    }

}
