package com.github.advra.roxas;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:ServerConfig.properties"})
public interface ServerConfig extends Config {
    String token();
    int port();
    String hostname();
    @DefaultValue("42")
    int maxThreads();
}