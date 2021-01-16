package com.github.advra.roxas;

import com.github.advra.roxas.commands.Command;
import com.github.advra.roxas.commands.CommandExecutor;
import com.github.advra.roxas.commands.PingCommand;
import com.github.advra.roxas.listeners.MessageCreateListener;
import com.github.advra.roxas.listeners.ReadyEventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
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
//                .setSharding(getStrategy())
//                .setStoreService(getStores())
                .setInitialStatus(shard -> Presence.idle(Activity.playing("Booting Up...")))
                .withGateway(client -> {

                    //Register listeners
                    final Mono<Void> onReady = client.on(ReadyEvent.class)
                            .flatMap(ReadyEventListener::handle)
                            .then();

//                    final Mono<Void> onTextChannelDelete = client
//                            .on(TextChannelDeleteEvent.class, ChannelDeleteListener::handle)
//                            .then();
//
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

    /*
    public Client(ServerConfig cfg){
        GatewayDiscordClient client = DiscordClientBuilder.create(cfg.token()).build().login().block();

        // Register Listeners
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

                    if(content.startsWith(cfg.botPrefix())){
                        final String[] cmdAndArgs = content.trim().split("\\s+");
                        if (cmdAndArgs.length > 1) {
                            //command with args
                            final String cmd = cmdAndArgs[0].replace(cfg.botPrefix(), "");
                            final List<String> args = Arrays.asList(cmdAndArgs).subList(1, cmdAndArgs.length);

                            //issue command
                            System.out.println("event::issueCommand::" + cmd + ":" + args);
                            CommandExecutor.issueCommand(cmd, args, event, settings);
                        } else if (cmdAndArgs.length == 1) {
                            //Only command, no args
                            final String cmd = cmdAndArgs[0].replace(cfg.botPrefix(), "");

                            //Issue command
                            System.out.println("event::issueCommand::" + cmd);
                            CommandExecutor.issueCommand(cmd, new ArrayList<>(), event, settings);
                        }
                    }
                });

        client.onDisconnect().block();
    }
    */

    /**
     * Creates the DisCal bot client.
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

}
