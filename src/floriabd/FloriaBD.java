/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floriabd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ecollazodominguez
 */
public class FloriaBD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BaseDatos flor = new BaseDatos();
        JOptionPane.showConfirmDialog(null, "¿Conectar a la Base de datos?");
        flor.setVisible(true);
        flor.setDefaultCloseOperation(3);
    }
    
}
