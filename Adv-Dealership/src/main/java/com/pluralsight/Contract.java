package com.pluralsight;

public abstract class Contract {
    private String contractDate; // YYYYMMDD
    private String customerName;
    private String customerEmail;
    private Vehicle vehicleSold;
    // totalPrice and monthlyPayment will be calculated by subclasses

    public Contract(String contractDate, String customerName, String customerEmail, Vehicle vehicleSold) {
        this.contractDate = contractDate;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vehicleSold = vehicleSold;
    }

    // Getters
    public String getContractDate() {
        return contractDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Vehicle getVehicleSold() {
        return vehicleSold;
    }

    // Setters
    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setVehicleSold(Vehicle vehicleSold) {
        this.vehicleSold = vehicleSold;
    }

    // Abstract methods for subclasses to implement
    public abstract double getTotalPrice();
    public abstract double getMonthlyPayment();

    // Helper method for subclasses, could be protected or private static in each
    protected static double calculateMonthlyPayment(double principal, double annualInterestRate, int numberOfMonths) {
        if (principal <= 0) {
            return 0.0;
        }
        // For 0% interest loans, the payment is simply principal / months
        if (annualInterestRate == 0) {
            return principal / numberOfMonths;
        }

        double monthlyInterestRate = annualInterestRate / 12.0 / 100.0; // e.g., 4.25% -> 0.0425/12

        // M = P [ i(1 + i)^n ] / [ (1 + i)^n – 1]
        double monthlyPayment = principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfMonths))
                / (Math.pow(1 + monthlyInterestRate, numberOfMonths) - 1);
        return monthlyPayment;
    }
}