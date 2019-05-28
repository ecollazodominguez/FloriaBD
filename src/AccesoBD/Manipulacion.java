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

/**
 *
 * @author ecollazodominguez
 */
public class Manipulacion {

    private static String sql;
    private static boolean correcto = true;

    /**
     *  Metodo que crea un Array de Exposiciones a partir de un Array de Plantas
     * para recoger el resultado de una consulta
     * @param conp ArrayList de Plantas que usaremos para hacer la consulta
     * @return Devuelve el Array Exposiciones
     */
    public static ArrayList<Exposiciones> añadirArrayExpoConsulta(ArrayList<Plantas> conp) {
        //sintaxis de la consulta
        sql = "SELECT idexpo, exposicion FROM exposiciones WHERE idexpo in(SELECT idexpo FROM plantas WHERE codigo = ?)";
        try (Connection conn = Conexion.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            int idExpo;
            String exposicion;
            Conexion.exp.clear();
            for (int i = 0; i < conp.size(); i++) {
                pstmt.setInt(1, conp.get(i).getCodigo());
                ResultSet rs = pstmt.executeQuery();
                // recorre el resultado y lo muestra
                while (rs.next()) {
                    idExpo = rs.getInt("idexpo");
                    exposicion = rs.getString("exposicion");
                    Conexion.exp.add(new Exposiciones(idExpo, exposicion));

                }
            }
            return Conexion.exp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Conexion.exp;
    }

    /**
     *Método para consultar en la tabla Plantas a través de los campos de la
     * interfaz metiendo el resultado en un Array Plantas
     * @param a Primer campo(Codigo)
     * @param b Segundo campo(Nombre)
     * @param c Tercer campo(IdExpo)
     * @return Array Plantas con los resultados de la consulta
     */
    public static ArrayList<Plantas> consultar(JTextField a, JTextField b, JTextField c) {
        int codigo, idexpo;
        String nombre, exposicion;
        try (Connection conn = Conexion.connect();) {

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

    /**
     *Método para añadir en la tabla Plantas a través de los campos de la interfaz,
     * necesario rellenar todos los campos.
     * @param a Primer campo(Codigo)
     * @param b Segundo campo(Nombre)
     * @param c Tercer campo(IdExpo)
     * @throws ValorVacioExcepcion Envía error si alguno de los campos está vacío
     * @throws NumeroMayorExcepcion Envía error si la ID de exposición es mayor que 3
     */
    public static String añadirPlantas(JTextField a, JTextField b, JTextField c) throws ValorVacioExcepcion, NumeroMayorExcepcion {
        //Sintaxis del insert

        if (a.getText().isEmpty() || b.getText().isEmpty() || c.getText().isEmpty()) {
            correcto = false;
            throw new ValorVacioExcepcion("Todos los campos tienen que tener un valor");      
        }
        if (Integer.valueOf(c.getText()) > 3) {
            correcto = false;
            throw new NumeroMayorExcepcion("La ID de exposición no puede ser mayor de 3");
        }
        sql = "INSERT INTO PLANTAS(codigo,nombre,idexpo) VALUES(?,?,?)";
        //conectamos a la BD
        try (Connection conn = Conexion.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //añadimos al statement los valores por orden
            pstmt.setInt(1, Integer.valueOf(a.getText()));
            pstmt.setString(2, b.getText());
            pstmt.setInt(3, Integer.valueOf(c.getText()));
            pstmt.executeUpdate();
            Conexion.añadirArrayPlantas();
            return "Linea añadida"
                    + "\n" + a.getText() + " " + b.getText() + " " + c.getText();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Ha habido un error";
    }

    /**
     *Método para borrar en la tabla Plantas a través de los campos de la interfaz,
     * Solo borra con un valor en un campo.
     * @param a Primer campo(Codigo)
     * @param b Segundo campo(Nombre)
     * @param c Tercer campo(Idexpo)
     * @throws ValorVacioExcepcion Envía error si el resultado está vacío
     */
    public static String borrarPlantas(JTextField a, JTextField b, JTextField c) throws ValorVacioExcepcion {
        if (!a.getText().isEmpty()) {
            sql = "DELETE FROM plantas WHERE codigo = ?";
            String sql2 = "Select count(codigo) from plantas where codigo = ?";
            try (Connection conn = Conexion.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setInt(1, Integer.valueOf(a.getText()));
                ResultSet rs = pstmt2.executeQuery();
                pstmt.setInt(1, Integer.valueOf(a.getText()));
                pstmt.executeUpdate();
                if (rs.getInt(1) == 0) {
                    throw new ValorVacioExcepcion("No hay lineas para borrar");
                }
                Conexion.añadirArrayPlantas();
                return rs.getString(1) + " linea(s) borrada(s)\n";
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!b.getText().isEmpty()) {
            sql = "DELETE FROM plantas WHERE nombre = ?";
            String sql2 = "Select count(nombre) from plantas where nombre = ?";

            try (Connection conn = Conexion.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setString(1, b.getText());
                ResultSet rs = pstmt2.executeQuery();
                pstmt.setString(1, b.getText());
                pstmt.executeUpdate();
                if (rs.getInt(1) == 0) {
                    throw new ValorVacioExcepcion("No hay lineas para borrar");
                }
                Conexion.añadirArrayPlantas();
                return rs.getString(1) + "linea(s) borrada(s)\n";

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!c.getText().isEmpty()) {
            sql = "DELETE FROM plantas WHERE idexpo = ?";
            String sql2 = "Select count(idexpo) from plantas where idexpo = ?";

            try (Connection conn = Conexion.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setInt(1, Integer.valueOf(c.getText()));
                ResultSet rs = pstmt2.executeQuery();
                pstmt.setInt(1, Integer.valueOf(c.getText()));
                pstmt.executeUpdate();
                if (rs.getInt(1) == 0) {
                    throw new ValorVacioExcepcion("No hay lineas para borrar");
                }

                Conexion.añadirArrayPlantas();
                return rs.getString(1) + " linea(s) borrada(s)\n";

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
            return "No se ha borrado nada";
    }

    /**
     *Método para modificar en la tabla Plantas a través de los campos de la interfaz,
     * necesario código en el campo de la interfaz para realizar la modificación
     * @param a Primer campo(Codigo)
     * @param b Segundo campo(Nombre)
     * @param c Tercer campo(Idexpo)
     * @throws NumeroMayorExcepcion Envía error si la ID de exposición es mayor que 3
     * @throws ValorVacioExcepcion Envía error si no se ha puesto un código
     */
    public static String modificarLinea(JTextField a, JTextField b, JTextField c) throws NumeroMayorExcepcion, ValorVacioExcepcion {

        try (Connection conn = Conexion.connect();) {
            PreparedStatement pstmt = null;
            if (b.getText().isEmpty() && c.getText().isEmpty()) {
                correcto = false;
                throw new NullPointerException();
            } else if (a.getText().isEmpty()) {
                correcto = false;
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
            return String.valueOf(pstmt.executeUpdate())+" linea modificada";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Linea modificada";
    }

    /**
     * Inserta una linea en la tabla Exposiciones
     * @param idexpo Primer valor de la tabla, ID de la exposición
     * @param exposicion Segundo valor de la tabla, tipo de exposición
     */
    public static void insertarLineaExpo(int idexpo, String exposicion) {
        //Sintaxis del insert
        sql = "INSERT INTO Exposiciones(idexpo,exposicion) VALUES(?,?)";

        //conectamos a la BD
        try (Connection conn = Conexion.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //añadimos al statement los valores por orden
            pstmt.setInt(1, idexpo);
            pstmt.setString(2, exposicion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("La linea ya existe se añadirá la siguiente si existe.");
        }
    }

    /**
     * Inserta una linea en la tabla Plantas
     * @param codigo Primer valor de la tabla, código de la planta
     * @param nombre Segundo valor de la tabla, nombre de la planta
     * @param idExpo Tercer valor de la tabla, ID de la exposición
     */
    public static void insertarLineaPlantas(int codigo, String nombre, int idExpo) {
        //Sintaxis del insert
        sql = "INSERT INTO Plantas(codigo,nombre,idexpo) VALUES(?,?,?)";

        //conectamos a la BD
        try (Connection conn = Conexion.connect();
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

}
