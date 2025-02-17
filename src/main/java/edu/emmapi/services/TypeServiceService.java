package edu.emmapi.services;

import edu.emmapi.entities.TypeService;
import edu.emmapi.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeServiceService {

    // Méthode pour ajouter un type de service
    public void addTypeService(TypeService typeService) throws SQLException {
        String query = "INSERT INTO TypeService(typeService) VALUES (?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, typeService.name());
            pst.executeUpdate();
            System.out.println("Type de service ajouté !");
        }
    }

    // Méthode pour récupérer tous les types de services
    public List<TypeService> getAllTypeServices() throws SQLException {
        List<TypeService> typeServices = new ArrayList<>();
        String query = "SELECT * FROM TypeService";
        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                TypeService typeService = TypeService.valueOf(rs.getString("typeService"));
                typeServices.add(typeService);
            }
        }
        return typeServices;
    }
}