package com.newapp.interconnectapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class Message {
    public static final ArrayList<String> SENT_MESSAGE = new ArrayList<>();
    public static final ArrayList<String> DISREGARDED_MESSAGES = new ArrayList<>();
    public static final ArrayList<String> STORED_MESSAGES = new ArrayList<>();
    public static final ArrayList<String> MESSAGE_HASH = new ArrayList<>();
    public static final ArrayList<String> MESSAGE_ID = new ArrayList<>();
    public static final ArrayList<String> recipients = new ArrayList<>();
    public static final ArrayList<String> storedRecipients = new ArrayList<>();
    private static int totalMessages = 0;

    private final String sender;
    private final String recipient;
    private final String content;
    private final String messageID;
    private final String messageHash;

    public Message(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        // Generate a unique message ID based on the current totalMessages
        return String.format("ID%d", totalMessages + 1);
    }

    public boolean checkRecipientCell() {
        return recipient != null && recipient.startsWith("+27") && recipient.length() == 12;
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String createMessageHash() {
        String[] words = content.split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        return (messageID + ":" + (totalMessages + 1) + ":" + first + last).toUpperCase();
    }

    public String sendMessage() {
        if (content.length() > 250) {
            return "Message too long.";
        }

        String[] options = {"Send", "Disregard", "Store"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose an option:",
                "Message",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0 -> {
                SENT_MESSAGE.add(content);
                recipients.add(recipient);
                MESSAGE_ID.add(messageID);
                MESSAGE_HASH.add(messageHash);
                totalMessages++;
                return "Message sent.";
            }
            case 1 -> {
                DISREGARDED_MESSAGES.add(content);
                return "Message disregarded.";
            }
            case 2 -> {
                STORED_MESSAGES.add(content);
                storedRecipients.add(recipient);
                MESSAGE_ID.add(messageID);
                MESSAGE_HASH.add(messageHash);
                storeMessage();
                return "Message stored.";
            }
        }
        return "No action taken.";
    }

    public void storeMessage() {
        JSONArray messages = new JSONArray();
        try (FileReader reader = new FileReader("messages.json")) {
            JSONParser parser = new JSONParser();
            messages = (JSONArray) parser.parse(reader);
        } catch (Exception ignored) {}

        JSONObject obj = new JSONObject();
        obj.put("Message", content);
        obj.put("Recipient", recipient);
        obj.put("MessageID", messageID);
        obj.put("MessageHash", messageHash);
        messages.add(obj);

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(messages.toJSONString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to write message.");
        }
    }

    public static void loadStoredMessages() {
        try (FileReader reader = new FileReader("messages.json")) {
            JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray) parser.parse(reader);

            for (Object obj : data) {
                JSONObject msg = (JSONObject) obj;
                String message = (String) msg.get("Message");
                String recipient = (String) msg.get("Recipient");
                String id = (String) msg.get("MessageID");
                String hash = (String) msg.get("MessageHash");

                STORED_MESSAGES.add(message);
                storedRecipients.add(recipient);
                MESSAGE_ID.add(id);
                MESSAGE_HASH.add(hash);
            }
        } catch (Exception ignored) {}
    }

    public static String findLongestMessage() {
        String longest = "";
        for (String msg : SENT_MESSAGE) if (msg.length() > longest.length()) longest = msg;
        for (String msg : STORED_MESSAGES) if (msg.length() > longest.length()) longest = msg;
        for (String msg : DISREGARDED_MESSAGES) if (msg.length() > longest.length()) longest = msg;
        return longest;
    }

    public static String searchByMessageID(String id) {
        for (int i = 0; i < MESSAGE_ID.size(); i++) {
            if (MESSAGE_ID.get(i).equals(id)) {
                if (i < SENT_MESSAGE.size()) return SENT_MESSAGE.get(i);
                else return STORED_MESSAGES.get(i - SENT_MESSAGE.size());
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(recipient)) sb.append(SENT_MESSAGE.get(i)).append("\n");
        }
        for (int i = 0; i < storedRecipients.size(); i++) {
            if (storedRecipients.get(i).equals(recipient)) sb.append(STORED_MESSAGES.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static boolean deleteByHash(String hash) {
        for (int i = 0; i < MESSAGE_HASH.size(); i++) {
            if (MESSAGE_HASH.get(i).equals(hash)) {
                MESSAGE_HASH.remove(i);
                MESSAGE_ID.remove(i);
                if (i < SENT_MESSAGE.size()) {
                    SENT_MESSAGE.remove(i);
                    recipients.remove(i);
                } else {
                    int idx = i - SENT_MESSAGE.size();
                    STORED_MESSAGES.remove(idx);
                    storedRecipients.remove(idx);
                }
                return true;
            }
        }
        return false;
    }

    public static String generateReport() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SENT_MESSAGE.size(); i++) {
            sb.append("Message Hash: ").append(MESSAGE_HASH.get(i)).append("\n")
              .append("Recipient: ").append(recipients.get(i)).append("\n")
              .append("Message: ").append(SENT_MESSAGE.get(i)).append("\n\n");
        }
        for (int i = 0; i < STORED_MESSAGES.size(); i++) {
            sb.append("Message Hash: ").append(MESSAGE_HASH.get(i + SENT_MESSAGE.size())).append("\n")
              .append("Recipient: ").append(storedRecipients.get(i)).append("\n")
              .append("Message: ").append(STORED_MESSAGES.get(i)).append("\n\n");
        }
        return sb.toString();
    }
}
// OpenAI ChatGPT version4 "how to store messages."Available at https://chatgpt.com/ Accessed 20 June 2025
// stack overflow. JSON file into memory. Avaliable at : https://stackoverflow.com/questions/10011011/how-do-i-read-a-json-file-into-server-memory . Accessed 20 June 2025
