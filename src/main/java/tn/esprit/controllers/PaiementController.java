package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Commande;
import tn.esprit.models.Paiement;
import tn.esprit.services.CommandeService;
import tn.esprit.services.PaiementService;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static tn.esprit.util.DBConnection.connection;

public class PaiementController {
    @FXML private ListView<String> commandeListView;
    @FXML private TextField paiementCommandeId;
    @FXML private TextField paiementUtilisateurId;
    @FXML private TextField paiementMontant;
    @FXML private ComboBox<String> paiementMode;
    @FXML private DatePicker paiementDate;
    @FXML private ComboBox<String> paiementStatus;
    @FXML private ListView<String> paiementList;

    private final PaiementService paiementService = new PaiementService();
    private final CommandeService commandeService = new CommandeService();

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        paiementMode.getItems().addAll("Cash", "Carte bancaire", "Chèque", "Virement");
        paiementStatus.getItems().addAll("Pending", "Completed", "Failed");

        loadCommandes();
        loadPaiements();

        // Listener to populate fields when selecting a paiement
        paiementList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populatePaiementFields(newSelection);
            }
        });
    }

    private void populatePaiementFields(String selectedPaiement) {
        try {
            // Extract payment ID from format: "Paiement ID: 12 | Utilisateur: 3 | Montant: 150 TND | Mode: Cash | Status: Pending"
            String[] details = selectedPaiement.split("\\|");

            int idCommande = Integer.parseInt(details[0].split(":")[1].trim());
            int idUtilisateur = Integer.parseInt(details[1].split(":")[1].trim());
            double montant = Double.parseDouble(details[2].split(":")[1].replace("TND", "").trim());
            String modeDePaiement = details[3].split(":")[1].trim();
            String status = details[4].split(":")[1].trim();

            // Populate fields
            paiementCommandeId.setText(String.valueOf(idCommande));
            paiementUtilisateurId.setText(String.valueOf(idUtilisateur));
            paiementMontant.setText(String.valueOf(montant));
            paiementMode.setValue(modeDePaiement);
            paiementStatus.setValue(status);

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les détails du paiement.");
        }
    }


    private void loadCommandes() {
        commandeListView.getItems().clear();
        List<Commande> commandes = commandeService.getAllCommandes();
        for (Commande commande : commandes) {
            commandeListView.getItems().add("Commande #" + commande.getIdCommande());
        }
    }

    private void loadPaiements() {
        paiementList.getItems().clear();
        List<Paiement> paiements = paiementService.getAllPaiements();

        if (paiements.isEmpty()) {
            paiementList.getItems().add("Aucun paiement disponible.");
            return;
        }

        for (Paiement paiement : paiements) {
            String paiementInfo = "Paiement ID: " + paiement.getIdCommande() +
                    " | Utilisateur: " + paiement.getIdUtilisateur() +
                    " | Montant: " + paiement.getMontant() + " TND" +
                    " | Mode: " + paiement.getModeDePaiement() +
                    " | Status: " + paiement.getStatus();
            paiementList.getItems().add(paiementInfo);
        }
    }


    @FXML
    private void effectuerPaiement() {
        System.out.println("Processing a Payment...");

        // Validate input fields
        if (paiementCommandeId.getText().isEmpty() || paiementUtilisateurId.getText().isEmpty() ||
                paiementMontant.getText().isEmpty() || paiementMode.getValue() == null || paiementDate.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate date format
        LocalDate date;
        try {
            date = paiementDate.getValue();
            if (date == null) {
                throw new DateTimeParseException("Invalid Date", "NULL", 0);
            }
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format de date invalide. Veuillez entrer une date correcte.");
            return;
        }

        // Convert input values
        int idCommande, idUtilisateur;
        double montant;
        try {
            idCommande = Integer.parseInt(paiementCommandeId.getText());
            idUtilisateur = Integer.parseInt(paiementUtilisateurId.getText());
            montant = Double.parseDouble(paiementMontant.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID ou Montant invalide.");
            return;
        }

        String modeDePaiement = paiementMode.getValue();
        String status = paiementStatus.getValue() != null ? paiementStatus.getValue() : "Pending";


        Paiement paiement = new Paiement(idCommande, idUtilisateur, montant, modeDePaiement, date, status);


        paiementService.effectuerPaiement(paiement);


        System.out.println("✅ Paiement inserted into DB: " + paiement);


        loadPaiements();


        showAlert("Succès", "Paiement effectué avec succès !");
    }



    @FXML
    private void modifierPaiement() {
        System.out.println("🔄 Modifying a Payment...");

        // Validate input fields
        if (paiementCommandeId.getText().isEmpty() || paiementUtilisateurId.getText().isEmpty() ||
                paiementMontant.getText().isEmpty() || paiementMode.getValue() == null || paiementDate.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Convert and validate input values
        int idCommande, idUtilisateur;
        double montant;
        try {
            idCommande = Integer.parseInt(paiementCommandeId.getText());
            idUtilisateur = Integer.parseInt(paiementUtilisateurId.getText());
            montant = Double.parseDouble(paiementMontant.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID ou Montant invalide.");
            return;
        }

        LocalDate dateDePaiement = paiementDate.getValue();
        String modeDePaiement = paiementMode.getValue();
        String status = paiementStatus.getValue() != null ? paiementStatus.getValue() : "Pending";

        // Check if the paiement exists before modifying
        Paiement existingPaiement = paiementService.getPaiementById(idCommande);
        if (existingPaiement == null) {
            showAlert("Erreur", "Le paiement sélectionné n'existe pas.");
            return;
        }

        // Create a new Paiement object with updated values
        Paiement updatedPaiement = new Paiement(idCommande, idUtilisateur, montant, modeDePaiement, dateDePaiement, status);

        // Call the service method to modify the paiement in the database
        paiementService.modifierPaiement(updatedPaiement);

        // Refresh the payment list
        loadPaiements();

        // Show success message
        showAlert("Succès", "Paiement modifié avec succès !");
    }


    @FXML
    private void supprimerPaiement() {
        System.out.println("🗑 Deleting a Payment...");

        // Get selected item from the ListView
        String selectedPaiement = paiementList.getSelectionModel().getSelectedItem();
        if (selectedPaiement == null) {
            showAlert("Erreur", "Veuillez sélectionner un paiement à supprimer.");
            return;
        }

        // Extract the idCommande from the selected item
        try {
            int idCommande = Integer.parseInt(selectedPaiement.split(":")[1].trim().split("\\|")[0].trim());

            // Confirm deletion
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText("Suppression de Paiement");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce paiement ?");

            // If user confirms, proceed with deletion
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    paiementService.supprimerPaiement(idCommande);
                    System.out.println("✅ Paiement deleted successfully: ID " + idCommande);

                    // Refresh the list
                    loadPaiements();

                    showAlert("Succès", "Paiement supprimé avec succès !");
                }
            });
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            showAlert("Erreur", "Format incorrect du paiement sélectionné.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<Paiement> getAllPaiements() {
        List<Paiement> paiements = new ArrayList<>();
        try {
            String query = "SELECT * FROM paiement";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Paiement paiement = new Paiement(
                        rs.getInt("idCommande"),
                        rs.getInt("idUtilisateur"),
                        rs.getDouble("montant"),
                        rs.getString("modeDePaiement"),
                        rs.getDate("datePaiement").toLocalDate(),
                        rs.getString("status")
                );
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }



}
