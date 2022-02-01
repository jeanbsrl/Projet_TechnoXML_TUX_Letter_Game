/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author tom
 */
public class Dico extends DefaultHandler {

    private static final String DICO_NAME = "dico.xml";

    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    private ArrayList<String> listeNiveau6;
    int niveauMot;
    private boolean inDictionnaire;
    private boolean inMot;
    private String cheminFichierDico;
    private StringBuffer buffer;

    public Dico(String cheminFichierDico) {
        this.cheminFichierDico = cheminFichierDico;
        listeNiveau1 = new ArrayList<>();
        listeNiveau2 = new ArrayList<>();
        listeNiveau3 = new ArrayList<>();
        listeNiveau4 = new ArrayList<>();
        listeNiveau5 = new ArrayList<>();
        listeNiveau6 = new ArrayList<>();

        lireDictionnaireDOM();
        afficheDico();
    }

    public Dico() {
        super();
        lireDictionnaire();
        afficheDico();
    }

    /**
     * Choisi au hasard un mot d'un niveau donné en paramètre
     *
     * @param niveau le niveau du mot
     * @return le mot choisi
     */
    public String getMotDepuisListeNiveau(int niveau) {
        String mot;
        switch (verifieNiveau(niveau)) {
            case 1:
                mot = getMotDepuisListe(listeNiveau1);
                break;
            case 2:
                mot = getMotDepuisListe(listeNiveau2);
                break;
            case 3:
                mot = getMotDepuisListe(listeNiveau3);
                break;
            case 4:
                mot = getMotDepuisListe(listeNiveau4);
                break;
            case 5:
                mot = getMotDepuisListe(listeNiveau5);
                break;
            case 6:
                mot = getMotDepuisListe(listeNiveau6);
                break;
            default:
                mot = getMotDepuisListe(listeNiveau1);
                break;
        }
        return mot;
    }

