package edu.emmapi.services;

import edu.emmapi.entities.Abonnement;
import edu.emmapi.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService {

    // Méthode pour ajouter un abonnement
    public void addAbonnement(Abonnement abonnement) throws SQLException {
        String query = "INSERT INTO Abonnement(nomService, typeAbonnement, date, statusAbonnement, estDisponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, abonnement.getNomService());
            pst.setString(2, abonnement.getTypeAbonnement());
            pst.setDate(3, new java.sql.Date(abonnement.getDate().getTime()));
            pst.setString(4, abonnement.getStatusAbonnement());
            pst.setBoolean(5, abonnement.isEstDisponible());
            pst.executeUpdate();
            System.out.println("Abonnement ajouté !");
        }
    }

    // Méthode pour supprimer un abonnement
    public void deleteAbonnement(int idAbonnement) throws SQLException {
        String query = "DELETE FROM Abonnement WHERE idAbonnement = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, idAbonnement);
            pst.executeUpdate();
            System.out.println("Abonnement supprimé !");
        }
    }

    // Méthode pour mettre à jour un abonnement
    public void updateAbonnement(Abonnement abonnement) throws SQLException {
        String query = "UPDATE Abonnement SET nomService=?, typeAbonnement=?, date=?, statusAbonnement=?, estDisponible=? WHERE idAbonnement=?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, abonnement.getNomService());
            pst.setString(2, abonnement.getTypeAbonnement());
            pst.setDate(3, new java.sql.Date(abonnement.getDate().getTime()));
            pst.setString(4, abonnement.getStatusAbonnement());
            pst.setBoolean(5, abonnement.isEstDisponible());
            pst.setInt(6, abonnement.getIdAbonnement());
            pst.executeUpdate();
            System.out.println("Abonnement mis à jour !");
        }
    }

    // Méthode pour récupérer tous les abonnements
    public List<Abonnement> getAllAbonnements() throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM Abonnement";
        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Abonnement abonnement = new Abonnement(
                        rs.getInt("idAbonnement"),
                        rs.getString("nomService"),
                        rs.getString("typeAbonnement"),
                        rs.getDate("date"),
                        rs.getString("statusAbonnement"),
                        rs.getBoolean("estDisponible")
                );
                abonnements.add(abonnement);
            }
        }
        return abonnements;
    }
}