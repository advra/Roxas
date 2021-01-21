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

    public static String USER_COLLECTION = "users";

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

//    public void addNewUser(String username, String gender){
//
//        MongoCollection<Document> collection = database.getCollection(USER_COLLECTION);
//        Document document = new Document();
//        document.put("userid", username);
//        document.put("gender", gender);
//        collection.insertOne(document);
//    }

//    // establish our tables
//    void init(){
//        database.createCollection(USER_COLLECTION, null);
//    }

//    public static boolean getUser(String username){
//        try{
//            MongoCollection<Document> collection = database.getCollection(USER_COLLECTION);
//            FindIterable fit = collection.find((Filters.eq("userid", username)));
//            ArrayList<Document> docs = new ArrayList<Document>();
//            fit.into(docs);
//            return (docs.size() > 0)? true : false;
//        }catch (Exception e){ }
//        return false;
//    }

}
