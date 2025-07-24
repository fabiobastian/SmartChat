package com.cloudhumans.smartchat.util;

public class TextUtils {

    private TextUtils() {}

    /**
     * Removes common English greetings from the beginning of a text.
     *
     * @param text the original text
     * @return the text without greetings
     */
    public static String removeGreetings(String text) {
        if (text == null || text.isBlank()) return text;

        // Remove greetings at the beginning of the message
        String cleaned = text.replaceAll(
                "(?i)^(\\s*(hi|hello|hey|good morning|good afternoon|good evening)[!,.\\s]*)+",
                ""
        );

        // Clean up extra spaces
        return cleaned.trim().replaceAll("\\s{2,}", " ");
    }
}

