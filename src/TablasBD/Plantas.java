/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablasBD;

/**
 *
 * @author ecollazodominguez
 */
public class Plantas {
    private int codigo;
    private String nombre;
    private int idExpo;

    public Plantas(int codigo, String nombre, int idExpo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.idExpo = idExpo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdExpo() {
        return idExpo;
    }

    @Override
    public String toString() {
        return codigo + "   " + nombre+"\n";
    }
    
    
}
