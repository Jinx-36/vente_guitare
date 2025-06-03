/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author tiaray
 */
public class UtilisateurTableModel extends AbstractTableModel {
    private List<Utilisateur> utilisateurs;
    private final String[] columnNames = {"ID", "Nom d'utilisateur", "Nom", "Prénom", "Rôle", "Actif"};
    private final Class<?>[] columnTypes = {Integer.class, String.class, String.class, String.class, String.class, Boolean.class};

    public UtilisateurTableModel() {
        this.utilisateurs = new ArrayList<>();
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = new ArrayList<>(utilisateurs);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return utilisateurs.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Utilisateur user = utilisateurs.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getId();
            case 1: return user.getNomUtilisateur();
            case 2: return user.getNom();
            case 3: return user.getPrenom();
            case 4: return user.getRole();
            case 5: return user.isActif();
            default: return null;
        }
    }

    public Utilisateur getUtilisateurAt(int rowIndex) {
        return utilisateurs.get(rowIndex);
    }

    public void addUtilisateur(Utilisateur user) {
        utilisateurs.add(user);
        fireTableRowsInserted(utilisateurs.size() - 1, utilisateurs.size() - 1);
    }

    public void updateUtilisateur(int row, Utilisateur user) {
        utilisateurs.set(row, user);
        fireTableRowsUpdated(row, row);
    }

    public void removeUtilisateur(int row) {
        utilisateurs.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
