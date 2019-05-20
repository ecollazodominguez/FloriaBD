/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package floriabd;

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
        BaseDatos flor = new BaseDatos("Floria");
        int opcion = JOptionPane.showConfirmDialog(null, "¿Conectar a la Base de datos?", "FloriaDB", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opcion == JOptionPane.YES_OPTION){
        flor.setVisible(true);
        flor.setDefaultCloseOperation(3);
        }else{
        System.exit(0);
        }
    }

    
}
