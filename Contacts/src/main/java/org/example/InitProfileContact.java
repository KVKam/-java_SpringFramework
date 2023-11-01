package org.example;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class InitProfileContact implements ContactsProfile {

    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${app.path.write}")
    private String writePath;
    @Value("${app.path.read}")
    private String readPath;

    @Override
    public Properties start() {
        System.out.println("--- Профиль работы: " + profile + " ---");
        TreeMap<String, Contact> contactInfoList = new TreeMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(readPath));
            String line = br.readLine();
            while (line != null) {

                String[] partsContact = line.split(";");
                Contact contact = new Contact();
                contact.setFullName(partsContact[0].trim());
                contact.setPhoneNumber(partsContact[1].trim().replace("[ -()]", ""));
                contact.setEmail(partsContact[2].trim());

                contactInfoList.put(contact.getEmail(), contact);

                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Ошибка при инициализации контактов из файла " + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Инициализация контактов из файла прошла успешно");
        return new Properties(contactInfoList, writePath);
    }
}
