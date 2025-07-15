package com.example.cattlemanagemen;

import java.util.HashMap;

public class FarmerService {
    private static HashMap<String, Farmer> farmers = new HashMap<>();

    public static boolean registerFarmer(String name, String location, String username, String password) {
        if (!farmers.containsKey(username)) {
            farmers.put(username, new Farmer(name, location, username, password));
            return true;
        }
        return false;
    }

    public static Farmer login(String username, String password) {
        Farmer farmer = farmers.get(username);
        if (farmer != null && farmer.checkPassword(password)) {
            return farmer;
        }
        return null;
    }

    public static Farmer findFarmerByUsername(String username) {
        // Retrieve the farmer with the specified username from the HashMap
        return farmers.get(username); // Returns null if the farmer is not found
    }
}
