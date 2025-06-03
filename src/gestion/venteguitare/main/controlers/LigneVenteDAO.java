/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.controlers;

import gestion.venteguitare.main.model.LigneVente;
import gestion.venteguitare.main.model.Guitare;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiaray
 */
public class LigneVenteDAO {
     private final ConnexionBase connexion;
    
    public LigneVenteDAO() throws Exception {
        this.connexion = new ConnexionBase();
    }
    
    // CREATE
    public void insert(LigneVente ligne, int venteId) throws SQLException {
        String sql = "INSERT INTO LigneVente (vente_id, guitare_id, quantite, prix_unitaire, remise) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, venteId);
            ps.setInt(2, ligne.getGuitare().getId());
            ps.setInt(3, ligne.getQuantite());
            ps.setDouble(4, ligne.getPrixUnitaire());
            ps.setDouble(5, ligne.getRemise());
            
            ps.executeUpdate();
        }
    }
    
    // READ - Récupère toutes les lignes d'une vente
    public List<LigneVente> getByVenteId(int venteId) throws SQLException {
        String sql = "SELECT l.*, g.marque, g.modele FROM LigneVente l "
                   + "JOIN Guitare g ON l.guitare_id = g.id "
                   + "WHERE l.vente_id = ?";
        List<LigneVente> lignes = new ArrayList<>();
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, venteId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LigneVente ligne = new LigneVente();
                    ligne.setId(rs.getInt("id"));
                    
                    Guitare guitare = new Guitare();
                    guitare.setId(rs.getInt("guitare_id"));
                    guitare.setMarque(rs.getString("marque"));
                    guitare.setModele(rs.getString("modele"));
                    ligne.setGuitare(guitare);
                    
                    ligne.setQuantite(rs.getInt("quantite"));
                    ligne.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                    ligne.setRemise(rs.getDouble("remise"));
                    
                    lignes.add(ligne);
                }
            }
        }
        return lignes;
    }
    
    // UPDATE - Mise à jour d'une ligne (quantité ou remise)
    public boolean update(LigneVente ligne) throws SQLException {
        String sql = "UPDATE LigneVente SET quantite = ?, remise = ? WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, ligne.getQuantite());
            ps.setDouble(2, ligne.getRemise());
            ps.setInt(3, ligne.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // DELETE - Suppression d'une ligne
    public boolean delete(int ligneId) throws SQLException {
        String sql = "DELETE FROM LigneVente WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, ligneId);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Mise à jour du stock après vente
    public void updateStock(int guitareId, int quantiteVendue) throws SQLException {
        String sql = "UPDATE Guitare SET quantite_stock = quantite_stock - ? WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, quantiteVendue);
            ps.setInt(2, guitareId);
            ps.executeUpdate();
        }
    }
    
    // Vérification du stock disponible
    public boolean isStockSuffisant(int guitareId, int quantite) throws SQLException {
        String sql = "SELECT quantite_stock FROM Guitare WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, guitareId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantite_stock") >= quantite;
                }
            }
        }
        return false;
    }
    public void insertBatch(List<LigneVente> lignes, int venteId) throws SQLException {
        String sql = "INSERT INTO LigneVente (vente_id, guitare_id, quantite, prix_unitaire, remise) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            for (LigneVente ligne : lignes) {
                ps.setInt(1, venteId);
                ps.setInt(2, ligne.getGuitare().getId());
                ps.setInt(3, ligne.getQuantite());
                ps.setDouble(4, ligne.getPrixUnitaire());
                ps.setDouble(5, ligne.getRemise());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}    

