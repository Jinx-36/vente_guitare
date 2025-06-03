/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.controlers;

import gestion.venteguitare.main.model.Vente;
import gestion.venteguitare.main.model.Client;
import gestion.venteguitare.main.model.Utilisateur;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiaray
 */
public class VenteDAO {
     private final ConnexionBase connexion;
    
    public VenteDAO() throws Exception {
        this.connexion = new ConnexionBase();
    }
    
    // Méthode pour obtenir la connexion (pour la gestion des transactions)
    public Connection getConnection() {
        return connexion.con;
    }
    
    // CREATE
    public int insert(Vente vente) throws SQLException {
        String sql = "INSERT INTO Vente (date_vente, client_id, utilisateur_id, montant_ht, tva, montant_ttc, statut_paiement) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    // READ
    public Vente getById(int id) throws SQLException {
        String sql = "SELECT * FROM Vente WHERE id = ?";
        Vente vente = null;
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vente = new Vente();
                    vente.setId(rs.getInt("id"));
                    vente.setDateVente(rs.getDate("date_vente").toLocalDate());
                    
                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    vente.setClient(client);
                    
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("utilisateur_id"));
                    vente.setUtilisateur(utilisateur);
                    
                    vente.setMontantHT(rs.getDouble("montant_ht"));
                    vente.setTva(rs.getDouble("tva"));
                    vente.setMontantTTC(rs.getDouble("montant_ttc"));
                    vente.setStatutPaiement(rs.getString("statut_paiement"));
                }
            }
        }
        return vente;
    }
    
    // Liste toutes les ventes
    public List<Vente> getAll() throws SQLException {
        String sql = "SELECT * FROM Vente ORDER BY date_vente DESC";
        List<Vente> ventes = new ArrayList<>();
        
        try (Statement stmt = connexion.con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vente vente = new Vente();
                vente.setId(rs.getInt("id"));
                vente.setDateVente(rs.getDate("date_vente").toLocalDate());
                
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                vente.setClient(client);
                
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("utilisateur_id"));
                vente.setUtilisateur(utilisateur);
                
                vente.setMontantHT(rs.getDouble("montant_ht"));
                vente.setTva(rs.getDouble("tva"));
                vente.setMontantTTC(rs.getDouble("montant_ttc"));
                vente.setStatutPaiement(rs.getString("statut_paiement"));
                
                ventes.add(vente);
            }
        }
        return ventes;
    }
    
    // Recherche par client
    public List<Vente> getByClient(int clientId) throws SQLException {
        String sql = "SELECT * FROM Vente WHERE client_id = ? ORDER BY date_vente DESC";
        List<Vente> ventes = new ArrayList<>();
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vente vente = new Vente();
                    vente.setId(rs.getInt("id"));
                    vente.setDateVente(rs.getDate("date_vente").toLocalDate());
                    
                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    vente.setClient(client);
                    
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("utilisateur_id"));
                    vente.setUtilisateur(utilisateur);
                    
                    vente.setMontantHT(rs.getDouble("montant_ht"));
                    vente.setTva(rs.getDouble("tva"));
                    vente.setMontantTTC(rs.getDouble("montant_ttc"));
                    vente.setStatutPaiement(rs.getString("statut_paiement"));
                    
                    ventes.add(vente);
                }
            }
        }
        return ventes;
    }
    
    // Mise à jour du statut de paiement
    public boolean updateStatutPaiement(int venteId, String statut) throws SQLException {
        String sql = "UPDATE Vente SET statut_paiement = ? WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, venteId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Statistiques
    public double getChiffreAffaires(LocalDate debut, LocalDate fin) throws SQLException {
        String sql = "SELECT SUM(montant_ttc) FROM Vente WHERE date_vente BETWEEN ? AND ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));
            ps.setDate(2, Date.valueOf(fin));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0;
    }
    public List<Vente> getByStatut(String statut) throws SQLException {
    String sql = "SELECT * FROM Vente WHERE statut_paiement = ? ORDER BY date_vente DESC";
    List<Vente> ventes = new ArrayList<>();
    
    try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
        ps.setString(1, statut);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vente vente = new Vente();
                vente.setId(rs.getInt("id"));
                vente.setDateVente(rs.getDate("date_vente").toLocalDate());
                
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                vente.setClient(client);
                
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("utilisateur_id"));
                vente.setUtilisateur(utilisateur);
                
                vente.setMontantHT(rs.getDouble("montant_ht"));
                vente.setTva(rs.getDouble("tva"));
                vente.setMontantTTC(rs.getDouble("montant_ttc"));
                vente.setStatutPaiement(rs.getString("statut_paiement"));
                
                ventes.add(vente);
            }
        }
    }
    return ventes;
}

