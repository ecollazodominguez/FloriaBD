/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floriabd;

import static AccesoBD.metodos.connect;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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


//                 // SQLite connection string
// String url = "jdbc:sqlite:Floria.db";
//        
//        // Para crear una tabla usamos esta sintaxis
//        String sql = "CREATE TABLE IF NOT EXISTS Exposiciones (\n"
//                + "	idexpo integer PRIMARY KEY,\n"
//                + "	exposicion text NOT NULL\n"
//                + ");";
//        
//        // Conectamos a la DB
//        try (Connection conn = DriverManager.getConnection(url);
//         // Creamos un "Statement" que cogerá la sintaxis sql
//                Statement stmt = conn.createStatement()) {
//            // Creamos la tabla
//            stmt.execute(sql);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage()+"1");
//        }
//    
//        
//        
//         // SQLite connection string
// url = "jdbc:sqlite:Floria.db";
//        
//        // Para crear una tabla usamos esta sintaxis
//         sql = "CREATE TABLE IF NOT EXISTS Plantas (\n"
//                + "	codigo integer PRIMARY KEY,\n"
//                + "	nombre text NOT NULL,\n"
//                + "	idexpo integer,\n"
//                + " FOREIGN KEY (idexpo) REFERENCES Exposiciones(idexpo)\n"
//                + ");";
//        
//        // Conectamos a la DB
//        try (Connection conn = DriverManager.getConnection(url);
//         // Creamos un "Statement" que cogerá la sintaxis sql
//                Statement stmt = conn.createStatement()) {
//            // Creamos la tabla
//            stmt.execute(sql);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    insert(1,"Sol");
//    insert(2,"Semisombra");
//    insert(3,"Sombra");
    


    }
    
//    public static void insert(int idexpo, String exposicion) {
// //Sintaxis del insert
//        String sql = "INSERT INTO Exposiciones(idexpo,exposicion) VALUES(?,?)";
//        
//        //conectamos a la BD
//        try (Connection conn = connect();
//                PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            //añadimos al statement los valores por orden
//            pstmt.setInt(1, idexpo);
//            pstmt.setString(2, exposicion);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//}
    

    
}
