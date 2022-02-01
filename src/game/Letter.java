/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.advanced.EnvNode;

/**
 *
 * @author tom
 */
public final class Letter extends EnvNode {

    private char lettre;
    protected static final double LETTER_SCALE = 4.0;
    protected static final double LETTER_Y = LETTER_SCALE * 1.1;
    
    public Letter(char l, double x, double y, double z) {
        lettre = l;
        setScale(LETTER_SCALE);
        
        setX(x);// positionnement au milieu de la largeur de la room
        setY(y); // positionnement en hauteur basé sur la taille de Letter
        setZ(z); // positionnement au milieu de la profondeur de la room
        setLetterTexture(l);
        setModel("models/letter/cube.obj");
    }
    
    public Letter(char l, double x, double z) {
        this(l, x, LETTER_Y, z);
    }
    
    

    
    public char getLettre(){
        return lettre;
    }

    /**
     * Choisi la texture de la lettre en fonction de sa valeur
     * @param letter 
     */
    public void setLetterTexture(char letter){
        switch(letter){
            case 'à':
                setTexture("models/letter/a_grave.png");
                break;
            case 'â':
                setTexture("models/letter/a_circonflexe.png");
                break;
            case 'ç':
                setTexture("models/letter/c_cedille.png");
                break;
            case 'é':
                setTexture("models/letter/e_aigu.png");
                break;
            case 'ê':
                setTexture("models/letter/e_circonflexe.png");
                break;
            case 'è':
                setTexture("models/letter/e_grave.png");
                break;  
            case 'ë':
                setTexture("models/letter/e_trema.png");
                break;
            case 'ï':
                setTexture("models/letter/i_trema.png");
                break;
            case 'î':
                setTexture("models/letter/i_circonflexe.png");
                break;
            case 'ô':
                setTexture("models/letter/o_circonflexe.png");
                break;
            case 'ù':
                setTexture("models/letter/u_grave.png");
                break;
            case 'û':
                setTexture("models/letter/u_circonflexe.png");
                break;
            case 'ü':
                setTexture("models/letter/u_trema.png");
                break;
            case '-':
                setTexture("models/letter/tiret.png");
                break;
            case ' ':
                setTexture("models/letter/cube.png");
                break;
            default:
                setTexture("models/letter/"+letter+".png");
                break;
        }
    }

}
