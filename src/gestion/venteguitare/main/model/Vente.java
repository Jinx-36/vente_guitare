/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author tiaray
 */
public class Vente {
    private int id;
    private LocalDate dateVente;
    private Client client;
    private double montantHT;
    private double tva;
    private double montantTTC;
    private String statutPaiement;
    private List<LigneVente> lignes;
    private Utilisateur utilisateur;

    // Constructeurs, getters et setters
    public Vente() {}

    public Vente(Client client, List<LigneVente> lignes) {
        this.client = client;
        this.lignes = lignes;
        this.dateVente = LocalDate.now();
    }

    // Méthode pour calculer le total
    public void calculerTotaux() {
        /*this.montantHT = lignes.stream()
            .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantite() * (1 - l.getRemise()/100))
            .sum();
        this.tva = montantHT * 0.20; // TVA 20%
        this.montantTTC = montantHT + tva;*/
        double sousTotal = 0;

        // Calcul du sous-total avec remises
        for (LigneVente ligne : lignes) {
            sousTotal += ligne.getSousTotal();
        }

        // Remise supplémentaire de 5% si total > 250000
        if (sousTotal > 250000) {
            double remiseSupplementaire = sousTotal * 0.05;
            sousTotal -= remiseSupplementaire;
        }

        this.montantHT = sousTotal;
        this.tva = montantHT * 0.20; // TVA 20%
        this.montantTTC = montantHT + tva;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public List<LigneVente> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneVente> lignes) {
        this.lignes = lignes;
    }
}
