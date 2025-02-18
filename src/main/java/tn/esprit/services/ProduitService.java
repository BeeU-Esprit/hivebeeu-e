package tn.esprit.services;

import tn.esprit.models.Produit;
import tn.esprit.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService {
    private Connection connection;

    public ProduitService() {
        this.connection = DBConnection.getConnection();
    }
    public Produit getProduitById(int idProduit) {
        String query = "SELECT * FROM produits WHERE idProduit = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idProduit);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nomProduit"),
                        rs.getString("description"),
                        rs.getDouble("prixUnitaire")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ajouterProduit(Produit produit) {
        String query = "INSERT INTO produits (nomProduit, description, prixUnitaire) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, produit.getNomProduit());
            pstmt.setString(2, produit.getDescription());
            pstmt.setDouble(3, produit.getPrixUnitaire());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerProduit(int idProduit) {
        String query = "DELETE FROM produits WHERE idProduit = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idProduit);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifierProduit(Produit produit) {
        String query = "UPDATE produits SET nomProduit = ?, description = ?, prixUnitaire = ? WHERE idProduit = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, produit.getNomProduit());
            pstmt.setString(2, produit.getDescription());
            pstmt.setDouble(3, produit.getPrixUnitaire());
            pstmt.setInt(4, produit.getIdProduit());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produits";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                produits.add(new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nomProduit"),
                        rs.getString("description"),
                        rs.getDouble("prixUnitaire")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
