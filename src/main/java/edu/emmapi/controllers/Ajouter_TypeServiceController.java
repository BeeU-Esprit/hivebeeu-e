package edu.emmapi.controllers;

import edu.emmapi.entities.TypeService;
import edu.emmapi.services.TypeServiceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class Ajouter_TypeServiceController {

    @FXML
    private Button ajouter_typeservice_button;

    @FXML
    private TextField typeService_textfield;

    @FXML
    void addTypeService(ActionEvent event) {
        TypeServiceService typeServiceService = new TypeServiceService();
        TypeService typeService = TypeService.valueOf(typeService_textfield.getText());

        try {
            typeServiceService.addTypeService(typeService);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Type de service ajouté avec succès !");
            alert.show();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de l'ajout du type de service : " + e.getMessage());
            alert.show();
        }
    }
}