package com.pluralsight;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ContractFileManager {
    private static final String CONTRACTS_FILE_NAME = "contracts.csv";

    public void saveContract(Contract contract) {
        // Append to the contracts file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONTRACTS_FILE_NAME, true))) {
            Vehicle vehicle = contract.getVehicleSold();
            String vehicleInfo = String.format("%d|%d|%s|%s|%s|%s|%d|%.2f",
                    vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(),
                    vehicle.getVehicleType(), vehicle.getColor(), vehicle.getOdometer(), vehicle.getPrice());

            String commonContractInfo = String.format("%s|%s|%s|%s",
                    contract.getContractDate(), contract.getCustomerName(), contract.getCustomerEmail(), vehicleInfo);

            if (contract instanceof SalesContract) {
                SalesContract salesContract = (SalesContract) contract;
                // SALE|date|name|email|VIN|Year|Make|Model|Type|Color|Odometer|OrigPrice|salesTaxAmt|recFee|procFee|totalSalePrice|isFinanced(String)|monthlyPayment
                String salesData = String.format("SALE|%s|%.2f|%.2f|%.2f|%.2f|%s|%.2f\n",
                        commonContractInfo,
                        salesContract.getSalesTaxAmount(),
                        salesContract.getRecordingFee(),
                        salesContract.getProcessingFee(),
                        salesContract.getTotalPrice(), // Total sale price
                        salesContract.isFinanced() ? "YES" : "NO",
                        salesContract.getMonthlyPayment());
                writer.write(salesData);
            } else if (contract instanceof LeaseContract) {
                LeaseContract leaseContract = (LeaseContract) contract;
                // LEASE|date|name|email|VIN|Year|Make|Model|Type|Color|Odometer|OrigPrice|expectedEndValue|leaseFee|leasePrincipalAmt|monthlyPayment
                String leaseData = String.format("LEASE|%s|%.2f|%.2f|%.2f|%.2f\n",
                        commonContractInfo,
                        leaseContract.getExpectedEndingValue(),
                        leaseContract.getLeaseFee(),
                        leaseContract.getTotalPrice(), // Lease principal amount
                        leaseContract.getMonthlyPayment());
                writer.write(leaseData);
            }
        } catch (IOException e) {
            System.err.println("Error saving contract to file: " + e.getMessage());
            // Consider a more robust error handling strategy for a real application
        }
    }
}