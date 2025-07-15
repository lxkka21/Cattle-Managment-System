package com.example.cattlemanagemen;

public class PremiumCalculator {
    public double calculatePremium(Cattle cattle) {
        double basePremium = 100;
        basePremium += cattle.getAge() * 10;
        basePremium += cattle.getWeight() * 2;
        return basePremium;
    }
}
