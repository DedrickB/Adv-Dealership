package com.pluralsight;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class UserInterface {
    private Dealership dealership;
    private Scanner scanner;
    private DealershipFileManager fileManager; // Added for saving

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.fileManager = new DealershipFileManager(); // Instantiate file manager
    }

    private void init() {
        // DealershipFileManager fileManager = new DealershipFileManager(); // Moved to constructor
        this.dealership = fileManager.getDealership();
        if (this.dealership == null) {
            // Handle case where dealership couldn't be loaded (e.g., file not found, corrupted)
            // For now, we might create a default empty dealership or exit.
            System.out.println("Failed to load dealership data. Starting with a new (empty) dealership or exiting.");
            // Example: Create a default if loading fails
            // this.dealership = new Dealership("Default Dealership", "123 Main St", "555-0000");
            // For this project, if file load fails, the app might not be very useful.
            // Let's assume getDealership prints an error and might return null.
            // The display() method will need to handle a null dealership.
        }
    }

    private void displayMenu() {
        System.out.println("\n========== Dealership Menu ==========");
        if (dealership != null) {
            System.out.println("Current Dealership: " + dealership.getName());
        }
        System.out.println("1 - Find vehicles within a price range");
        System.out.println("2 - Find vehicles by make / model");
        System.out.println("3 - Find vehicles by year range");
        System.out.println("4 - Find vehicles by color");
        System.out.println("5 - Find vehicles by mileage range");
        System.out.println("6 - Find vehicles by type (Car, Truck, SUV, Van)");
        System.out.println("7 - List ALL vehicles");
        System.out.println("8 - Add a vehicle");
        System.out.println("9 - Remove a vehicle");
        System.out.println("99 - Quit");
        System.out.print("Enter your choice: ");
    }

    public void display() {
        init(); // Load the dealership

        if (this.dealership == null) {
            System.out.println("Critical error: Dealership data could not be initialized. Exiting application.");
            return; // Exit if dealership is null after init
        }

        boolean running = true;
        while (running) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        processGetByPriceRequest();
                        break;
                    case 2:
                        processGetByMakeModelRequest();
                        break;
                    case 3:
                        processGetByYearRequest();
                        break;
                    case 4:
                        processGetByColorRequest();
                        break;
                    case 5:
                        processGetByMileageRequest();
                        break;
                    case 6:
                        processGetByTypeRequest();
                        break;
                    case 7:
                        processAllVehiclesRequest();
                        break;
                    case 8:
                        processAddVehicleRequest();
                        break;
                    case 9:
                        processRemoveVehicleRequest();
                        break;
                    case 99:
                        System.out.println("Thank you for using the Dealership Application. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
        scanner.close(); // Close scanner when application exits
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found matching your criteria.");
            return;
        }
        System.out.println("\n--- Search Results ---");
        System.out.println("VIN    | Year  | Make       | Model      | Type  | Color    | Odometer | Price");
        System.out.println("-------|-------|------------|------------|-------|----------|----------|----------");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.toString());
        }
        System.out.println("----------------------");
    }

    private void processAllVehiclesRequest() {
        System.out.println("\n--- Listing All Vehicles ---");
        List<Vehicle> allVehicles = dealership.getAllVehicles();
        displayVehicles(allVehicles);
    }

    // Stubs for other process methods (to be implemented in Phase 5)
    private void processGetByPriceRequest() {
        System.out.println("\n--- Find Vehicles by Price Range ---");
        try {
            System.out.print("Enter minimum price: ");
            double minPrice = scanner.nextDouble();
            System.out.print("Enter maximum price: ");
            double maxPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
                System.out.println("Invalid price range. Please enter positive values with min <= max.");
                return;
            }
            List<Vehicle> vehicles = dealership.getVehiclesByPrice(minPrice, maxPrice);
            displayVehicles(vehicles);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for price. Please enter a number.");
            scanner.nextLine(); // Consume invalid input
        }
    }

    private void processGetByMakeModelRequest() {
        System.out.println("\n--- Find Vehicles by Make/Model ---");
        System.out.print("Enter make: ");
        String make = scanner.nextLine().trim();
        System.out.print("Enter model: ");
        String model = scanner.nextLine().trim();

        if (make.isEmpty() || model.isEmpty()) {
            System.out.println("Make and Model cannot be empty.");
            return;
        }
        List<Vehicle> vehicles = dealership.getVehiclesByMakeModel(make, model);
        displayVehicles(vehicles);
    }

    private void processGetByYearRequest() {
        System.out.println("\n--- Find Vehicles by Year Range ---");
        try {
            System.out.print("Enter minimum year: ");
            int minYear = scanner.nextInt();
            System.out.print("Enter maximum year: ");
            int maxYear = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (minYear > maxYear || minYear < 1886 || maxYear > java.time.Year.now().getValue() + 1) { // Basic validation
                System.out.println("Invalid year range.");
                return;
            }
            List<Vehicle> vehicles = dealership.getVehiclesByYear(minYear, maxYear);
            displayVehicles(vehicles);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for year. Please enter a number.");
            scanner.nextLine(); // Consume invalid input
        }
    }

    private void processGetByColorRequest() {
        System.out.println("\n--- Find Vehicles by Color ---");
        System.out.print("Enter color: ");
        String color = scanner.nextLine().trim();
        if (color.isEmpty()) {
            System.out.println("Color cannot be empty.");
            return;
        }
        List<Vehicle> vehicles = dealership.getVehiclesByColor(color);
        displayVehicles(vehicles);
    }

    private void processGetByMileageRequest() {
        System.out.println("\n--- Find Vehicles by Mileage Range ---");
        try {
            System.out.print("Enter minimum mileage: ");
            int minMileage = scanner.nextInt();
            System.out.print("Enter maximum mileage: ");
            int maxMileage = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (minMileage < 0 || maxMileage < 0 || minMileage > maxMileage) {
                System.out.println("Invalid mileage range. Please enter positive values with min <= max.");
                return;
            }
            List<Vehicle> vehicles = dealership.getVehiclesByMileage(minMileage, maxMileage);
            displayVehicles(vehicles);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for mileage. Please enter a number.");
            scanner.nextLine(); // Consume invalid input
        }
    }

    private void processGetByTypeRequest() {
        System.out.println("\n--- Find Vehicles by Type ---");
        System.out.print("Enter vehicle type (Car, Truck, SUV, Van): ");
        String type = scanner.nextLine().trim();
        if (type.isEmpty()) {
            System.out.println("Vehicle type cannot be empty.");
            return;
        }
        List<Vehicle> vehicles = dealership.getVehiclesByType(type);
        displayVehicles(vehicles);
    }

    private void processAddVehicleRequest() {
        System.out.println("\n--- Add a New Vehicle ---");
        try {
            System.out.print("Enter VIN: ");
            int vin = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Check for duplicate VIN
            for(Vehicle v : dealership.getAllVehicles()){
                if(v.getVin() == vin){
                    System.out.println("Error: A vehicle with this VIN already exists.");
                    return;
                }
            }

            System.out.print("Enter Year: ");
            int year = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Make: ");
            String make = scanner.nextLine().trim();

            System.out.print("Enter Model: ");
            String model = scanner.nextLine().trim();

            System.out.print("Enter Vehicle Type (e.g., Car, Truck, SUV, Van): ");
            String vehicleType = scanner.nextLine().trim();

            System.out.print("Enter Color: ");
            String color = scanner.nextLine().trim();

            System.out.print("Enter Odometer reading: ");
            int odometer = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Price: $");
            double price = scanner.nextDouble();
            scanner.nextLine();

            if (make.isEmpty() || model.isEmpty() || vehicleType.isEmpty() || color.isEmpty() || year < 1886 || odometer < 0 || price < 0) {
                System.out.println("Invalid input for one or more fields. Vehicle not added.");
                return;
            }

            Vehicle newVehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
            dealership.addVehicle(newVehicle);
            fileManager.saveDealership(this.dealership); // Save changes
            System.out.println("Vehicle added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid data type entered. Please try again.");
            scanner.nextLine(); // consume the rest of the invalid line
        }
    }

    private void processRemoveVehicleRequest() {
        System.out.println("\n--- Remove a Vehicle ---");
        System.out.print("Enter VIN of the vehicle to remove: ");
        try {
            int vinToRemove = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Vehicle vehicleToRemove = null;
            for (Vehicle v : dealership.getAllVehicles()) { // Use getAllVehicles to iterate over a copy if modifying
                if (v.getVin() == vinToRemove) {
                    vehicleToRemove = v;
                    break;
                }
            }

            if (vehicleToRemove != null) {
                dealership.removeVehicle(vehicleToRemove); // Assumes Dealership.removeVehicle works with the object
                fileManager.saveDealership(this.dealership); // Save changes
                System.out.println("Vehicle with VIN " + vinToRemove + " removed successfully.");
            } else {
                System.out.println("Vehicle with VIN " + vinToRemove + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid VIN format. Please enter a number.");
            scanner.nextLine(); // Consume invalid input
        }
    }
}