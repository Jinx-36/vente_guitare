/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gestion.venteguitare.main.vue;


import gestion.venteguitare.main.model.Utilisateur;
import gestion.venteguitare.main.vue.panel.*;
/**
 *
 * @author tiaray
 */
public class MainFrame extends javax.swing.JFrame {
    private final Utilisateur currentUser;

    
    public MainFrame(Utilisateur user) {
        this.currentUser = user;
        initComponents();
        setupUIAccordingToRole();
    }
    
    private void setupUIAccordingToRole() {
    tabbedPane.removeAll();
    
    // Dashboard accessible à tous
    if(currentUser.isAdmin())
        tabbedPane.addTab("Dashboard", new DashboardPannel());
    
    // Panels communs
    tabbedPane.addTab("Guitares", new GuitarePannel(this.currentUser));
    tabbedPane.addTab("Clients", new ClientPannel());
    tabbedPane.addTab("Nouvelle Vente", new VentePannel());
    tabbedPane.addTab("Ventes", new GestionVentePannel());
    
    
    
    // Panel réservé aux admins
    if (currentUser.isAdmin()) {
        tabbedPane.addTab("Utilisateurs", new UtilisateurPannel());
    }
}
    
    public Utilisateur getCurrentUser() {
        return this.currentUser;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemDeconnexion = new javax.swing.JMenuItem();
        itemQuitter = new javax.swing.JMenuItem();
        jMenuAdmin = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion de vente de guitares");
        setLocation(new java.awt.Point(0, 0));
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(1500, 1300));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        jMenu1.setText("Fichier");

        itemDeconnexion.setText("Déconnexion");
        itemDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDeconnexionActionPerformed(evt);
            }
        });
        jMenu1.add(itemDeconnexion);

        itemQuitter.setText("Quitter");
        itemQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemQuitterActionPerformed(evt);
            }
        });
        jMenu1.add(itemQuitter);

        jMenuBar1.add(jMenu1);

        jMenuAdmin.setText("Administration");
        jMenuAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAdminActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Gérer les utilisateurs");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuAdmin.add(jMenuItem1);

        jMenuBar1.add(jMenuAdmin);

        jMenu2.setText("?");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDeconnexionActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_itemDeconnexionActionPerformed

    private void itemQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemQuitterActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_itemQuitterActionPerformed

    private void jMenuAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuAdminActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(3);
    }//GEN-LAST:event_jMenuItem1ActionPerformed
      
    
    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem itemDeconnexion;
    private javax.swing.JMenuItem itemQuitter;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenuAdmin;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

}
