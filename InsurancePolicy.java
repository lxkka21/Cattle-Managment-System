package com.example.cattlemanagemen;

public class InsurancePolicy {
    private String policyId;
    private String policyName;
    private double premiumAmount;

    public InsurancePolicy(String policyId, String policyName, double premiumAmount) {
        this.policyId = policyId;
        this.policyName = policyName;
        this.premiumAmount = premiumAmount;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    // Getters for policyId and policyName
}
