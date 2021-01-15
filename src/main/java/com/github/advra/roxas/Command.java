package com.github.advra.roxas;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

interface Command {
    void execute(MessageCreateEvent event);
}