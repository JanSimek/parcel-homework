package com.postoffice.parser;

import com.postoffice.parser.exception.LineParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.BiConsumer;

public interface LineParser<K, V> {

    /**
     * Load and parse items separated by new lines
     * @param reader stream to load
     * @param resultHandler functional interface for batch processing items
     */
    default void load(BufferedReader reader, BiConsumer<K, V> resultHandler) throws IOException {

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isBlank()) {
                parseLine(line, resultHandler);
            }
        }
    }

    default String[] tokenizeLine(String line) {

        String[] tokens = line.split(" ");
        if (tokens.length != 2) {
            throw new LineParserException("Expected only 2 elements but found " + tokens.length + " instead on line: " + line);
        }
        return tokens;
    }

    default int countDecimalPlaces(String token) {

        String[] div = token.split("\\.");
        return div.length == 1 ? 0 : div[1].length();
    }

    /**
     * Implementation-specific parser function
     * @param line raw string from file/stream
     * @param resultHandler functional interface to process each item
     */
    void parseLine(String line, BiConsumer<K, V> resultHandler);

    /**
     * @param token weight as a valid positive number of up to 3 decimal places using dot as decimal separator
     * @return weight converted to Float
     */
    default Float parseWeight(String token) {
        try {
            Float weight = Float.valueOf(token);

            if (weight <= 0) {
                throw new LineParserException("Weight cannot be negative");
            }

            if (countDecimalPlaces(token) > 3) {
                throw new LineParserException("Weight should not have more than 3 decimal places");
            }

            return weight;
        } catch (NumberFormatException ex) {
            throw new LineParserException("Invalid weight format", ex);
        }
    }
}
