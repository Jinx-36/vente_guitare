
import gestion.venteguitare.main.services.EmailService;
import java.io.File;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


/*import gestion.venteguitare.main.services.EmailService;
import java.io.File;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class TestEmail {
    public static void main(String[] args) throws MessagingException {
        EmailService emailService = new EmailService();
        emailService.envoyerFacture(
                "gitareuniverse@gmail.com",
                "tia",
                "/src/gestion/venteguitare/main/services/Factures/Facture_12.pdf",
                12
        );
        System.out.println("Email envoyé avec succès");
    }
}*/
public class TestEmail {
    public static void main(String[] args) {
        System.out.println("Début du test d'envoi d'email...");
        
        try {
            EmailService emailService = new EmailService();
            System.out.println("Service email initialisé");
            
            // Créez un fichier PDF test si nécessaire
            String testPdfPath = "test.pdf";
            new File(testPdfPath).createNewFile();
            
            System.out.println("Envoi en cours...");
            emailService.envoyerFacture(
                "guitareuniverse@gmail.com", 
                "Client Test",
                testPdfPath,
                12
            );
            
            System.out.println("Email envoyé avec succès !");
        } catch (AuthenticationFailedException e) {
            System.err.println("ERREUR D'AUTHENTIFICATION :");
            System.err.println("1. Vérifiez que vous avez activé l'accès pour les applications moins sécurisées");
            System.err.println("2. OU créez un mot de passe d'application");
            System.err.println("3. Vérifiez que le nom d'utilisateur/mot de passe sont corrects");
            e.printStackTrace();
        } catch (MessagingException e) {
            System.err.println("ERREUR D'ENVOI :");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERREUR INATTENDUE :");
            e.printStackTrace();
        }
    }
}