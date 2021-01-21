package com.github.advra.roxas.database.services;

import com.github.advra.roxas.database.DatabaseManager;
import com.github.advra.roxas.database.models.PlayerDataModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PlayerServices {
    public static void addNewPlayer(PlayerDataModel p){
        DatabaseManager.getInstance().getCollection("players").insertOne(p.toDocument());
    }

    public static Document loadPlayerFromId(String userid){
        MongoCollection<Document> collection = DatabaseManager.getInstance().getCollection("players");
        List<PlayerDataModel> players = new ArrayList<>();
        return collection.find(Filters.eq("userid", userid)).first();
    }
}
