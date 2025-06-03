package gestion.venteguitare.main.controlers;

import com.mysql.jdbc.Connection;
import gestion.venteguitare.main.model.Vente;
import gestion.venteguitare.main.model.LigneVente;
import gestion.venteguitare.main.model.Utilisateur;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VenteService {
    private static final Logger logger = Logger.getLogger(VenteService.class.getName());
    private static final int BATCH_SIZE = 5;
    
    private final VenteDAO venteDAO;
    private final LigneVenteDAO ligneVenteDAO;
    private final ClientDAO clientDAO;
    private final GuitareDAO guitareDAO;
    
    public VenteService() throws Exception {
        this.venteDAO = new VenteDAO();
        this.ligneVenteDAO = new LigneVenteDAO();
        this.clientDAO = new ClientDAO();
        this.guitareDAO = new GuitareDAO();
    }

    public void enregistrerVente(Vente vente, Utilisateur utilisateurConnecte) throws Exception {
        Connection con = null;
        try {
            ConnexionBase connexion = new ConnexionBase();
            logger.log(Level.INFO, "=== DEBUT TRANSACTION ===");
            connexion.getConnection().setAutoCommit(false);
            
            try {
                // 1. Validation
                validateVente(vente);
                
                // 2. Enregistrement vente principale
                int venteId = enregistrerVentePrincipale(vente, utilisateurConnecte, connexion);
                
                // 3. Enregistrement lignes
                traiterLignesVente(vente.getLignes(), venteId, connexion);
                
                connexion.getConnection().commit();
                logger.log(Level.INFO, "=== TRANSACTION REUSSIE ===");
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "!!! ERREUR TRANSACTION !!!", e);
                try {
                    connexion.getConnection().rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Échec du rollback", ex);
                }
                throw e;
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "!!! ERREUR TRANSACTION !!!", e);
            if (con != null) {
                try {
                    con.rollback();
                    logger.log(Level.WARNING, "Rollback effectué");
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Échec du rollback", ex);
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Erreur fermeture connexion", ex);
                }
            }
        }
    }

    private void validateVente(Vente vente) throws Exception {
        if (vente.getLignes() == null || vente.getLignes().isEmpty()) {
            throw new Exception("Le panier est vide");
        }
        
        for (LigneVente ligne : vente.getLignes()) {
            if (ligne.getQuantite() <= 0) {
                throw new Exception("Quantité invalide pour " + ligne.getGuitare().getMarque());
            }
            
            if (!ligneVenteDAO.isStockSuffisant(ligne.getGuitare().getId(), ligne.getQuantite())) {
                throw new Exception("Stock insuffisant pour " + ligne.getGuitare().getMarque());
            }
        }
    }

    private int enregistrerVentePrincipale(Vente vente, Utilisateur utilisateur, ConnexionBase con) throws Exception {
        vente.setUtilisateur(utilisateur);
        logger.log(Level.INFO, "Enregistrement entête vente");
        
        String sql = "INSERT INTO Vente (date_vente, client_id, utilisateur_id, montant_ht, tva, montant_ttc, statut_paiement) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(vente.getDateVente()));
            ps.setInt(2, vente.getClient().getId());
            ps.setInt(3, vente.getUtilisateur().getId());
            ps.setDouble(4, vente.getMontantHT());
            ps.setDouble(5, vente.getTva());
            ps.setDouble(6, vente.getMontantTTC());
            ps.setString(7, vente.getStatutPaiement());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int venteId = rs.getInt(1);
                    logger.log(Level.INFO, "Vente créée ID: " + venteId);
                    return venteId;
                }
            }
        }
        throw new Exception("Échec création vente");
    }

    private void traiterLignesVente(List<LigneVente> lignes, int venteId, ConnexionBase con) throws Exception {
        logger.log(Level.INFO, "Début traitement de " + lignes.size() + " lignes");
        
        // Insertion des lignes
        String sqlInsert = "INSERT INTO LigneVente (vente_id, guitare_id, quantite, prix_unitaire, remise) "
                        + "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement psInsert = con.getConnection().prepareStatement(sqlInsert)) {
            int count = 0;
            for (LigneVente ligne : lignes) {
                psInsert.setInt(1, venteId);
                psInsert.setInt(2, ligne.getGuitare().getId());
                psInsert.setInt(3, ligne.getQuantite());
                psInsert.setDouble(4, ligne.getPrixUnitaire());
                psInsert.setDouble(5, ligne.getRemise());
                psInsert.addBatch();
                
                if (++count % BATCH_SIZE == 0) {
                    psInsert.executeBatch();
                    logger.log(Level.FINE, "Batch exécuté: " + count + " lignes");
                }
            }
            psInsert.executeBatch(); // Pour les lignes restantes
        }
        
        // Mise à jour des stocks
        String sqlUpdate = "UPDATE Guitare SET quantite_stock = quantite_stock - ? WHERE id = ?";
        try (PreparedStatement psUpdate = con.getConnection().prepareStatement(sqlUpdate)) {
            for (LigneVente ligne : lignes) {
                psUpdate.setInt(1, ligne.getQuantite());
                psUpdate.setInt(2, ligne.getGuitare().getId());
                psUpdate.addBatch();
            }
            psUpdate.executeBatch();
        }
        
        logger.log(Level.INFO, "Traitement lignes terminé");
    }
    
}