package gestion.venteguitare.main.vue.panel;

import gestion.venteguitare.main.controlers.VenteDAO;
import gestion.venteguitare.main.controlers.GuitareDAO;
import gestion.venteguitare.main.controlers.ClientDAO;
import gestion.venteguitare.main.model.Client;
import gestion.venteguitare.main.model.Vente;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class DashboardPannel extends javax.swing.JPanel {

    private VenteDAO venteDao;
    private GuitareDAO guitareDao;
    private ClientDAO clientDao;

    public DashboardPannel() {
        try {
            this.venteDao = new VenteDAO();
            this.guitareDao = new GuitareDAO();
            this.clientDao = new ClientDAO();
            initComponents();
            loadStats();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erreur d'initialisation du dashboard: " + ex.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel des statistiques
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistiques clés"));
        statsPanel.setAlignmentX(LEFT_ALIGNMENT);
        
        // Ajouter les cartes de statistiques avec taille fixe
        statsPanel.add(createStatCard("Chiffre d'affaires", "0 Ar", new Color(52, 152, 219)));
        statsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        statsPanel.add(createStatCard("Ventes", "0", new Color(46, 204, 113)));
        statsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        statsPanel.add(createStatCard("Clients", "0", new Color(155, 89, 182)));
        statsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        statsPanel.add(createStatCard("Stocks faibles", "0", new Color(231, 76, 60)));
        
        add(statsPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel des graphiques
        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new BoxLayout(chartsPanel, BoxLayout.X_AXIS));
        chartsPanel.setBorder(BorderFactory.createTitledBorder("Graphiques"));
        chartsPanel.setAlignmentX(LEFT_ALIGNMENT);
        
        // Graphique d'évolution des ventes
        JPanel lineChartPanel = createLineChart();
        chartsPanel.add(lineChartPanel);
        chartsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        
        // Graphique camembert des catégories
        JPanel pieChartPanel = createPieChart();
        chartsPanel.add(pieChartPanel);
        
        add(chartsPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Dernières ventes
        JPanel salesPanel = new JPanel();
        salesPanel.setLayout(new BoxLayout(salesPanel, BoxLayout.Y_AXIS));
        salesPanel.setBorder(BorderFactory.createTitledBorder("Dernières ventes"));
        salesPanel.setAlignmentX(LEFT_ALIGNMENT);
        
        JTable salesTable = createSalesTable();
        salesPanel.add(new JScrollPane(salesTable));
        
        add(salesPanel);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(color.brighter().brighter());
        card.setPreferredSize(new Dimension(200, 100));
        card.setMaximumSize(new Dimension(200, 100));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(14.0f));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(24.0f).deriveFont(1)); // Bold
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            Map<String, Double> ventesParMois = venteDao.getVentesParMois();
            ventesParMois.forEach((mois, total) -> {
                dataset.addValue(total, "Ventes", mois);
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
            dataset.addValue(0, "Ventes", "Erreur");
        }
        
        JFreeChart chart = ChartFactory.createLineChart(
            "Évolution des ventes (Ar)", 
            "Mois", 
            "Montant (Ar)", 
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(chartPanel);
        return panel;
    }
    
    private JPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        try {
            Map<String, Double> ventesParCategorie = venteDao.getVentesParCategorie();
            ventesParCategorie.forEach((categorie, total) -> {
                // Remplacer "Acoustique" par "Basse"
                String cat = categorie.equals("Acoustique") ? "Basse" : categorie;
                dataset.setValue(cat, total);
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
            dataset.setValue("Erreur", 100);
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Répartition par catégorie (Ar)", 
            dataset, 
            true, true, false
        );
        
        // Personnalisation des couleurs
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Électrique", new Color(52, 152, 219));
        plot.setSectionPaint("Basse", new Color(46, 204, 113));
        plot.setSectionPaint("Classique", new Color(155, 89, 182));
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(chartPanel);
        return panel;
    }
    
    private JTable createSalesTable() {
        String[] columnNames = {"ID", "Date", "Client", "Montant (Ar)"};
        Object[][] data = {};
        
        try {
            List<Vente> dernieresVentes = venteDao.getDernieresVentes(5);
            data = new Object[dernieresVentes.size()][4];
            
            for (int i = 0; i < dernieresVentes.size(); i++) {
                Vente v = dernieresVentes.get(i);
                Client c = clientDao.getById(v.getClient().getId());
                data[i][0] = v.getId();
                data[i][1] = v.getDateVente().toString();
                data[i][2] = c.getNom() + " " + c.getPrenom();
                data[i][3] = String.format("%.2f Ar", v.getMontantTTC());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            data = new Object[][]{{"Erreur", "", "", ""}};
        }
        
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        return table;
    }
    
    private void loadStats() {
        try {
            // Chiffre d'affaires total
            double caTotal = venteDao.getChiffreAffairesTotal();
            updateStatCard(0, String.format("%.2f Ar", caTotal));
            
            // Nombre de ventes
            int nbVentes = venteDao.getNombreVentes();
            updateStatCard(1, String.valueOf(nbVentes));
            
            // Nombre de clients
            int nbClients = clientDao.getNombreClients();
            updateStatCard(2, String.valueOf(nbClients));
            
            // Nombre de stocks faibles
            int nbStocksFaibles = guitareDao.getNombreStocksFaibles(3); // Seuil à 3
            updateStatCard(3, String.valueOf(nbStocksFaibles));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void updateStatCard(int index, String value) {
        JPanel statsPanel = (JPanel) getComponent(0);
        JPanel card = (JPanel) statsPanel.getComponent(index * 2); // ×2 à cause des Box.Filler
        JLabel valueLabel = (JLabel) card.getComponent(2); // [0]=titre, [1]=Box, [2]=valeur
        valueLabel.setText(value);
    }
}