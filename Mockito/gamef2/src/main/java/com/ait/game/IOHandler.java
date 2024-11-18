package com.ait.game;

import java.util.Scanner;

public class IOHandler {
    private Scanner sc;

    public IOHandler() {
        this.sc = new Scanner(System.in);
    }

    // Get string input from the user
    public String getString(String prompt) {
        System.out.print(prompt);
        return sc.next();
    }

    // Get string input and convert to lowercase
    public String getLowercaseString(String prompt) {
        System.out.print(prompt);
        return sc.next().toLowerCase();
    }

    // Output a message to the user
    public void print(String message) {
        System.out.println(message);
    }

    // Output a message without a new line
    public void printInline(String message) {
        System.out.print(message);
    }

    // Close the scanner resource
    public void closeScanner() {
        sc.close();
    }
}

