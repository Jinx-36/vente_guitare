/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.model;

/**
 *
 * @author tiaray
 */
public class LigneVente {
    private int id;
    private Guitare guitare;
    private int quantite;
    private double prixUnitaire;
    private double remise; // en pourcentage

    // Constructeurs, getters et setters
    public LigneVente() {}

    public LigneVente(Guitare guitare, int quantite, double remise) {
        this.guitare = guitare;
        this.quantite = quantite;
        this.prixUnitaire = guitare.getPrix();
        this.remise = remise;
    }

    // Méthode pour calculer le sous-total
    public double getSousTotal() {
        double prixAvantRemise = prixUnitaire * quantite;
        return prixAvantRemise * (1 - remise/100);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guitare getGuitare() {
        return guitare;
    }

    public void setGuitare(Guitare guitare) {
        this.guitare = guitare;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }
}
