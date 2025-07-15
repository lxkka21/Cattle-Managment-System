package com.example.cattlemanagemen;

import java.util.ArrayList;
import java.util.List;

public class AuditLog {
    private static List<String> logEntries = new ArrayList<>();

    public void log(String entry) {
        logEntries.add(entry);
    }

    public static List<String> getLogs() {
        return logEntries;
    }
}
