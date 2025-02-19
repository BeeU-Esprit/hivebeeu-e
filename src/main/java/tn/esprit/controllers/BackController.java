package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Paiement;
import tn.esprit.services.PaiementService;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static tn.esprit.util.DBConnection.connection;

public class BackController implements Initializable {
    @FXML private ListView<Paiement> paiementListView;
    @FXML private Pagination paginationPaiement;
    @FXML private Button validerPaiementButton;
    @FXML private Button rejeterPaiementButton;
    @FXML private Button backButton;

    private ObservableList<Paiement> allPaiements;
    private final PaiementService paiementService = new PaiementService();
    public static final int ITEMS_PER_PAGE = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (paginationPaiement != null) {
            paginationPaiement.setPageFactory(this::createPage);
        }
        loadPaiements();
        paiementListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Paiement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label infoLabel = new Label("ID: " + item.getIdPaiement() +
                            " | Utilisateur: " + item.getIdUtilisateur() +
                            " | Montant: " + item.getMontant() + " TND" +
                            " | Mode: " + item.getModeDePaiement() +
                            " | Status: " + item.getStatus());

                    // Just display the information without the action buttons in the ListView
                    VBox layout = new VBox(infoLabel);
                    layout.setSpacing(10);

                    setGraphic(layout);
                }
            }
        });
    }

    private void loadPaiements() {
        allPaiements = FXCollections.observableArrayList(paiementService.getAllPaiements());
        paiementListView.setItems(allPaiements);
        if (paginationPaiement != null) {
            paginationPaiement.setPageCount(calculatePageCount(allPaiements.size()));
        }
    }

    private int calculatePageCount(int totalItems) {
        return (totalItems + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allPaiements.size());
        paiementListView.setItems(FXCollections.observableArrayList(allPaiements.subList(fromIndex, toIndex)));
        return paiementListView;
    }

    private void updatePaiementStatus(Paiement paiement, String newStatus) {
        try {
            String query = "UPDATE paiement SET status = ? WHERE idCommande = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, paiement.getIdCommande());
            pstmt.executeUpdate();
            loadPaiements();
            showAlert("Succès", "Le paiement a été mis à jour avec succès!");
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de mettre à jour le paiement: " + e.getMessage());
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
    public void validerPaiement(ActionEvent actionEvent) {
        Paiement selectedPaiement = paiementListView.getSelectionModel().getSelectedItem();
        if (selectedPaiement != null) {
            updatePaiementStatus(selectedPaiement, "Completed");
        } else {
            showAlert("Erreur", "Veuillez sélectionner un paiement à valider.");
        }
    }

    @FXML
    public void rejeterPaiement(ActionEvent actionEvent) {
        Paiement selectedPaiement = paiementListView.getSelectionModel().getSelectedItem();
        if (selectedPaiement != null) {
            updatePaiementStatus(selectedPaiement, "Failed");
        } else {
            showAlert("Erreur", "Veuillez sélectionner un paiement à rejeter.");
        }
    }

    @FXML
    public void goToBackInterface(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
