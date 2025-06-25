package com.newapp.interconnectapp;

import javax.swing.*;
import java.awt.*;

public class MessagingInterface extends JFrame {
    private JTextField searchIDField;
    private JButton searchButton;
    private JTextArea searchResultArea;

    private JTextField deleteHashField;
    private JButton deleteButton;
    private JLabel deleteStatusLabel;

    public MessagingInterface() {
        setTitle("Message Operations");
        setSize(480, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 480, 300);
        add(panel);

        // SEARCH BY MESSAGE ID
        JLabel searchIDLabel = new JLabel("Enter Message ID:");
        searchIDLabel.setBounds(20, 20, 150, 25);
        panel.add(searchIDLabel);

        searchIDField = new JTextField();
        searchIDField.setBounds(160, 20, 150, 25);
        panel.add(searchIDField);

        searchButton = new JButton("Search");
        searchButton.setBounds(320, 20, 100, 25);
        panel.add(searchButton);

        searchResultArea = new JTextArea();
        searchResultArea.setBounds(20, 55, 400, 60);
        searchResultArea.setLineWrap(true);
        searchResultArea.setWrapStyleWord(true);
        searchResultArea.setEditable(false);
        panel.add(searchResultArea);

        // DELETE BY HASH
        JLabel deleteHashLabel = new JLabel("Enter Message Hash:");
        deleteHashLabel.setBounds(20, 130, 150, 25);
        panel.add(deleteHashLabel);

        deleteHashField = new JTextField();
        deleteHashField.setBounds(160, 130, 150, 25);
        panel.add(deleteHashField);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(320, 130, 100, 25);
        panel.add(deleteButton);

        deleteStatusLabel = new JLabel("");
        deleteStatusLabel.setBounds(20, 160, 400, 25);
        panel.add(deleteStatusLabel);

        
        searchButton.addActionListener(e -> {
            String id = searchIDField.getText().trim();
            if (!id.isEmpty()) {
                String result = Message.searchByMessageID(id); 
                searchResultArea.setText(result);
            } else {
                searchResultArea.setText("Please enter a Message ID.");
            }
        });

        deleteButton.addActionListener(e -> {
            String hash = deleteHashField.getText().trim();
            if (!hash.isEmpty()) {
                boolean deleted = Message.deleteByHash(hash);
                deleteStatusLabel.setText(deleted ? "Message deleted." : "Hash not found.");
            } else {
                deleteStatusLabel.setText("Please enter a Message Hash.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        // Load stored messages from messages.json so that GUI operations work OpenAI.2025 Chatgpt https://chatgpt.com/ accessed 20 June 2025
        Message.loadStoredMessages();
        SwingUtilities.invokeLater(MessagingInterface::new);
    }
}
