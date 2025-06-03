/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gestion.venteguitare.main.vue.panel;


import gestion.venteguitare.main.controlers.*;
import gestion.venteguitare.main.model.*;
import gestion.venteguitare.main.vue.MainFrame;
import gestion.venteguitare.main.vue.dialog.ClientDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import gestion.venteguitare.main.vue.panel.*;

/**
 *
 * @author tiaray
 */
public class VentePannel extends javax.swing.JPanel {
    private ClientDAO clientDao;
    private GuitareDAO guitareDao;
    private VenteService venteService;
    private List<LigneVente> panier;
    private DefaultTableModel panierModel;
    
    /**
     * Creates new form VentePannel
     */
    public VentePannel() {
        try {
            this.clientDao = new ClientDAO();
            this.guitareDao = new GuitareDAO();
            this.venteService = new VenteService();
            this.panier = new ArrayList<>();
            initComponents();
            // Configurer le spinner avec min=1, max=stock, step=1
            spQuantite.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
            setupSpinnerListener();
            initModels();
            loadClients();
            loadGuitares();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur d'initialisation: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
     private void ajouterAuPanier() {
        int selectedRow = tableGuitares.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une guitare",
                "Aucune sélecti on",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = tableGuitares.convertRowIndexToModel(selectedRow);
            int guitareId = (int) tableGuitares.getModel().getValueAt(modelRow, 0);
            int quantite = (Integer) spQuantite.getValue();

            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La quantité doit être positive",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            Guitare guitare = guitareDao.getById(guitareId);

            // Vérifier le stock disponible
            if (quantite > guitare.getQuantiteStock()) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuffisant! Disponible: " + guitare.getQuantiteStock(),
                    "Erreur de stock",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            LigneVente ligne = new LigneVente(guitare, quantite, 0); // 0% de remise par défaut

            panier.add(ligne);
            updatePanier();
            calculerTotaux();

            // Réinitialiser la quantité
            spQuantite.setValue(1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePanier() {
        panierModel.setRowCount(0); // Vider le tableau
        for (LigneVente ligne : panier) {
            panierModel.addRow(new Object[]{
                ligne.getGuitare().getMarque() + " " + ligne.getGuitare().getModele(),
                ligne.getPrixUnitaire(),
                ligne.getQuantite(),
                ligne.getRemise() + "%",
                ligne.getSousTotal()
            });
        }
    }
    
    private void calculerTotaux() {
        double sousTotal = panier.stream().mapToDouble(LigneVente::getSousTotal).sum();
        double tva = sousTotal * 0.20; // TVA à 20%
        double total = sousTotal + tva;
        
        lblSousTotal.setText(String.format("Sous-total: %.2f Ar", sousTotal));
        lblTVA.setText(String.format("TVA (20%%): %.2f Ar", tva));
        lblTotal.setText(String.format("Total: %.2f Ar", total));
    }
    
    private void validerVente() {
        if (panier.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le panier est vide",
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Vérification client sélectionné
            
            if (cbClients.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un client",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int clientId = extraireClientId();
            Client client = clientDao.getById(clientId);
            Vente vente = new Vente(client, panier);
            vente.calculerTotaux();
            vente.setStatutPaiement("impaye");
            
            // Récupération utilisateur connecté
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            Utilisateur utilisateurConnecte = mainFrame.getCurrentUser();
            vente.setUtilisateur(utilisateurConnecte);
            VenteService service = new VenteService();
            service.enregistrerVente(vente, utilisateurConnecte);

            JOptionPane.showMessageDialog(this,
                "Vente enregistrée avec succès. Numéro: " + vente.getId(),
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);

            // Réinitialisation
            panier.clear();
            updatePanier();
            calculerTotaux();
            loadGuitares(); // Recharger les stocks

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur base de données: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Erreur de sélection",
                JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur inattendue: " + ex.getMessage(),
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

        jSplitPane1 = new javax.swing.JSplitPane();
        leftTopPanel = new javax.swing.JPanel();
        btnNouveauClient = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        cbClients = new javax.swing.JComboBox<>();
        leftBottomPanel = new javax.swing.JPanel();
        btnAjouterPanier = new javax.swing.JButton();
        spQuantite = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        leftCenterPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtRechercheGuitare = new javax.swing.JTextField();
        btnRechercheGuitare = new javax.swing.JButton();
        jScrollPanel = new javax.swing.JScrollPane();
        tableGuitares = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        rightBottomPanel = new javax.swing.JPanel();
        lblSousTotal = new javax.swing.JLabel();
        lblTVA = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        btnValiderVente = new javax.swing.JButton();
        btnAnnulerVente = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePanier = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(300);

        leftTopPanel.setMinimumSize(new java.awt.Dimension(500, 25));
        leftTopPanel.setPreferredSize(new java.awt.Dimension(500, 536));
        leftTopPanel.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Nouveau Client");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbClients.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout btnNouveauClientLayout = new javax.swing.GroupLayout(btnNouveauClient);
        btnNouveauClient.setLayout(btnNouveauClientLayout);
        btnNouveauClientLayout.setHorizontalGroup(
            btnNouveauClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnNouveauClientLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbClients, 0, 449, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(14, 14, 14))
        );
        btnNouveauClientLayout.setVerticalGroup(
            btnNouveauClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnNouveauClientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btnNouveauClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(cbClients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        leftTopPanel.add(btnNouveauClient, java.awt.BorderLayout.PAGE_START);

        btnAjouterPanier.setText("Ajouter Panier");
        btnAjouterPanier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterPanierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftBottomPanelLayout = new javax.swing.GroupLayout(leftBottomPanel);
        leftBottomPanel.setLayout(leftBottomPanelLayout);
        leftBottomPanelLayout.setHorizontalGroup(
            leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftBottomPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(spQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAjouterPanier)
                .addContainerGap())
        );
        leftBottomPanelLayout.setVerticalGroup(
            leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftBottomPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAjouterPanier))
                .addContainerGap())
        );

        leftTopPanel.add(leftBottomPanel, java.awt.BorderLayout.PAGE_END);

        leftCenterPanel.setLayout(new java.awt.BorderLayout());

        btnRechercheGuitare.setText("Rechercher");
        btnRechercheGuitare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercheGuitareActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtRechercheGuitare, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRechercheGuitare)
                .addContainerGap(298, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRechercheGuitare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRechercheGuitare))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        leftCenterPanel.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        tableGuitares.setModel(new javax.swing.table.DefaultTableModel(
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
        tableGuitares.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableGuitaresMouseClicked(evt);
            }
        });
        jScrollPanel.setViewportView(tableGuitares);

        leftCenterPanel.add(jScrollPanel, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(leftCenterPanel);

        leftTopPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(leftTopPanel);

        jPanel2.setLayout(new java.awt.BorderLayout());

        lblSousTotal.setText("Sous Total :");

        lblTVA.setText("TVA :");

        lblTotal.setText("Total :");

        btnValiderVente.setText("Valider");
        btnValiderVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderVenteActionPerformed(evt);
            }
        });

        btnAnnulerVente.setText("Annuler");
        btnAnnulerVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnulerVenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rightBottomPanelLayout = new javax.swing.GroupLayout(rightBottomPanel);
        rightBottomPanel.setLayout(rightBottomPanelLayout);
        rightBottomPanelLayout.setHorizontalGroup(
            rightBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightBottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSousTotal)
                    .addComponent(lblTVA)
                    .addComponent(lblTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightBottomPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAnnulerVente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnValiderVente)
                .addContainerGap())
        );
        rightBottomPanelLayout.setVerticalGroup(
            rightBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightBottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSousTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTVA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rightBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnValiderVente)
                    .addComponent(btnAnnulerVente)))
        );

        jPanel2.add(rightBottomPanel, java.awt.BorderLayout.PAGE_END);

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        tablePanier.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablePanier);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAjouterPanierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterPanierActionPerformed
        // TODO add your handling code here:
        ajouterAuPanier();
    }//GEN-LAST:event_btnAjouterPanierActionPerformed

    private void btnValiderVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderVenteActionPerformed
        // TODO add your handling code here:
        validerVente();
    }//GEN-LAST:event_btnValiderVenteActionPerformed

    private void btnAnnulerVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnulerVenteActionPerformed
        // TODO add your handling code here:
        panier.clear();
        updatePanier();
        calculerTotaux();
    }//GEN-LAST:event_btnAnnulerVenteActionPerformed
                                                                                                                                                                                                                                               
    private void btnRechercheGuitareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercheGuitareActionPerformed
        // TODO add your handling code here:
        filtrerGuitare();
    }//GEN-LAST:event_btnRechercheGuitareActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ClientDialog dialog = new ClientDialog(
            (JFrame)SwingUtilities.getWindowAncestor(this),
            ClientDialog.Mode.AJOUT,
            null,
            null
        );
        dialog.setVisible(true);
        try {
            loadClients();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de rafraîchissement: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tableGuitaresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableGuitaresMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            ajouterAuPanier();
        }
    }//GEN-LAST:event_tableGuitaresMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
        if (SwingUtilities.isRightMouseButton(evt)) {
            int row = tablePanier.rowAtPoint(evt.getPoint());
            if (row >= 0) {
                tablePanier.setRowSelectionInterval(row, row);
                JPopupMenu menu = new JPopupMenu();
                JMenuItem deleteItem = new JMenuItem("Supprimer");
                deleteItem.addActionListener(ev -> {
                    panier.remove(row);
                    updatePanier();
                    calculerTotaux();
                });
                menu.add(deleteItem);
                menu.show(tablePanier, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jScrollPane2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAjouterPanier;
    private javax.swing.JButton btnAnnulerVente;
    private javax.swing.JPanel btnNouveauClient;
    private javax.swing.JButton btnRechercheGuitare;
    private javax.swing.JButton btnValiderVente;
    private javax.swing.JComboBox<String> cbClients;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPanel;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblSousTotal;
    private javax.swing.JLabel lblTVA;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel leftBottomPanel;
    private javax.swing.JPanel leftCenterPanel;
    private javax.swing.JPanel leftTopPanel;
    private javax.swing.JPanel rightBottomPanel;
    private javax.swing.JSpinner spQuantite;
    private javax.swing.JTable tableGuitares;
    private javax.swing.JTable tablePanier;
    private javax.swing.JTextField txtRechercheGuitare;
    // End of variables declaration//GEN-END:variables

    private void initModels() {
        // Modèle pour le panier
        panierModel = new DefaultTableModel(
            new Object[]{"Guitare", "Prix unitaire", "Quantité", "Remise", "Sous-total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        tablePanier.setModel(panierModel);
    }
    
    public void loadClients() throws Exception {
        try {
            cbClients.removeAllItems();
            List<Client> clients = clientDao.getAll();
            for (Client client : clients) {
                cbClients.addItem(client.getNom() + " " + client.getPrenom() + " (" + client.getId() + ")");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de chargement des clients: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGuitares() {
        try {
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Marque", "Modèle", "Prix", "Stock"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            List<Guitare> guitares = guitareDao.getAll();
            for (Guitare g : guitares) {
                model.addRow(new Object[]{
                    g.getId(),
                    g.getMarque(),
                    g.getModele(),
                    g.getPrix(),
                    g.getQuantiteStock()
                });
            }

            tableGuitares.setModel(model);
            // Masquer la colonne ID
            tableGuitares.removeColumn(tableGuitares.getColumnModel().getColumn(0));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de chargement des guitares: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrerGuitare() {
        String recherche = txtRechercheGuitare.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tableGuitares.getModel();

        if (recherche.isEmpty()) {
            loadGuitares();
            return;
        }

        try {
            List<Guitare> guitares = guitareDao.getAll();
            model.setRowCount(0);

            for (Guitare g : guitares) {
                if (g.getMarque().toLowerCase().contains(recherche) ||
                    g.getModele().toLowerCase().contains(recherche) ||
                    g.getCategorie().toLowerCase().contains(recherche)) {

                    model.addRow(new Object[]{
                        g.getId(),
                        g.getMarque(),
                        g.getModele(),
                        g.getPrix(),
                        g.getQuantiteStock()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur de recherche: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setupSpinnerListener() {
        tableGuitares.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableGuitares.getSelectedRow();
                if (row >= 0) {
                    try {
                        int modelRow = tableGuitares.convertRowIndexToModel(row);
                        int stock = (int) tableGuitares.getModel().getValueAt(modelRow, 4);
                        ((SpinnerNumberModel)spQuantite.getModel()).setMaximum(stock);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }  
    private int extraireClientId() {
        String selected = (String) cbClients.getSelectedItem();
        System.out.println(selected);
        if (selected == null || selected.isEmpty()) {
            throw new IllegalArgumentException("Aucun client sélectionné");
        }

        // Le format est "Nom Prénom (ID)"
        try {
            // Récupère la partie entre parenthèses
            String idPart = selected.substring(selected.lastIndexOf("(") + 1);
            idPart = idPart.replace(")", "").trim();
            System.out.println(idPart);
            return Integer.parseInt(idPart);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de client invalide", e);
        }
    }
}
