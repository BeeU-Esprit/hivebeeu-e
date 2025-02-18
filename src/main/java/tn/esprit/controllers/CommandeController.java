package tn.esprit.controllers;

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

public class CommandeController {
    @FXML private ListView<String> produitListView;
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

    @FXML
    public void initialize() {
        loadProduits();
        loadCommandes();
        commandeStatus.getItems().addAll("Pending", "Validated", "Canceled");
    }

    private void loadProduits() {
        produitListView.getItems().clear();
        List<Produit> produits = produitService.getAllProduits();
        for (Produit produit : produits) {
            produitListView.getItems().add(produit.getNomProduit() + " (ID: " + produit.getIdProduit() + ")");
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
                    int productId = Integer.parseInt(selectedItem.split(" ")[1].replaceAll("[^0-9]", ""));
                    Produit produit = produitService.getProduitById(productId);

                    if (produit != null) {
                        CommandeProduits commandeProduits = new CommandeProduits(idCommande, productId, 1, produit.getPrixUnitaire());
                        commandeProduitsService.ajouterCommandeProduit(commandeProduits);
                        System.out.println("Product " + productId + " added to CommandeProduits");
                    } else {
                        System.out.println("Produit ID " + productId + " not found.");
                    }
                }
            } else {
                showAlert("Erreur", "Échec de l'ajout de la commande.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur doit être un nombre valide.");
            e.printStackTrace();
        }
    }




    @FXML
    private void modifierCommande() {
        String selectedCommande = commandeList.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert("Erreur", "Veuillez sélectionner une commande à modifier.");
            return;
        }

        int idCommande = Integer.parseInt(selectedCommande.split("#")[1].split(" - ")[0].trim());

        String newStatus = commandeStatus.getValue();
        if (newStatus == null) {
            showAlert("Erreur", "Veuillez sélectionner un nouveau statut.");
            return;
        }

        Commande commande = commandeService.getCommandeById(idCommande);
        if (commande != null) {
            commande.setStatus(newStatus);
            commandeService.modifierCommande(commande);
            showAlert("Succès", "Commande mise à jour avec succès.");
            loadCommandes();  // Refresh the list
        } else {
            showAlert("Erreur", "Commande non trouvée.");
        }
    }


    @FXML
    private void supprimerCommande() {
        String selectedCommande = commandeList.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert("Erreur", "Veuillez sélectionner une commande à supprimer.");
            return;
        }

        int idCommande = Integer.parseInt(selectedCommande.split("#")[1].split(" - ")[0].trim());

        if (commandeService.getCommandeById(idCommande) != null) {
            commandeService.supprimerCommande(idCommande);
            showAlert("Succès", "Commande supprimée avec succès.");
            loadCommandes();  // Refresh the list
        } else {
            showAlert("Erreur", "Commande non trouvée.");
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
