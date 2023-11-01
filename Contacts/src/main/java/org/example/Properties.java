package org.example;

import java.util.TreeMap;

public class Properties {

    private TreeMap<String, Contact> profile;

    private String writePath;

    public Properties(TreeMap<String, Contact> profile, String writePath) {
        this.profile = profile;
        this.writePath = writePath;
    }

    public TreeMap<String, Contact> getEnv() {
        return profile;
    }

    public String getWritePath() {
        return writePath;
    }

}
