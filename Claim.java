package com.example.cattlemanagemen;

public class Claim {
    private String claimId;
    private Cattle cattle;
    private String status;

    public Claim(String claimId, Cattle cattle) {
        this.claimId = claimId;
        this.cattle = cattle;
        this.status = "Pending";
    }

    public void approve() {
        this.status = "Approved";
    }

    public void reject() {
        this.status = "Rejected";
    }

    public String getStatus() {
        return status;
    }

    public String getClaimId() {
        return claimId;
    }

    public Cattle getCattle() {
        return cattle; // Added getter for Cattle
    }
}
