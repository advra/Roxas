package com.github.advra.roxas.utils;

import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

public class DatabaseUtils {
    public static void setUserGender(User user, ReactionEmoji gender){
        String genderout = null;
        if(gender == EmojiUtils.EMOJI_MALE){
            genderout = "male";
        }else{
            genderout = "female";
        }

        System.out.println("User " + user.getMention() + "selected gener " + genderout);
    }
}
