/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gestion.venteguitare.main.vue.dialog;

import gestion.venteguitare.main.controlers.GuitareDAO;
import gestion.venteguitare.main.model.Guitare;
import gestion.venteguitare.main.vue.panel.GuitarePannel;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.PlainDocument;

/**
 *
 * @author tiaray
 */
public class GuitareDialog extends javax.swing.JDialog {

    private void initMode() {
        setTitle(mode == Mode.AJOUT ? "Ajouter une guitare" : "Modifier la guitare");
        btnValider.setText(mode == Mode.AJOUT ? "Ajouter" : "Modifier");
        cdCategorie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Electrique", "Basse", "Classique" }));
        
        if(mode == Mode.MODIFICATION){
            txtMarque.setText(guitare.getMarque());
            txtModele.setText(guitare.getModele());
            txtPrix.setText(String.valueOf(guitare.getPrix()));
            spStock.setValue(guitare.getQuantiteStock());
            cdCategorie.setSelectedItem(guitare.getCategorie());
            txtImgPath.setText(guitare.getImagePath());
            
            // charger l'image
            if(guitare.getImagePath() != null) {
                try{
                    ImageIcon icon =  new ImageIcon(getClass().getResource("/Images/" + guitare.getImagePath()));
                    lblImgPrev.setIcon(resizeIcon(icon, 50, 50));
                }catch(Exception e){
                    ImageIcon icon =  new ImageIcon(getClass().getResource("/Images/placeHolder.png"));
                    lblImgPrev.setIcon(resizeIcon(icon, 50, 50));
                }
            }
        }
    }
    
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
    return new ImageIcon(icon.getImage()
        .getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
}
    private String uploadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File src = chooser.getSelectedFile();
                String destName = System.currentTimeMillis() + "_" + src.getName();
                Path dest = Paths.get("src/Images/" + destName);
                Files.copy(src.toPath(), dest);

                ImageIcon icon = new ImageIcon(dest.toString());
                lblImgPrev.setIcon(resizeIcon(icon, 50, 50));
                return destName;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'upload", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
    
