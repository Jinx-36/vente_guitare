/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion.venteguitare.main.controlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Executors;

/**
 *
 * @author tiaray
 */
public class ConnexionBase implements AutoCloseable{
    public Connection con;
    public Statement stat;
    private boolean isClosed = false;
    
    public ConnexionBase() throws Exception{
	Class.forName("org.gjt.mm.mysql.Driver");//charger le pilote jdbc
	con = DriverManager.getConnection(Constant.URL, Constant.USER, Constant.PASSWORD);
	stat = con.createStatement();//lanceur de requette
    }

    public Connection getConnection() {
        if (isClosed) {
            throw new IllegalStateException("La connexion est déjà fermée");
        }
        return con;
    }
    
    //select
    public ResultSet executeQuery (String query) throws Exception{
        ResultSet res = null;
        res = stat.executeQuery(query);
        return(res);
    }
    //insertion, update, delete
    public int executeUpdate (String query) throws Exception{
        int res=0;
	res=stat.executeUpdate(query);
	return(res);
    }

    @Override
    public void close() throws Exception {
        if (!isClosed) {
            try {
                if (stat != null) stat.close();
            } finally {
                if (con != null) {
                    con.close();
                    isClosed = true;
                }
            }
        }
    }
}
