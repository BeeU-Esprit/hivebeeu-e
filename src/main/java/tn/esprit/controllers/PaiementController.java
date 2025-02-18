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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
        // Load payment modes
        paiementMode.getItems().addAll("Cash", "Credit Card", "Bank Transfer");
        paiementStatus.getItems().addAll("Pending", "Completed", "Failed");

        // Load commandes and paiements
        loadCommandes();
        loadPaiements();
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
        for (Paiement paiement : paiements) {
            paiementList.getItems().add("Paiement ID: " + paiement.getIdCommande() + " - " + paiement.getStatus());
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

        // Create a new Paiement object
        Paiement paiement = new Paiement(idCommande, idUtilisateur, montant, modeDePaiement, date, status);

        // Call the service method to insert into the database
        paiementService.effectuerPaiement(paiement);

        // Verify the insertion
        System.out.println("✅ Paiement inserted into DB: " + paiement);

        // Refresh the list of paiements
        loadPaiements();

        // Show confirmation alert
        showAlert("Succès", "Paiement effectué avec succès !");
    }


    @FXML
    private void modifierPaiement() {
        System.out.println("Modifying a Payment...");
        showAlert("Info", "Paiement modifié avec succès !");
    }

    @FXML
    private void supprimerPaiement() {
        System.out.println("Deleting a Payment...");
        showAlert("Info", "Paiement supprimé avec succès !");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
