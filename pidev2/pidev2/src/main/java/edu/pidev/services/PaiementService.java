package edu.pidev.services;

import edu.pidev.entities.Paiement;
import edu.pidev.interfaces.IServices;
import edu.pidev.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import edu.pidev.entities.Paiement;
import edu.pidev.interfaces.IServices;
import edu.pidev.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class PaiementService implements IServices<Paiement> {

        @Override
        public void addEntity(Paiement paiement) throws SQLException {
            String requete = "INSERT INTO paiement (montant, dateP, mode_de_P, statutP, idCommande, statut) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
                pst.setFloat(1, paiement.getMontant());
                pst.setDate(2, Date.valueOf(paiement.getDateP())); // Conversion LocalDate -> Date SQL
                pst.setString(3, paiement.getMode_de_P());
                pst.setString(4, paiement.getStatutP());
                pst.setInt(5, paiement.getIdCommande());
                pst.setString(6, paiement.getStatut());

                pst.executeUpdate();

                // Récupérer l'ID généré automatiquement
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        paiement.setIdPaiement(rs.getInt(1)); // Mise à jour de l'idPaiement
                        System.out.println("Paiement ajouté avec succès ! ID généré: " + paiement.getIdPaiement());
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ajout du paiement : " + e.getMessage());
                throw e;
            }
        }

        @Override
        public void deleteEntity(Paiement paiement) throws SQLException {
            String requete = "DELETE FROM paiement WHERE idPaiement=?";
            try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
                pst.setInt(1, paiement.getIdPaiement());
                pst.executeUpdate();
                System.out.println("Paiement supprimé avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression du paiement : " + e.getMessage());
                throw e;
            }
        }

        @Override
        public void updateEntity(Paiement paiement) throws SQLException {
            String requete = "UPDATE paiement SET montant=?, dateP=?, mode_de_P=?, statutP=?, idCommande=?, statut=? WHERE idPaiement=?";
            try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
                pst.setFloat(1, paiement.getMontant());
                pst.setDate(2, Date.valueOf(paiement.getDateP())); // Conversion LocalDate -> Date SQL
                pst.setString(3, paiement.getMode_de_P());
                pst.setString(4, paiement.getStatutP());
                pst.setInt(5, paiement.getIdCommande());
                pst.setString(6, paiement.getStatut());
                pst.setInt(7, paiement.getIdPaiement());

                pst.executeUpdate();
                System.out.println("Paiement mis à jour avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la mise à jour du paiement : " + e.getMessage());
                throw e;
            }
        }

        @Override
        public List<Paiement> getAllData() {
            List<Paiement> results = new ArrayList<>();
            String requete = "SELECT * FROM paiement";

            try (Statement st = MyConnection.getInstance().getCnx().createStatement();
                 ResultSet rs = st.executeQuery(requete)) {

                while (rs.next()) {
                    Paiement p = new Paiement(
                            rs.getInt("idPaiement"),
                            rs.getFloat("montant"),
                            rs.getDate("dateP").toLocalDate(), // Conversion Date SQL -> LocalDate
                            rs.getString("mode_de_P"),
                            rs.getString("statutP"),
                            rs.getInt("idCommande"),
                            rs.getString("statut")
                    );

                    results.add(p);
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération des paiements : " + e.getMessage());
            }
            return results;
        }
    }


