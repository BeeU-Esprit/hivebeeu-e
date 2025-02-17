package edu.pidev.Controllers;

import edu.pidev.entities.Commande;
import edu.pidev.services.CommandeService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;
/*
public class AjouteC {

    @FXML
    private DatePicker dateCTextField;

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField produitCTextField;

    @FXML
    private Button btnsave;

    @FXML
    private TextField quantiteTextField;

    @FXML
    private TextField statutTextField;

    @FXML
    private Button buttonExportPDF;

    @FXML
    private Button typeactivitetextfield;

    @FXML
    private TextField totalTextField;

    @FXML
    public void ajouteCommande(ActionEvent event) {
        CommandeService commandeService = new CommandeService();

        try {
            // Vérifier que la quantité est un entier positif
            String quantiteText = quantiteTextField.getText();
            if (!quantiteText.matches("\\d+")) {  // Vérifie que la chaîne contient uniquement des chiffres
                throw new NumberFormatException("La quantité doit être un entier.");
            }
            int quantite = Integer.parseInt(quantiteText);

            // Vérifier que le prix est un nombre à virgule flottante
            String prixText = prixTextField.getText();
            if (!prixText.matches("\\d+(\\.\\d+)?")) {  // Vérifie que la chaîne est un nombre décimal
                throw new NumberFormatException("Le prix doit être un nombre valide.");
            }
            float prix = Float.parseFloat(prixText);

            // Vérifier que le total est un nombre à virgule flottante
            String totalText = totalTextField.getText();
            if (!totalText.matches("\\d+(\\.\\d+)?")) {  // Vérifie que la chaîne est un nombre décimal
                throw new NumberFormatException("Le total doit être un nombre valide.");
            }
            float total = Float.parseFloat(totalText);

            // Vérifier que le statut contient uniquement des lettres alphabétiques
            String statut = statutTextField.getText();
            if (!statut.matches("[a-zA-Z]+")) {  // Vérifie que le statut ne contient que des lettres
                throw new IllegalArgumentException("Le statut doit contenir uniquement des lettres.");
            }

            // Vérification que la date est sélectionnée
            String dateC = (dateCTextField.getValue() != null) ? dateCTextField.getValue().toString() : "";
            if (dateC.isEmpty()) {
                throw new IllegalArgumentException("La date est obligatoire.");
            }

            // Création de l'objet Commande avec les types corrects
            Commande commande = new Commande(
                    produitCTextField.getText(),  // String pour le produit
                    dateC,  // String pour la date
                    quantite,  // int pour la quantité
                    prix,  // float pour le prix
                    total,  // float pour le total
                    statut  // String pour le statut
            );

            // Ajouter la commande via le service
            commandeService.addEntity(commande);

            // Affichage d'une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ajout réussi avec succès !");
            alert.showAndWait();



            // Ouvrir l'interface de la liste des commandes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Commande_dash.fxml")); // Remplacez par le bon chemin de votre fichier FXML
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();

        } catch (SQLException e) {
            // Gestion des erreurs liées à la base de données
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Échec de l'ajout : " + e.getMessage());
            alert.show();
        } catch (Exception e) {
            // Gestion des autres erreurs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur inattendue : " + e.getMessage());
            alert.show();
        }
    }
}
*/