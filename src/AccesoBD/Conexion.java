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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ecollazodominguez
 */
public class Conexion {

    private static String sql;

    /**
     * Array Plantas que usaremos para visualizar la tabla
     */
    public static ArrayList<Plantas> pl = new ArrayList<>();

    /**
     *Array Exposiciones que usaremos para visualizar la tabla
     */
    public static ArrayList<Exposiciones> exp = new ArrayList<>();

    /**
     * Método para conectar a la database
     * @return devuelve el objeto Connection
     */
    public static Connection connect() {

        // parámetro BD
        String url = "jdbc:sqlite:Floria.db";
        Connection conn = null;
        // Creando conexión a la BD
        try {
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     *Método para poner en un Array Plantas toda la tabla de Plantas
     * @return ArrayPlantas con la tabla entera
     */
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

    /**
     *Método para poner en un Array Exposiciones los valores relacionados
     * con cada linea de la tabla Plantas y mostrar la tabla exposiciones relacionada con Plantas
     * @return Arrayexposiciones con la tabla relacionada
     */
    public static ArrayList<Exposiciones> añadirArrayExposiciones() {
        pl = añadirArrayPlantas();
        //sintaxis de la consulta
        sql = "SELECT idexpo, exposicion FROM exposiciones WHERE idexpo in(SELECT idexpo FROM plantas WHERE codigo = ?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            int idExpo;
            String exposicion;
            exp.clear();
            //Bucle para ir cogiendo el código de las plantas y completar la sintaxis
            for (int i = 0; i < pl.size(); i++) {
                pstmt.setInt(1, pl.get(i).getCodigo());
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

    /**
     * Método que crea la Tabla Plantas y Exposiciones añadiendo un número de lineas
     * @param filename Nombre de la base de datos
     * @param a Primera tabla(plantas)
     * @param b Segunda tabla(exposiciones)
     */
    public static void crearTablas(String filename, JTable a, JTable b) {
        //Sintaxis para la conexión
        String url = "jdbc:sqlite:" + filename + ".db";

        // Para crear una tabla usamos esta sintaxis
        sql = "CREATE TABLE IF NOT EXISTS Exposiciones (\n"
                + "	idexpo integer PRIMARY KEY,\n"
                + "	exposicion text NOT NULL\n"
                + ");";

        // Conectamos a la BD
        try (Connection conn = DriverManager.getConnection(url);
        // Creamos un "Statement" que cogerá la sintaxis sql
            Statement stmt = conn.createStatement()) {
        // Creamos la tabla
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "1");
        }

        sql = "CREATE TABLE IF NOT EXISTS Plantas (\n"
                + "	codigo integer PRIMARY KEY,\n"
                + "	nombre text NOT NULL UNIQUE,\n"
                + "	idexpo integer,\n"
                + " FOREIGN KEY (idexpo) REFERENCES Exposiciones(idexpo)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Manipulacion.insertarLineaExpo(1, "Sol");
        Manipulacion.insertarLineaExpo(2, "Semisombra");
        Manipulacion.insertarLineaExpo(3, "Sombra");
        Manipulacion.insertarLineaPlantas(1, "Malus domestica", 2);
        Manipulacion.insertarLineaPlantas(2, "Echeveria elegans", 1);
        Manipulacion.insertarLineaPlantas(5, "Camelia japonica", 3);
        Manipulacion.insertarLineaPlantas(7, "Prunus avium", 2);

    }

    /**
     *Método para actualizar la Tabla Exposiciones de la interfaz y te muestre los datos de la consulta
     * @param conp ArrayPlantas con los resultados de la consulta
     * @param a La tabla Exposiciones de la interfaz
     */
    public static void actuConsultaExpo(ArrayList<Plantas> conp, JTable a) {
        //EXPOSICIONES
        DefaultTableModel model2 = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model2.setRowCount(0);
        exp = Manipulacion.añadirArrayExpoConsulta(conp);
        for (int j = 0; j < exp.size(); j++) {
            model2.addRow(O);
            Exposiciones getE = (Exposiciones) exp.get(j);
            model2.setValueAt(getE.getIdExpo(), j, 0);
            model2.setValueAt(getE.getExposicion(), j, 1);
        }
    }

    /**
     *Método para actualizar la Tabla Plantas de la interfaz y te muestre los datos de la consulta
     * @param conp ArrayPlantas con los resultados de la consulta
     * @param a La tabla Plantas de la interfaz
     */
    public static void actuConsultaPlantas(ArrayList<Plantas> conp, JTable a) {
        //PLANTAS
        DefaultTableModel model = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model.setRowCount(0);
        for (int i = 0; i < conp.size(); i++) {
            model.addRow(O);
            Plantas getP = (Plantas) conp.get(i);
            model.setValueAt(getP.getCodigo(), i, 0);
            model.setValueAt(getP.getNombre(), i, 1);
            model.setValueAt(getP.getIdExpo(), i, 2);
        }
    }

    /**
     *Método que usando un Array mostramos los datos de la Tabla Plantas
     * @param a La tabla Plantas de la interfaz
     */
    public static void actualizarTablaPlantas(JTable a) {
        //PLANTAS
        DefaultTableModel model = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model.setRowCount(0);
        pl = añadirArrayPlantas();
        for (int i = 0; i < pl.size(); i++) {
            model.addRow(O);
            Plantas getP = (Plantas) pl.get(i);
            model.setValueAt(getP.getCodigo(), i, 0);
            model.setValueAt(getP.getNombre(), i, 1);
            model.setValueAt(getP.getIdExpo(), i, 2);
        }
    }

    /**
     *Método que usando un Array mostramos los datos de la Tabla Exposiciones
     * @param a La tabla Exposiciones de la interfaz
     */
    public static void actualizarTablaExposiciones(JTable a) {
        //EXPOSICIONES
        DefaultTableModel model2 = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model2.setRowCount(0);
        exp = añadirArrayExposiciones();
        for (int j = 0; j < exp.size(); j++) {
            model2.addRow(O);
            Exposiciones getE = (Exposiciones) exp.get(j);
            model2.setValueAt(getE.getIdExpo(), j, 0);
            model2.setValueAt(getE.getExposicion(), j, 1);
        }
    }
}
