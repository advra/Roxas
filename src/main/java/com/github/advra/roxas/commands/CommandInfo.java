package com.github.advra.roxas.commands;

import java.util.HashMap;

public class CommandInfo {
    private final String name;
    private final String description;
    private final String example;

    private final HashMap<String, String> subCommands = new HashMap<>();

    public CommandInfo(final String name, final String description, final String example) {
        this.name = name;
        this.description = description;
        this.example = example;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getExample() {
        return this.example;
    }

    public HashMap<String, String> getSubCommands() {
        return this.subCommands;
    }
}