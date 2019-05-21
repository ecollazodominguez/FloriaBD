/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccesoBD;

import Excepciones.NumeroMayorExcepcion;
import Excepciones.ValorVacioExcepcion;
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
public class Manipulacion {

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

    public static ArrayList<Exposiciones> añadirArrayExposiciones() {
        pl = añadirArrayPlantas();
        //sintaxis de la consulta
        sql = "SELECT idexpo, exposicion FROM exposiciones WHERE idexpo in(SELECT idexpo FROM plantas WHERE codigo = ?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            int idExpo;
            String exposicion;
            exp.clear();
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

    public static ArrayList<Exposiciones> añadirArrayExpoConsulta(ArrayList<Plantas> conp) {
        //sintaxis de la consulta
        sql = "SELECT idexpo, exposicion FROM exposiciones WHERE idexpo in(SELECT idexpo FROM plantas WHERE codigo = ?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            int idExpo;
            String exposicion;
            exp.clear();
            for (int i = 0; i < conp.size(); i++) {
                pstmt.setInt(1, conp.get(i).getCodigo());
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

    public static ArrayList<Plantas> consultar(JTextField a, JTextField b, JTextField c) {
        int codigo, idexpo;
        String nombre, exposicion;
        try (Connection conn = connect();) {

            PreparedStatement pstmt = null;

            if (!a.getText().isEmpty() && !b.getText().isEmpty()) {
                sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE codigo = ? and nombre = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, a.getText());
                pstmt.setString(2, b.getText());

            } else if (!a.getText().isEmpty() && !c.getText().isEmpty()) {
                sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE codigo = ? and idexpo = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, a.getText());
                pstmt.setString(2, c.getText());
            } else if (!b.getText().isEmpty() && !c.getText().isEmpty()) {
                sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE nombre = ? and idexpo = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, b.getText());
                pstmt.setString(2, c.getText());
            } else if (!a.getText().isEmpty()) {
                sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE codigo = ? ";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, a.getText());
            } else if (!b.getText().isEmpty()) {
                sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE nombre = ? ";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, b.getText());
            } else if (!c.getText().isEmpty()) {
                sql = "SELECT codigo, nombre, idexpo FROM plantas WHERE idexpo = ? ";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, c.getText());

            }
            ArrayList<Plantas> conp = new ArrayList<>();
            ArrayList<Exposiciones> cone = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                codigo = rs.getInt("codigo");
                nombre = rs.getString("nombre");
                idexpo = rs.getInt("idexpo");
                conp.add(new Plantas(codigo, nombre, idexpo));
            }
            return conp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void añadirPlantas(JTextField a, JTextField b, JTextField c) throws ValorVacioExcepcion, NumeroMayorExcepcion {
        //Sintaxis del insert

        if (a.getText().isEmpty() || b.getText().isEmpty() || c.getText().isEmpty()) {
            throw new ValorVacioExcepcion("Todos los campos tienen que tener un valor");
        }
        if (Integer.valueOf(c.getText()) > 3) {
            throw new NumeroMayorExcepcion("La ID de exposición no puede ser mayor de 3");
        }
        sql = "INSERT INTO PLANTAS(codigo,nombre,idexpo) VALUES(?,?,?)";
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

    public static void borrarPlantas(JTextField a, JTextField b, JTextField c) throws ValorVacioExcepcion {
        if (!a.getText().isEmpty()) {
            sql = "DELETE FROM plantas WHERE codigo = ?";
            String sql2 = "Select count(codigo) from plantas where codigo = ?";
            try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setInt(1, Integer.valueOf(a.getText()));
                ResultSet rs = pstmt2.executeQuery();
                pstmt.setInt(1, Integer.valueOf(a.getText()));
                pstmt.executeUpdate();
                if (rs.getInt(1) == 0) {
                    throw new ValorVacioExcepcion("No hay lineas para borrar");
                }
                JOptionPane.showMessageDialog(null, rs.getString(1) + "linea(s) borrada(s)\n");
                añadirArrayPlantas();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!b.getText().isEmpty()) {
            sql = "DELETE FROM plantas WHERE nombre = ?";
            String sql2 = "Select count(nombre) from plantas where nombre = ?";

            try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setString(1, b.getText());
                ResultSet rs = pstmt2.executeQuery();
                pstmt.setString(1, b.getText());
                pstmt.executeUpdate();
                if (rs.getInt(1) == 0) {
                    throw new ValorVacioExcepcion("No hay lineas para borrar");
                }
                JOptionPane.showMessageDialog(null, rs.getString(1) + "linea(s) borrada(s)\n");
                añadirArrayPlantas();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!c.getText().isEmpty()) {
            sql = "DELETE FROM plantas WHERE idexpo = ?";
            String sql2 = "Select count(idexpo) from plantas where idexpo = ?";

            try (Connection conn = connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setInt(1, Integer.valueOf(c.getText()));
                ResultSet rs = pstmt2.executeQuery();
                pstmt.setInt(1, Integer.valueOf(c.getText()));
                pstmt.executeUpdate();
                if (rs.getInt(1) == 0) {
                    throw new ValorVacioExcepcion("No hay lineas para borrar");
                }
                JOptionPane.showMessageDialog(null, rs.getString(1) + " linea(s) borrada(s)\n");

                añadirArrayPlantas();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void modificarLinea(JTextField a, JTextField b, JTextField c) throws NumeroMayorExcepcion, ValorVacioExcepcion {

        try (Connection conn = connect();) {
            PreparedStatement pstmt = null;
            if (b.getText().isEmpty() && c.getText().isEmpty()){
                throw new NullPointerException();
            }else if(a.getText().isEmpty()){
                throw new ValorVacioExcepcion("Introduzca un código para modificar la linea.");
            }

            if (!b.getText().isEmpty() && !c.getText().isEmpty()) {
                sql = "UPDATE plantas SET nombre = ? , "
                        + "idexpo = ? "
                        + "WHERE codigo = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, b.getText());
                if (Integer.valueOf(c.getText()) > 3) {
                    throw new NumeroMayorExcepcion("La ID de exposición no puede ser mayor de 3");
                }
                pstmt.setInt(2, Integer.valueOf(c.getText()));
                pstmt.setInt(3, Integer.valueOf(a.getText()));
            } else if (!b.getText().isEmpty()) {
                sql = "UPDATE plantas SET nombre = ? "
                        + "WHERE codigo = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, b.getText());
                pstmt.setInt(2, Integer.valueOf(a.getText()));
            } else if (!c.getText().isEmpty()) {
                sql = "UPDATE plantas SET idexpo = ? "
                        + "WHERE codigo = ?";
                pstmt = conn.prepareStatement(sql);
                if (Integer.valueOf(c.getText()) > 3) {
                    throw new NumeroMayorExcepcion("La ID de exposición no puede ser mayor de 3");
                }
                pstmt.setString(1, c.getText());
                pstmt.setInt(2, Integer.valueOf(a.getText()));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void crearTablas(String filename,JTable a,JTable b) {
//         SQLite connection string
        String url = "jdbc:sqlite:" + filename + ".db";

        // Para crear una tabla usamos esta sintaxis
        sql = "CREATE TABLE IF NOT EXISTS Exposiciones (\n"
                + "	idexpo integer PRIMARY KEY,\n"
                + "	exposicion text NOT NULL\n"
                + ");";

        // Conectamos a la DB
        try (Connection conn = DriverManager.getConnection(url);
                // Creamos un "Statement" que cogerá la sintaxis sql
                Statement stmt = conn.createStatement()) {
            // Creamos la tabla
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "1");
        }

        // SQLite connection string
        url = "jdbc:sqlite:" + filename + ".db";

        // Para crear una tabla usamos esta sintaxis
        sql = "CREATE TABLE IF NOT EXISTS Plantas (\n"
                + "	codigo integer PRIMARY KEY,\n"
                + "	nombre text NOT NULL UNIQUE,\n"
                + "	idexpo integer,\n"
                + " FOREIGN KEY (idexpo) REFERENCES Exposiciones(idexpo)\n"
                + ");";

        // Conectamos a la DB
        try (Connection conn = DriverManager.getConnection(url);
                // Creamos un "Statement" que cogerá la sintaxis sql
                Statement stmt = conn.createStatement()) {
            // Creamos la tabla
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        insertarLineaExpo(1, "Sol");
        insertarLineaExpo(2, "Semisombra");
        insertarLineaExpo(3, "Sombra");
        insertarLineaPlantas(1, "Malus domestica", 2);
        insertarLineaPlantas(2, "Echeveria elegans", 1);
        insertarLineaPlantas(5, "Camelia japonica", 3);
        insertarLineaPlantas(7, "Prunus avium", 2);
        actualizarTablaExposiciones(a);
        actualizarTablaPlantas(b);

    }

    public static void insertarLineaExpo(int idexpo, String exposicion) {
        //Sintaxis del insert
        sql = "INSERT INTO Exposiciones(idexpo,exposicion) VALUES(?,?)";

        //conectamos a la BD
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //añadimos al statement los valores por orden
            pstmt.setInt(1, idexpo);
            pstmt.setString(2, exposicion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("La linea ya existe se añadirá la siguiente si existe.");
        }
    }

    public static void insertarLineaPlantas(int codigo, String nombre, int idExpo) {
        //Sintaxis del insert
        sql = "INSERT INTO Plantas(codigo,nombre,idexpo) VALUES(?,?,?)";

        //conectamos a la BD
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //añadimos al statement los valores por orden
            pstmt.setInt(1, codigo);
            pstmt.setString(2, nombre);
            pstmt.setInt(3, idExpo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("La linea ya existe se añadirá la siguiente si existe.");
        }
    }
    
        public static void actuConsultaExpo(ArrayList<Plantas> conp, JTable a) {
        //EXPOSICIONES
        DefaultTableModel model2 = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model2.setRowCount(0);
        Manipulacion.exp = Manipulacion.añadirArrayExpoConsulta(conp);
        for (int j = 0; j < Manipulacion.exp.size(); j++) {
            model2.addRow(O);
            Exposiciones getE = (Exposiciones) Manipulacion.exp.get(j);
            model2.setValueAt(getE.getIdExpo(), j, 0);
            model2.setValueAt(getE.getExposicion(), j, 1);
        }
    }
        
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
            
        public static void actualizarTablaPlantas(JTable a) {
        //PLANTAS
        DefaultTableModel model = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model.setRowCount(0);
        Manipulacion.pl = Manipulacion.añadirArrayPlantas();
        for (int i = 0; i < Manipulacion.pl.size(); i++) {
            model.addRow(O);
            Plantas getP = (Plantas) Manipulacion.pl.get(i);
            model.setValueAt(getP.getCodigo(), i, 0);
            model.setValueAt(getP.getNombre(), i, 1);
            model.setValueAt(getP.getIdExpo(), i, 2);
        }
    }
        
        public static void actualizarTablaExposiciones(JTable a) {
        //EXPOSICIONES
        DefaultTableModel model2 = (DefaultTableModel) a.getModel();
        Object O[] = null;
        model2.setRowCount(0);
        Manipulacion.exp = Manipulacion.añadirArrayExposiciones();
        for (int j = 0; j < Manipulacion.exp.size(); j++) {
            model2.addRow(O);
            Exposiciones getE = (Exposiciones) Manipulacion.exp.get(j);
            model2.setValueAt(getE.getIdExpo(), j, 0);
            model2.setValueAt(getE.getExposicion(), j, 1);
        }
    }

}
