/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author tom
 */
public class Tux extends EnvNode {

    private final Env env;
    private final Room room;
    protected static final double TUX_SCALE = 4.0;
    private static final double TUX_Y = TUX_SCALE * 1.1;
    private static final double TUX_MOVEMENT = 0.9;

    public Tux(Env env, Room room) {
        this.env = env;
        this.room = room;
        setScale(TUX_SCALE);
        setX(room.getWidth() / 2);// positionnement au milieu de la largeur de la room
        setY(TUX_Y); // positionnement en hauteur basé sur la taille de Tux
        setZ(room.getDepth() / 2); // positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux.png");
        setModel("models/tux/tux.obj");
    }

    /**
     * Déplace Tux selon l'axe X (width), si cela est possible
     *
     * @param mouvement le déplacement que Tux essaye d'effectuer, positif vers la droite et négatif vers la gauche
     */
    private void deplaceX(double mouvement) {
        if (getX() + mouvement < room.getWidth() - TUX_SCALE && getX() + mouvement > 0 + TUX_SCALE) {
            setX(getX() + mouvement);
        }
    }
    
    /**
     * Déplace Tux selon l'axe Y (height), si cela est possible
     *
     * @param mouvement le déplacement que Tux essaye d'effectuer, positif vers le haut et négatif vers le bas
     */
    protected void deplaceY(double mouvement) {
        if (getY() + mouvement < room.getHeight()- TUX_SCALE && getY() + mouvement > 0 + TUX_SCALE) {
            setY(getY() + mouvement);
        }
    }

    /**
     * Déplace Tux selon l'axe Z (depth), si cela est possible
     *
     * @param mouvement le déplacement que Tux essaye d'effectuer, positif vers l'arrière et négatif vers l'avant
     */
    private void deplaceZ(double mouvement) {
        if (getZ() + mouvement < room.getDepth() - TUX_SCALE && getZ() + mouvement > 0 + TUX_SCALE) {
            setZ(getZ() + mouvement);
        }
    }

    /**
     * Gère le déplacement de Tux en fonction des touches préssées du clavier
     */
    public void deplace() {
        if ((env.getKeyDown(Keyboard.KEY_D) && env.getKeyDown(Keyboard.KEY_Z)) || (env.getKeyDown(Keyboard.KEY_RIGHT) && env.getKeyDown(Keyboard.KEY_UP))) { // Fleche 'droite' ou D
            // Haut Droit
            setRotateY(135);
            deplaceX(TUX_MOVEMENT);
            deplaceZ(-TUX_MOVEMENT);
        } else if ((env.getKeyDown(Keyboard.KEY_Z) && env.getKeyDown(Keyboard.KEY_Q)) || (env.getKeyDown(Keyboard.KEY_UP) && env.getKeyDown(Keyboard.KEY_LEFT))) { // Fleche 'droite' ou D
            // Haut Gauche
            setRotateY(225);
            deplaceX(-TUX_MOVEMENT);
            deplaceZ(-TUX_MOVEMENT);
        } else if ((env.getKeyDown(Keyboard.KEY_Q) && env.getKeyDown(Keyboard.KEY_S)) || (env.getKeyDown(Keyboard.KEY_LEFT) && env.getKeyDown(Keyboard.KEY_DOWN))) { // Fleche 'droite' ou D
            // Gauche bas
            setRotateY(315);
            deplaceX(-TUX_MOVEMENT);
            deplaceZ(TUX_MOVEMENT);
        } else if ((env.getKeyDown(Keyboard.KEY_S) && env.getKeyDown(Keyboard.KEY_D)) || (env.getKeyDown(Keyboard.KEY_DOWN) && env.getKeyDown(Keyboard.KEY_RIGHT))) { // Fleche 'droite' ou D
            // Bas droit
            setRotateY(45);
            deplaceX(TUX_MOVEMENT);
            deplaceZ(TUX_MOVEMENT);
        } else {
            if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { // Fleche 'haut' ou Z
                // Haut
                setRotateY(180);
                deplaceZ(-TUX_MOVEMENT);
            } 
            if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
                // Gauche
                setRotateY(270);
                deplaceX(-TUX_MOVEMENT);
            }
            if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) { // Fleche 'bas' ou S
                // Bas
                setRotateY(0);
                deplaceZ(TUX_MOVEMENT);
            }
            if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) { // Fleche 'droite' ou D
                // Droite
                setRotateY(90);
                deplaceX(TUX_MOVEMENT);
            }
        }
    }
    
    
}
