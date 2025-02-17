package edu.emmapi.controllers;

import edu.emmapi.entities.Abonnement;
import edu.emmapi.services.AbonnementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class Ajouter_AbonnementController {

    @FXML
    private Button ajouter_abonnement_button;

    @FXML
    private TextField nomService_textfield;

    @FXML
    private TextField typeAbonnement_textfield;

    @FXML
    private DatePicker date_datepicker;

    @FXML
    private TextField statusAbonnement_textfield;

    @FXML
    void addAbonnement(ActionEvent event) {
        // Validation des champs
        if (nomService_textfield.getText().isEmpty() ||
                typeAbonnement_textfield.getText().isEmpty() ||
                date_datepicker.getValue() == null ||
                statusAbonnement_textfield.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        AbonnementService abonnementService = new AbonnementService();
        LocalDate localDate = date_datepicker.getValue();
        java.sql.Date date = java.sql.Date.valueOf(localDate);
        Abonnement abonnement = new Abonnement(
                0, // id will be auto-generated
                nomService_textfield.getText(),
                typeAbonnement_textfield.getText(),
                date,
                statusAbonnement_textfield.getText(),
                true // estDisponible
        );

        // Appeler le service pour ajouter l'abonnement
        try {
            abonnementService.addAbonnement(abonnement);
            showAlert("Succès", "Abonnement ajouté avec succès !");
            // Réinitialiser les champs
            nomService_textfield.clear();
            typeAbonnement_textfield.clear();
            date_datepicker.setValue(null);
            statusAbonnement_textfield.clear();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'abonnement : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}