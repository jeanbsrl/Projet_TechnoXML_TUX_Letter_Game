/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open  the template in the editor.
 */
package game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.w3c.dom.*;

/**
 *
 * @author tom
 */
public class Partie {

    private final String mot;
    private final String date;
    private final int niveau;
    private int trouve;
    private int temps;
    private final int tempsMax;

    public Partie(String mot, int niveau) {
        this.mot = mot;
        // Récupération de la date du jour
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(Calendar.getInstance().getTime());
        this.niveau = niveau;
        //temps initialisé à -1 et sera mis à jour avec une valeur positive quand la partie sera finie
        this.temps = -1;
        this.tempsMax = Jeu.limiteChrono(niveau);
    }

    public Partie(Element partieElt) {
        this.date = partieElt.getAttributes().item(0).getTextContent(); // à changer car surement pas bon
        this.mot = partieElt.getElementsByTagName("mot").item(0).getTextContent();
        this.niveau = Integer.parseInt(partieElt.getElementsByTagName("mot").item(0).getAttributes().item(0).getTextContent());
        this.tempsMax = Jeu.limiteChrono(niveau);
        
        if (partieElt.getAttributes().item(1) != null) {
            String leTrouve = partieElt.getAttributes().item(1).getTextContent();
            this.trouve = Integer.parseInt(leTrouve.substring(0, leTrouve.length()-1));
            this.temps = Jeu.limiteChrono(niveau);
        }else {
            this.temps = Integer.parseInt(partieElt.getElementsByTagName("temps").item(0).getTextContent());
            this.trouve = 100;
        }
        
    }

    public String getMot() {
        return mot;
    }

    /**
     * Met à jour le pourcentage d'avancement dans la recherche des lettres
     *
     * @param nbLettresRestantes le nombre de lettres qu'il reste à Tux à
     * trouver
     */
    public void setTrouve(int nbLettresRestantes) {
        int nbLettresTrouvees = mot.length() - nbLettresRestantes;
        LanceurDeJeu.log.log(Level.INFO, String.format("Nombre de lettres trouvées : %d", nbLettresTrouvees));
        int oldTrouve = trouve;
        trouve = nbLettresTrouvees * 100 / mot.length();
        if (trouve != oldTrouve) {
            LanceurDeJeu.log.log(Level.INFO, String.format("Mot trouvé à %d%%", trouve));
        }

    }

    public int getTrouve() {
        return trouve;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getTemps() {
        return temps;
    }

    public int getTempsMax() {
        return tempsMax;
    }
    
    public int getNiveau() {
        return niveau;
    }

    /**
     * Crée un Element partie avec les informations de cette partie
     * @param doc 
     * @return Element
     */
    public Element getPartie(Document doc) {
        Element partie = doc.createElement("partie");
        partie.setAttribute("date", date);
        
        if (getTrouve() < 100) {
            partie.setAttribute("trouvé", Integer.toString(getTrouve()) + "%");
        }else{
            Element tempsElt = doc.createElement("temps");
            tempsElt.setTextContent(String.valueOf(getTemps()));
            partie.appendChild(tempsElt);
        }
        
        Element motElt = doc.createElement("mot");
        motElt.setAttribute("niveau", Integer.toString(getNiveau()));
        motElt.setTextContent(getMot());
        
        partie.appendChild(motElt);

        return partie;
    }

    @Override
    public String toString() {
        return "Date : " + date
                + "\nMot : " + mot
                + "\nNiveau : " + niveau
                + "\nTrouvé à " + trouve + "% en " + temps + " secondes.";
    }
}
