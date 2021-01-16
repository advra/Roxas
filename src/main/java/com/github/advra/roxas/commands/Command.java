package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public interface Command {

    /**
     * Gets the command this Object is responsible for.
     *
     * @return The command this Object is responsible for.
     */
    default String getCommand() {
        return "COMMAND_NAME";
    }

    /**
     * Gets the short aliases of the command this object is responsible for.
     * <br>
     * This will return an empty ArrayList if none are present
     *
     * @return The aliases of the command.
     */

    default List<String> getAliases() {
        final List<String> aliases = new ArrayList<>();

        aliases.add("ALIAS");

        return aliases;
    }

    /**
     * Gets the info on the command (not sub command) to be used in help menus.
     *
     * @return The command info.
     */

    default CommandInfo getCommandInfo() {
        final CommandInfo info = new CommandInfo(
                "COMMAND_NAME",
                "COMMAND_DESCRIPTION",
                "!command <sub> (sub2)"
        );

        info.getSubCommands().put("a", "b");

        return info;
    }

    Mono<Void> issueCommand(String[] args, MessageCreateEvent event, GuildSettings settings);
}
