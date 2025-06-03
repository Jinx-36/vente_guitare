/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main;

import gestion.venteguitare.main.vue.LoginFrame;
import java.lang.System.Logger.Level;
import java.util.logging.Logger;
/**
 *
 * @author tiaray
 */
public class Main {
    
    public static void main(String[] args){
                // Configuration des logs
    System.setProperty("java.util.logging.SimpleFormatter.format", 
        "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    Logger.getLogger("");
    
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
