/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoBD;

import TablasBD.Plantas;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ecollazodominguez
 */
public class metodos {
    
    private static String sql;
    public static ArrayList<Plantas> pl = new ArrayList<>();
    
        public static Connection connect() {

        // parámetro DB
        String url = "jdbc:sqlite:Floria.db";
        Connection conn = null;
        // Creando conexión a la DB
        try {
            conn = DriverManager.getConnection(url);

            System.out.println("La conexión a SQLite ha sido establecida");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static ArrayList<Plantas> añadirArray() {
        //sintaxis de la consulta
        sql = "SELECT codigo, nombre, idexpo FROM plantas";
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            int codigo, idExpo;
            String nombre;
            pl.clear();
            // recorre el resultado y lo muestra
            while (rs.next()) {
                codigo = rs.getInt("codigo");
                nombre = rs.getString("nombre");
                idExpo = rs.getInt("idexpo");
                pl.add(new Plantas(codigo,nombre,idExpo));
            }
            return pl;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public void consultas(JTextField a,JTextField b){
        try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery(sql)) {
                pstmt.setString(1, a.getText());
                pstmt.setString(1, b.getText());

                while (rs.next()) {
                    System.out.println(rs.getInt("codigo") + "\t"
                            + rs.getString("nombre") + "\t"
                            + rs.getString("exposicion"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
        }
    }
    
    public static void añadirPlantas(JTextField a, JTextField b, JTextField c){
                //Sintaxis del insert
        String sql = "INSERT INTO PLANTAS(codigo,nombre,idexpo) VALUES(?,?,?)";

        //conectamos a la BD
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //añadimos al statement los valores por orden
            pstmt.setInt(1, Integer.valueOf(a.getText()));
            pstmt.setString(2, b.getText());
            pstmt.setInt(3, Integer.valueOf(c.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Linea añadida"
                                          + "\n" + a.getText()+" "+b.getText()+" "+c.getText());
            añadirArray();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
