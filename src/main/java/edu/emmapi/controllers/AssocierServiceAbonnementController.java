package edu.emmapi.controllers;

import edu.emmapi.tools.MyConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AssocierServiceAbonnementController {

    @FXML
    private TextField idServiceField;

    @FXML
    private TextField idAbonnementField;

    private Connection conn;

    public void initialize() {
        conn = MyConnection.getInstance().getCnx();
    }

    @FXML
    private void associerServiceAbonnement() {
        int idService = Integer.parseInt(idServiceField.getText());
        int idAbonnement = Integer.parseInt(idAbonnementField.getText());

        String query = "INSERT INTO AbonnementService (idAbonnement, idService) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idAbonnement);
            stmt.setInt(2, idService);
            stmt.executeUpdate();
            System.out.println("Service associé à l'abonnement avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}