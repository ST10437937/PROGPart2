package com.newapp.interconnectapp;

import javax.swing.*;

public class MessageApp {

    public static void launch(String username) {
        Message.loadStoredMessages();
        JOptionPane.showMessageDialog(null, "Welcome to InterConnectApp, " + username + "!");

        while (true) {
            String[] options = {
                "Send Messages",
                "Message Operations",
                "Generate Report",
                "Quit"
            };

            int choice = JOptionPane.showOptionDialog(null,
                    "Select an option:",
                    "InterConnectApp Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                sendMessagesOperation(username);
            } else if (choice == 1) {
                messageOperationsMenu();
            } else if (choice == 2) {
                JOptionPane.showMessageDialog(null, Message.generateReport());
            } else if (choice == 3 || choice == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Thank you for using InterConnectApp. Goodbye!");
                break;
            }
        }
    }

    private static void sendMessagesOperation(String username) {
        try {
            String input = JOptionPane.showInputDialog("How many messages would you like to send?");
            if (input == null) return;

            int numMessages = Integer.parseInt(input);

            for (int i = 1; i <= numMessages; i++) {
                String recipient = JOptionPane.showInputDialog("Enter recipient phone number for message " + i + ":");
                if (recipient == null) break;

                String content = JOptionPane.showInputDialog("Enter the message content for message " + i + ":");
                if (content == null) break;

                Message msg = new Message(username, recipient, content);

                if (!msg.checkRecipientCell()) {
                    JOptionPane.showMessageDialog(null, "Recipient number must be a valid South African number (+27 followed by 9 digits).");
                    continue;
                }

                if (!msg.checkMessageID()) {
                    JOptionPane.showMessageDialog(null, "Generated Message ID is too long.");
                    continue;
                }

                String result = msg.sendMessage();
                JOptionPane.showMessageDialog(null, result);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
        }
    }

    private static void messageOperationsMenu() {
        String[] operations = {
            "Find longest message",
            "Search by message ID",
            "Search by recipient",
            "Delete by message hash",
            "Back to main menu"
        };

        int choice = JOptionPane.showOptionDialog(null,
                "Select operation:",
                "Message Operations",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                operations,
                operations[0]);

        switch (choice) {
            case 0:
                JOptionPane.showMessageDialog(null, "Longest Message:\n" + Message.findLongestMessage());
                break;

            case 1:
                String id = JOptionPane.showInputDialog("Enter message ID to search:");
                if (id != null) {
                    JOptionPane.showMessageDialog(null, Message.searchByMessageID(id));
                }
                break;

            case 2:
                String recipient = JOptionPane.showInputDialog("Enter recipient to search:");
                if (recipient != null) {
                    JOptionPane.showMessageDialog(null, Message.searchByRecipient(recipient));
                }
                break;

            case 3:
                String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
                if (hash != null) {
                    boolean deleted = Message.deleteByHash(hash);
                    JOptionPane.showMessageDialog(null, deleted ? "Message deleted successfully." : "Message hash not found.");
                }
                break;

            case 4:
            case JOptionPane.CLOSED_OPTION:
                break;
        }
    }
}
