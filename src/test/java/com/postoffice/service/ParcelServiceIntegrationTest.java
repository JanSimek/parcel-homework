package com.postoffice.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParcelServiceIntegrationTest {

    static final double DELTA = 0.0001;

    static final String FILE_PATH = "src/test/resources/";
    static final String PARCELS_FILE = FILE_PATH + "parcels.txt";
    static final String FEES_FILE = FILE_PATH + "fees.txt";

    @Test
    void testParcelService() throws IOException {

        File parcels = new File(PARCELS_FILE);
        File fees = new File(FEES_FILE);

        ParcelService parcelService = new ParcelService(parcels, fees);
        var postalCodeTotals = parcelService.getPostalCodeTotalWeight();

        assertEquals(15.96f, postalCodeTotals.get("08801"), DELTA);
        assertEquals(2f, postalCodeTotals.get("90005"), DELTA);
        assertEquals(5.5f, postalCodeTotals.get("08079"), DELTA);
        assertEquals(3.2f, postalCodeTotals.get("09300"), DELTA);

        assertEquals(5.0, parcelService.calculatePrice(10f));
        assertEquals(5.0, parcelService.calculatePrice(10.1f));
        assertEquals(2.5, parcelService.calculatePrice(6f));
        assertEquals(2.0, parcelService.calculatePrice(3.5f));
        assertEquals(1.5, parcelService.calculatePrice(2.2f));
        assertEquals(1.0, parcelService.calculatePrice(1.99f));
        assertEquals(0.7, parcelService.calculatePrice(0.9f));
        assertEquals(0.5, parcelService.calculatePrice(0.3f));
        assertEquals(0.5, parcelService.calculatePrice(0.1f));
    }
}
