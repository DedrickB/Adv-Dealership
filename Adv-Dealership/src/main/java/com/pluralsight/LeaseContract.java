package com.pluralsight;
public class LeaseContract extends Contract {
    private static final double EXPECTED_ENDING_VALUE_PERCENTAGE = 0.50; // 50% of original price
    private static final double LEASE_FEE_PERCENTAGE = 0.07; // 7% of original price
    private static final double INTEREST_RATE = 4.0; // Annual %
    private static final int LEASE_TERM = 36; // months

    private double expectedEndingValue;
    private double leaseFee;

    public LeaseContract(String contractDate, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(contractDate, customerName, customerEmail, vehicleSold);
        // Vehicle age check should be done before creating the contract ideally
        // For now, we assume it's checked externally or this class might throw an error if rule violated.
        this.expectedEndingValue = vehicleSold.getPrice() * EXPECTED_ENDING_VALUE_PERCENTAGE;
        this.leaseFee = vehicleSold.getPrice() * LEASE_FEE_PERCENTAGE;
    }

    // Getters
    public double getExpectedEndingValue() {
        return expectedEndingValue;
    }

    public double getLeaseFee() {
        return leaseFee;
    }

    @Override
    public double getTotalPrice() {
        // For a lease, "total price" here means the amount to be financed (depreciation + lease fee)
        return (getVehicleSold().getPrice() - this.expectedEndingValue) + this.leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        double principal = getTotalPrice(); // This is the amount financed for the lease
        return calculateMonthlyPayment(principal, INTEREST_RATE, LEASE_TERM);
    }
}