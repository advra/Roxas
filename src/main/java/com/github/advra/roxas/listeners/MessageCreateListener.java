package com.github.advra.roxas.listeners;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.commands.CommandExecutor;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MessageCreateListener {
    public static Mono<Void> handle(final MessageCreateEvent event) {
        // todo: per server basis using db
        GuildSettings settings = new GuildSettings();

        return Mono.just(event.getMessage())
                .filter(message -> !message.getContent().isEmpty())
                .filterWhen(message ->
                        Mono.justOrEmpty(event.getMember()
                                .map(member -> !member.isBot()))
                )
                .map(Message::getContent)
                .flatMap(content -> {
                    if (!content.startsWith("!")) return Mono.empty();
                    System.out.println("MessageCreateListener");
                    final String[] cmdAndArgs = content.trim().split("\\s+");
                    if (cmdAndArgs.length > 1) {
                        //command with args
                        final String cmd = cmdAndArgs[0].replace("!", "");
                        final List<String> args = Arrays.asList(cmdAndArgs).subList(1, cmdAndArgs.length);

                        //issue command
                        return CommandExecutor.issueCommand(cmd, args, event, settings);
                    } else if (cmdAndArgs.length == 1) {
                        //Only command, no args
                        final String cmd = cmdAndArgs[0].replace("!", "");

                        //Issue command
                        return CommandExecutor.issueCommand(cmd, new ArrayList<>(), event, settings);
                    } else {
                        //Bot not mentioned, and this is not a command, ignore this
                        return Mono.empty();
                    }
                }).then();
    }
}
