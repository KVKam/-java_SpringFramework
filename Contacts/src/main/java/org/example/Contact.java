package org.example;

public class Contact implements Comparable<Contact> {


    private String fullName;

    private String phoneNumber;

    private String email;

    @Override
    public String toString() {
        return fullName + "| " + phoneNumber + "| " + email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(Contact o) {
        return this.getEmail().compareTo(o.getEmail());
    }
}
