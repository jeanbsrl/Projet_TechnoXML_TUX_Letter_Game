/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.LinkedHashSet;
import java.util.logging.Level;

/**
 *
 * @author tom
 */
public class JeuDevineLeMotOrdre extends Jeu {

    private int nbLettresRestantes;
    private LinkedHashSet<Letter> setLettresTrouvees;
    private String motTrouve;
    private int nbErreurs;
    private Chronometre chrono;

    public JeuDevineLeMotOrdre() {
        super();
    }

    /**
     * Indique si Tux a trouvé la prochaine lettre du mot
     *
     * @return true si Tux a trouvé la lettre, false sinon
     */
    private Letter tuxTrouveLettre(int niveau) {
        Letter lettreTrouvee = null;
        if (!getLettresLetter().isEmpty()) {
            lettreTrouvee = collision(niveau);
        }
        return lettreTrouvee;
    }

    private void setNbErreurs(Partie partie) {
        int nbErr;
        if (partie.getMot().length() == motTrouve.length()) {
            nbErr = 0;
            for (int i = 0; i < partie.getMot().length(); i++) {
                if (partie.getMot().charAt(i) != motTrouve.charAt(i)) {
                    nbErr++;
                }
            }
        } else {
            nbErr = partie.getMot().length() - motTrouve.length();
            for (int i = 0; i < motTrouve.length(); i++) {
                if (partie.getMot().charAt(i) != motTrouve.charAt(i)) {
                    nbErr++;
                }
            }
        }

        nbErreurs = nbErr;
    }

    /**
     * Initialise le nombre de lettres restantes à trouver et le chronomètre
     *
     * @param partie
     */
    @Override
    protected void demarrePartie(Partie partie) {
        nbLettresRestantes = getLettresLetter().size();
        setLettresTrouvees = new LinkedHashSet<>();
        motTrouve = "";
        chrono = new Chronometre(Jeu.limiteChrono(partie.getNiveau()));
        chrono.start();
    }

    @Override
    protected void appliqueRegles(Partie partie) {
        Letter lettreTrouvee = tuxTrouveLettre(partie.getNiveau());

        if (lettreTrouvee != null) {
            String l = ((Character) lettreTrouvee.getLettre()).toString();
            motTrouve = motTrouve.concat(l);
            nbLettresRestantes--;
            setLettresTrouvees.add(lettreTrouvee);
            partie.setTrouve(nbLettresRestantes);
        }
        if (nbLettresRestantes == 0) {
            LanceurDeJeu.log.log(Level.INFO, "Plus de lettres à trouver");
        }
        if (!chrono.remainsTime()) {
            LanceurDeJeu.log.log(Level.INFO, "Temps écoulé");
            partie.setTemps(chrono.getSeconds());
        } else {
            if (chrono.getCurrentMilliseconds() % 1000 == 0) {
                LanceurDeJeu.log.log(Level.INFO, String.format("Temps : %d", chrono.getTimeSpent()));
            }
        }
    }

    @Override
    protected void terminePartie(Partie partie) {
        chrono.stop();
        partie.setTemps((int) chrono.getTimeSpent());
        getLettresChar().clear();
        getLettresLetter().clear();
        getTux().setX(getMainRoom().getWidth() / 2.0);
        getTux().setZ(getMainRoom().getDepth() - Tux.TUX_SCALE - 10);
        getTux().setTexture("models/tux/tux_happy.png");
        getTux().setRotateY(0);

        setNbErreurs(partie);
        if (partie.getTrouve() == 100) {
            if (nbErreurs == 0) {
                partieGagnee(partie);
            } else {
                partieTermineeAvecErreurs(partie);
            }
        } else {
            partieNonTerminee(partie);
        }
    }

    /**
     * Affiche les résultats d'une partie gagnée
     * @param partie 
     */
    public void partieGagnee(Partie partie) {
        menuText.getText("Bravo").display(1.5);
        menuText.getText("TrouvéMot").display();
        menuText.getText("MotEtNiveau").modifyTextAndDisplay(motTrouve + " (niveau " + partie.getNiveau() + ")");
        menuText.getText("TempsTrouveMot").modifyTextAndDisplay("en " + partie.getTemps() + " secondes");
        Chronometre chronoFin = new Chronometre(7);
        chronoFin.start();
        while (chronoFin.remainsTime()) {
            deplaceTuxVictoire();
        }
        chronoFin.stop();
        menuText.getText("Bravo").clean();
        menuText.getText("TrouvéMot").clean();
        menuText.getText("MotEtNiveau").clean();
        menuText.getText("TempsTrouveMot").clean();
    }

    /**
     * Affiche les résultats d'une partie où toutes les lettres ont été récupérées mais avec des erreurs
     * @param partie 
     */
    public void partieTermineeAvecErreurs(Partie partie) {
        menuText.getText("Presque").display(1.5);
        menuText.getText("TrouvéMot").display();
        menuText.getText("MotEtNiveauErreur").modifyTextAndDisplay(String.format("%s au lieu de %s (niveau %d)", motTrouve, partie.getMot(), partie.getNiveau(), nbErreurs));
        menuText.getText("NbErreurs").modifyTextAndDisplay(String.format("Nombre d'erreur(s) : %d", nbErreurs));
        menuText.getText("TempsTrouveMot").modifyTextAndDisplay("en " + partie.getTemps() + " secondes");
        getEnv().advanceOneFrame();
        Chronometre chronoFin = new Chronometre(7);
        deplaceTuxDefaite();
        chronoFin.start();
        while (chronoFin.remainsTime());
        chronoFin.stop();
        menuText.getText("Presque").clean();
        menuText.getText("TrouvéMot").clean();
        menuText.getText("MotEtNiveauErreur").clean();
        menuText.getText("NbErreurs").clean();
        menuText.getText("TempsTrouveMot").clean();
    }

    /**
     * Affiche les résultats d'une partie où toutes les lettres n'ont pas été récupérées
     * @param partie 
     */
    public void partieNonTerminee(Partie partie) {
        partie.setTrouve(nbErreurs);
        menuText.getText("TempsEcoulé").display(1.5);
        menuText.getText("TrouvéMot").display();
        menuText.getText("TauxMotTrouvé").modifyTextAndDisplay(String.format("%s (niveau %d) à %d%%", partie.getMot(), partie.getNiveau(), partie.getTrouve()));
        Chronometre chronoFin = new Chronometre(6);
        chronoFin.start();
        deplaceTuxDefaite();
        while (chronoFin.remainsTime());
        chronoFin.stop();
        menuText.getText("TempsEcoulé").clean();
        menuText.getText("TrouvéMot").clean();
        menuText.getText("TauxMotTrouvé").clean();
    }
    
    /**
     * Animation de tux lorsque la partie à été gagnée
     */
    public void deplaceTuxVictoire() {
        double max = 4.0;
        double min = -0.8;
        double pas = 0.3;
        for (double i = max; i > min; i -= pas) {
            getTux().deplaceY(Math.exp(i) * 0.05);
            getEnv().advanceOneFrame();
        }
        for (double i = min; i < max; i += pas) {
            getTux().deplaceY(-Math.exp(i) * 0.05);
            getEnv().advanceOneFrame();
        }
    }

    /**
     * Animation de tux lorsque la partie à été perdue
     */
    public void deplaceTuxDefaite() {
        getTux().setRotateX(270);
        getTux().setRotateY(90);
        getEnv().advanceOneFrame();
    }
}
