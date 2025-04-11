package org.example;

public class Exceptions {
    public static void main(String[] args) {

        String[] hhtpMethods = {"GET", "POST", "PUT"};

        System.out.println(("BEFORE"));

        try {
            System.out.println(hhtpMethods[3]);
        } catch (Exception ex) {
            System.out.println("Exeption : (");
        }



        System.out.println(("AFTER"));
    }
}
