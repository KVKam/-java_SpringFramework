package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

@Component
public class Contacts {
    @Autowired
    private final ContactsProfile contactsProfile;
    private TreeMap<String, Contact> contactInfoList;
    private String writePath;

    public Contacts(ContactsProfile contactsProfile) {
        this.contactsProfile = contactsProfile;
    }

    public void startWork() {
        Properties properties = contactsProfile.start();
        contactInfoList = properties.getEnv();
        writePath = properties.getWritePath();

        while (true) {
            System.out.println("\n Приложение 'Контакты' \n" +
                    "Меню: \n" +
                    "1 - Вывод всех имеющихся контактов \n" +
                    "2 - Добавить новый контакт \n" +
                    "3 - Удалить контакт по email \n" +
                    "4 - Сохранить все контакты в файл \n" +
                    "0 - Выход \n" +
                    "Выберете один из пунктов меню и напишите соответсвующую цифру");

            Scanner in = new Scanner(System.in);
            String menu = in.nextLine().trim();
            if (menu.equals("0")) {
                return;
            }
            switch (menu) {
                case "1" -> getAllContacts();
                case "2" -> addContact();
                case "3" -> deleteContactByEmail();
                case "4" -> saveContactsToFile();
                default -> System.out.println("Некорректный ввод. Попробуйте еще раз");
            }
        }
    }

    private void saveContactsToFile() {
        if (contactInfoList.isEmpty()) {
            System.out.println("Книга 'Контакты' пуста. Сохранение отменено. \n");
            return;
        }
        StringBuilder contacts = new StringBuilder();
        for (Contact contact : contactInfoList.values()) {
            contacts.append(contact.toString().replace("| ", ";")).append('\n');
        }
        try (FileWriter writer = new FileWriter(writePath)) {
            writer.write(contacts.toString());
            writer.flush();
            System.out.println("Контакты успешно сохранены в файл \n");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения контактов в файл " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void deleteContactByEmail() {
        System.out.println("Укажите email для которого необходимо удалить контакт");
        Scanner in = new Scanner(System.in);
        String email = in.nextLine().trim();
        Contact contact = contactInfoList.get(email);
        if (contact != null) {
            contactInfoList.remove(email);
            System.out.println("Контакт с email " + email + " удален");
        } else {
            System.out.println("К сожалению, контакт с email " + email + " не найден");
        }
    }

    private void getAllContacts() {
        if (contactInfoList.isEmpty()) {
            System.out.println("Книга 'Контакты' пуста");
            return;
        }
        System.out.println("\n Ф.И.О| Номер телефона| Адрес электронной почты");
        for (Contact contact : contactInfoList.values()) {
            System.out.println(contact.toString());
        }
    }

    private void addContact() {
        System.out.println("\n 0 - выход в главное меню\n" +
                "Укажите новый контакт в формате: Ф.И.О.; Номер телефона; Адрес электронной почты\n" +
                "Например: Иванов Иван Иванович; +7909999999; someEmail@example.example");
        while (true) {
            Scanner in = new Scanner(System.in);
            String newContact = in.nextLine().trim();
            if (newContact.equals("0")) {
                return;
            }

            String[] partsContact = newContact.split(";");

            if (partsContact.length != 3) {
                System.out.println("\n 0 - выход в главное меню\n" +
                        "Некорректно введены данные! Возможно вы забыли разделительный знак м/д полями ';'" +
                        "\n Попробуйте еще раз:");
                continue;
            }
            Contact contact = new Contact();

            String fullName = partsContact[0].trim();
            if (checkFullName(fullName)) {
                contact.setFullName(fullName);
            } else {
                continue;
            }

            String phoneNumber = partsContact[1].trim().replace("[ -()]", "");
            if (checkPhoneNumber(phoneNumber)) {
                contact.setPhoneNumber(phoneNumber);
            } else {
                continue;
            }

            String email = partsContact[2].trim();
            if (checkEmail(email, in)) {
                contact.setEmail(email);
            } else {
                System.out.println("\n 0 - выход в главное меню\n" +
                        "Укажите новый контакт в формате: Ф.И.О.; Номер телефона; Адрес электронной почты\n" +
                        "Например: Иванов Иван Иванович; +7909999999; someEmail@example.example");
                continue;
            }
            contactInfoList.put(contact.getEmail(), contact);
            System.out.println("Контакт " + contact + " успешно добавлен");
            return;
        }
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\+[78][0-9]{9}")) {
            return true;
        }
        System.out.println("\n 0 - выход в главное меню\n" +
                "Некорректно введен номер телефона: " + phoneNumber +
                "\n Попробуйте еще раз:");
        return false;
    }

    private boolean checkFullName(String fullName) {
        if (fullName.matches("[А-ЯA-ZЁЙ][a-zа-яёй]* [А-ЯA-ZЁЙ][a-zа-яёй]* [А-ЯA-ZЁЙ][a-zа-яёй]*")) {
            return true;
        }
        System.out.println("\n 0 - выход в главное меню\n" +
                "Некорректно введен Ф.И.О: " + fullName +
                "\n Попробуйте еще раз:");
        return false;
    }

    private boolean checkEmail(String email,Scanner in) {
        if (!email.matches("[a-zA-Z0-9]*@[a-zA-Z0-9]*.[a-zA-Z]*")) {
            System.out.println("\n 0 - выход в главное меню\n" +
                    "Некорректно введен email: " + email +
                    "\n Попробуйте еще раз:");
            return false;
        }
        if (contactInfoList.get(email) == null) {
            return true;
        }
        System.out.println("Контакт с данным email уже существует.\n" +
                "Хотите ли вы заменить его?\n" +
                "1 - да\n" +
                "2 - нет");
        String answer = in.nextLine().trim();
        switch (answer) {
            case "1" -> {
                return true;
            }
            case "2" -> {
                System.out.println("Замена контакта отменена");
                return false;
            }
            default -> {
                System.out.println("Некорректный ввод.");
                return false;
            }
        }
    }

}
