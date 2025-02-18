package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Produit;
import tn.esprit.services.ProduitService;

import java.io.IOException;
import java.util.List;

public class ProduitController {
    @FXML
    private TextField produitNom;
    @FXML
    private TextField produitDescription;
    @FXML
    private TextField produitPrix;
    @FXML
    private VBox produitListContainer;

    private final ProduitService produitService = new ProduitService();
    private Produit selectedProduit = null;
    @FXML private ListView<Produit> produitListView;

    public void initialize() {
        List<Produit> produits = new ProduitService().getAllProduits();
        produitListView.setItems(FXCollections.observableList(produits));
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
    private void loadProduits() {
        produitListContainer.getChildren().clear();
        List<Produit> produits = produitService.getAllProduits();
        for (Produit produit : produits) {
            HBox produitBox = new HBox(10);
            Label nomLabel = new Label(produit.getNomProduit());
            Label descLabel = new Label(produit.getDescription());
            Label prixLabel = new Label(String.valueOf(produit.getPrixUnitaire()));
            Button deleteButton = new Button("Supprimer");
            Button editButton = new Button("Modifier");

            deleteButton.setOnAction(e -> supprimerProduit(produit));
            editButton.setOnAction(e -> remplirChampsModification(produit));

            produitBox.getChildren().addAll(nomLabel, descLabel, prixLabel, editButton, deleteButton);
            produitListContainer.getChildren().add(produitBox);
        }
    }

    @FXML
    private void ajouterProduit(ActionEvent event) {
        String nom = produitNom.getText();
        String description = produitDescription.getText();
        double prix;

        try {
            prix = Double.parseDouble(produitPrix.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide.");
            return;
        }

        Produit produit = new Produit(nom, description, prix);
        produitService.ajouterProduit(produit);
        clearFields();
        loadProduits();
    }

    @FXML
    private void modifierProduit(ActionEvent event) {
        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à modifier.");
            return;
        }

        selectedProduit.setNomProduit(produitNom.getText());
        selectedProduit.setDescription(produitDescription.getText());

        try {
            selectedProduit.setPrixUnitaire(Double.parseDouble(produitPrix.getText()));
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide.");
            return;
        }

        produitService.modifierProduit(selectedProduit);
        clearFields();
        loadProduits();
    }

    private void supprimerProduit(Produit produit) {
        produitService.supprimerProduit(produit.getIdProduit());
        loadProduits();
    }

    private void remplirChampsModification(Produit produit) {
        selectedProduit = produit;
        produitNom.setText(produit.getNomProduit());
        produitDescription.setText(produit.getDescription());
        produitPrix.setText(String.valueOf(produit.getPrixUnitaire()));
    }

    private void clearFields() {
        produitNom.clear();
        produitDescription.clear();
        produitPrix.clear();
        selectedProduit = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void supprimerProduit(ActionEvent event) {
        if (selectedProduit == null) {
            showAlert("Erreur", "Veuillez sélectionner un produit à supprimer.");
            return;
        }

        produitService.supprimerProduit(selectedProduit.getIdProduit());
        clearFields();
        loadProduits();
    }
}