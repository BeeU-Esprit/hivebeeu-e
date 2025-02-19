package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Paiement;
import tn.esprit.services.PaiementService;

import java.io.IOException;
import java.util.List;

public class PaiementController {
    @FXML private ListView<String> paiementList;
    @FXML private Button AjPa;
    @FXML private Button modifP;

    private final PaiementService paiementService = new PaiementService();

    @FXML
    public void initialize() {
        loadPaiements();
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
    private void gotoPa(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjoutPaiement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gotomodif(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierPaiement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerPaiement() {
        String selectedPaiement = paiementList.getSelectionModel().getSelectedItem();
        if (selectedPaiement == null || selectedPaiement.equals("Aucun paiement disponible.")) {
            showAlert("Erreur", "Veuillez sélectionner un paiement à supprimer.");
            return;
        }

        try {
            int idCommande = Integer.parseInt(selectedPaiement.split(":")[1].trim().split("\\|")[0].trim());

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText("Suppression de Paiement");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce paiement ?");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    paiementService.supprimerPaiement(idCommande);
                    loadPaiements();
                    showAlert("Succès", "Paiement supprimé avec succès !");
                }
            });
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de supprimer le paiement.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}