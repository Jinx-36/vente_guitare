/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.controlers;

import java.sql.*;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import gestion.venteguitare.main.model.Client;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiaray
 */
public class ClientDAO {
    private final ConnexionBase connexion;
    private Connection externalCon = null;
    
    public void setConnection(Connection con) {
        this.externalCon = con;
    }
    
    private Connection getActiveConnection() throws SQLException {
        return externalCon != null ? externalCon : connexion.con;
    }
    
    public ClientDAO() throws Exception{
        this.connexion = new ConnexionBase();
    }
    
    // Create
     public void insert(Client client) throws Exception {
        String query = "INSERT INTO Client (nom, prenom, email, telephone, date_inscription) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.PreparedStatement ps = getActiveConnection().prepareStatement(query)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelephone());
            
            LocalDate date;
            date = client.getDateInscription() != null ? client.getDateInscription() : LocalDate.now();
            ps.setDate(5, Date.valueOf(date));
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
        }
    }
    
     // READ - Récupération de tous les clients
    public List<Client> getAll() throws SQLException, Exception {
        List<Client> clients = new ArrayList<>();
        
        try (ResultSet rs = this.selectTout()) {
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setEmail(rs.getString("email"));
                c.setTelephone(rs.getString("telephone"));
                
                // Conversion Date SQL -> LocalDate
                Date dateSql = rs.getDate("date_inscription");
                c.setDateInscription(dateSql != null ? dateSql.toLocalDate() : null);
                
                clients.add(c);
            }
        }
        return clients;
    }
    
        public ResultSet selectTout() throws Exception {
        String query = "SELECT * FROM Client ORDER BY nom ASC";
        ResultSet rs = connexion.executeQuery(query);
        return rs;
    }
      
     // UPDATE - Mise à jour d'un client existant
    public void update(Client client) throws SQLException {
        String query = "UPDATE Client SET nom=?, prenom=?, email=?, telephone=? WHERE id=?";
        
        try (java.sql.PreparedStatement ps = getActiveConnection().prepareStatement(query)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelephone());
            ps.setInt(5, client.getId());
            
            ps.executeUpdate();
        }
    }
    
     // DELETE - Suppression d'un client
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Client WHERE id=?";
        
        try (java.sql.PreparedStatement ps = getActiveConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
     // SEARCH - Recherche multicritère
    public List<Client> search(String keyword) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client WHERE " +
                     "nom LIKE ? OR " +
                     "prenom LIKE ? OR " +
                     "email LIKE ? OR " +
                     "telephone LIKE ? " +
                     "ORDER BY nom ASC";
        
        try (java.sql.PreparedStatement ps = getActiveConnection().prepareStatement(sql)) {
            String searchTerm = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, searchTerm);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Client c = new Client();
                    c.setId(rs.getInt("id"));
                    c.setNom(rs.getString("nom"));
                    c.setPrenom(rs.getString("prenom"));
                    c.setEmail(rs.getString("email"));
                    c.setTelephone(rs.getString("telephone"));

                    // Conversion Date SQL -> LocalDate
                    Date dateSql = rs.getDate("date_inscription");
                    c.setDateInscription(dateSql != null ? dateSql.toLocalDate() : null);

                    clients.add(c);
                }
            }
        }
        return clients;
    }
    
    public Client getById(int id) throws SQLException {
        String sql = "SELECT * FROM Client WHERE id = ?";
        Client client = null;

        try (java.sql.PreparedStatement ps = getActiveConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    client = new Client();
                    client.setId(rs.getInt("id"));
                    client.setNom(rs.getString("nom"));
                    client.setPrenom(rs.getString("prenom"));
                    client.setEmail(rs.getString("email"));
                    client.setTelephone(rs.getString("telephone"));

                    Date dateSql = rs.getDate("date_inscription");
                    client.setDateInscription(dateSql != null ? dateSql.toLocalDate() : null);
                }
            }
        }
        return client;
    }
    public int getNombreClients() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Client";
        try (Statement stmt = (Statement) connexion.con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
