package com.postoffice.parser;

import com.postoffice.model.Parcel;
import com.postoffice.parser.exception.LineParserException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FeeParserTest {

    FeeParser parser = new FeeParser();

    String feeList = "10 5.00\n" +
                    "5 2.50\n" +
                    "3 2.00\n" +
                    "2 1.50\n" +
                    "1 1.00\n" +
                    "0.5 0.70\n" +
                    "0.2 0.50\n";

    @Test
    void testLoadParcels() throws IOException {
        Float[] weights = getFees(feeList).keySet().toArray(new Float[0]);
        Double[] fees = getFees(feeList).values().toArray(new Double[0]);

        assertEquals(7, weights.length);

        assertEquals(10f, weights[0]);
        assertEquals(5.0, fees[0]);

        assertEquals(5f, weights[1]);
        assertEquals(2.5, fees[1]);

        assertEquals(3f, weights[2]);
        assertEquals(2.0, fees[2]);

        assertEquals(2f, weights[3]);
        assertEquals(1.5, fees[3]);

        assertEquals(1f, weights[4]);
        assertEquals(1.0, fees[4]);

        assertEquals(0.5f, weights[5]);
        assertEquals(0.7, fees[5]);

        assertEquals(0.2f, weights[6]);
        assertEquals(0.5, fees[6]);
    }

    @Test
    void testInvalidFee() {
        Exception ex = assertThrows(LineParserException.class, () -> getFees(feeList + "1 -1.11"));
        assertEquals("Fee cannot be negative", ex.getMessage());

        ex = assertThrows(LineParserException.class, () -> getFees(feeList + "1 1.1"));
        assertEquals("Fee must have exactly 2 decimal places", ex.getMessage());
    }

    TreeMap<Float, Double> getFees(String testList) throws IOException {
        TreeMap<Float, Double> fees = new TreeMap<>(Comparator.reverseOrder());
        try (var reader = new BufferedReader(new StringReader(testList))) {
            parser.load(reader, fees::put);
        }
        return fees;
    }
}
