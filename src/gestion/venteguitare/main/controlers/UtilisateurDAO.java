/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.controlers;

import gestion.venteguitare.main.model.Utilisateur;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiaray
 */
public class UtilisateurDAO {
     private final ConnexionBase connexion;
    
    public UtilisateurDAO() throws Exception {
        this.connexion = new ConnexionBase();
    }
    
    // CREATE
    public void insert(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO Utilisateur (nom_utilisateur, mot_de_passe, nom, prenom, role, date_creation, actif) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utilisateur.getNomUtilisateur());
            ps.setString(2, utilisateur.getMotDePasse()); // À hasher en pratique
            ps.setString(3, utilisateur.getNom());
            ps.setString(4, utilisateur.getPrenom());
            ps.setString(5, utilisateur.getRole());
            ps.setDate(6, Date.valueOf(utilisateur.getDateCreation()));
            ps.setBoolean(7, utilisateur.isActif());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    utilisateur.setId(rs.getInt(1));
                }
            }
        }
    }
    
    // READ
    public Utilisateur getById(int id) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        Utilisateur utilisateur = null;
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utilisateur = new Utilisateur();
                    mapResultSetToUtilisateur(rs, utilisateur);
                }
            }
        }
        return utilisateur;
    }
    
    public Utilisateur getByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE nom_utilisateur = ?";
        Utilisateur utilisateur = null;
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utilisateur = new Utilisateur();
                    mapResultSetToUtilisateur(rs, utilisateur);
                }
            }
        }
        return utilisateur;
    }
    
    private void mapResultSetToUtilisateur(ResultSet rs, Utilisateur utilisateur) throws SQLException {
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setNomUtilisateur(rs.getString("nom_utilisateur"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setRole(rs.getString("role"));
        utilisateur.setDateCreation(rs.getDate("date_creation").toLocalDate());
        utilisateur.setActif(rs.getBoolean("actif"));
    }
    
    // UPDATE
    public boolean update(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE Utilisateur SET nom_utilisateur = ?, nom = ?, prenom = ?, role = ?, actif = ? WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setString(1, utilisateur.getNomUtilisateur());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getPrenom());
            ps.setString(4, utilisateur.getRole());
            ps.setBoolean(5, utilisateur.isActif());
            ps.setInt(6, utilisateur.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // DELETE (désactivation plutôt que suppression)
    public boolean disable(int id) throws SQLException {
        String sql = "UPDATE Utilisateur SET actif = FALSE WHERE id = ?";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Authentification
    public Utilisateur authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE nom_utilisateur = ? AND mot_de_passe = ? AND actif = TRUE";
        Utilisateur utilisateur = null;
        
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password); // À comparer avec mot de passe hashé en pratique
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utilisateur = new Utilisateur();
                    mapResultSetToUtilisateur(rs, utilisateur);
                }
            }
        }
        return utilisateur;
    }
    
    public List<Utilisateur> getAll() throws SQLException {
        String sql = "SELECT * FROM Utilisateur ORDER BY nom ASC";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Statement stmt = connexion.con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilisateur user = new Utilisateur();
                mapResultSetToUtilisateur(rs, user);
                utilisateurs.add(user);
            }
        }
        return utilisateurs;
    }
}
