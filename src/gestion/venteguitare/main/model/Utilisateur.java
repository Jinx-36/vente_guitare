/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.model;

import java.time.LocalDate;

/**
 *
 * @author tiaray
 */
public class Utilisateur {
     private int id;
    private String nomUtilisateur;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String role; // 'admin' ou 'vendeur'
    private LocalDate dateCreation;
    private boolean actif;

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nomUtilisateur, String motDePasse, String nom, String prenom, String role) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.dateCreation = LocalDate.now();
        this.actif = true;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
    

    public boolean isAdmin() {
        return "admin".equals(role);
    }

    public boolean isVendeur() {
        return "vendeur".equals(role);
    }
}
