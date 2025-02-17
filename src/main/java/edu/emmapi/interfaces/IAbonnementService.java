package edu.emmapi.interfaces;

import edu.emmapi.entities.Abonnement;
import java.sql.SQLException;
import java.util.List;

public interface IAbonnementService {
    void addAbonnement(Abonnement abonnement) throws SQLException;
    void deleteAbonnement(int idAbonnement) throws SQLException;
    void updateAbonnement(Abonnement abonnement) throws SQLException;
    List<Abonnement> getAllAbonnements() throws SQLException;
}