    /**
     * Parse un document XML pour remplir le dictionnaire, i.e. la liste des
     * mots suivant les niveaux
     *
     * @param path le chemin vers le document XML
     * @param filename le nom du document XML
     */
    public final void lireDictionnaireDOM() {
        try {
            //Analyse et création du DOM du document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document xmlDocument = db.parse(cheminFichierDico);

            //Récupère tous les éléments "mot"
            NodeList listeMots = xmlDocument.getElementsByTagName("mot");

            //Pour chaque élément "mot" récupéré, on récupère son
            //niveau et on ajoute ce mot à la bonne liste
            for (int i = 0; i < listeMots.getLength(); i++) {
                Element mot = (Element) listeMots.item(i);
                ajouteMotADico(Integer.parseInt(mot.getAttribute("niveau")), mot.getTextContent());
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.getStackTrace();
        }

    }

    private void afficheDico() {
        LanceurDeJeu.log.log(Level.INFO, "Affichage du dictionnaire : \n");

        LanceurDeJeu.log.log(Level.INFO, "Mots de niveau 1 :");
        listeNiveau1.forEach(mot -> LanceurDeJeu.log.log(Level.INFO, mot));

        LanceurDeJeu.log.log(Level.INFO, "Mots de niveau 2 :");
        listeNiveau2.forEach(mot -> LanceurDeJeu.log.log(Level.INFO, mot));

        LanceurDeJeu.log.log(Level.INFO, "Mots de niveau 3 :");
        listeNiveau3.forEach(mot -> LanceurDeJeu.log.log(Level.INFO, mot));

        LanceurDeJeu.log.log(Level.INFO, "Mots de niveau 4 :");
        listeNiveau4.forEach(mot -> LanceurDeJeu.log.log(Level.INFO, mot));

        LanceurDeJeu.log.log(Level.INFO, "Mots de niveau 5 :");
        listeNiveau5.forEach(mot -> LanceurDeJeu.log.log(Level.INFO, mot));

        LanceurDeJeu.log.log(Level.INFO, "Mots de niveau 6 :");
        listeNiveau6.forEach(mot -> LanceurDeJeu.log.log(Level.INFO, mot));
    }

    /**
     * Extrait un mot au hasard depuis la liste passée en paramètre
     *
     * @param liste la liste de mots
     * @return le mot
     */
    private String getMotDepuisListe(ArrayList<String> liste) {
        Random rand;
        try {
            rand = SecureRandom.getInstanceStrong();
            int indiceMot = rand.nextInt(liste.size());
            return liste.get(indiceMot);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Dico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Vérifie que le niveau passé en paramètre est bien compris entre 1 et 6,
     * sinon le met à 1 par défault
     *
     * @param niveau
     * @return le niveau
     */
    private int verifieNiveau(int niveau) {
        //niveau 1 par défault
        return (niveau < 1 || niveau > 6 ? 1 : niveau);
    }

    /**
     * Ajoute un mot à la liste adéquate, en fonction du niveau du mot
     *
     * @param niveau le niveau du mot à ajouter
     * @param mot le mot à ajouter
     */
    public void ajouteMotADico(int niveau, String mot) {
        switch (verifieNiveau(niveau)) {
            case 1:
                listeNiveau1.add(mot);
                break;
            case 2:
                listeNiveau2.add(mot);
                break;
            case 3:
                listeNiveau3.add(mot);
                break;
            case 4:
                listeNiveau4.add(mot);
                break;
            case 5:
                listeNiveau5.add(mot);
                break;
            case 6:
                listeNiveau6.add(mot);
                break;
            default:
                break;
        }
    }

    //Parsing SAX
    private void lireDictionnaire() {
        try {
            // création d'une fabrique de parseurs SAX 
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX 
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler 
            File fichier = new File(Jeu.PATH_TO_XML + DICO_NAME);
            DefaultHandler gestionnaire = this;
            parseur.parse(fichier, gestionnaire);

        } catch (ParserConfigurationException pce) {
            LanceurDeJeu.log.log(Level.SEVERE, "Erreur de configuration du parseur");
            LanceurDeJeu.log.log(Level.SEVERE, "Lors de l'appel à newSAXParser()");
        } catch (SAXException se) {
            LanceurDeJeu.log.log(Level.SEVERE, "Erreur de parsing");
            LanceurDeJeu.log.log(Level.SEVERE, "Lors de l'appel à parse()");
        } catch (IOException ioe) {
            LanceurDeJeu.log.log(Level.SEVERE, "Erreur d'entrée/sortie");
            LanceurDeJeu.log.log(Level.SEVERE, "Lors de l'appel à parse()");
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("dictionnaire")) {
            inDictionnaire = true;
            listeNiveau1 = new ArrayList<>();
            listeNiveau2 = new ArrayList<>();
            listeNiveau3 = new ArrayList<>();
            listeNiveau4 = new ArrayList<>();
            listeNiveau5 = new ArrayList<>();
            listeNiveau6 = new ArrayList<>();
        } else if (qName.equals("mot")) {
            inMot = true;
            buffer = new StringBuffer();
            niveauMot = Integer.parseInt(attributes.getValue("niveau"));
        } else {
            LanceurDeJeu.log.log(Level.SEVERE, String.format("Element %s inconnu", qName));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("dictionnaire")) {
            if (!inDictionnaire) {
                LanceurDeJeu.log.log(Level.SEVERE, "Pas dans le dictionnaire !");
            } else {
                inDictionnaire = false;
            }
        } else if (qName.equals("mot")) {
            if (!inMot) {
                LanceurDeJeu.log.log(Level.SEVERE, "Pas dans un mot !");
            } else {
                inMot = false;
                LanceurDeJeu.log.log(Level.INFO, String.format("Ajout du mot %s (niveau %d) au dictionnaire", buffer.toString(), niveauMot));
                ajouteMotADico(niveauMot, buffer.toString());
                buffer = null;
                niveauMot = 0;
            }
        } else {
            LanceurDeJeu.log.log(Level.SEVERE, String.format("Element %s inattendu", qName));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (buffer != null) {
            String word = new String(ch, start, length);
            buffer.append(word);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        LanceurDeJeu.log.log(Level.INFO, "Début du parsing SAX");
    }

    @Override
    public void endDocument() throws SAXException {
        LanceurDeJeu.log.log(Level.INFO, "FIN du parsing SAX");
    }
}
