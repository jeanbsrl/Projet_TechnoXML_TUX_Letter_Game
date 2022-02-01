/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.*;



/**
 *
 * @author tom
 */
public class Profil {
    
    private String nom;
    private ArrayList<Partie> parties;
    private String avatar;
    private String dateNaissance;
    String path = Jeu.PATHPROFIL; // à changer
    public Document _doc;
    
    
    public Profil(){
    }
    /*public Profil(String nom, String dateNaissance){
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        parties = new ArrayList<>();
    }
    */
   public Profil(String nom, String dateNaissance, String avatar){
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.avatar = avatar;
        parties = new ArrayList<>();
    }
    
    public Profil(String filename){
        _doc = fromXML(filename);
        nom = _doc.getElementsByTagName("nom").item(0).getTextContent();
        dateNaissance = xmlDateToProfileDate(_doc.getElementsByTagName("anniversaire").item(0).getTextContent());
        avatar = _doc.getElementsByTagName("avatar").item(0).getTextContent();
        parties = new ArrayList<>();
        NodeList partiesNode = _doc.getElementsByTagName("partie");
        for (int i = 0; i < partiesNode.getLength(); i++) {
            parties.add(new Partie((Element) partiesNode.item(i)));
        }
    }
    
    
    private void toXML(String filename){
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, this.path + filename + ".xml");
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Document fromXML(String filename){
        try{
            return XMLUtil.DocumentFactory.fromFile(this.path + filename + ".xml"); // changer this.path
        } catch (Exception e){
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    
    /**
     * Sauvegarde un profil dans son document xml
     * 2 cas de figure :
     *  - si le profil n'existe pas, c'est que l'utilisateur est en train de créer son profil, donc crée le document xml et le remplit avec ses informations
     *  - si le profil existe, c'est que l'on veut sauvegarder une partie, donc enregistre la partie dans le document xml
     * @param filename le nom du document xml du profil
     */
    public void sauvegarder(String filename){ // à faire
        if (_doc == null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                _doc = dBuilder.newDocument();

                Element profil = _doc.createElement("profil");
                profil.setAttribute("xmlns", "http://myGame/tux");
                profil.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
                profil.setAttribute("xsi:schemaLocation", "http://myGame/tux profil.xsd");

                Element nomElt = _doc.createElement("nom");
                nomElt.setTextContent(nom);
                profil.appendChild(nomElt);

                if (this.avatar != null) {
                    Element avatarElt = _doc.createElement("avatar");
                    avatarElt.setTextContent(String.format("%s.jpg",avatar));
                    profil.appendChild(avatarElt);
                }

                Element anniversaireElt = _doc.createElement("anniversaire");
                anniversaireElt.setTextContent(dateNaissance);
                profil.appendChild(anniversaireElt);

                Element partiesElt = _doc.createElement("parties");

                profil.appendChild(partiesElt);
                _doc.appendChild(profil);
            } catch (ParserConfigurationException | DOMException e) {
                e.printStackTrace();
            }
        } else {
            LanceurDeJeu.log.log(Level.INFO, "ajout d'une partie");
            if(parties.size()>0){
                Element eltPartie = _doc.createElement("partie");
                eltPartie = parties.get(parties.size() - 1).getPartie(_doc);
                Node partiesNode = _doc.getElementsByTagName("parties").item(0);
                partiesNode.appendChild(eltPartie);
            } else {
                LanceurDeJeu.log.log(Level.SEVERE, "Attention quelque chose c'est mal passé, ya pas de parties mais vous voulez en sauvegarder.");
            }
        }
        toXML(filename);
        LanceurDeJeu.log.log(Level.INFO, String.format("la partie est sauvegardé dans le fichier %s%s.xml", path, filename));
    }
    
    public String getNom() {
        return this.nom;
    }
    
    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }
    
    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }
    
    /**
     * Ajoute une partie à la liste des parties d'un profil
     * @param partie 
     */
    public void ajoutePartie(Partie partie){
        parties.add(partie);
    }
    
    /**
     * Récupère la liste des dernières parties non terminées d'un joueur (max 8 parties)
     * @return ArrayList<Partie> la liste des parties non terminées
     */
    public ArrayList<Partie> dernieresPartiesNonTerminees(){
        ArrayList<Partie> listeParties = new ArrayList<>();
        int i = parties.size()-1;
        while(listeParties.size() < 8 && i >= 0){
            if(parties.get(i).getTrouve() != 100){
                listeParties.add(parties.get(i));
            }
            i--;
        }
        return listeParties;
    }
    
    /**
     * Indique si un profil est déjà enregistré ou non
     * @param nomJoueur le nom du joueur dont on souhaite chercher profil
     * @return boolean true si le profil existe, false sinon
     */
    public static boolean trouveProfil(String nomJoueur){
        try{
            return (XMLUtil.DocumentFactory.fromFile("src/xml/profil_" + nomJoueur + ".xml") != null);
        } catch (Exception e){
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "Nom : " + nom +
                "\nAvatar : " + avatar +
                "\nAnniversaire : " + dateNaissance +
                "\nNombre de parties : " + (parties != null ? parties.size() : "null");
                   
    }
}

