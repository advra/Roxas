package com.github.advra.roxas.commands;

import com.github.advra.roxas.GuildSettings;
import com.github.advra.roxas.utils.EmojiUtils;
import com.github.advra.roxas.utils.MessageUtils;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

public class StartCommand implements Command{

    long durationTimeout = 30;
    Member user;
    MessageChannel channel;
    String gender;
//    Message unionSelectPrompt;

    int state;

    final String unionImageLinks[] = {
            "https://i.imgur.com/vN74ybN.png",
            "https://i.imgur.com/QY3G7YO.png",
            "https://i.imgur.com/xKhTKiT.png",
            "https://i.imgur.com/q406YaG.png",
            "https://i.imgur.com/JorlW7M.png"
    };
    int unionIndex = 0;

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
        state = 0;
        Mono<Message> initialMessage = MessageUtils.sendDisposableEmbedMessage(
                event,
                "Hey you two! I haven't either of you around here before-\n OH, right!! Please excuse me "
                        +  user.getNicknameMention() + ", my memory is hazy. I haven't been feeling myself lately.",
                "https://i.imgur.com/eFczQTu.png",
                "https://i.imgur.com/tnZTxt7.png",
                "ENTER: male or female"
        );

        Mono<Void> userResponseSubscriber = event.getClient().on(MessageCreateEvent.class)
            .timeout(Duration.ofSeconds(30))
            .doOnError(e-> MessageUtils.sendUserActionMessage(event, event.getMember().get(),
                "Timed out. Use !start to begin again."))
            .filter(rae -> !rae.getMember().get().isBot())                // response is not bot
            .filter(rae -> rae.getMember().get().equals(user))            // response is original user
            .flatMap(mce -> {
                System.out.println("Reading: " + state);
                if (state == 0) return userGenderReply(mce);
                else if (state == 1) return botUnionPrompt(mce);
                else if (state == 1) return userUnionReply(mce);
                return Mono.empty();
            })
            .then();

        // Emoji React Events
        Mono<Void> userReactSubscriber = event.getClient().on(ReactionAddEvent.class)
            .timeout(Duration.ofSeconds(durationTimeout))
            .doOnError(e-> MessageUtils.sendUserActionMessage(event, event.getMember().get(),
                "Timed out. Use !start to begin again."))
            .filter(rae -> !rae.getMember().get().isBot())         // reactor isnt a bot
            .filter(rae -> rae.getMember().get().equals(event.getMember().get()))     // reactor is original user
            .flatMap(rae -> {
                System.out.println("Reacted emoji");
//                if(rae.getEmoji().equals(EmojiUtils.PREVIOUS)) return previousPage(rae);
//                else if(rae.getEmoji().equals(EmojiUtils.SELECT)) return selectPage(rae);
                if(rae.getEmoji().equals(EmojiUtils.NEXT)) return pageNext(rae);
                else return Mono.empty();
            })
            .then();

        return Mono.when(initialMessage, userResponseSubscriber, userReactSubscriber);
    }

    Mono<Void> userGenderReply(MessageCreateEvent mce){
        if(mce.getMember().equals(user)) return  Mono.empty();

        String response = mce.getMessage().getContent();
        System.out.println("Response is: " + response);
        if(!(response.equalsIgnoreCase("male") || response.equalsIgnoreCase("female") || response.equalsIgnoreCase("nonbinary")))
            return Mono.empty();

        // store into database here
//        FindIterable<Document> doc = DatabaseManager.getInstance().getPlayers()
//            .find(Filters.eq("userid",event.getMember().get().getId().asLong()));
//
//            if(doc.first() == null){
//                DatabaseManager.getInstance().getPlayers()
//                        .insertOne(new PlayerDataModel(event.getMember().get().getId().asLong(), genderResponse)
//                        .toDocument());
//            }else{
//                DatabaseManager.getInstance().getPlayers()
//                    .updateOne(Filters.eq("userid", event.getMember().get().getId().asLong()), new Document("$set", new Document("gender",genderResponse)));
//            }

        state++;

        Message unionSelectPrompt = channel.createEmbed(spec -> {
            spec.setColor(Color.DARK_GOLDENROD);
            spec.setThumbnail("https://i.imgur.com/v7L004T.png");
            spec.setDescription("And which of the following Unions do you identify yourself with? \n One");
            spec.setImage(unionImageLinks[0]);
            spec.setFooter( "REACT To Emojis to navigate PREVIOUS SELECT or NEXT", "https://i.imgur.com/tnZTxt7.png");
        }).block();

        Mono<Void> previousReaction = unionSelectPrompt.addReaction(EmojiUtils.PREVIOUS);
        Mono<Void> selectReaction = unionSelectPrompt.addReaction(EmojiUtils.CHECK);
        Mono<Void> nextReaction = unionSelectPrompt.addReaction(EmojiUtils.NEXT);

        return Mono.when(previousReaction,selectReaction,nextReaction);
    }

    Mono <Message> botUnionPrompt(MessageCreateEvent mce){
        if(mce.getMember().get().isBot()) return Mono.empty();
        return Mono.empty();
    }

    Mono <Message> userUnionReply(MessageCreateEvent mce){
        String response = mce.getMessage().getContent();
        state++;
        return MessageUtils.sendUserActionMessage(mce, user, "You selected " + response);
    }

    Mono <Void> pageNext(ReactionAddEvent rae){
        Message targetMessage = rae.getChannel().block().getMessageById(rae.getMessageId()).block();
        Embed oldEmbed = targetMessage.getEmbeds().get(0);
        unionIndex++;
        if(unionIndex>5) unionIndex = 0;
        System.out.println("index:" + unionIndex);

        Mono<Void> removeReaction = targetMessage.removeReaction(rae.getEmoji(), rae.getUserId());
        Mono<Void> updateEmbed = targetMessage.edit(
                messageEditSpec -> messageEditSpec.setEmbed(embedSpec -> {
                    embedSpec.setDescription(oldEmbed.getDescription().get());
                    embedSpec.setImage(unionImageLinks[unionIndex]);
                    embedSpec.setColor(oldEmbed.getColor().get());
                    embedSpec.setFooter(oldEmbed.getFooter().get().getText(), oldEmbed.getFooter().get().getIconUrl());
                })).then();

        return Mono.when(removeReaction, updateEmbed);
    }

    Mono<Void> previousPage(ReactionAddEvent rae){
        return Mono.empty();
    }

    Mono<Void> selectPage(ReactionAddEvent rae){
        return Mono.empty();
    }

    Mono<Void> setEmbedPage(ReactionAddEvent rae, Message message, int index){
        if(unionIndex<0) unionIndex = 0;
        if(unionIndex%6==0) unionIndex = 0;
        Embed targetEmbed = message.getEmbeds().get(0);

        return Mono.empty();
    }

}
