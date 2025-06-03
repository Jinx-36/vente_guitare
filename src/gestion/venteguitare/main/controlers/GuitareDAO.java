/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.controlers;

import java.sql.*;
import java.sql.ResultSet;
import gestion.venteguitare.main.model.Guitare;
import gestion.venteguitare.main.controlers.ConnexionBase;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiaray
 */
public class GuitareDAO {
    public ConnexionBase connexion;
    
    public GuitareDAO() throws Exception{
        connexion = new ConnexionBase();
    } 
    
    public void close() throws Exception {
        if(connexion != null){
            connexion.close();
        }
    }
    
    public void insert(Guitare g) throws Exception {
        String query = "INSERT INTO Guitare (marque, modele, prix, quantite_stock, categorie, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connexion.con.prepareStatement(query)) {
            ps.setString(1, g.getMarque());
            ps.setString(2, g.getModele());
            ps.setDouble(3, g.getPrix());
            ps.setInt(4, g.getQuantiteStock());
            ps.setString(5, g.getCategorie());
            ps.setString(6, g.getImagePath());
            ps.executeUpdate();
        }
    }
    
    public List<Guitare> getAll() throws Exception {
        List<Guitare> guitares = new ArrayList<>();
        try (ResultSet rs = this.selectTout()) {
            while (rs.next()) {
                Guitare g = new Guitare();
                g.setId(rs.getInt("id"));
                g.setMarque(rs.getString("marque"));
                g.setModele(rs.getString("modele"));
                g.setPrix(rs.getInt("prix")); 
                g.setCategorie(rs.getString("categorie"));
                g.setImagePath(rs.getString("image_path"));
                g.setQuantiteStock(rs.getInt("quantite_stock"));
                guitares.add(g);
            }
        }
        return guitares;
    }
    
    public ResultSet selectTout() throws Exception {
        String query = "SELECT * FROM Guitare";
        ResultSet rs = connexion.executeQuery(query);
        return rs;
    }
    
    public void update(Guitare g) throws SQLException {
        String sql = "UPDATE Guitare SET marque=?, modele=?, prix=?, quantite_stock=?, categorie=?, image_path=? WHERE id=?";
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setString(1, g.getMarque());
            ps.setString(2, g.getModele());
            ps.setInt(3, g.getPrix());
            ps.setInt(4, g.getQuantiteStock());
            ps.setString(5, g.getCategorie());
            ps.setString(6, g.getImagePath());
            ps.setInt(7, g.getId());
            ps.executeUpdate();
        }
    }
    
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Guitare WHERE id=?";
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public Guitare getById(int id) throws SQLException {
        String sql = "SELECT * FROM Guitare WHERE id = ?";
        Guitare guitare = null;

        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    guitare = new Guitare();
                    guitare.setId(rs.getInt("id"));
                    guitare.setMarque(rs.getString("marque"));
                    guitare.setModele(rs.getString("modele"));
                    guitare.setPrix(rs.getInt("prix"));
                    guitare.setCategorie(rs.getString("categorie"));
                    guitare.setImagePath(rs.getString("image_path"));
                    guitare.setQuantiteStock(rs.getInt("quantite_stock"));
                }
            }
        }
        return guitare;
    }
    public List<Guitare> getAllAvailable() throws SQLException {
        String sql = "SELECT * FROM Guitare WHERE quantite_stock > 0";
        List<Guitare> guitares = new ArrayList<>();

        try (Statement stmt = connexion.con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guitare g = new Guitare();
                // Initialisez l'objet Guitare...
                guitares.add(g);
            }
        }
        return guitares;
    }
    public int getNombreStocksFaibles(int seuil) throws SQLException {
    String sql = "SELECT COUNT(*) FROM Guitare WHERE quantite_stock <= ?";
        try (PreparedStatement ps = connexion.con.prepareStatement(sql)) {
            ps.setInt(1, seuil);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
