package gestion.venteguitare.main.services;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;

public class EmailService {
    private final String SMTP_HOST;
    private final String SMTP_PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final String FROM_EMAIL;
    private final String FROM_NAME;

    public EmailService() {
        // Configuration - à adapter selon vos besoins
        this.SMTP_HOST = "smtp.gmail.com";
        this.SMTP_PORT = "587";
        this.USERNAME = "olivtiaray36@gmail.com";
        this.PASSWORD = "oiwngpdvfhdneiif";
        this.FROM_EMAIL = "olivtiaray36@gmail.com";
        this.FROM_NAME = "Vente de Guitares";
    }

    public void envoyerFacture(String toEmail, String clientName, 
                             String filePath, int numFacture) throws MessagingException {
        // 1. Configuration du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST); // Important pour Gmail

        // 2. Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // 3. Construction du message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Facture n°" + numFacture + " - Guitar Universe");

            // 4. Corps du message (texte simple)
            String textContent = "Bonjour " + clientName + ",\n\n"
                + "Merci pour votre achat chez nous. Veuillez trouver ci-joint "
                + "votre facture n°" + numFacture + ".\n\n"
                + "Cordialement,\n"
                + "Guitare universe";

            // 5. Pièce jointe (PDF)
            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile(new File(filePath));
            pdfAttachment.setFileName("Facture_" + numFacture + ".pdf");

            // 6. Texte du message
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textContent);

            // 7. Assemblage des parties
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);     // Texte du message
            multipart.addBodyPart(pdfAttachment); // Pièce jointe
            
            message.setContent(multipart);

            // 8. Envoi de l'email
            Transport.send(message);
            
        } catch (Exception e) {
            throw new MessagingException("Échec de l'envoi de l'email: " + e.getMessage(), e);
        }
    }
}
/*package gestion.venteguitare.main.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.File;

public class EmailService {
    private final String SMTP_HOST;
    private final String SMTP_PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final String FROM_EMAIL;
    private final String FROM_NAME;

    public EmailService() {
        // Configuration basique - à remplacer par vos valeurs
        this.SMTP_HOST = "smtp.gmail.com";
        this.SMTP_PORT = "587";
        this.USERNAME = "olivtiaray36@gmail.com";
        this.PASSWORD = "oiwngpdvfhdneiif";
        this.FROM_EMAIL = "olivtiaray36@gmail.com";
        this.FROM_NAME = "Vente de Guitares";
    }

    public void envoyerFactureClient(String toEmail, String clientName, 
                                   String filePath, int numFacture) throws MessagingException {
        // 1. Configuration SMTP
        Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Ajout important
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.ciphersuites", "TLS_AES_128_GCM_SHA256"); // Nouveau
        // 2. Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // 3. Création du message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Votre facture n°" + numFacture + " - Vente de Guitares");

            // 4. Corps du message (texte + HTML)
            MimeBodyPart textPart = new MimeBodyPart();
            String textContent = "Bonjour " + clientName + ",\n\n"
                + "Merci pour votre achat. Veuillez trouver ci-joint votre facture.\n\n"
                + "Cordialement,\n"
                + "L'équipe de vente";
            
            String htmlContent = "<html><body>"
                + "<p>Bonjour " + clientName + ",</p>"
                + "<p>Merci pour votre achat. Veuillez trouver ci-joint votre facture.</p>"
                + "<p>Cordialement,<br>"
                + "L'équipe de vente</p>"
                + "</body></html>";

            // Alternative texte/HTML
            MimeMultipart alternativePart = new MimeMultipart("alternative");
            
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(textContent);
            alternativePart.addBodyPart(textBodyPart);
            
            MimeBodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(htmlContent, "text/html; charset=utf-8");
            alternativePart.addBodyPart(htmlBodyPart);

            // 5. Pièce jointe (PDF)
            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile(new File(filePath));
            pdfAttachment.setFileName("Facture_" + numFacture + ".pdf");

            // 6. Assemblage final
            MimeMultipart multipart = new MimeMultipart();
            // Texte + HTML
            multipart.addBodyPart(pdfAttachment);   // Pièce jointe
            
            message.setContent(multipart);

            // 7. Envoi
            Transport.send(message);
            
        } catch (Exception e) {
            throw new MessagingException("Échec de l'envoi de l'email", e);
        }
    }
}*/