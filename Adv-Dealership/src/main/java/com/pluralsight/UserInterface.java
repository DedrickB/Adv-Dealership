package com.pluralsight;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private Dealership dealership;
    private Scanner scanner;
    private DealershipFileManager dealershipFileManager;
    private ContractFileManager contractFileManager; // Added for saving contracts

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        // Initialize file managers here as they don't depend on dealership state
        this.dealershipFileManager = new DealershipFileManager();
        this.contractFileManager = new ContractFileManager();
    }

    private void init() {
        this.dealership = dealershipFileManager.getDealership();
        if (this.dealership == null) {
            System.out.println("Failed to load dealership data. Starting with a new (empty) dealership or exiting.");
            // For this project, if file load fails, the app might not be very useful.
            // A robust app might create a default dealership or prompt user.
        }
    }

    private String getCurrentDateString() {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // YYYYMMDD
    }

    private void displayMenu() {
        System.out.println("\n========== Dealership Menu ==========");
        if (dealership != null) {
            System.out.println("Current Dealership: " + dealership.getName());
        }
        System.out.println("1  - Find vehicles within a price range");
        System.out.println("2  - Find vehicles by make / model");
        System.out.println("3  - Find vehicles by year range");
        System.out.println("4  - Find vehicles by color");
        System.out.println("5  - Find vehicles by mileage range");
        System.out.println("6  - Find vehicles by type (Car, Truck, SUV, Van)");
        System.out.println("7  - List ALL vehicles");
        System.out.println("8  - Add a vehicle");
        System.out.println("9  - Remove a vehicle (from inventory, not sale)");
        System.out.println("10 - Sell / Lease a Vehicle"); // New Option
        System.out.println("99 - Quit");
        System.out.print("Enter your choice: ");
    }

    public void display() {
        init();

        if (this.dealership == null) {
            System.out.println("Critical error: Dealership data could not be initialized. Exiting application.");
            return;
        }

        boolean running = true;
        while (running) {
            displayMenu();
            try {
                int choice = -1;
                if (scanner.hasNextInt()){
                    choice = scanner.nextInt();
                }
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: processGetByPriceRequest(); break;
                    case 2: processGetByMakeModelRequest(); break;
                    case 3: processGetByYearRequest(); break;
                    case 4: processGetByColorRequest(); break;
                    case 5: processGetByMileageRequest(); break;
                    case 6: processGetByTypeRequest(); break;
                    case 7: processAllVehiclesRequest(); break;
                    case 8: processAddVehicleRequest(); break;
                    case 9: processRemoveVehicleRequest(); break;
                    case 10: processSellLeaseVehicleRequest(); break; // New case
                    case 99:
                        System.out.println("Thank you for using the Dealership Application. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number for menu choice.");
                if (scanner.hasNextLine()) scanner.nextLine(); // Consume the invalid input
            }  catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Good for debugging
            }
        }
        scanner.close();
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found matching your criteria.");
            return;
        }
        System.out.println("\n--- Search Results ---");
        System.out.printf("%-6s | %-5s | %-12s | %-15s | %-8s | %-10s | %-8s | %s\n",
                "VIN", "Year", "Make", "Model", "Type", "Color", "Odometer", "Price");
        System.out.println("-------|-------|--------------|-----------------|----------|------------|----------|-----------");
        for (Vehicle vehicle : vehicles) {
            System.out.printf("%-6d | %-5d | %-12s | %-15s | %-8s | %-10s | %-8d | $%-9.2f\n",
                    vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(),
                    vehicle.getVehicleType(), vehicle.getColor(), vehicle.getOdometer(), vehicle.getPrice());
        }
        System.out.println("---------------------------------------------------------------------------------------------");
    }


    private void processAllVehiclesRequest() {
        System.out.println("\n--- Listing All Vehicles ---");
        List<Vehicle> allVehicles = dealership.getAllVehicles();
        displayVehicles(allVehicles);
    }

    private void processGetByPriceRequest() {
        System.out.println("\n--- Find Vehicles by Price Range ---");
        try {
            System.out.print("Enter minimum price: ");
            double minPrice = scanner.nextDouble();
            System.out.print("Enter maximum price: ");
            double maxPrice = scanner.nextDouble();
            scanner.nextLine();

            if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
                System.out.println("Invalid price range. Please enter positive values with min <= max.");
                return;
            }
            List<Vehicle> vehicles = dealership.getVehiclesByPrice(minPrice, maxPrice);
            displayVehicles(vehicles);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for price. Please enter a number.");
            scanner.nextLine();
        }
    }

    private void processGetByMakeModelRequest() {
        System.out.println("\n--- Find Vehicles by Make/Model ---");
        System.out.print("Enter make: ");
        String make = scanner.nextLine().trim();
        System.out.print("Enter model: ");
        String model = scanner.nextLine().trim();

        if (make.isEmpty() && model.isEmpty()) { // Allow searching by only make or only model
            System.out.println("Please enter at least a make or a model.");
            return;
        }
        // Assuming Dealership's getVehiclesByMakeModel can handle one of them being empty/null
        // Or adjust the Dealership method. For now, this UI logic requires both.
        // If you want to search by make OR model, the Dealership method needs to change.
        // The prompt was "make / model", implying both.
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
            scanner.nextLine();

            if (minYear > maxYear || minYear < 1886 || maxYear > java.time.Year.now().getValue() + 1) {
                System.out.println("Invalid year range.");
                return;
            }
            List<Vehicle> vehicles = dealership.getVehiclesByYear(minYear, maxYear);
            displayVehicles(vehicles);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for year. Please enter a number.");
            scanner.nextLine();
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
            scanner.nextLine();

            if (minMileage < 0 || maxMileage < 0 || minMileage > maxMileage) {
                System.out.println("Invalid mileage range. Please enter positive values with min <= max.");
                return;
            }
            List<Vehicle> vehicles = dealership.getVehiclesByMileage(minMileage, maxMileage);
            displayVehicles(vehicles);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for mileage. Please enter a number.");
            scanner.nextLine();
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
            scanner.nextLine();

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
            dealershipFileManager.saveDealership(this.dealership);
            System.out.println("Vehicle added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Invalid data type entered. Please try again.");
            if(scanner.hasNextLine()) scanner.nextLine();
        }
    }

    private void processRemoveVehicleRequest() {
        System.out.println("\n--- Remove a Vehicle from Inventory (Not a Sale) ---");
        System.out.print("Enter VIN of the vehicle to remove: ");
        try {
            int vinToRemove = scanner.nextInt();
            scanner.nextLine();

            Vehicle vehicleToRemove = null;
            for (Vehicle v : dealership.getAllVehicles()) {
                if (v.getVin() == vinToRemove) {
                    vehicleToRemove = v;
                    break;
                }
            }

            if (vehicleToRemove != null) {
                dealership.removeVehicle(vehicleToRemove);
                dealershipFileManager.saveDealership(this.dealership);
                System.out.println("Vehicle with VIN " + vinToRemove + " removed from inventory.");
            } else {
                System.out.println("Vehicle with VIN " + vinToRemove + " not found in inventory.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid VIN format. Please enter a number.");
            if(scanner.hasNextLine()) scanner.nextLine();
        }
    }

    // New method for Sell/Lease
    private void processSellLeaseVehicleRequest() {
        System.out.println("\n--- Sell/Lease a Vehicle ---");
        System.out.print("Enter VIN of the vehicle: ");
        int vinToProcess;
        try {
            vinToProcess = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid VIN format. Please enter a number.");
            if(scanner.hasNextLine()) scanner.nextLine();
            return;
        }

        Vehicle vehicle = dealership.getAllVehicles().stream()
                .filter(v -> v.getVin() == vinToProcess)
                .findFirst()
                .orElse(null);

        if (vehicle == null) {
            System.out.println("Vehicle with VIN " + vinToProcess + " not found in inventory.");
            return;
        }

        System.out.println("Selected Vehicle:");
        displayVehicles(List.of(vehicle)); // Display single vehicle

        String contractDate = getCurrentDateString();
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();
        System.out.print("Enter customer email: ");
        String customerEmail = scanner.nextLine().trim();

        if (customerName.isEmpty() || customerEmail.isEmpty()) {
            System.out.println("Customer name and email cannot be empty. Transaction cancelled.");
            return;
        }

        System.out.print("Is this a (S)ale or a (L)ease? Enter S or L: ");
        String contractType = scanner.nextLine().trim().toUpperCase();

        if (contractType.equals("L")) {
            // Lease contract
            int currentYear = Year.now().getValue();
            if ((currentYear - vehicle.getYear()) > 3) {
                System.out.println("Vehicle is too old to lease (must be 3 years old or newer). Transaction cancelled.");
                return;
            }

            LeaseContract leaseContract = new LeaseContract(contractDate, customerName, customerEmail, vehicle);
            System.out.println("\n--- Lease Contract Details ---");
            System.out.printf("Vehicle Price: $%.2f\n", vehicle.getPrice());
            System.out.printf("Expected Ending Value: $%.2f\n", leaseContract.getExpectedEndingValue());
            System.out.printf("Lease Fee: $%.2f\n", leaseContract.getLeaseFee());
            System.out.printf("Amount to be Financed (Lease Principal): $%.2f\n", leaseContract.getTotalPrice());
            System.out.printf("Interest Rate: %.2f%%\n", 4.0);
            System.out.printf("Lease Term: %d months\n", 36);
            System.out.printf("Monthly Payment: $%.2f\n", leaseContract.getMonthlyPayment());

            System.out.print("\nConfirm lease? (Y/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                contractFileManager.saveContract(leaseContract);
                dealership.removeVehicle(vehicle);
                dealershipFileManager.saveDealership(this.dealership);
                System.out.println("Lease contract created successfully and vehicle removed from inventory.");
            } else {
                System.out.println("Lease cancelled.");
            }

        } else if (contractType.equals("S")) {
            // Sales contract
            System.out.print("Do you want to finance this purchase? (Y/N): ");
            boolean isFinanced = scanner.nextLine().trim().equalsIgnoreCase("Y");

            SalesContract salesContract = new SalesContract(contractDate, customerName, customerEmail, vehicle, isFinanced);
            System.out.println("\n--- Sales Contract Details ---");
            System.out.printf("Vehicle Price: $%.2f\n", vehicle.getPrice());
            System.out.printf("Sales Tax (%.1f%%): $%.2f\n", 5.0, salesContract.getSalesTaxAmount());
            System.out.printf("Recording Fee: $%.2f\n", salesContract.getRecordingFee());
            System.out.printf("Processing Fee: $%.2f\n", salesContract.getProcessingFee());
            System.out.printf("Total Sale Price: $%.2f\n", salesContract.getTotalPrice());
            System.out.printf("Financed: %s\n", salesContract.isFinanced() ? "YES" : "NO");

            if (salesContract.isFinanced()) {
                double interestRate = (vehicle.getPrice() >= 10000) ? 4.25 : 5.25;
                int term = (vehicle.getPrice() >= 10000) ? 48 : 24;
                System.out.printf("Interest Rate: %.2f%%\n", interestRate);
                System.out.printf("Loan Term: %d months\n", term);
                System.out.printf("Monthly Payment: $%.2f\n", salesContract.getMonthlyPayment());
            } else {
                System.out.println("Monthly Payment: $0.00 (Not financed)");
            }

            System.out.print("\nConfirm sale? (Y/N): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                contractFileManager.saveContract(salesContract);
                dealership.removeVehicle(vehicle);
                dealershipFileManager.saveDealership(this.dealership);
                System.out.println("Sales contract created successfully and vehicle removed from inventory.");
            } else {
                System.out.println("Sale cancelled.");
            }
        } else {
            System.out.println("Invalid contract type. Transaction cancelled.");
        }
    }
}