/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gestion.venteguitare.main.vue.panel;

import gestion.venteguitare.main.controlers.UtilisateurDAO;
import gestion.venteguitare.main.model.Utilisateur;
import gestion.venteguitare.main.model.UtilisateurTableModel;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import gestion.venteguitare.main.vue.dialog.UtilisateurDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author tiaray
 */
public class UtilisateurPannel extends javax.swing.JPanel {

    private UtilisateurDAO userDao;
    private UtilisateurTableModel model;
    /**
     * Creates new form UtilisateurPannel
     */
    public UtilisateurPannel() {
        try {
            this.userDao = new UtilisateurDAO();
            initComponents();
            initTable();
            loadUsers();
            setupListeners();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur d'initialisation: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void initTable() {
        model = new UtilisateurTableModel();
        jTable.setModel(model);
        jTable.setAutoCreateRowSorter(true);
        jTable.removeColumn(jTable.getColumnModel().getColumn(0));
    }
    private void loadUsers() {
        try {
            List<Utilisateur> users = userDao.getAll();
            model.setUtilisateurs(users);
            txtRecherche.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de chargement: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
     private void ajouterUtilisateur() {
        UtilisateurDialog dialog = new UtilisateurDialog(
            (JFrame)SwingUtilities.getWindowAncestor(this),
            UtilisateurDialog.Mode.AJOUT,
            this,
            null
        );
        dialog.setVisible(true);
    }
    
    private void modifierUtilisateur() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un utilisateur",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = jTable.convertRowIndexToModel(selectedRow);
        Utilisateur user = model.getUtilisateurAt(modelRow);
        
        UtilisateurDialog dialog = new UtilisateurDialog(
            (JFrame)SwingUtilities.getWindowAncestor(this),
            UtilisateurDialog.Mode.MODIFICATION,
            this,
            user
        );
        dialog.setVisible(true);
    }
    
    private void desactiverUtilisateur() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un utilisateur",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Désactiver cet utilisateur ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int modelRow = jTable.convertRowIndexToModel(selectedRow);
                Utilisateur user = model.getUtilisateurAt(modelRow);
                userDao.disable(user.getId());
                loadUsers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la désactivation: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
     private void rechercherUtilisateurs() {
        String recherche = txtRecherche.getText().trim();
        
        try {
            List<Utilisateur> users;
            
            if (recherche.isEmpty()) {
                users = userDao.getAll();
            } else {
                users = userDao.getAll().stream()
                    .filter(u -> 
                        u.getNom().toLowerCase().contains(recherche.toLowerCase()) ||
                        u.getPrenom().toLowerCase().contains(recherche.toLowerCase()) ||
                        u.getNomUtilisateur().toLowerCase().contains(recherche.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            model.setUtilisateurs(users);
            
            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Aucun utilisateur trouvé",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la recherche: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refreshUsers() {
        loadUsers();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtRecherche = new javax.swing.JTextField();
        btnRafraichir = new javax.swing.JButton();
        btnDesactiver = new javax.swing.JButton();
        btnModifier = new javax.swing.JButton();
        btnAjouter = new javax.swing.JButton();
        btnRechercher = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        btnRafraichir.setText("Rafraîchir");

        btnDesactiver.setText("Désactiver");

        btnModifier.setText("Modifier");

        btnAjouter.setText("Ajouter");

        btnRechercher.setText("Rechercher");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(txtRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRechercher)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRafraichir)
                .addGap(5, 5, 5)
                .addComponent(btnDesactiver)
                .addGap(5, 5, 5)
                .addComponent(btnModifier)
                .addGap(5, 5, 5)
                .addComponent(btnAjouter))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRafraichir)
                        .addComponent(btnRechercher))
                    .addComponent(btnDesactiver)
                    .addComponent(btnModifier)
                    .addComponent(btnAjouter))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAjouter;
    private javax.swing.JButton btnDesactiver;
    private javax.swing.JButton btnModifier;
    private javax.swing.JButton btnRafraichir;
    private javax.swing.JButton btnRechercher;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField txtRecherche;
    // End of variables declaration//GEN-END:variables

    private void setupListeners() {
        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        btnModifier.addActionListener(e -> modifierUtilisateur());
        btnDesactiver.addActionListener(e -> desactiverUtilisateur());
        btnRafraichir.addActionListener(e -> loadUsers());
        btnRechercher.addActionListener(e -> rechercherUtilisateurs());
    }
}