    private void validerFormulaire() throws Exception {
        StringBuilder errors = new StringBuilder();

        // Validation des champs
        if (txtMarque.getText().isEmpty()) {
            errors.append("- La marque est obligatoire\n");
        }
        if (txtModele.getText().isEmpty()) {
            errors.append("- Le modèle est obligatoire\n");
        }

        try {
            double prix = Double.parseDouble(txtPrix.getText());
            if (prix <= 0) {
                errors.append("- Le prix doit être positif\n");
            }
        } catch (NumberFormatException ex) {
            errors.append("- Le prix doit être un nombre valide\n");
        }

        if ((Integer)spStock.getValue() <= 0) {
            errors.append("- La quantité doit être positive\n");
        }

        // Si des erreurs, les afficher et sortir
        if (errors.length() > 0) {
            txtErreur.setText("Veuillez corriger les erreurs suivantes :\n" + errors.toString());
        return;
        }

        // Si pas d'erreurs, procéder à l'enregistrement
        if (mode == Mode.AJOUT) {
            guitare = new Guitare();
        }

        guitare.setMarque(txtMarque.getText());
        guitare.setModele(txtModele.getText());
        guitare.setPrix((int) Double.parseDouble(txtPrix.getText()));
        guitare.setQuantiteStock((Integer)spStock.getValue());
        guitare.setCategorie((String)cdCategorie.getSelectedItem());

        // Ne mettre à jour l'image que si nouvel upload
        if (mode == Mode.AJOUT || lblImgPrev.getIcon() != null) {
            guitare.setImagePath(txtImgPath.getText());
        }

        try {
            GuitareDAO dao = new GuitareDAO();
            if (mode == Mode.AJOUT) {
                dao.insert(guitare);
                JOptionPane.showMessageDialog(this, 
                    "Guitare ajoutée avec succès",
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                dao.update(guitare);
                JOptionPane.showMessageDialog(this,
                    "Modifications enregistrées",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }

            parentPanel.refreshGuitares();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur base de données: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates new form GuitareDialog
     */
    
    public enum Mode {AJOUT , MODIFICATION}
    
    private final Mode mode;
    private Guitare guitare;
    
    private GuitarePannel parentPanel;
    
    public GuitareDialog(JFrame parent, Mode mode, GuitarePannel panel, Guitare guitare) {
        super(parent, true);
        this.mode = mode;
        this.parentPanel = panel;
        this.guitare = guitare;
        initComponents();
        txtImgPath.setVisible(false);
        txtErreur.setPreferredSize(new Dimension(300, 20));
        initMode();
        
        setupInputValidation();

        btnUpload.addActionListener(e -> {
            txtImgPath.setText(uploadImage());
        });
        btnValider.addActionListener(e -> {
            try {
                validerFormulaire();
            } catch (Exception ex) {
                Logger.getLogger(GuitareDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnAnnuler.addActionListener(e -> dispose());
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    private void setupInputValidation() {
        // Pour le champ prix (uniquement chiffres et point)
        ((PlainDocument) txtPrix.getDocument()).setDocumentFilter(new NumericFilter());

        // Pour les champs marque et modèle (lettres et caractères spéciaux autorisés)
        ((PlainDocument) txtMarque.getDocument()).setDocumentFilter(new AlphaFilter());
        ((PlainDocument) txtModele.getDocument()).setDocumentFilter(new AlphaFilter());
    }
    
    
    
    private boolean isAlphaOrValidChars(String str) {
        return str.matches("[a-zA-Z0-9\\s\\-\\']+");
    }
    private class NumericFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);

            if (isNumeric(sb.toString())) {
                super.insertString(fb, offset, string, attr);
                txtErreur.setText("");
            } else {
                txtErreur.setText("Seuls les chiffres et le point décimal sont autorisés");
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);

            if (isNumeric(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
                txtErreur.setText("");
            } else {
                txtErreur.setText("Seuls les chiffres et le point décimal sont autorisés");
            }
        }
    }

    private class AlphaFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);

            if (isAlphaOrValidChars(sb.toString())) {
                super.insertString(fb, offset, string, attr);
                txtErreur.setText("");
            } else {
                txtErreur.setText("Caractères non autorisés: seulement lettres, espaces, apostrophes et tirets");
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);

            if (isAlphaOrValidChars(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
                txtErreur.setText("");
            } else {
                txtErreur.setText("Caractères non autorisés: seulement lettres, espaces, apostrophes et tirets");
            }
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
        txtMarque = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtModele = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrix = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        spStock = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        cdCategorie = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        btnUpload = new javax.swing.JButton();
        lblImgPrev = new javax.swing.JLabel();
        btnAnnuler = new javax.swing.JButton();
        btnValider = new javax.swing.JButton();
        txtImgPath = new javax.swing.JLabel();
        txtErreur = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ajouter une Guitare");
        setLocationByPlatform(true);
        setModal(true);
        setSize(new java.awt.Dimension(500, 400));

        jLabel1.setText("Marque :");

        jLabel2.setText("Modèle :");

        jLabel4.setText("Prix :");

        jLabel3.setText("Stock :");

        jLabel5.setText("Categorie :");

        cdCategorie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Image:");

        btnUpload.setText("Parcourir");

        lblImgPrev.setText("Aperçu Image");

        btnAnnuler.setText("Annuler");

        btnValider.setText("Valider");

        txtErreur.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtErreur)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMarque, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(spStock, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnUpload)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblImgPrev)
                                    .addComponent(cdCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAnnuler)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnValider)))))
                        .addContainerGap(92, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtModele)
                            .addComponent(txtPrix, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtImgPath)
                        .addGap(47, 47, 47))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMarque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtModele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImgPath))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spStock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cdCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btnUpload)
                    .addComponent(lblImgPrev))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(txtErreur)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnnuler)
                    .addComponent(btnValider))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnUpload;
    private javax.swing.JButton btnValider;
    private javax.swing.JComboBox<String> cdCategorie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblImgPrev;
    private javax.swing.JSpinner spStock;
    private javax.swing.JLabel txtErreur;
    private javax.swing.JLabel txtImgPath;
    private javax.swing.JTextField txtMarque;
    private javax.swing.JTextField txtModele;
    private javax.swing.JTextField txtPrix;
    // End of variables declaration//GEN-END:variables
}
