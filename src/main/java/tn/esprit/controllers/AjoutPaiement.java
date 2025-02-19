package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Commande;
import tn.esprit.models.Paiement;
import tn.esprit.services.CommandeService;
import tn.esprit.services.PaiementService;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static tn.esprit.util.DBConnection.connection;

public class AjoutPaiement {

    @FXML private ListView<String> commandeListView;
    @FXML private ListView<String> paiementList;
    @FXML private TextField paiementCommandeId;
    @FXML private TextField paiementUtilisateurId;
    @FXML private TextField paiementMontant;
    @FXML private ComboBox<String> paiementMode;
    @FXML private DatePicker paiementDate;
    @FXML private ComboBox<String> paiementStatus;

    private final PaiementService paiementService = new PaiementService();
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
        if (commandeListView == null) {
            System.out.println("❌ commandeListView is null! Check fx:id in FXML.");
        } else {
            loadCommandes();
        }

        if (paiementMode == null) {
            System.out.println("❌ paiementMode is null! Check fx:id in FXML.");
        } else {
            paiementMode.getItems().addAll("Cash", "Carte bancaire", "Chèque", "Virement");
        }

        if (paiementStatus == null) {
            System.out.println("❌ paiementStatus is null! Check fx:id in FXML.");
        } else {
            paiementStatus.getItems().addAll("Pending", "Completed", "Failed");
        }

        if (paiementList == null) {
            System.out.println("❌ paiementList is null! Check fx:id in FXML.");
        } else {
            loadPaiements();
        }
    }


    private void populatePaiementFields(String selectedPaiement) {
        try {
            String[] details = selectedPaiement.split("\\|");
            int idCommande = Integer.parseInt(details[0].split(":")[1].trim());
            int idUtilisateur = Integer.parseInt(details[1].split(":")[1].trim());
            double montant = Double.parseDouble(details[2].split(":")[1].replace("TND", "").trim());
            String modeDePaiement = details[3].split(":")[1].trim();
            String status = details[4].split(":")[1].trim();

            paiementCommandeId.setText(String.valueOf(idCommande));
            paiementUtilisateurId.setText(String.valueOf(idUtilisateur));
            paiementMontant.setText(String.valueOf(montant));
            paiementMode.setValue(modeDePaiement);
            paiementStatus.setValue(status);

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les détails du paiement.");
        }
    }

    private void loadCommandes() {
        commandeListView.getItems().clear();
        List<Commande> commandes = commandeService.getAllCommandes();
        for (Commande commande : commandes) {
            commandeListView.getItems().add("Commande #" + commande.getIdCommande());
        }
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




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void effectuerPaiement(ActionEvent actionEvent) {
        // Validate input fields before proceeding
        if (paiementCommandeId.getText().trim().isEmpty() ||
                paiementUtilisateurId.getText().trim().isEmpty() ||
                paiementMontant.getText().trim().isEmpty() ||
                paiementMode.getValue() == null ||
                paiementDate.getValue() == null ||
                paiementStatus.getValue() == null) {

            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int idCommande = Integer.parseInt(paiementCommandeId.getText().trim());
            int idUtilisateur = Integer.parseInt(paiementUtilisateurId.getText().trim());
            double montant = Double.parseDouble(paiementMontant.getText().trim());
            String modeDePaiement = paiementMode.getValue();
            String status = paiementStatus.getValue();
            LocalDate date = paiementDate.getValue();

            // Create Paiement object
            Paiement paiement = new Paiement(idCommande, idUtilisateur, montant, modeDePaiement, date, status);

            // Prepare SQL query
            String query = "INSERT INTO paiement (idCommande, idUtilisateur, montant, modeDePaiement, dateDePaiement, status) VALUES (?, ?, ?, ?, ?, ?)";

            System.out.println("🔍 Mode de paiement before inserting: " + paiement.getModeDePaiement());

            // Execute query
            try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, paiement.getIdCommande());
                pstmt.setInt(2, paiement.getIdUtilisateur());
                pstmt.setDouble(3, paiement.getMontant());
                pstmt.setString(4, paiement.getModeDePaiement());
                pstmt.setDate(5, Date.valueOf(paiement.getDateDePaiement()));
                pstmt.setString(6, paiement.getStatus());

                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    // Retrieve generated payment ID
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        paiement.setIdPaiement(generatedKeys.getInt(1));
                    }

                    System.out.println("✅ Paiement enregistré: " + paiement);
                    showAlert("Succès", "Paiement effectué avec succès !");

                    // Refresh the list of payments after adding
                    loadPaiements();
                } else {
                    showAlert("Erreur", "Échec de l'enregistrement du paiement.");
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID et Montant doivent être des nombres valides.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'enregistrement du paiement.");
        }
    }

}
