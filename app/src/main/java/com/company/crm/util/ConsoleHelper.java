package com.company.crm.util;

import java.util.Scanner;

public class ConsoleHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String ask(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public static int askInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Wrong format number ");
            }
        }
    }

    public static double askDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Add number.");
            }
        }
    }

    public static boolean askBoolean(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String s = scanner.nextLine().trim().toLowerCase();
        return s.startsWith("y");
    }

    public static void pause() {
        System.out.println("\nTouch Enter for continue...");
        scanner.nextLine();
    }
}
