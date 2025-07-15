package com.example.cattlemanagemen;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloApplication extends Application {

    private List<Cattle> cattleList = new ArrayList<>(); // List to store registered cattle
    private List<Claim> claimsList = new ArrayList<>(); // List to store claims
    private Stage mainStage;
    Farmer currentFarmer;// Main application stage


    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        showLoginScreen();
    }

    private void showLoginScreen() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));
        registerButton.setOnAction(e -> showRegistrationScreen());

        VBox loginLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, registerButton);
        Scene loginScene = new Scene(loginLayout, 300, 200);
        mainStage.setScene(loginScene);
        mainStage.setTitle("Cattle Management System - Login");
        mainStage.show();
    }

    private void showRegistrationScreen() {
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        registerButton.setOnAction(e -> {
            boolean success = FarmerService.registerFarmer(
                    nameField.getText(),
                    locationField.getText(),
                    usernameField.getText(),
                    passwordField.getText()
            );
            if (success) {
                System.out.println("Farmer registered successfully.");
                showLoginScreen();
            } else {
                System.out.println("Username already taken.");
            }
        });

        backButton.setOnAction(e -> showLoginScreen());

        VBox registrationLayout = new VBox(10, nameLabel, nameField, locationLabel, locationField, usernameLabel, usernameField, passwordLabel, passwordField, registerButton, backButton);
        Scene registrationScene = new Scene(registrationLayout, 300, 300);
        mainStage.setScene(registrationScene);
        mainStage.setTitle("Register Farmer");
    }

    private void showMainMenu(Farmer farmer) {
        Label welcomeLabel = new Label("Welcome, " + farmer.getName());
        Button cattleRegistrationButton = new Button("Register Cattle");
        Button viewClaimsButton = new Button("File/View Claims");
        Button logOutButton = new Button("Log Out");
        Button transferCattleButton = new Button("Transfer Cattle");
        Button insuranceButton = new Button("Manage Insurance");
        Button auditLogButton = new Button("View Audit Log"); // New button for audit log access

        transferCattleButton.setOnAction(e -> showTransferCattleScreen(farmer));
        cattleRegistrationButton.setOnAction(e -> showCattleRegistrationScreen(farmer));
        viewClaimsButton.setOnAction(e -> showClaimsScreen(farmer));
        logOutButton.setOnAction(e -> showLoginScreen());
        insuranceButton.setOnAction(e -> {
            if (!cattleList.isEmpty()) {
                showInsuranceSelectionScreen(farmer);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No cattle registered.");
                alert.showAndWait();
            }
        });
        auditLogButton.setOnAction(e -> showAuditLogScreen()); // Open audit log screen

        VBox menuLayout = new VBox(10, welcomeLabel, cattleRegistrationButton, viewClaimsButton, transferCattleButton, insuranceButton, auditLogButton, logOutButton);
        Scene mainMenuScene = new Scene(menuLayout, 300, 250);
        mainStage.setScene(mainMenuScene);
        mainStage.setTitle("Cattle Management System - Main Menu");
    }


    private void handleLogin(String username, String password) {
        Farmer farmer = FarmerService.login(username, password);
        if (farmer != null) {
            showMainMenu(farmer);
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    private void showCattleRegistrationScreen(Farmer farmer) {
        Label idLabel = new Label("Cattle ID:");
        TextField idField = new TextField();
        Label breedLabel = new Label("Breed:");
        TextField breedField = new TextField();
        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();
        Label weightLabel = new Label("Weight in KG:");
        TextField weightField = new TextField();
        Label vaccinationLabel = new Label("Vaccination Records:");
        TextField vaccinationField = new TextField();
        Button registerCattleButton = new Button("Register Cattle");
        Button backButton = new Button("Back");

        registerCattleButton.setOnAction(e -> {
            try {
                Cattle cattle = new Cattle(
                        idField.getText(),
                        breedField.getText(),
                        Integer.parseInt(ageField.getText()),
                        Double.parseDouble(weightField.getText()),
                        vaccinationField.getText(),
                        farmer
                );
                cattleList.add(cattle);
                System.out.println("Cattle registered successfully: " + cattle.getId());
                showMainMenu(farmer);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid age or weight format.");
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showMainMenu(farmer));

        VBox registrationLayout = new VBox(10, idLabel, idField, breedLabel, breedField, ageLabel, ageField, weightLabel, weightField, vaccinationLabel, vaccinationField, registerCattleButton, backButton);
        Scene registrationScene = new Scene(registrationLayout, 400, 400);
        mainStage.setScene(registrationScene);
        mainStage.setTitle("Register Cattle");
    }

    private void showClaimsScreen(Farmer farmer) {
        Label claimsLabel = new Label("File/View Claims");
        Button fileClaimButton = new Button("File Claim");
        Button viewClaimsButton = new Button("View Claims");
        Button backButton = new Button("Back");

        fileClaimButton.setOnAction(e -> showFileClaimScreen(farmer));
        viewClaimsButton.setOnAction(e -> showViewClaimsScreen(farmer));

        backButton.setOnAction(e -> showMainMenu(farmer));

        VBox claimsLayout = new VBox(10, claimsLabel, fileClaimButton, viewClaimsButton, backButton);
        Scene claimsScene = new Scene(claimsLayout, 300, 200);
        mainStage.setScene(claimsScene);
        mainStage.setTitle("Claims Management");
    }

    private void showFileClaimScreen(Farmer farmer) {
        Label cattleIdLabel = new Label("Cattle ID:");
        TextField cattleIdField = new TextField();
        Button submitClaimButton = new Button("Submit Claim");
        Button backButton = new Button("Back");

        submitClaimButton.setOnAction(e -> {
            String cattleId = cattleIdField.getText();
            Optional<Cattle> cattleOptional = cattleList.stream()
                    .filter(cattle -> cattle.getId().equals(cattleId) && cattle.getOwner().equals(farmer))
                    .findFirst();

            if (cattleOptional.isPresent()) {
                Cattle cattle = cattleOptional.get();
                // Check if there's an existing claim for this cattle
                boolean hasExistingClaim = claimsList.stream()
                        .anyMatch(claim -> claim.getCattle().equals(cattle) &&
                                (claim.getStatus().equals("Pending") || claim.getStatus().equals("Approved")));

                if (hasExistingClaim) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "A claim has already been filed for this cattle.");
                    alert.setHeaderText("Claim Submission Failed");
                    alert.showAndWait();
                } else {
                    String claimId = "CLAIM-" + (claimsList.size() + 1); // Generate a simple claim ID
                    Claim newClaim = new Claim(claimId, cattle);
                    claimsList.add(newClaim);
                    cattle.fileClaim("Claim for cattle ID: " + cattleId); // File the claim
                    System.out.println("Claim submitted for cattle ID: " + cattleId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Claim submitted successfully.");
                    alert.setHeaderText("Claim Submitted");
                    alert.showAndWait();
                    showMainMenu(farmer);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cattle not found or you do not own this cattle.");
                alert.setHeaderText("Claim Submission Failed");
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showClaimsScreen(farmer));

        VBox fileClaimLayout = new VBox(10, cattleIdLabel, cattleIdField, submitClaimButton, backButton);
        Scene fileClaimScene = new Scene(fileClaimLayout, 300, 200);
        mainStage.setScene(fileClaimScene);
        mainStage.setTitle("File Claim");
    }


    private void showViewClaimsScreen(Farmer farmer) {
        Label claimsLabel = new Label("Viewing Claims");
        VBox claimsLayout = new VBox(10, claimsLabel);

        claimsList.stream()
                .filter(claim -> claim.getStatus().equals("Pending"))
                .forEach(claim -> {
                    Button approveButton = new Button("Approve " + claim.getClaimId());
                    Button rejectButton = new Button("Reject " + claim.getClaimId());

                    approveButton.setOnAction(e -> {
                        claim.approve();
                        double payout = calculatePremium(claim.getCattle());
                        farmer.addPayout(payout); // Assuming Farmer has addPayout method
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Claim approved. Payout: " + payout);
                        alert.showAndWait();
                        showViewClaimsScreen(farmer); // Refresh the claims screen
                    });

                    rejectButton.setOnAction(e -> {
                        claim.reject();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Claim rejected.");
                        alert.showAndWait();
                        showViewClaimsScreen(farmer); // Refresh the claims screen
                    });

                    claimsLayout.getChildren().addAll(new Label("Claim ID: " + claim.getClaimId()), approveButton, rejectButton);
                });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showClaimsScreen(farmer));
        claimsLayout.getChildren().add(backButton);

        Scene viewClaimsScene = new Scene(claimsLayout, 400, 300);
        mainStage.setScene(viewClaimsScene);
        mainStage.setTitle("View Claims");
    }

    private void showTransferCattleScreen(Farmer currentFarmer) {
        Label cattleIdLabel = new Label("Cattle ID:");
        TextField cattleIdField = new TextField();
        Label targetUsernameLabel = new Label("Target Farmer Username:");
        TextField targetUsernameField = new TextField();
        Button transferButton = new Button("Transfer Cattle");
        Button backButton = new Button("Back");

        transferButton.setOnAction(e -> {
            String cattleId = cattleIdField.getText();
            String targetUsername = targetUsernameField.getText();

            Optional<Cattle> cattleToTransfer = cattleList.stream()
                    .filter(c -> c.getId().equals(cattleId) && c.getOwner().equals(currentFarmer))
                    .findFirst();

            Farmer targetFarmer = FarmerService.findFarmerByUsername(targetUsername); // Use instance

            if (cattleToTransfer.isPresent() && targetFarmer != null) {
                cattleToTransfer.get().transferOwnership(targetFarmer);
                System.out.println("Cattle ID " + cattleId + " transferred to " + targetUsername);
                Alert transferAlert = new Alert(Alert.AlertType.INFORMATION,
                        "Cattle ID " + cattleId + " transferred successfully to " + targetUsername);
                transferAlert.setHeaderText("Transfer Successful");
                transferAlert.showAndWait();
                showMainMenu(currentFarmer);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid Cattle ID or Target Farmer Username.");
                errorAlert.setHeaderText("Transfer Failed");
                errorAlert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showMainMenu(currentFarmer));

        VBox transferLayout = new VBox(10, cattleIdLabel, cattleIdField, targetUsernameLabel, targetUsernameField, transferButton, backButton);
        Scene transferScene = new Scene(transferLayout, 400, 300);
        mainStage.setScene(transferScene);
        mainStage.setTitle("Transfer Cattle Ownership");
    }

    private void showInsuranceSelectionScreen(Farmer farmer) {
        Label selectCattleLabel = new Label("Select Cattle for Insurance Management:");
        ComboBox<Cattle> cattleComboBox = new ComboBox<>();

        // Populate the ComboBox with the farmer's cattle
        cattleList.stream()
                .filter(cattle -> cattle.getOwner().equals(farmer))
                .forEach(cattleComboBox.getItems()::add);

        Button manageInsuranceButton = new Button("Manage Insurance");
        Button backButton = new Button("Back");

        manageInsuranceButton.setOnAction(e -> {
            Cattle selectedCattle = cattleComboBox.getValue();
            if (selectedCattle != null) {
                showInsuranceScreen(farmer, selectedCattle); // Show insurance screen for the selected cattle
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a cattle.");
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showMainMenu(farmer));

        VBox selectionLayout = new VBox(10, selectCattleLabel, cattleComboBox, manageInsuranceButton, backButton);
        Scene selectionScene = new Scene(selectionLayout, 400, 200);
        mainStage.setScene(selectionScene);
        mainStage.setTitle("Select Cattle for Insurance Management");

    }
    private void showAuditLogScreen() {
        Label auditLogLabel = new Label("Audit Log Entries");
        VBox logEntriesLayout = new VBox(10, auditLogLabel);

        List<String> auditLogEntries = AuditLog.getLogs(); // Fetch log entries
        for (String entry : auditLogEntries) {
            Label logEntryLabel = new Label(entry);
            logEntriesLayout.getChildren().add(logEntryLabel);
        }

        Button backButton = new Button("Back");

        backButton.setOnAction(e -> showMainMenu(currentFarmer)); // Return to main menu
        logEntriesLayout.getChildren().add(backButton);

        Scene auditLogScene = new Scene(logEntriesLayout, 400, 300);
        mainStage.setScene(auditLogScene);
        mainStage.setTitle("Audit Log");
    }

    private void showInsuranceScreen(Farmer farmer, Cattle cattle) {
        Label statusLabel = new Label("Insurance Status: " + (cattle.isInsured() ? "Insured" : "Not Insured"));
        Button applyInsuranceButton = new Button("Apply for Insurance");
        Button payPremiumButton = new Button("Pay Premium");
        Button backButton = new Button("Back");

        applyInsuranceButton.setOnAction(e -> {
            cattle.applyForInsurance();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Insurance applied for cattle ID: " + cattle.getId());
            alert.showAndWait();
        });

        payPremiumButton.setOnAction(e -> {
            double amountPaid = cattle.payPremium();
            if (amountPaid > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Premium paid successfully: " + amountPaid);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No premium to pay.");
                alert.showAndWait();
            }
        });

        backButton.setOnAction(e -> showInsuranceSelectionScreen(farmer));

        VBox insuranceLayout = new VBox(10, statusLabel, applyInsuranceButton, payPremiumButton, backButton);
        Scene insuranceScene = new Scene(insuranceLayout, 300, 200);
        mainStage.setScene(insuranceScene);
        mainStage.setTitle("Manage Insurance");
    }
    private double calculatePremium(Cattle cattle) {
        return 100 * cattle.getWeight() * cattle.getAge();
    }

    public static void main(String[] args) {
        launch();
    }
}
