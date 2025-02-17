package edu.emmapi.controllers;

import edu.emmapi.entities.TypeService;
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

public class TypesServiceController {

    @FXML
    private TextField typeServiceField;

    @FXML
    private TableView<TypeService> typesServiceTable;

    @FXML
    private TableColumn<TypeService, Integer> idTypeServiceColumn;

    @FXML
    private TableColumn<TypeService, String> typeServiceColumn;

    private Connection conn;

    public void initialize() {
        conn = MyConnection.getInstance().getCnx();
        configurerTableau();
        chargerTypesService();
    }

    private void configurerTableau() {
        idTypeServiceColumn.setCellValueFactory(new PropertyValueFactory<>("idTypeService"));
        typeServiceColumn.setCellValueFactory(new PropertyValueFactory<>("typeService"));
    }

    private void chargerTypesService() {
        String query = "SELECT * FROM TypeService";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            typesServiceTable.getItems().clear();
            while (rs.next()) {
                // Utiliser les valeurs de l'enum
                String typeServiceStr = rs.getString("typeService");
                TypeService typeService = TypeService.valueOf(typeServiceStr.toUpperCase().replace(" ", "_"));
                typesServiceTable.getItems().add(typeService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterTypeService() {
        String typeService = typeServiceField.getText();

        // Vérifier si le type de service existe dans l'enum
        try {
            TypeService.valueOf(typeService.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            System.out.println("Type de service invalide !");
            return;
        }

        String query = "INSERT INTO TypeService (typeService) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, typeService);
            stmt.executeUpdate();
            chargerTypesService(); // Recharger les types de services après l'ajout
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}