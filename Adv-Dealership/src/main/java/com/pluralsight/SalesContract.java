package com.pluralsight;

public class SalesContract extends Contract {
    private static final double SALES_TAX_RATE = 0.05; // 5%
    private static final double RECORDING_FEE = 100.00;
    private static final double PROCESSING_FEE_UNDER_10K = 295.00;
    private static final double PROCESSING_FEE_10K_AND_OVER = 495.00;

    private static final double INTEREST_RATE_10K_AND_OVER = 4.25; // Annual %
    private static final int LOAN_TERM_10K_AND_OVER = 48; // months
    private static final double INTEREST_RATE_UNDER_10K = 5.25; // Annual %
    private static final int LOAN_TERM_UNDER_10K = 24; // months

    private double salesTaxAmount;
    private double recordingFee;
    private double processingFee;
    private boolean isFinanced;

    public SalesContract(String contractDate, String customerName, String customerEmail,
                         Vehicle vehicleSold, boolean isFinanced) {
        super(contractDate, customerName, customerEmail, vehicleSold);
        this.isFinanced = isFinanced;
        // Calculate fees based on vehicle price
        this.salesTaxAmount = vehicleSold.getPrice() * SALES_TAX_RATE;
        this.recordingFee = RECORDING_FEE;
        if (vehicleSold.getPrice() < 10000) {
            this.processingFee = PROCESSING_FEE_UNDER_10K;
        } else {
            this.processingFee = PROCESSING_FEE_10K_AND_OVER;
        }
    }

    // Getters
    public double getSalesTaxAmount() {
        return salesTaxAmount;
    }

    public double getRecordingFee() {
        return recordingFee;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public boolean isFinanced() {
        return isFinanced;
    }

    // Setter for isFinanced (if needed, though typically set at construction)
    public void setFinanced(boolean financed) {
        isFinanced = financed;
    }

    @Override
    public double getTotalPrice() {
        // Total price is vehicle price + sales tax + recording fee + processing fee
        return getVehicleSold().getPrice() + this.salesTaxAmount + this.recordingFee + this.processingFee;
    }

    @Override
    public double getMonthlyPayment() {
        if (!isFinanced) {
            return 0.0;
        }

        double principal = getTotalPrice();
        double annualInterestRate;
        int numberOfMonths;

        if (getVehicleSold().getPrice() >= 10000) {
            annualInterestRate = INTEREST_RATE_10K_AND_OVER;
            numberOfMonths = LOAN_TERM_10K_AND_OVER;
        } else {
            annualInterestRate = INTEREST_RATE_UNDER_10K;
            numberOfMonths = LOAN_TERM_UNDER_10K;
        }
        return calculateMonthlyPayment(principal, annualInterestRate, numberOfMonths);
    }
}