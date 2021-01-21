package com.github.advra.roxas.database.services;

import com.github.advra.roxas.database.DatabaseManager;
import com.github.advra.roxas.database.models.PlayerDataModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import static com.github.advra.roxas.database.DatabaseManager.PLAYER_COLLECTION;

public class PlayerServices {

    public static Document addNewPlayer(PlayerDataModel p){
        Document d = p.toDocument();
        DatabaseManager.getInstance().getCollection(PLAYER_COLLECTION).insertOne(d);
        return d;
    }

    public static Document addNewPlayer(PlayerDataModel p, String gender){
        p.setGender(gender);
        Document d = p.toDocument();
        DatabaseManager.getInstance().getCollection(PLAYER_COLLECTION).insertOne(d);
        return d;
    }

    public static Document getPlayer(Long userid){
        MongoCollection<Document> collection = DatabaseManager.getInstance().getCollection(PLAYER_COLLECTION);
        Document d = collection.find(Filters.eq("userid", userid)).first();
        if(d == null)
            return addNewPlayer(new PlayerDataModel(userid));
        return d;
    }
}
