package edu.emmapi.controllers;

import edu.emmapi.entities.Abonnement;
import edu.emmapi.tools.MyConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AbonnementsController {

    @FXML
    private TextField nomAbonnementField;

    @FXML
    private TextField typeAbonnementField;

    @FXML
    private TextField dateDebutField;

    @FXML
    private TextField statutAbonnementField;

    @FXML
    private TableView<Abonnement> abonnementsTable;

    @FXML
    private TableColumn<Abonnement, Integer> idAbonnementColumn;

    @FXML
    private TableColumn<Abonnement, String> nomAbonnementColumn;

    @FXML
    private TableColumn<Abonnement, String> typeAbonnementColumn;

    @FXML
    private TableColumn<Abonnement, Date> dateDebutColumn;

    @FXML
    private TableColumn<Abonnement, String> statutAbonnementColumn;

    private Connection conn;

    public void initialize() {
        conn = MyConnection.getInstance().getCnx();
        configurerTableau();
        chargerAbonnements();
    }

    private void configurerTableau() {
        idAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("idAbonnement"));
        nomAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("nomAbonnement"));
        typeAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("typeAbonnement"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        statutAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("statutAbonnement"));
    }

    private void chargerAbonnements() {
        String query = "SELECT * FROM Abonnement";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            abonnementsTable.getItems().clear();
            while (rs.next()) {
                Abonnement abonnement = new Abonnement(
                        rs.getInt("idAbonnement"),
                        rs.getString("nomAbonnement"),
                        rs.getString("typeAbonnement"),
                        rs.getDate("dateDebut"),
                        rs.getString("statutAbonnement"),
                        rs.getBoolean("estDisponible")
                );
                abonnementsTable.getItems().add(abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterAbonnement() {
        String nomAbonnement = nomAbonnementField.getText();
        String typeAbonnement = typeAbonnementField.getText();
        Date dateDebut = java.sql.Date.valueOf(dateDebutField.getText()); // Convertir la chaîne en Date
        String statutAbonnement = statutAbonnementField.getText();
        boolean estDisponible = true; // Par défaut, l'abonnement est disponible

        String query = "INSERT INTO Abonnement (nomAbonnement, typeAbonnement, dateDebut, statutAbonnement, estDisponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomAbonnement);
            stmt.setString(2, typeAbonnement);
            stmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
            stmt.setString(4, statutAbonnement);
            stmt.setBoolean(5, estDisponible);
            stmt.executeUpdate();
            chargerAbonnements(); // Recharger les abonnements après l'ajout
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierAbonnement() {
        Abonnement selectedAbonnement = abonnementsTable.getSelectionModel().getSelectedItem();
        if (selectedAbonnement != null) {
            String nomAbonnement = nomAbonnementField.getText();
            String typeAbonnement = typeAbonnementField.getText();
            Date dateDebut = java.sql.Date.valueOf(dateDebutField.getText()); // Convertir la chaîne en Date
            String statutAbonnement = statutAbonnementField.getText();
            boolean estDisponible = selectedAbonnement.isEstDisponible(); // Conserver la disponibilité actuelle

            String query = "UPDATE Abonnement SET nomAbonnement = ?, typeAbonnement = ?, dateDebut = ?, statutAbonnement = ?, estDisponible = ? WHERE idAbonnement = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomAbonnement);
                stmt.setString(2, typeAbonnement);
                stmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
                stmt.setString(4, statutAbonnement);
                stmt.setBoolean(5, estDisponible);
                stmt.setInt(6, selectedAbonnement.getIdAbonnement());
                stmt.executeUpdate();
                chargerAbonnements(); // Recharger les abonnements après la modification
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void supprimerAbonnement() {
        Abonnement selectedAbonnement = abonnementsTable.getSelectionModel().getSelectedItem();
        if (selectedAbonnement != null) {
            String query = "DELETE FROM Abonnement WHERE idAbonnement = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, selectedAbonnement.getIdAbonnement());
                stmt.executeUpdate();
                chargerAbonnements(); // Recharger les abonnements après la suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}