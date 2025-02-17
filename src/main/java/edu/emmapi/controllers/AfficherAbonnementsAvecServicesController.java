package edu.emmapi.controllers;

import edu.emmapi.services.AbonnementService;
import edu.emmapi.tools.MyConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AfficherAbonnementsAvecServicesController {

    @FXML
    private TableView<AbonnementService> abonnementsAvecServicesTable;

    @FXML
    private TableColumn<AbonnementService, Integer> idAbonnementColumn;

    @FXML
    private TableColumn<AbonnementService, String> nomAbonnementColumn;

    @FXML
    private TableColumn<AbonnementService, String> nomServiceColumn;

    private Connection conn;

    public void initialize() {
        conn = MyConnection.getInstance().getCnx();
        configurerTableau();
        chargerAbonnementsAvecServices();
    }

    private void configurerTableau() {
        idAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("idAbonnement"));
        nomAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("nomAbonnement"));
        nomServiceColumn.setCellValueFactory(new PropertyValueFactory<>("nomService"));
    }

    private void chargerAbonnementsAvecServices() {
        String query = "SELECT a.idAbonnement, a.nomAbonnement, s.nomService " +
                "FROM Abonnement a " +
                "JOIN AbonnementService abs ON a.idAbonnement = abs.idAbonnement " +
                "JOIN Service s ON abs.idService = s.idService";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            abonnementsAvecServicesTable.getItems().clear();
            while (rs.next()) {
                AbonnementService abonnementService = new AbonnementService(
                );
                abonnementsAvecServicesTable.getItems().add(abonnementService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}