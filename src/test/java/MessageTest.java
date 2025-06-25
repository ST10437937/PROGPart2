package com.newapp.interconnectapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void setup() {
        // Clear all parallel arrays
        Message.SENT_MESSAGE.clear();
        Message.DISREGARDED_MESSAGES.clear();
        Message.STORED_MESSAGES.clear();
        Message.MESSAGE_HASH.clear();
        Message.MESSAGE_ID.clear();
        Message.recipients.clear();
        Message.storedRecipients.clear();

        //  test data
        Message.SENT_MESSAGE.add("Did you get the cake?");
        Message.recipients.add("+27834557896");
        Message.MESSAGE_ID.add("ID1");
        Message.MESSAGE_HASH.add("HASH1");

        Message.SENT_MESSAGE.add("it is dinner time!");
        Message.recipients.add("0838884567");
        Message.MESSAGE_ID.add("ID2");
        Message.MESSAGE_HASH.add("HASH2");

        Message.STORED_MESSAGES.add("where are you? you are late! i have asked you to be on time.");
        Message.storedRecipients.add("+27838884567");
        Message.MESSAGE_ID.add("ID3");
        Message.MESSAGE_HASH.add("HASH3");

        Message.STORED_MESSAGES.add("Ok i am leaving without you");
        Message.storedRecipients.add("+27838884567");
        Message.MESSAGE_ID.add("ID4");
        Message.MESSAGE_HASH.add("HASH4");

        Message.DISREGARDED_MESSAGES.add("Yohooooo, i am at your gate");
    }

    @Test
    public void SENT_MESSAGE_ARRAY_CORRECTLY_POPULATED() {
        String actual = String.join(", ", Message.SENT_MESSAGE);
        String expected = "Did you get the cake?, it is dinner time!";
        assertEquals(expected, actual);
    }

    @Test
    public void DISPLAY_THE_LONGEST_MESSAGE() {
        String expected = "where are you? you are late! i have asked you to be on time.";
        assertEquals(expected, Message.findLongestMessage());
    }

    @Test
    public void SEARCH_FOR_messageID() {
        String expected = "it is dinner time!";
        String actual = Message.searchByMessageID("ID2");
        assertEquals(expected, actual);
    }

    @Test
    public void SEARCH_ALL_MESSAGES_SENT_OR_STORED_REGARDING_A_PARTICULAR_RECIPIENT() {
        String actual = Message.searchByRecipient("+27838884567");
        String expected = "where are you? you are late! i have asked you to be on time.\nOk i am leaving without you\n";
        assertEquals(expected, actual);
    }

    @Test
    public void DELETE_MESSAGE_USING_MESSAGE_HASH() {
        assertTrue(Message.deleteByHash("HASH3"));
        assertFalse(Message.MESSAGE_HASH.contains("HASH3"));
    }

    @Test
    public void DISPLAY_REPORT() {
        String expected =
                "Message Hash: HASH1\nRecipient: +27834557896\nMessage: Did you get the cake?\n\n" +
                "Message Hash: HASH2\nRecipient: 0838884567\nMessage: it is dinner time!\n\n" +
                "Message Hash: HASH3\nRecipient: +27838884567\nMessage: where are you? you are late! i have asked you to be on time.\n\n" +
                "Message Hash: HASH4\nRecipient: +27838884567\nMessage: Ok i am leaving without you\n\n";
        String actual = Message.generateReport();
        assertEquals(expected, actual);
    }
}
// OpenAI, 2025. ChatGPT version 4. Avaialble at :https://chatgpt.com/ Accessed 24 June 2025
