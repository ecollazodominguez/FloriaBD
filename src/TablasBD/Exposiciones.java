/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablasBD;

/**
 *
 * @author Mirroriced
 */
public class Exposiciones {
    private int idExpo;
    private String exposicion;

    public Exposiciones(int idExpo, String exposicion) {
        this.idExpo = idExpo;
        this.exposicion = exposicion;
    }

    public int getIdExpo() {
        return idExpo;
    }

    public String getExposicion() {
        return exposicion;
    }
    
    
}
