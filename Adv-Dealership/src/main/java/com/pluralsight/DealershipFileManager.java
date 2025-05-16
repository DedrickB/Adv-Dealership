package com.pluralsight;

import java.io.*;
import java.util.List;

public class DealershipFileManager {
    private static final String FILE_NAME = "inventory.csv"; // Relative path to the project root

    public Dealership getDealership() {
        Dealership dealership = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            // Read dealership info (first line)
            if ((line = reader.readLine()) != null) {
                String[] dealershipData = line.split("\\|");
                if (dealershipData.length == 3) {
                    dealership = new Dealership(dealershipData[0], dealershipData[1], dealershipData[2]);
                } else {
                    System.err.println("Error: Dealership information line is malformed.");
                    return null; // Or throw an exception
                }
            } else {
                System.err.println("Error: Dealership file is empty or dealership info missing.");
                return null; // Or throw an exception
            }

            // Read vehicle inventory (subsequent lines)
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split("\\|");
                if (vehicleData.length == 8) {
                    try {
                        int vin = Integer.parseInt(vehicleData[0]);
                        int year = Integer.parseInt(vehicleData[1]);
                        String make = vehicleData[2];
                        String model = vehicleData[3];
                        String vehicleType = vehicleData[4];
                        String color = vehicleData[5];
                        int odometer = Integer.parseInt(vehicleData[6]);
                        double price = Double.parseDouble(vehicleData[7]);

                        Vehicle vehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
                        dealership.addVehicle(vehicle);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing vehicle data: " + line + " - " + e.getMessage());
                        // Optionally skip this vehicle or stop processing
                    }
                } else {
                    System.err.println("Error: Vehicle data line is malformed: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: Inventory file not found: " + FILE_NAME);
            // Potentially create a new empty dealership or prompt user
            // For this project, we'll assume the file should exist.
        } catch (IOException e) {
            System.err.println("Error reading inventory file: " + e.getMessage());
        }
        return dealership;
    }

    public void saveDealership(Dealership dealership) {
        // Implementation for Phase 5
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) { // false to overwrite
            // Write dealership info
            writer.write(String.format("%s|%s|%s\n",
                    dealership.getName(), dealership.getAddress(), dealership.getPhone()));

            // Write vehicle inventory
            List<Vehicle> vehicles = dealership.getAllVehicles();
            for (Vehicle vehicle : vehicles) {
                writer.write(String.format("%d|%d|%s|%s|%s|%s|%d|%.2f\n",
                        vehicle.getVin(),
                        vehicle.getYear(),
                        vehicle.getMake(),
                        vehicle.getModel(),
                        vehicle.getVehicleType(),
                        vehicle.getColor(),
                        vehicle.getOdometer(),
                        vehicle.getPrice()));
            }
        } catch (IOException e) {
            System.err.println("Error saving dealership data: " + e.getMessage());
        }
    }
}