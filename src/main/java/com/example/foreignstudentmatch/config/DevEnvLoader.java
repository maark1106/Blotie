package com.example.foreignstudentmatch.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevEnvLoader {

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.configure().directory("/home/ec2-user/Blotie/.env").load();

        for (DotenvEntry entry : dotenv.entries()) {
            System.setProperty(entry.getKey(), entry.getValue());
            System.out.println("Setting environment variable: " + entry.getKey() + "=" + entry.getValue());
        }
    }
}