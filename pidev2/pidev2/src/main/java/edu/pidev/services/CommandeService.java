package edu.pidev.services;

import edu.pidev.entities.Commande;
import edu.pidev.interfaces.IServices;
import edu.pidev.tools.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


    import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


        import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IServices<Commande> {

            @Override
            public void addEntity(Commande commande) throws SQLException {
                String requete = "INSERT INTO commande (dateC, quani" +
                        "tite, prix, total, statut, idClient) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
                    pst.setDate(1, Date.valueOf(commande.getDateC())); // Conversion LocalDate -> Date SQL
                    pst.setInt(2, commande.getQuanitite());
                    pst.setDouble(3, commande.getPrix());
                    pst.setDouble(4, commande.getTotal());
                    pst.setString(5, commande.getStatut());
                    pst.setInt(6, commande.getIdClient());

                    pst.executeUpdate();
                    System.out.println("Commande ajoutée avec succès !");
                } catch (SQLException e) {
                    System.err.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
                    throw e;
                }
            }

    public void deleteEntity(Commande commande) throws SQLException {
        // Supprimer les paiements associés à la commande
        String paiementDeleteQuery = "DELETE FROM paiement WHERE idCommande=?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(paiementDeleteQuery)) {
            pst.setInt(1, commande.getIdC());
            pst.executeUpdate();
            System.out.println("Paiements associés à la commande supprimés avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des paiements associés : " + e.getMessage());
            throw e;
        }

        // Ensuite, supprimer la commande
        String commandeDeleteQuery = "DELETE FROM commande WHERE idC=?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(commandeDeleteQuery)) {
            pst.setInt(1, commande.getIdC());
            pst.executeUpdate();
            System.out.println("Commande supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la commande : " + e.getMessage());
            throw e;
        }
    }


    @Override
            public void updateEntity(Commande commande) {
                String requete = "UPDATE commande SET dateC=?, quanitite=?, prix=?, total=?, statut=?, idClient=? WHERE idC=?";
                try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
                    pst.setDate(1, Date.valueOf(commande.getDateC())); // Conversion LocalDate -> Date SQL
                    pst.setInt(2, commande.getQuanitite());
                    pst.setDouble(3, commande.getPrix());
                    pst.setDouble(4, commande.getTotal());
                    pst.setString(5, commande.getStatut());
                    pst.setInt(6, commande.getIdClient());
                    pst.setInt(7, commande.getIdC());

                    pst.executeUpdate();
                    System.out.println("Commande mise à jour avec succès !");
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la mise à jour de la commande : " + e.getMessage());
                }
            }

            @Override
            public List<Commande> getAllData() {
                List<Commande> results = new ArrayList<>();
                String requete = "SELECT * FROM commande";

                try (Statement st = MyConnection.getInstance().getCnx().createStatement();
                     ResultSet rs = st.executeQuery(requete)) {

                    while (rs.next()) {
                        Commande p = new Commande(
                                rs.getInt("idC"),
                                rs.getDate("dateC").toLocalDate(), // Conversion Date SQL -> LocalDate
                                rs.getInt("quanitite"),
                                rs.getDouble("prix"),
                                rs.getDouble("total"),
                                rs.getString("statut"),
                                rs.getInt("idClient")
                        );

                        results.add(p);
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la récupération des commandes : " + e.getMessage());
                }
                return results;
            }
        }
