package edu.emmapi.controllers;

import edu.emmapi.entities.Service;
import edu.emmapi.services.ServiceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class Ajouter_ServiceController {

    @FXML
    private Button ajouter_service_button;

    @FXML
    private TextField nomService_textfield;

    @FXML
    private TextField typeService_textfield;

    @FXML
    private TextField description_textfield;

    @FXML
    void addService(ActionEvent event) {
        // Validation des champs
        if (nomService_textfield.getText().isEmpty() ||
                typeService_textfield.getText().isEmpty() ||
                description_textfield.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Récupérer les valeurs des champs
        String nomService = nomService_textfield.getText();
        String typeService = typeService_textfield.getText();
        String description = description_textfield.getText();

        // Créer un nouvel objet Service
        Service service = new Service(
                0, // id sera auto-généré
                nomService,
                typeService,
                description,
                true // estDisponible
        );

        // Appeler le service pour ajouter le service
        ServiceService serviceService = new ServiceService();
        try {
            serviceService.addService(service);
            showAlert("Succès", "Service ajouté avec succès !");
            // Réinitialiser les champs
            nomService_textfield.clear();
            typeService_textfield.clear();
            description_textfield.clear();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du service : " + e.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     *
     * @param title   Le titre de l'alerte.
     * @param message Le message à afficher.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}