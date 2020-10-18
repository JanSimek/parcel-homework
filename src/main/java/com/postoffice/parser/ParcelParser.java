package com.postoffice.parser;

import com.postoffice.parser.exception.LineParserException;

import java.util.function.BiConsumer;

public class ParcelParser implements LineParser<Float, String> {

    /**
     * Parse weight of package and destination postal code
     *
     * @param line Input line format:
     *             <code>weight: positive number, >0, maximal 3 decimal places, . (dot) as decimal separator (space)
     *             postal code: fixed 5 digits</code>
     * @param resultHandler functional interface to process each item
     */
    @Override
    public void parseLine(String line, BiConsumer<Float, String> resultHandler) {

        String[] tokens = tokenizeLine(line);

        Float weight = parseWeight(tokens[0]);
        String postalCode = parsePostalCode(tokens[1]);

        resultHandler.accept(weight, postalCode);
    }

    /**
     * @param token postal code exactly 5 characters long
     * @return postal code
     */
    private String parsePostalCode(String token) {
        if (token.length() != 5) {
            throw new LineParserException("Invalid postal code. Expected 5 letters but got " + token.length());
        }
        return token;
    }
}
