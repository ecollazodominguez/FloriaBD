/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoBD;

import TablasBD.Exposiciones;
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
    public static ArrayList<Exposiciones> exp = new ArrayList<>();

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

    public static ArrayList<Plantas> añadirArrayPlantas() {
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
                pl.add(new Plantas(codigo, nombre, idExpo));
            }
            return pl;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ArrayList<Exposiciones> añadirArrayExposiciones(){
        pl = añadirArrayPlantas();
        //sintaxis de la consulta
        sql = "SELECT idexpo, exposicion FROM exposiciones WHERE idexpo in(SELECT idexpo FROM plantas WHERE codigo = ?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            int idExpo;
            String exposicion;
            exp.clear();
            for (int i = 0; i < pl.size(); i++) {
                pstmt.setInt(1,pl.get(i).getCodigo());
                ResultSet rs = pstmt.executeQuery();
                // recorre el resultado y lo muestra
                while (rs.next()) {
                    idExpo = rs.getInt("idexpo");
                    exposicion = rs.getString("exposicion");
                    exp.add(new Exposiciones(idExpo, exposicion));
                    
                }
            }
            return exp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exp;
    }

    public static void consultar(JTextField a, JTextField b, JTextField c) {
        String sql2;
        int codigo, idexpo;
        String nombre, exposicion;
        try (Connection conn = connect();) {
            
            PreparedStatement pstmt = null;
            PreparedStatement pstmt2 = null;
            
        if (!a.getText().isEmpty() && !b.getText().isEmpty()) {
            sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE codigo = ? and nombre = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, a.getText());
            pstmt.setString(2, b.getText());
            
        }else if(!a.getText().isEmpty() && !c.getText().isEmpty()){
            sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE codigo = ? and idexpo = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, a.getText());
            pstmt.setString(2, c.getText());
        }else if(!b.getText().isEmpty() && !c.getText().isEmpty()){
            sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE nombre = ? and idexpo = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, b.getText());
            pstmt.setString(2, c.getText());
        }else if(!a.getText().isEmpty()){
            sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE codigo = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, a.getText());
        }else if(!b.getText().isEmpty()){
            sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE nombre = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, b.getText());
        }else if(!c.getText().isEmpty()){
            sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE idexpo = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.getText());
            
        }
            ArrayList<Plantas> conp= new ArrayList<>();
            ArrayList<Exposiciones> cone= new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                codigo= rs.getInt("codigo");
                nombre= rs.getString("nombre");
                idexpo= rs.getInt("idexpo");
                conp.add(new Plantas(codigo,nombre,idexpo));
                
            sql2="SELECT exposicion FROM exposiciones where idexpo = ?";
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setInt(1, rs.getInt("idexpo"));
                ResultSet rs2 = pstmt2.executeQuery();
            while (rs2.next()){
                exposicion= rs2.getString("exposicion");
                 cone.add(new Exposiciones(idexpo,exposicion));
            }
            }
            System.out.println("PLANTAS\n"+conp.toString()+"EXPOSICIONES\n"+cone.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void añadirPlantas(JTextField a, JTextField b, JTextField c) {
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
                    + "\n" + a.getText() + " " + b.getText() + " " + c.getText());
            añadirArrayPlantas();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void borrarPlantas(JTextField a, JTextField b, JTextField c) {
        if (!a.getText().isEmpty()) {
            String sql = "DELETE FROM plantas WHERE codigo = ?";

            try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.valueOf(a.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Linea borrada"
                                            + pstmt.toString());;
                añadirArrayPlantas();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!b.getText().isEmpty()) {
            String sql = "DELETE FROM plantas WHERE nombre = ?";

            try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, b.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Linea borrada"
                                            + pstmt.toString());
                añadirArrayPlantas();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!c.getText().isEmpty()) {
            String sql = "DELETE FROM plantas WHERE idexpo = ?";

            try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, c.getText());
                pstmt.executeUpdate();
                System.out.println("Linea borrada"
                                            + pstmt.toString());
                
                añadirArrayPlantas();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