public Vente getVenteComplete(int id) throws SQLException {
        String sql = "SELECT v.*, c.nom as client_nom, c.prenom as client_prenom, "
                   + "u.nom as user_nom, u.prenom as user_prenom "
                   + "FROM Vente v "
                   + "JOIN Client c ON v.client_id = c.id "
                   + "JOIN Utilisateur u ON v.utilisateur_id = u.id "
                   + "WHERE v.id = ?";

        Vente vente = null;

        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vente = new Vente();
                    vente.setId(rs.getInt("id"));
                    vente.setDateVente(rs.getDate("date_vente").toLocalDate());

                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setNom(rs.getString("client_nom"));
                    client.setPrenom(rs.getString("client_prenom"));
                    vente.setClient(client);

                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("utilisateur_id"));
                    utilisateur.setNom(rs.getString("user_nom"));
                    utilisateur.setPrenom(rs.getString("user_prenom"));
                    vente.setUtilisateur(utilisateur);

                    vente.setMontantHT(rs.getDouble("montant_ht"));
                    vente.setTva(rs.getDouble("tva"));
                    vente.setMontantTTC(rs.getDouble("montant_ttc"));
                    vente.setStatutPaiement(rs.getString("statut_paiement"));
                }
            }
        }
        return vente;
    }
    // Dans VenteDAO.java
    public Map<String, Double> getVentesParMois() throws SQLException {
            Map<String, Double> ventesParMois = new LinkedHashMap<>();
            String sql = "SELECT DATE_FORMAT(date_vente, '%Y-%m') as mois, SUM(montant_ttc) as total "
                       + "FROM Vente GROUP BY mois ORDER BY mois";

            try (Statement stmt = connexion.con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    ventesParMois.put(rs.getString("mois"), rs.getDouble("total"));
                }
            }
            return ventesParMois;
        }

        public Map<String, Double> getVentesParCategorie() throws SQLException {
            Map<String, Double> ventesParCategorie = new HashMap<>();
            String sql = "SELECT g.categorie, SUM(l.prix_unitaire * l.quantite) as total "
                       + "FROM LigneVente l JOIN Guitare g ON l.guitare_id = g.id "
                       + "GROUP BY g.categorie";

            try (Statement stmt = connexion.con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    ventesParCategorie.put(rs.getString("categorie"), rs.getDouble("total"));
                }
            }
            return ventesParCategorie;
        }
        public double getChiffreAffairesTotal() throws SQLException {
        String sql = "SELECT SUM(montant_ttc) FROM Vente";
        try (Statement stmt = connexion.con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public int getNombreVentes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Vente";
        try (Statement stmt = connexion.con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public List<Vente> getDernieresVentes(int limit) throws SQLException {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM Vente ORDER BY date_vente DESC LIMIT ?";

        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vente v = new Vente();
                    v.setId(rs.getInt("id"));
                    v.setDateVente(rs.getDate("date_vente").toLocalDate());
                    v.setMontantTTC(rs.getDouble("montant_ttc"));

                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    v.setClient(client);

                    ventes.add(v);
                }
            }
        }
        return ventes;
    }
    public String getClientEmail(int venteId) throws SQLException {
        String sql = "SELECT c.email FROM Vente v JOIN Client c ON v.client_id = c.id WHERE v.id = ?";
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, venteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("email") : null;
            }
        }
    }
}
