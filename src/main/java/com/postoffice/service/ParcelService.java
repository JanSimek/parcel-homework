package com.postoffice.service;

import com.postoffice.model.Parcel;
import com.postoffice.parser.LineParser;
import com.postoffice.parser.ParcelParser;
import com.postoffice.parser.FeeParser;
import com.postoffice.parser.exception.LineParserException;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ParcelService {

    private final ParcelParser parcelParser = new ParcelParser();
    private final FeeParser feeParser = new FeeParser();

    private final List<Parcel> parcelList = new CopyOnWriteArrayList<>();

    /**
     * Contains <b>weight</b> as key and the corresponding <b>price</b> as value.
     * Weights are sorted from the heaviest to the lightest.
     * Once the data is loaded, this map shall not be modified from other threads.
     */
    private final TreeMap<Float, Double> weightFeeList = new TreeMap<>(Comparator.reverseOrder());

    public ParcelService(File parcels, File fees) throws IOException {
        loadData(parcels, fees);
    }

    private void loadData(File parcels, File fees) throws IOException {

        if (parcels == null) {
            throw new IllegalArgumentException("Parcel file must be supplied");
        }

        loadFromFile(parcelParser, parcels, this::addParcel);

        // Fees are optional
        if (fees != null) {
            loadFromFile(feeParser, fees, weightFeeList::put);
        }
    }

    private void addParcel(Float weight, String postalCode) {
        parcelList.add(new Parcel(weight, postalCode));
    }

    private <K, V> void loadFromFile(LineParser<K, V> parser, File file, BiConsumer<K, V> resultHandler) throws IOException {
        if (!file.isFile()) {
            throw new IllegalArgumentException("Only files can be parsed. Invalid parameter: " + file.getPath());
        }
        try (var reader = new BufferedReader(new FileReader(file))) {
            parser.load(reader, resultHandler);
        }
    }

    public void startIOLoop() {
        AtomicBoolean running = new AtomicBoolean(true);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        // Periodically print out total package weight and shipping cost per each postal code
        executorService.scheduleWithFixedDelay(() -> getPostalCodeTotalWeight().entrySet().stream()
                .sorted(Map.Entry.<String, Float>comparingByValue(Comparator.reverseOrder()))
                .forEach(this::printStatistics),
                0,1, TimeUnit.MINUTES);

        // Read user input from console, user enters line consisting of weight of package and destination postal code
        executorService.submit(() -> {
            while (running.get()) {
                Console console = System.console();
                String prompt = console.readLine("\n");

                if ("quit".equalsIgnoreCase(prompt)) {
                    running.set(false);
                    executorService.shutdownNow();
                    break;
                }

                try {
                    parcelParser.parseLine(prompt, this::addParcel);
                } catch (LineParserException e) {
                    System.err.println("ERROR: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Prints postal code, total weight of all packages for that postal code and shipping fee (if available).
     * Sample output: 08801 15.960 7.00
     * @param entry with postal code as the key and total weight as the value
     */
    private void printStatistics(Map.Entry<String, Float> entry) {
        String postalCode = entry.getKey();
        Float totalWeight = entry.getValue();
        String totalPrice = (!weightFeeList.isEmpty() ? String.format(Locale.US, "%.2f", calculatePrice(totalWeight)) : "");

        System.out.printf(Locale.US, "%s %.3f %s%n", postalCode, totalWeight, totalPrice);
    }

    /**
     * Group parcels by the postal code and add up their weights
     */
    public Map<String, Float> getPostalCodeTotalWeight() {
        return parcelList.stream().collect(Collectors.groupingBy(
                Parcel::getPostalCode,
                Collectors.reducing(0f, Parcel::getWeight, Float::sum))
        );
    }

    /**
     * Calculate shipping cost for the given package <code>weight</code>
     * @param weight of the package
     * @return price corresponding to the <code>weight</code> class of the package
     *          or the lowest price available if the package is lighter than any weight class
     */
    public Double calculatePrice(Float weight) {
        return weightFeeList.entrySet().stream()
                .filter(e -> weight >= e.getKey())
                .map(Map.Entry::getValue)
                .findFirst().orElse(weightFeeList.lastEntry().getValue());
    }
}
