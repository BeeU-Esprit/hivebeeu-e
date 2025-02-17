package edu.pidev.Controllers;

import edu.pidev.entities.Commande;
import edu.pidev.services.CommandeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class modifC {

    @FXML
    private Button ButtonAccueil;

    @FXML
    private Button ButtonAjouterP;

    @FXML
    private Button ButtonListeP;

    @FXML
    private Button ButtonListeV;

    @FXML
    private Button boutonmodif;

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

    // Variable pour stocker l'ID de la commande
    private int idCommande;

    public void initData(String idClientText, String prixText, String quantiteText, String statutText, String totalText, LocalDate dateCommande) {
        idClient.setText(idClientText);
        prix.setText(prixText);
        quanitite.setText(quantiteText);
        statut.setText(statutText);
        total.setText(totalText);
        dateC.setValue(dateCommande);
    }


    // Méthode pour modifier la commande
    @FXML
    void modifC(ActionEvent event) {
        // Récupérer les valeurs des champs
        String idClientText = idClient.getText();
        String prixText = prix.getText();
        String quantiteText = quanitite.getText();
        String statutText = statut.getText();
        String totalText = total.getText();
        LocalDate dateCommande = dateC.getValue();

        // Vérifier que tous les champs sont remplis et valides
        if (idClientText.isEmpty() || prixText.isEmpty() || quantiteText.isEmpty() || statutText.isEmpty() || totalText.isEmpty() || dateCommande == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        // Calculer le total si nécessaire
        float prixValue = Float.parseFloat(prixText);
        int quantiteValue = Integer.parseInt(quantiteText);
        float totalValue = prixValue * quantiteValue;

        // Créer l'objet Commande avec les nouvelles valeurs
        Commande commande = new Commande(
                0, // L'ID de la commande devrait être récupéré de la commande sélectionnée (ex : idCommande)
                dateCommande,
                quantiteValue,
                prixValue,
                totalValue,
                statutText,
                Integer.parseInt(idClientText)
        );

        // Appeler la méthode de service pour mettre à jour la commande dans la base de données
        CommandeService commandeService = new CommandeService();
        commandeService.updateEntity(commande);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès!");
        alert.setHeaderText(null);
        alert.setContentText("Commande mise à jour avec succès !");
        alert.showAndWait();

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
}
