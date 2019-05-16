/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoBD;

import TablasBD.Plantas;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ecollazodominguez
 */
public class metodos {
    
    static String sql;
    
        public static Connection connect() {

        // parámetro DB
        String url = "jdbc:sqlite:/home/local/DANIELCASTELAO/ecollazodominguez/NetBeansProjects/SQLiteJDBC/db/Floria.db";
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

    public static Object[] actualizarTabla() {
        //sintaxis de la consulta
        sql = "SELECT codigo, nombre, exposicion FROM plantas";
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            int codigo;
            String nombre, exposicion;
            // recorre el resultado y lo muestra
            while (rs.next()) {
                codigo = rs.getInt("codigo");
                nombre = rs.getString("nombre");
                exposicion = rs.getString("exposicion");
                return new Object[]{codigo,nombre};
            }
            
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
}
