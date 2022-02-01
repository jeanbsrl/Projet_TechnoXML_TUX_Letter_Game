/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.logging.Logger;



/**
 *
 * @author tom
 */
public class LanceurDeJeu {
    public static Logger log = Logger.getLogger("logger");   //public static Logger log = Logger.getLogger("logger");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Jeu jeu = new JeuDevineLeMotOrdre();
        jeu.execute();
    }
    
}
