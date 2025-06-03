package gestion.venteguitare.main.services;

import gestion.venteguitare.main.controlers.*;
import gestion.venteguitare.main.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FactureGenerator {
    public static void generate(int venteId) throws Exception {
        String facturesDir = System.getProperty("user.dir") + "/src/gestion/venteguitare/main/services/Factures/";
        new File(facturesDir).mkdirs();
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            
            // Chargement des polices - Méthode robuste
            PDType0Font font = loadFont(document, "OpenSans-Regular.ttf");
            PDType0Font fontBold = loadFont(document, "OpenSans-Bold.ttf");
            
            // Récupération des données
            VenteDAO venteDao = new VenteDAO();
            LigneVenteDAO ligneVenteDao = new LigneVenteDAO();
            ClientDAO clientDao = new ClientDAO();
            
            Vente vente = venteDao.getVenteComplete(venteId);
            Client client = clientDao.getById(vente.getClient().getId());
            List<LigneVente> lignes = ligneVenteDao.getByVenteId(venteId);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // En-tête
                writeText(contentStream, fontBold, 16, 50, 750, "FACTURE N°" + vente.getId());
                
                // Informations client
                writeText(contentStream, font, 10, 50, 700, "Date: " + vente.getDateVente().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                writeText(contentStream, font, 10, 50, 685, "Client: " + client.getNom() + " " + client.getPrenom());
                writeText(contentStream, font, 10, 50, 670, "Telephone: " + (client.getTelephone() != null ? client.getTelephone() : "N/A"));
                
                 // Ligne séparatrice
                contentStream.moveTo(50, 650);
                contentStream.lineTo(550, 650);
                contentStream.stroke();
                
                // Entête tableau
                writeText(contentStream, fontBold, 10, 50, 630, "Désignation");
                writeText(contentStream, fontBold, 10, 250, 630, "Prix Unitaire");
                writeText(contentStream, fontBold, 10, 330, 630, "Qté");
                writeText(contentStream, fontBold, 10, 380, 630, "Remise");
                writeText(contentStream, fontBold, 10, 430, 630, "Total");
                
                // Lignes
                int y = 610;
                for (LigneVente ligne : lignes) {
                    writeText(contentStream, font, 9, 50, y, ligne.getGuitare().getMarque() + " " + ligne.getGuitare().getModele());
                    writeText(contentStream, font, 9, 250, y, String.format("%.2f Ar", ligne.getPrixUnitaire()));
                    writeText(contentStream, font, 9, 330, y, String.valueOf(ligne.getQuantite()));
                    writeText(contentStream, font, 9, 380, y, ligne.getRemise() + "%");
                    writeText(contentStream, font, 9, 430, y, String.format("%.2f Ar", ligne.getSousTotal()));
                    y -= 15;
                }
                
                // Ligne séparatrice
                contentStream.moveTo(50, y - 10);
                contentStream.lineTo(550, y - 10);
                contentStream.stroke();
                
                // Totaux
                writeText(contentStream, fontBold, 10, 350, y - 30, "Sous-total: " + String.format("%.2f Ar", vente.getMontantHT()));
                writeText(contentStream, fontBold, 10, 350, y - 45, "TVA (20%): " + String.format("%.2f Ar", vente.getTva()));
                writeText(contentStream, fontBold, 10, 350, y - 60, "Total TTC: " + String.format("%.2f Ar", vente.getMontantTTC()));
                
                // Statut
                writeText(contentStream, fontBold, 10, 50, y - 90, "Statut: " + vente.getStatutPaiement().toUpperCase());
                
                // Pied de page
                writeText(contentStream, font, 8, 50, 30, "Merci pour votre achat - Vente de guitares © " + LocalDate.now().getYear());
                
            }
            
            // Sauvegarde
            String fileName = facturesDir + "Facture_" + venteId + ".pdf";
            document.save(fileName);
            
            try {
                String clientEmail = venteDao.getClientEmail(venteId);

                if (clientEmail != null && !clientEmail.isEmpty()) {
                     EmailService emailService = new EmailService();
                emailService.envoyerFacture(
                    client.getEmail(),
                    client.getPrenom() + " " + client.getNom(),
                    fileName,
                    venteId
                );
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            }
        }
    }
    
    private static PDType0Font loadFont(PDDocument doc, String fontName) throws IOException {
    String path = "/gestion/venteguitare/main/resources/fonts/" + fontName;
    InputStream is = FactureGenerator.class.getResourceAsStream(path);
    
    if (is == null) {
        // Message d'erreur détaillé
        String error = "ERREUR: Police non trouvée.\n"
            + "Chemin recherché: " + path + "\n"
            + "Lieux possibles:\n"
            + "- src/gestion/venteguitare/main/resources/fonts/\n"
            + "- Dans le JAR: gestion/venteguitare/main/resources/fonts/\n"
            + "Vérifiez que:\n"
            + "1. Le fichier .ttf existe bien\n"
            + "2. Le nom du fichier est EXACTEMENT '" + fontName + "'\n"
            + "3. Le fichier n'est pas corrompu (taille > 100KB)";
        throw new IOException(error);
    }
    
    return PDType0Font.load(doc, is);
}
    
    private static void writeText(PDPageContentStream stream, PDType0Font font, 
                                float fontSize, float x, float y, String text) throws IOException {
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(x, y);
        stream.showText(text);
        stream.endText();
    }
}