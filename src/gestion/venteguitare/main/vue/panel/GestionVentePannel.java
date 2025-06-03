/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gestion.venteguitare.main.vue.panel;

import gestion.venteguitare.main.controlers.ClientDAO;
import gestion.venteguitare.main.controlers.LigneVenteDAO;
import gestion.venteguitare.main.controlers.VenteDAO;
import gestion.venteguitare.main.model.Client;
import gestion.venteguitare.main.model.Vente;
import gestion.venteguitare.main.services.FactureGenerator;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author tiaray
 */
public class GestionVentePannel extends javax.swing.JPanel {

    private VenteDAO venteDao;
    private ClientDAO clientDao;
    private DefaultTableModel ventesModel;
    /**
     * Creates new form GestionVentePannel
     */
    public GestionVentePannel() {
        try {
            this.venteDao = new VenteDAO();
            this.clientDao = new ClientDAO();
            initComponents();
            initModels();
            loadVentes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur d'initialisation: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void initModels() {
        ventesModel = new DefaultTableModel(
            new Object[]{"ID", "Date", "Client", "Montant TTC", "Statut"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 3 -> Double.class;
                    default -> String.class;
                };
            }
        };
        tableVentes.setModel(ventesModel);
    }
    public void loadVentes() {
        try {
            ventesModel.setRowCount(0);
            List<Vente> ventes = venteDao.getAll();
            
            for (Vente vente : ventes) {
                Client client = clientDao.getById(vente.getClient().getId());
                ventesModel.addRow(new Object[]{
                    vente.getId(),
                    vente.getDateVente().toString(),
                    client.getNom() + " " + client.getPrenom(),
                    vente.getMontantTTC(),
                    vente.getStatutPaiement()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de chargement: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void filtrerVentes() {
        String statut = (String) cbStatut.getSelectedItem();
        
        try {
            ventesModel.setRowCount(0);
            List<Vente> ventes;
            
            if ("Tous".equals(statut)) {
                ventes = venteDao.getAll();
            } else {
                ventes = venteDao.getByStatut(statut);
            }
            
            for (Vente vente : ventes) {
                Client client = clientDao.getById(vente.getClient().getId());
                ventesModel.addRow(new Object[]{
                    vente.getId(),
                    vente.getDateVente().toString(),
                    client.getNom() + " " + client.getPrenom(),
                    vente.getMontantTTC(),
                    vente.getStatutPaiement()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de filtrage: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changerStatut() {
        int selectedRow = tableVentes.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une vente",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int venteId = (int) ventesModel.getValueAt(selectedRow, 0);
        String[] options = {"payé", "impayé"};
        
        String nouveauStatut = (String) JOptionPane.showInputDialog(
            this,
            "Nouveau statut pour la vente #" + venteId,
            "Changer statut",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (nouveauStatut != null) {
            try {
                venteDao.updateStatutPaiement(venteId, nouveauStatut);
                ventesModel.setValueAt(nouveauStatut, selectedRow, 4);
                JOptionPane.showMessageDialog(this,
                    "Statut mis à jour avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur de mise à jour: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void genererFacture() {
        int selectedRow = tableVentes.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une vente",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int venteId = (int) ventesModel.getValueAt(selectedRow, 0);
        
        try {
            FactureGenerator.generate(venteId);
            
            JOptionPane.showMessageDialog(this,
                "Facture générée avec succès dans le dossier Factures",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de génération: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
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

        pannelFiltres = new javax.swing.JPanel();
        cbStatut = new javax.swing.JComboBox<>();
        btnFiltrer = new javax.swing.JButton();
        btnRafraichir = new javax.swing.JButton();
        btnChangerStatut = new javax.swing.JButton();
        btnGenererFacture = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableVentes = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        cbStatut.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous", "payé", "impayé" }));
        pannelFiltres.add(cbStatut);

        btnFiltrer.setText("Filtrer");
        btnFiltrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrerActionPerformed(evt);
            }
        });
        pannelFiltres.add(btnFiltrer);

        btnRafraichir.setText("Rafraîchir");
        btnRafraichir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRafraichirActionPerformed(evt);
            }
        });
        pannelFiltres.add(btnRafraichir);

        btnChangerStatut.setText("Changer Statut");
        btnChangerStatut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangerStatutActionPerformed(evt);
            }
        });
        pannelFiltres.add(btnChangerStatut);

        btnGenererFacture.setText("Générer Facture");
        btnGenererFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenererFactureActionPerformed(evt);
            }
        });
        pannelFiltres.add(btnGenererFacture);

        add(pannelFiltres, java.awt.BorderLayout.PAGE_START);

        tableVentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Client", "Montant TTC", "Statut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableVentes);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrerActionPerformed
        // TODO add your handling code here:
        filtrerVentes();
    }//GEN-LAST:event_btnFiltrerActionPerformed

    private void btnChangerStatutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangerStatutActionPerformed
        // TODO add your handling code here:
        changerStatut();
    }//GEN-LAST:event_btnChangerStatutActionPerformed

    private void btnGenererFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenererFactureActionPerformed
        // TODO add your handling code here:
        genererFacture();
    }//GEN-LAST:event_btnGenererFactureActionPerformed

    private void btnRafraichirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRafraichirActionPerformed
        // TODO add your handling code here:
        loadVentes();
    }//GEN-LAST:event_btnRafraichirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangerStatut;
    private javax.swing.JButton btnFiltrer;
    private javax.swing.JButton btnGenererFacture;
    private javax.swing.JButton btnRafraichir;
    private javax.swing.JComboBox<String> cbStatut;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pannelFiltres;
    private javax.swing.JTable tableVentes;
    // End of variables declaration//GEN-END:variables
}
