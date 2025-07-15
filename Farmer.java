package com.example.cattlemanagemen;

public class Farmer {
    private String name;
    private String location;
    private String username;
    private String password;
    private double totalPayout;

    public Farmer(String name, String location, String username, String password) {
        this.name = name;
        this.location = location;
        this.username = username;
        this.password = password;
        this.totalPayout=0;
    }
    public void addPayout(double payout) {
        this.totalPayout += payout; // Update total payout
    }
    public double getTotalPayout() {
        return totalPayout;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // Getters for name and location
    public String getName() { return name; }
    public String getLocation() { return location; }
}

