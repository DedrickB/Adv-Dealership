package com.pluralsight;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Dealership {
    private String name;
    private String address;
    private String phone;
    private ArrayList<Vehicle> inventory;

    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.inventory = new ArrayList<>(); // Instantiate the ArrayList
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Methods for inventory management
    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(this.inventory); // Return a copy
    }

    public void addVehicle(Vehicle vehicle) {
        this.inventory.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        // Implementation later
        // For now, to ensure successful removal for later phase testing, we'll implement a basic removal.
        // It's better to remove by a unique identifier like VIN if passed,
        // but the prompt indicates passing a Vehicle object.
        this.inventory.remove(vehicle);
    }

    // Search methods (stubs for now, returning empty list is better than null for collections)
    public List<Vehicle> getVehiclesByPrice(double minPrice, double maxPrice) {
        return this.inventory.stream()
                .filter(v -> v.getPrice() >= minPrice && v.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        return this.inventory.stream()
                .filter(v -> v.getMake().equalsIgnoreCase(make) && v.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByYear(int minYear, int maxYear) {
        return this.inventory.stream()
                .filter(v -> v.getYear() >= minYear && v.getYear() <= maxYear)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByColor(String color) {
        return this.inventory.stream()
                .filter(v -> v.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByMileage(int minMileage, int maxMileage) {
        return this.inventory.stream()
                .filter(v -> v.getOdometer() >= minMileage && v.getOdometer() <= maxMileage)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByType(String vehicleType) {
        return this.inventory.stream()
                .filter(v -> v.getVehicleType().equalsIgnoreCase(vehicleType))
                .collect(Collectors.toList());
    }
}
