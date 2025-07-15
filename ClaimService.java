package com.example.cattlemanagemen;

import java.util.HashMap;

public class ClaimService {
    private HashMap<String, Claim> claims = new HashMap<>();

    public String fileClaim(Cattle cattle) {
        String claimId = "CLM" + System.currentTimeMillis();
        Claim claim = new Claim(claimId, cattle);
        claims.put(claimId, claim);
        return claimId;
    }

    public Claim getClaim(String claimId) {
        return claims.get(claimId);
    }
}
