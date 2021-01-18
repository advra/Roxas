package com.github.advra.roxas.stores;

import com.github.advra.roxas.utils.EmojiUtils;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.Reaction;
import discord4j.core.object.reaction.ReactionEmoji;
import reactor.core.publisher.Mono;

public class Database {
    static{
        // initalize the columns
    }

    Database instance = null;

    public void write(){}
    public static void write(String k, String v){
        System.out.println("Wrote to db: " + k + ", " + v);
    }

    //prevent initialization
    public Database(){ }

    void init(){ }

    public static void setUserGender(String u, ReactionEmoji e){
        String gender = (e.equals(EmojiUtils.EMOJI_MALE)) ? "male" : "female";
        System.out.println("Called setUserGender");
//        Database.write(u, gender);
    }

    public static void setUserGender(ReactionEmoji e){
        System.out.println(e.toString());
    }

}
