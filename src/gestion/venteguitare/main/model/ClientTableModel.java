/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author tiaray
 */
public class ClientTableModel extends AbstractTableModel {
    private List<Client> clients;
    private final String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Inscrit le"};
private final Class<?>[] columnTypes = {Integer.class, String.class, String.class, String.class, String.class, String.class};

    public ClientTableModel() {
        this.clients = new ArrayList<>();
    }

    public void setClients(List<Client> clients) {
        this.clients = new ArrayList<>(clients);
        fireTableDataChanged();
    }

    public int getRowCount() {
        return clients.size();
    }        

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
         if (rowIndex >= clients.size()) return null;
        
        Client client = clients.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> client.getId();
            case 1 -> client.getNom();
            case 2 -> client.getPrenom();
            case 3 -> client.getEmail() != null ? client.getEmail() : "N/A";
            case 4 -> client.getTelephone() != null ? client.getTelephone() : "N/A";
            case 5 -> client.getDateInscription();
            default -> null;
        };
    }


    // Méthode utilitaire pour récupérer un client spécifique
    public Client getClientAt(int rowIndex) {
        return clients.get(rowIndex);
    }
}
