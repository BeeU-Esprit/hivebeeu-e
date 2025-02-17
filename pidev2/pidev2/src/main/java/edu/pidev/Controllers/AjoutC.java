package edu.pidev.Controllers;

import edu.pidev.entities.Commande;
import edu.pidev.services.CommandeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjoutC {

    @FXML
    private Button ButtonAccueil;

    @FXML
    private Button ButtonAjouterP;

    @FXML
    private Button ButtonListeP;

    @FXML
    private Button ButtonListeV;

    @FXML
    private Button boutonajp;

    @FXML
    private Button buttonstatistiqueP;

    @FXML
    private DatePicker dateC;

    @FXML
    private TextField idClient;

    @FXML
    private TextField prix;

    @FXML
    private TextField quanitite;

    @FXML
    private TextField statut;

    @FXML
    private TextField total;
    @FXML
    private Button boutonmodif;

    @FXML
    void Home(ActionEvent event) {

    }

    @FXML
    void ajP(ActionEvent event) {

    }

    @FXML
    void ajouterc(ActionEvent event) {
        // Récupérer les valeurs des champs
        String idClientText = idClient.getText();
        String prixText = prix.getText();
        String quantiteText = quanitite.getText();
        String statutText = statut.getText();
        String totalText = total.getText();
        LocalDate dateCommande = dateC.getValue(); // Date de la commande

        // Vérification des entrées
        if (!isNumeric(prixText)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setHeaderText(null);
            alert.setContentText("Saisie invalide pour le prix !");
            alert.showAndWait();
            return;
        }

        if (!isNumericInt(quantiteText)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setHeaderText(null);
            alert.setContentText("Saisie invalide pour la quantité !");
            alert.showAndWait();
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (idClientText.isEmpty() || prixText.isEmpty() || quantiteText.isEmpty() || statutText.isEmpty() || totalText.isEmpty() || dateCommande == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        // Calcul du total si nécessaire (prix * quantité)
        float prixValue = Float.parseFloat(prixText);
        int quantiteValue = Integer.parseInt(quantiteText);
        float totalValue = prixValue * quantiteValue;

        // Créer l'objet Commande
        Commande commande = new Commande(0, dateCommande, quantiteValue, prixValue, totalValue, statutText, Integer.parseInt(idClientText));

        // Ajouter la commande à la base de données via le service
        try {
            CommandeService commandeService = new CommandeService();  // Assure-toi que commandeService est bien instancié
            commandeService.addEntity(commande);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès!");
            alert.setHeaderText(null);
            alert.setContentText("Commande ajoutée avec succès !");
            alert.showAndWait();

            // Optionnel : après l'ajout, tu peux rediriger vers une autre vue
            // Par exemple, revenir à la liste des commandes
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("/Commande_dash.fxml"));
            // Parent root = loader.load();
            // Stage stage = (Stage) ButtonAjouterP.getScene().getWindow();
            // stage.setScene(new Scene(root));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout de la commande.");
            alert.showAndWait();
        }
    }

    // Fonction pour vérifier si une chaîne est un nombre (pour le prix)
    private boolean isNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Fonction pour vérifier si une chaîne est un nombre entier (pour la quantité)
    private boolean isNumericInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    void listP(ActionEvent event) {

    }

    @FXML
    void ovrire(ActionEvent event) {
        try {
            // Charger le fichier FXML de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifC.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la fenêtre de modification
            modifC modifController = loader.getController();
            // Passer les valeurs actuelles à la fenêtre de modification
            modifController.initData(
                    idClient.getText(),
                    prix.getText(),
                    quanitite.getText(),
                    statut.getText(),
                    total.getText(),
                    dateC.getValue()
            );

            // Ouvrir la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier la Commande");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void listV(ActionEvent event) {

    }



    @FXML
    void showStatP(ActionEvent event) {

    }

}
