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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ModifierPaiement {
    @FXML private ListView<String> commandeListView;
    @FXML private TextField paiementCommandeId;
    @FXML private TextField paiementUtilisateurId;
    @FXML private TextField paiementMontant;
    @FXML private ComboBox<String> paiementMode;
    @FXML private DatePicker paiementDate;
    @FXML private ComboBox<String> paiementStatus;
    @FXML private ListView<String> paiementList; // Ensure it's properly linked to FXML

    private final PaiementService paiementService = new PaiementService();
    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        System.out.println("🛠 Initializing ModifierPaiement Controller...");

        if (paiementMode != null) {
            paiementMode.getItems().addAll("Cash", "Carte bancaire", "Chèque", "Virement");
        }
        if (paiementStatus != null) {
            paiementStatus.getItems().addAll("Pending", "Completed", "Failed");
        }
        if (paiementList != null) {
            loadPaiements();

            // ✅ Add Selection Listener to Populate Fields
            paiementList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populatePaiementFields(newSelection);
                }
            });
        } else {
            System.err.println("❌ paiementList is NULL! Check FXML binding.");
        }
        if (commandeListView != null) {
            loadCommandes();
        } else {
            System.err.println("❌ commandeListView is NULL! Check FXML binding.");
        }
    }

    /**
     * 🏷 Extract payment details from ListView selection and populate the fields.
     */
    private void populatePaiementFields(String selectedPaiement) {
        try {
            System.out.println("📌 Selected Paiement: " + selectedPaiement);

            // ✅ Extracting details using regex or split
            String[] details = selectedPaiement.split("\\|");

            int idCommande = Integer.parseInt(details[0].split(":")[1].trim());
            int idUtilisateur = Integer.parseInt(details[1].split(":")[1].trim());
            double montant = Double.parseDouble(details[2].split(":")[1].replace("TND", "").trim());
            String modeDePaiement = details[3].split(":")[1].trim();
            String status = details[4].split(":")[1].trim();

            // ✅ Populate UI Fields
            paiementCommandeId.setText(String.valueOf(idCommande));
            paiementUtilisateurId.setText(String.valueOf(idUtilisateur));
            paiementMontant.setText(String.valueOf(montant));
            paiementMode.setValue(modeDePaiement);
            paiementStatus.setValue(status);

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les détails du paiement.");
            e.printStackTrace();
        }
    }

    private void loadPaiements() {
        if (paiementList == null) {
            System.err.println("⚠ paiementList is NULL, skipping loadPaiements()");
            return;
        }
        paiementList.getItems().clear();
        List<Paiement> paiements = paiementService.getAllPaiements();
        if (paiements.isEmpty()) {
            paiementList.getItems().add("Aucun paiement disponible.");
        } else {
            for (Paiement paiement : paiements) {
                String paiementInfo = "Paiement ID: " + paiement.getIdCommande() +
                        " | Utilisateur: " + paiement.getIdUtilisateur() +
                        " | Montant: " + paiement.getMontant() + " TND" +
                        " | Mode: " + paiement.getModeDePaiement() +
                        " | Status: " + paiement.getStatus();
                paiementList.getItems().add(paiementInfo);
            }
        }
    }

    private void loadCommandes() {
        if (commandeListView == null) {
            System.err.println("⚠ commandeListView is NULL, skipping loadCommandes()");
            return;
        }
        commandeListView.getItems().clear();
        List<Commande> commandes = commandeService.getAllCommandes();
        for (Commande commande : commandes) {
            commandeListView.getItems().add("Commande #" + commande.getIdCommande());
        }
    }

    @FXML
    private void modifierPaiement() {
        if (paiementCommandeId.getText().isEmpty() || paiementUtilisateurId.getText().isEmpty() ||
                paiementMontant.getText().isEmpty() || paiementMode.getValue() == null || paiementDate.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

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
        String status = paiementStatus.getValue();

        Paiement existingPaiement = paiementService.getPaiementById(idCommande);
        if (existingPaiement == null) {
            showAlert("Erreur", "Le paiement sélectionné n'existe pas.");
            return;
        }

        Paiement updatedPaiement = new Paiement(idCommande, idUtilisateur, montant, modeDePaiement, dateDePaiement, status);
        paiementService.modifierPaiement(updatedPaiement);

        loadPaiements();
        showAlert("Succès", "Paiement modifié avec succès !");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goBack(ActionEvent event) {
        switchScene(event, "/views/MainView.fxml");
    }

    @FXML
    public void gotoPa(ActionEvent event) {
        switchScene(event, "/views/ModifierPaiement.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
