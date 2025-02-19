
package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Commande;
import tn.esprit.models.CommandeProduits;
import tn.esprit.models.Produit;
import tn.esprit.services.CommandeProduitsService;
import tn.esprit.services.CommandeService;
import tn.esprit.services.ProduitService;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AjoutCommande {

    @FXML
    private ListView<String> produitListView;
    @FXML private ListView<String> commandeList;
    @FXML private TextField commandeUtilisateur;
    @FXML private DatePicker commandeDate;
    @FXML private ComboBox<String> commandeStatus;

    private final ProduitService produitService = new ProduitService();
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (commandeList == null) {
                System.out.println("❌ commandeList is null!");
            } else {
                loadCommandes();
            }

            if (produitListView == null) {
                System.out.println("❌ produitListView is null!");
            } else {
                loadProduits(); // Ensure it gets called
            }
        });
    }



    private void populateCommandeFields(String selectedCommande) {
        try {
            // Extracting the ID from the string "Commande #12 - Validated"
            int idCommande = Integer.parseInt(selectedCommande.split("#")[1].split(" - ")[0].trim());

            // Fetch the full commande details
            Commande commande = commandeService.getCommandeById(idCommande);
            if (commande != null) {
                commandeUtilisateur.setText(String.valueOf(commande.getIdUtilisateur())); // Set user ID
                commandeDate.setValue(commande.getDateDeCommande()); // Set date
                commandeStatus.setValue(commande.getStatus()); // Set status
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les détails de la commande.");
        }
    }



    private void loadProduits() {
        produitListView.getItems().clear();
        List<Produit> produits = produitService.getAllProduits();

        if (produits == null || produits.isEmpty()) {
            System.out.println("🔴 No products found in database!");
        } else {
            System.out.println("✅ Found " + produits.size() + " products.");
            for (Produit produit : produits) {
                String produitString = produit.getNomProduit() + " (ID: " + produit.getIdProduit() + ")";
                System.out.println("Adding: " + produitString);
                produitListView.getItems().add(produitString);
            }
        }
    }



    private void loadCommandes() {
        commandeList.getItems().clear();
        List<Commande> commandes = commandeService.getAllCommandes();
        for (Commande commande : commandes) {
            commandeList.getItems().add("Commande #" + commande.getIdCommande() + " - " + commande.getStatus());
        }
    }
    @FXML
    private void refreshCommandes() {
        System.out.println("🔄 Rafraîchissement des commandes...");
        loadCommandes();
        showAlert("Info", "Liste des commandes rafraîchie avec succès !");}

    @FXML
    private void createCommande() {
        if (commandeUtilisateur.getText().isEmpty() || commandeDate.getValue() == null || commandeStatus.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            LocalDate dateDeCommande = commandeDate.getValue();
            int idUtilisateur = Integer.parseInt(commandeUtilisateur.getText().trim());
            String status = commandeStatus.getValue();

            // Create Commande
            Commande commande = new Commande(dateDeCommande, idUtilisateur, status);
            int idCommande = commandeService.ajouterCommande(commande);

            if (idCommande != -1) {
                System.out.println("Commande added successfully with ID: " + idCommande);

                // Add Products to CommandeProduits table
                CommandeProduitsService commandeProduitsService = new CommandeProduitsService();

                for (String selectedItem : produitListView.getSelectionModel().getSelectedItems()) {
                    if (selectedItem == null || selectedItem.isEmpty()) {
                        System.out.println("❌ Skipping empty product entry!");
                        continue;
                    }

                    String[] parts = selectedItem.split(" ");
                    if (parts.length < 2) {
                        System.out.println("❌ Skipping invalid product format: " + selectedItem);
                        continue;
                    }

                    String numericPart = parts[1].replaceAll("[^0-9]", "");
                    if (numericPart.isEmpty()) {
                        System.out.println("Skipping product with missing ID: " + selectedItem);
                        continue;
                    }

                    int productId = Integer.parseInt(numericPart);
                    Produit produit = produitService.getProduitById(productId);

                    if (produit != null) {
                        CommandeProduits commandeProduits = new CommandeProduits(idCommande, productId, 1, produit.getPrixUnitaire());
                        commandeProduitsService.ajouterCommandeProduit(commandeProduits);
                        System.out.println("🟢 Product " + productId + " added to CommandeProduits");
                    } else {
                        System.out.println("❌ Produit ID " + productId + " not found.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur doit être un nombre valide.");
            e.printStackTrace();
        }
    } // ✅ Properly close `createCommande()`


    }
