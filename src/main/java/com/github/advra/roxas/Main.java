package com.github.advra.roxas;
import org.aeonbits.owner.ConfigFactory;

public class Main {

    public static void main(String[] args) {

        // load server properties
        ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
        try{
            System.out.println("Roxas Server booted " + cfg.hostname() + ":" + cfg.port() +
                    " will run " + cfg.maxThreads() + " threads with key: " + cfg.token());
        }catch(Exception e){
            e.printStackTrace();
        }

        // Initiate client instance
        Client instance = new Client(cfg);

    }

}
