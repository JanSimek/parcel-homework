package com.postoffice.parser;

import com.postoffice.model.Parcel;
import com.postoffice.parser.exception.LineParserException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParcelParserTest {

    ParcelParser parser = new ParcelParser();

    String parcelList = "3.4 08801\n"
                        + "2 90005\n"
                        + "12.56 08801\n"
                        + "5.5 08079\n"
                        + "3.2 09300\n";

    @Test
    void testLoadParcels() throws IOException {
        List<Parcel> parcels = getParcels(parcelList);

        assertEquals(5, parcels.size());

        assertEquals("08801", parcels.get(0).getPostalCode());
        assertEquals(3.4f, parcels.get(0).getWeight());

        assertEquals("90005", parcels.get(1).getPostalCode());
        assertEquals(2f, parcels.get(1).getWeight());

        assertEquals("08801", parcels.get(2).getPostalCode());
        assertEquals(12.56f, parcels.get(2).getWeight());

        assertEquals("08079", parcels.get(3).getPostalCode());
        assertEquals(5.5f, parcels.get(3).getWeight());

        assertEquals("09300", parcels.get(4).getPostalCode());
        assertEquals(3.2f, parcels.get(4).getWeight());
    }

    @Test
    void testInvalidPostalCode() throws IOException {
        Exception ex = assertThrows(LineParserException.class, () -> getParcels(parcelList + "1.1 ABC"));
        assertTrue(ex.getMessage().startsWith("Invalid postal code. Expected 5 letters but got 3"));
    }

    List<Parcel> getParcels(String testList) throws IOException {
        List<Parcel> parcels = new ArrayList<>();
        try (var reader = new BufferedReader(new StringReader(testList))) {
            parser.load(reader, (Float weight, String postalCode) -> {
                parcels.add(new Parcel(weight, postalCode));
            });
        }
        return parcels;
    }
}
