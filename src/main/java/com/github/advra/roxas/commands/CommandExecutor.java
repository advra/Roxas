package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.utils.GeneralUtils;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    private static final List<Command> commands = new ArrayList<>();

    private CommandExecutor() {
    }

    //Functional

    /**
     * Registers a command that can be executed.
     *
     * @param _command The command to register.
     */
    public static void registerCommand(final Command _command) {
        commands.add(_command);
    }

    public static void initRegistry(){
        CommandExecutor.registerCommand(new PingCommand());
        CommandExecutor.registerCommand(new StartCommand());
    }

    public static Mono<Void> issueCommand(final String cmd, final List<String> argsOr, final MessageCreateEvent event, final GuildSettings settings) {
        final String[] args;
        if (!argsOr.isEmpty()) {
            final String toParse = GeneralUtils.getContent(argsOr, 0);
            args = GeneralUtils.overkillParser(toParse).split(" ");
        } else {
            args = new String[0];
        }

        return Mono.from(getCommand(cmd)
            .flatMap(c -> c.issueCommand(args, event, settings)));
    }

    /**
     * Gets an ArrayList of all valid commands.
     *
     * @return An ArrayList of all valid commands.
     */
    public static ArrayList<String> getAllCommands() {
        final ArrayList<String> cmds = new ArrayList<>();
        for (final Command c : commands) {
            if (!cmds.contains(c.getCommand()))
                cmds.add(c.getCommand());
        }
        return cmds;
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static Mono<Command> getCommand(final String cmdNameOrAlias) {
        return Flux.fromIterable(commands)
            .filter(c -> c.getCommand().equalsIgnoreCase(cmdNameOrAlias)
                || c.getAliases().contains(cmdNameOrAlias.toLowerCase()))
            .next();
    }
}