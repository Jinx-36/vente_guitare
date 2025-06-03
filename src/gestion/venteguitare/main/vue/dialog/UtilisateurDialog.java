/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gestion.venteguitare.main.vue.dialog;

import gestion.venteguitare.main.model.Utilisateur;
import gestion.venteguitare.main.vue.panel.UtilisateurPannel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author tiaray
 */
public class UtilisateurDialog extends javax.swing.JDialog {

    public enum Mode { AJOUT, MODIFICATION }
    
    private final Mode mode;
    private Utilisateur utilisateur;
    private final UtilisateurPannel parentPanel;
    /**
     * Creates new form UtilisateurDialog
     */
    public UtilisateurDialog(JFrame parent, Mode mode, UtilisateurPannel parentPanel, Utilisateur utilisateur) {
        super(parent, true);
        this.mode = mode;
        this.parentPanel = parentPanel;
        this.utilisateur = (mode == Mode.AJOUT) ? new Utilisateur() : utilisateur;
        
        initComponents();
        lblErreur.setPreferredSize(new Dimension(300, 20));
        lblErreur.setForeground(Color.RED);
        initMode();
        setupInputValidation();
        pack();
        setLocationRelativeTo(parent);
    }
    private void initMode() {
        setTitle(mode == Mode.AJOUT ? "Ajouter un utilisateur" : "Modifier l'utilisateur");
        
        if (mode == Mode.MODIFICATION) {
            txtNomUtilisateur.setText(utilisateur.getNomUtilisateur());
            txtNom.setText(utilisateur.getNom());
            txtPrenom.setText(utilisateur.getPrenom());
            cbRole.setSelectedItem(utilisateur.getRole());
            txtMotDePasse.setText("");
            txtConfirmation.setText("");
        }
    }
    private void setupInputValidation() {
        // Filtre pour le nom d'utilisateur (alphanumérique seulement)
        ((PlainDocument) txtNomUtilisateur.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) 
                    throws BadLocationException {
                if (text.matches("[a-zA-Z0-9]*")) {
                    super.insertString(fb, offset, text, attr);
                } else {
                    showError("Seuls les lettres et chiffres sont autorisés");
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text.matches("[a-zA-Z0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    showError("Seuls les lettres et chiffres sont autorisés");
                }
            }
        });

        // Filtre pour le nom (lettres et espaces seulement)
        ((PlainDocument) txtNom.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) 
                    throws BadLocationException {
                if (text.matches("[a-zA-Z\\s']*")) {
                    super.insertString(fb, offset, text, attr);
                } else {
                    showError("Seuls les lettres, espaces et apostrophes sont autorisés");
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text.matches("[a-zA-Z\\s']*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    showError("Seuls les lettres, espaces et apostrophes sont autorisés");
                }
            }
        });

        // Filtre pour le prénom (mêmes règles que le nom)
        ((PlainDocument) txtPrenom.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) 
                    throws BadLocationException {
                if (text.matches("[a-zA-Z\\s']*")) {
                    super.insertString(fb, offset, text, attr);
                } else {
                    showError("Seuls les lettres, espaces et apostrophes sont autorisés");
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text.matches("[a-zA-Z\\s']*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    showError("Seuls les lettres, espaces et apostrophes sont autorisés");
                }
            }
        });
    }

    private void checkRequiredFields() {
        boolean nomUtilisateurEmpty = txtNomUtilisateur.getText().trim().isEmpty();
        boolean nomEmpty = txtNom.getText().trim().isEmpty();
        boolean prenomEmpty = txtPrenom.getText().trim().isEmpty();

        int filledFields = 0;
        if (!nomUtilisateurEmpty) filledFields++;
        if (!nomEmpty) filledFields++;
        if (!prenomEmpty) filledFields++;

        if (filledFields > 0 && filledFields < 3) {
            showError("Tous les champs sont obligatoires");
        } else if (lblErreur.getText().equals("Tous les champs sont obligatoires")) {
            lblErreur.setText("");
        }
    }

    private void showError(String message) {
        lblErreur.setText(message);
        lblErreur.setForeground(Color.RED);
        lblErreur.setVisible(true);
        pack();
    }

    
    private void validerFormulaire() {
        try {
            String nomUtilisateur = txtNomUtilisateur.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String role = (String) cbRole.getSelectedItem();
            String motDePasse = new String(txtMotDePasse.getPassword());
            String confirmation = new String(txtConfirmation.getPassword());

            // Validation des champs obligatoires
            if (nomUtilisateur.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs sont obligatoires");
            }

            // Validation du mot de passe
            if (mode == Mode.AJOUT && motDePasse.isEmpty()) {
                throw new IllegalArgumentException("Le mot de passe est obligatoire");
            }

            if (!motDePasse.equals(confirmation)) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }

            // Si tout est valide, procéder à l'enregistrement
            utilisateur.setNomUtilisateur(nomUtilisateur);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setRole(role);
            utilisateur.setMotDePasse(motDePasse);

            parentPanel.refreshUsers();
            dispose();

            JOptionPane.showMessageDialog(this,
                "Utilisateur " + (mode == Mode.AJOUT ? "ajouté" : "modifié") + " avec succès",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnValider = new javax.swing.JButton();
        btnAnnuler = new javax.swing.JButton();
        txtNomUtilisateur = new javax.swing.JTextField();
        txtNom = new javax.swing.JTextField();
        txtPrenom = new javax.swing.JTextField();
        cbRole = new javax.swing.JComboBox<>();
        txtMotDePasse = new javax.swing.JPasswordField();
        txtConfirmation = new javax.swing.JPasswordField();
        lblErreur = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);

        jLabel1.setText("Nom d'utilisateur :");

        jLabel2.setText("Nom :");

        jLabel3.setText("Prénom :");

        jLabel4.setText("Rôle :");

        jLabel5.setText("Mot de passe :");

        jLabel6.setText("Confirmation :");

        btnValider.setText("Vanlider");
        btnValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderActionPerformed(evt);
            }
        });

        btnAnnuler.setText("Annuler");
        btnAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnulerActionPerformed(evt);
            }
        });

        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "vendeur", " " }));

        lblErreur.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAnnuler))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(lblErreur))
                            .addComponent(jLabel6))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNom, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(txtNomUtilisateur)
                            .addComponent(txtPrenom)
                            .addComponent(cbRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMotDePasse)
                            .addComponent(txtConfirmation))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnValider)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMotDePasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(lblErreur)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnValider)
                    .addComponent(btnAnnuler))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderActionPerformed
        // TODO add your handling code here:
        validerFormulaire();
    }//GEN-LAST:event_btnValiderActionPerformed

    private void btnAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnulerActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnAnnulerActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnValider;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblErreur;
    private javax.swing.JPasswordField txtConfirmation;
    private javax.swing.JPasswordField txtMotDePasse;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtNomUtilisateur;
    private javax.swing.JTextField txtPrenom;
    // End of variables declaration//GEN-END:variables
}
