package edu.emmapi.services;

import edu.emmapi.entities.Service;
import edu.emmapi.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceService {

    // Méthode pour ajouter un service
    public void addService(Service service) throws SQLException {
        String query = "INSERT INTO Service(nomService, typeService, description, estDisponible) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, service.getNomService());
            pst.setString(2, service.getTypeService()); // Type de service en tant que String
            pst.setString(3, service.getDescription());
            pst.setBoolean(4, service.isEstDisponible());
            pst.executeUpdate();
            System.out.println("Service ajouté !");
        }
    }

    // Méthode pour supprimer un service
    public void deleteService(int idService) throws SQLException {
        String query = "DELETE FROM Service WHERE idService = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, idService);
            pst.executeUpdate();
            System.out.println("Service supprimé !");
        }
    }

    // Méthode pour mettre à jour un service
    public void updateService(Service service) throws SQLException {
        String query = "UPDATE Service SET nomService=?, typeService=?, description=?, estDisponible=? WHERE idService=?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, service.getNomService());
            pst.setString(2, service.getTypeService()); // Type de service en tant que String
            pst.setString(3, service.getDescription());
            pst.setBoolean(4, service.isEstDisponible());
            pst.setInt(5, service.getIdService());
            pst.executeUpdate();
            System.out.println("Service mis à jour !");
        }
    }

    // Méthode pour récupérer tous les services
    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM Service";
        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("idService"),
                        rs.getString("nomService"),
                        rs.getString("typeService"), // Type de service en tant que String
                        rs.getString("description"),
                        rs.getBoolean("estDisponible")
                );
                services.add(service);
            }
        }
        return services;
    }
}