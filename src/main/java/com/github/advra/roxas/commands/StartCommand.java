package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.database.DatabaseManager;
import com.github.advra.roxas.database.models.PlayerDataModel;
import com.github.advra.roxas.utils.MessageUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.bson.Document;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

public class StartCommand implements Command{

    long durationTimeout = 30;
    Member user;
    MessageChannel channel;

    @Override
    public String getCommand() {
        return "start";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public CommandInfo getCommandInfo() {
        return new CommandInfo("start", "To begin setup your character.", "!start");
    }

    @Override
    public Mono<Void> issueCommand(final String[] args, final MessageCreateEvent event, final GuildSettings settings) {
        user = event.getMember().get();
        channel = event.getMessage().getChannel().block();

        // initial prompt
        MessageUtils.sendStoryboardMessage(
            event,
            "Hey! I haven't seen you around here before- Oh right, sorry " +  user.getNicknameMention() +
                    " my memory's hazy lately.",
            "https://i.imgur.com/eFczQTu.png",
            "https://i.imgur.com/tnZTxt7.png",
            "ENTER: male or female"
        );

        Mono<Void> userResponse = event.getClient().on(MessageCreateEvent.class)
            .timeout(Duration.ofSeconds(durationTimeout))
            .doOnError(e-> MessageUtils.sendUserActionMessage(event, event.getMember().get(),
                "Timed out. Use !start to begin again."))
            .filter(e -> !event.getMessage().getAuthor().get().equals(user))    // filter out other users
            .filter(e -> {
                return e.getMessage().getContent().equalsIgnoreCase("male") ||
                    e.getMessage().getContent().equalsIgnoreCase("female");
            })
            .next()
            .doOnNext(data -> {
                String genderResponse = "";
                if(data.getMessage().getContent().equalsIgnoreCase("male")){
                    genderResponse = "male";
                }else if(data.getMessage().getContent().equalsIgnoreCase("female")){
                    genderResponse = "female";
                }
                FindIterable<Document> doc = DatabaseManager.getInstance().getPlayers()
                        .find(Filters.eq("userid",event.getMember().get().getId().asLong()));

                if(doc.first() == null){
                    DatabaseManager.getInstance().getPlayers()
                        .insertOne(new PlayerDataModel(event.getMember().get().getId().asLong(), genderResponse)
                            .toDocument());
                }else{
                    System.out.println("Update instead of insert");
                    DatabaseManager.getInstance().getPlayers()
                        .updateOne(Filters.eq("userid", event.getMember().get().getId().asLong()),
                                    new Document("$set", new Document("gender",genderResponse)));
                }

                MessageUtils.sendUserActionMessage(event, event.getMember().get(), "Selected " +
                    genderResponse + ".");
            })
            .then();

        return Mono.when(userResponse);
    }

//    Mono<MessageCreateEvent> selectGenderPrompt(Me){
//        Mono<MessageCreateEvent> event =
//    }
}
