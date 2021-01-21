package com.github.advra.roxas.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseManager {
    public static String databaseName; // from properties

    public static boolean verbose = false;
    private static DatabaseManager instance;
    static MongoClient mongoClient;
    static MongoDatabase database;

    public static String PLAYER_COLLECTION = "players";

    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void verbose(boolean v) {verbose = v;}

    private DatabaseManager(){
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase(databaseName);
        if(verbose)
            System.out.println("MongoDB: localhost:27017 "+ databaseName +")");
    }

    public MongoCollection<Document> getCollection(String collection){
        if(database==null)
            database = mongoClient.getDatabase(databaseName);
        return database.getCollection(collection);
    }

}
