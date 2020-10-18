package com.postoffice.app;

import com.postoffice.service.ParcelService;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.File;

@Command(name = "PostOffice", version="1.0", mixinStandardHelpOptions = true, sortOptions = false)
public class Main implements Runnable {

    @Option(names = {"-p", "--parcels"}, description = "File containing package list",
            required = true, paramLabel = "parcels.txt")
    File parcels;

    @Option(names = {"-f", "--fees"}, description = "File containing shipping fees",
            required = false, paramLabel = "fees.txt")
    File fees;

    @Override
    public void run() {
        try {
            ParcelService service = new ParcelService(parcels, fees);
            service.startIOLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

}
