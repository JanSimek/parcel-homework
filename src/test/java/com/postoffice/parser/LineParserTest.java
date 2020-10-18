package com.postoffice.parser;

import com.postoffice.parser.exception.LineParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

class LineParserTest {

    LineParser parser = spy(LineParser.class);

    @Test
    void testCountDecimalPlaces() {
        assertEquals(0, parser.countDecimalPlaces("1"));
        assertEquals(1, parser.countDecimalPlaces("2.1"));
        assertEquals(2, parser.countDecimalPlaces("3.21"));
        assertEquals(3, parser.countDecimalPlaces("4.333"));
    }

    @Test
    void testTokenizeLine() {
        String[] tokens = parser.tokenizeLine("one two");

        assertEquals(2, tokens.length);
        assertEquals("one", tokens[0]);
        assertEquals("two", tokens[1]);

        Exception exception = assertThrows(LineParserException.class, () -> {
            parser.tokenizeLine("this should fail");
        });
        assertTrue(exception.getMessage().startsWith("Expected only 2 elements but found 3"));
    }

    @Test
    void testParseWeight() throws IOException {

        assertEquals(9.999f, parser.parseWeight("9.999"));

        Exception ex = assertThrows(LineParserException.class, () -> parser.parseWeight("X"));
        assertTrue(ex.getMessage().startsWith("Invalid weight format"));

        ex = assertThrows(LineParserException.class, () -> parser.parseWeight("5.4321"));
        assertTrue(ex.getMessage().startsWith("Weight should not have more than 3 decimal places"));

        ex = assertThrows(LineParserException.class, () -> parser.parseWeight("-1.2"));
        assertTrue(ex.getMessage().startsWith("Weight cannot be negative"));
    }
}
