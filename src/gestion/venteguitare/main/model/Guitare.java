/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.model;

/**
 *
 * @author tiaray
 */
public class Guitare {
    private int id;
    private String marque;
    private String modele;
    private int prix;
    private String categorie;
    private String imagePath;
    private int quantiteStock;

    public Guitare(int id, String marque, String modele, int prix, String categorie, String imagePath, int quantiteStock) {
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        this.prix = prix;
        this.categorie = categorie;
        this.imagePath = imagePath;
        this.quantiteStock = quantiteStock;
    }

    public Guitare(String marque, String modele, int prix, String categorie, String imagePath, int quantiteStock) {
        this.marque = marque;
        this.modele = modele;
        this.prix = prix;
        this.categorie = categorie;
        this.imagePath = imagePath;
        this.quantiteStock = quantiteStock;
    }

    public Guitare() {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
}
