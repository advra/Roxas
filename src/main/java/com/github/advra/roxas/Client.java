package com.github.advra.roxas;

import com.github.advra.roxas.commands.Command;
import com.github.advra.roxas.commands.CommandExecutor;
import com.github.advra.roxas.commands.PingCommand;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Client {

    private static final List<Command> commands = new ArrayList<>();
    GuildSettings settings;

    public Client(ServerConfig cfg){
        GatewayDiscordClient client = DiscordClientBuilder.create(cfg.token()).build().login().block();

        //Register commands
        RegisterEventCommands();

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

    private void RegisterEventCommands(){
        CommandExecutor.registerCommand(new PingCommand());
    }

}
