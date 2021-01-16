package com.github.advra.roxas.listeners;

import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import reactor.core.publisher.Mono;

public class ReadyEventListener {
    public static Mono<Void> handle(final ReadyEvent event) {
        User self = event.getSelf();
        System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));

        return event.getSelf().getClient().updatePresence(Presence.online(Activity.playing("KH | !help")))
                .then();
    }
}
