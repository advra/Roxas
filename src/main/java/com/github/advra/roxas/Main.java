package com.github.advra.roxas;
import com.github.advra.roxas.commands.CommandExecutor;
import com.github.advra.roxas.commands.PingCommand;
import com.github.advra.roxas.commands.StartCommand;
import com.github.advra.roxas.database.DatabaseManager;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import org.aeonbits.owner.ConfigFactory;

public class Main {

    public static GatewayDiscordClient client;
    public static String version = "2021.01.16";

    public static void main(String[] args) {
        assert args.length == 1 : "Must have token set in args!";

        // load server properties
        ServerConfig cfg;
        try{
            cfg = ConfigFactory.create(ServerConfig.class);
            System.out.println("Roxas Server booted " + cfg.hostname() + ":" + cfg.port() + " will run "
                    + cfg.maxThreads() + " threads with key: " + args[0]);
            System.out.println("Version: " + version);
            System.out.println("MongoDB Name: " + cfg.databaseName());
            DatabaseManager.databaseName = cfg.databaseName();

        //Connect to MongoDB
//        DatabaseManager.
//        DatabaseManager.getManager().connectToMySQL(botSettings);
//        DatabaseManager.getManager().createTables();

        //Register commands
        CommandExecutor.initRegistry();

        // Initiate and Login client instance
        client = Client.create(args[0]);
        if (client == null)
            throw new NullPointerException("Failed to log in! Client cannot be null!");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
