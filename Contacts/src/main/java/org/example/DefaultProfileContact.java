package org.example;

import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class DefaultProfileContact implements ContactsProfile {

    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${app.path.write}")
    private String writePath;

    @Override
    public Properties start() {
        System.out.println("--- Профиль работы: " + profile + " ----");
        return new Properties(new TreeMap<>(), writePath);
    }
}
