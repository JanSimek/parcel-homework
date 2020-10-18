package com.postoffice.parser;

import com.postoffice.parser.exception.LineParserException;

import java.util.function.BiConsumer;

public class FeeParser implements LineParser<Float, Double> {

    /**
     * Parse fees related to package weight
     *
     * @param line Input line format:
     *             <code>weight: positive number, >0, maximal 3 decimal places, . (dot) as decimal separator (space)
     *             fee: positive number, >=0, fixed two decimals, . (dot) as decimal separator</code>
     * @param resultHandler functional interface to process each item
     */
    @Override
    public void parseLine(String line, BiConsumer<Float, Double> resultHandler) {

        String[] tokens = tokenizeLine(line);

        Float weight = parseWeight(tokens[0]);
        Double fee = parseFee(tokens[1]);

        resultHandler.accept(weight, fee);
    }

    /**
     * @param token fee as a valid positive number with exactly 2 decimal places using dot as decimal separator
     * @return fee converted to Double
     */
    private Double parseFee(String token) {
        try {
            Double fee = Double.valueOf(token);

            if (fee < 0) {
                throw new LineParserException("Fee cannot be negative");
            }

            if (countDecimalPlaces(token) != 2) {
                throw new LineParserException("Fee must have exactly 2 decimal places");
            }

            return fee;
        } catch (NumberFormatException ex) {
            throw new LineParserException("Invalid shipping cost format", ex);
        }
    }
}
