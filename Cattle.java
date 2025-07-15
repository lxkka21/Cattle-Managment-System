package com.example.cattlemanagemen;

import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Cattle {
    private String id;
    private String breed;
    private int age;
    private double weight;
    private String vaccinationRecords;
    private Farmer owner;
    private List<String> claims = new ArrayList<>();
    private boolean claimProcessed = false;
    private String checksum;
    private boolean isInsured = false;
    private boolean premiumPaid = false;
    private double premiumAmount;
    private AuditLog auditLog = new AuditLog(); // Audit log instance for logging actions

    public Cattle(String id, String breed, int age, double weight, String vaccinationRecords, Farmer owner) {
        this.id = id;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.vaccinationRecords = vaccinationRecords;
        this.owner = owner;
        this.checksum = generateChecksum();
        this.claims = new ArrayList<>();
        auditLog.log("Cattle created with ID: " + id + ", Breed: " + breed);
    }

    public void fileClaim(String description) {
        if (isInsured && premiumPaid) {
            claims.add(description);
            auditLog.log("Claim filed for cattle ID: " + this.id + " with description: " + description);
            System.out.println("Claim filed for cattle ID: " + this.id + ". Description: " + description);
        } else {
            auditLog.log("Failed to file claim for cattle ID: " + this.id + " (insurance not active or premium unpaid)");
            System.out.println("Cannot file claim. Ensure the insurance is applied and premium is paid.");
        }
    }
    public void applyForInsurance() {
        if (!isInsured) {
            this.premiumAmount = calculatePremium();
            this.isInsured = true;
            this.premiumPaid = false;
            auditLog.log("Insurance applied for cattle ID: " + this.id + ". Premium set at: " + this.premiumAmount);
        } else {
            auditLog.log("Insurance already applied for cattle ID: " + this.id);
        }
    }

    public double payPremium() {
        if (isInsured() && !isPremiumPaid()) {
            this.premiumPaid = true;
            auditLog.log("Premium paid for cattle ID: " + this.id + ". Amount: " + this.premiumAmount);
            return premiumAmount;
        }
        auditLog.log("Premium payment failed for cattle ID: " + this.id + " (either not insured or already paid)");
        return 0;
    }

    public double calculatePremium() {
        return age * weight * 0.05;
    }

    public boolean canFileClaim() {
        return isInsured && premiumPaid && !claimProcessed;
    }

    // Getters
    public boolean isPremiumPaid() { return premiumPaid; }
    public boolean isInsured() { return isInsured; }
    public double getPremiumAmount() { return premiumAmount; }
    public boolean isClaimProcessed() { return claimProcessed; }
    public String getId() { return id; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public List<String> getClaims() { return claims; }
    public Farmer getOwner() { return owner; }
    public AuditLog getAuditLog() {
        return auditLog;
    }

    public void transferOwnership(Farmer newOwner) {
        auditLog.log("Ownership transferred from " + owner.getName() + " to " + newOwner.getName() + " for cattle ID: " + id);
        this.owner = newOwner;
        this.claimProcessed = false;
        this.checksum = generateChecksum();
    }


    public boolean isDataIntegrityMaintained() {
        String currentChecksum = generateChecksum();
        return currentChecksum.equals(this.checksum);
    }

    public void updateCattleInfo(String breed, int age, double weight, String vaccinationRecords) {
        auditLog.log("Cattle info updated for ID: " + id + ". New breed: " + breed + ", age: " + age + ", weight: " + weight);
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.vaccinationRecords = vaccinationRecords;
        this.checksum = generateChecksum();
    }

    private String generateChecksum() {
        String data = id + owner + breed + age + weight + vaccinationRecords;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            auditLog.log("Error generating checksum for cattle ID: " + id);
            throw new RuntimeException("Error generating checksum", e);
        }
    }

    public void setClaimProcessed(boolean claimProcessed) {
        this.claimProcessed = claimProcessed;
    }
}
