package edu.emmapi.controllers;

import edu.emmapi.entities.Service;
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

public class ServicesController {

    @FXML
    private TextField nomServiceField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField typeServiceField;

    @FXML
    private TableView<Service> servicesTable;

    @FXML
    private TableColumn<Service, Integer> idServiceColumn;

    @FXML
    private TableColumn<Service, String> nomServiceColumn;

    @FXML
    private TableColumn<Service, String> descriptionColumn;

    @FXML
    private TableColumn<Service, String> typeServiceColumn;

    @FXML
    private TableColumn<Service, Boolean> estDisponibleColumn;

    private Connection conn;

    public void initialize() {
        conn = MyConnection.getInstance().getCnx();
        configurerTableau();
        chargerServices();
    }

    private void configurerTableau() {
        idServiceColumn.setCellValueFactory(new PropertyValueFactory<>("idService"));
        nomServiceColumn.setCellValueFactory(new PropertyValueFactory<>("nomService"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeServiceColumn.setCellValueFactory(new PropertyValueFactory<>("typeService"));
        estDisponibleColumn.setCellValueFactory(new PropertyValueFactory<>("estDisponible"));
    }

    private void chargerServices() {
        String query = "SELECT * FROM Service";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            servicesTable.getItems().clear();
            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("idService"),
                        rs.getString("nomService"),
                        rs.getString("typeService"),
                        rs.getString("description"),
                        rs.getBoolean("estDisponible")
                );
                servicesTable.getItems().add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterService() {
        String nomService = nomServiceField.getText();
        String description = descriptionField.getText();
        String typeService = typeServiceField.getText();
        boolean estDisponible = true; // Par défaut, le service est disponible

        String query = "INSERT INTO Service (nomService, description, typeService, estDisponible) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomService);
            stmt.setString(2, description);
            stmt.setString(3, typeService);
            stmt.setBoolean(4, estDisponible);
            stmt.executeUpdate();
            chargerServices(); // Recharger les services après l'ajout
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierService() {
        Service selectedService = servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            String nomService = nomServiceField.getText();
            String description = descriptionField.getText();
            String typeService = typeServiceField.getText();
            boolean estDisponible = selectedService.isEstDisponible(); // Conserver la disponibilité actuelle

            String query = "UPDATE Service SET nomService = ?, description = ?, typeService = ?, estDisponible = ? WHERE idService = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomService);
                stmt.setString(2, description);
                stmt.setString(3, typeService);
                stmt.setBoolean(4, estDisponible);
                stmt.setInt(5, selectedService.getIdService());
                stmt.executeUpdate();
                chargerServices(); // Recharger les services après la modification
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void supprimerService() {
        Service selectedService = servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            String query = "DELETE FROM Service WHERE idService = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, selectedService.getIdService());
                stmt.executeUpdate();
                chargerServices(); // Recharger les services après la suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}