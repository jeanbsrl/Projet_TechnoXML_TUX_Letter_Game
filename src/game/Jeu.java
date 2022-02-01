/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.logging.Level;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author gladen
 */
public abstract class Jeu {

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    private final Env env;
    private Tux tux;
    private final Room mainRoom;
    private final Room menuRoom;
    private Profil profil;
    private final Dico dico;
    private final ArrayList<Character> lettresChar;
    private final LinkedHashSet<Letter> lettresLetter;
    protected EnvTextMap menuText;                         //text (affichage des texte du jeu)

    public static final String PATH_TO_XML = "src/xml/";
    public static final String PATHPROFIL = PATH_TO_XML + "profil_";// à voir
    private static final int MIDDLE_WIDTH_ENV = 310;

    protected Jeu() {

        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        mainRoom = new Room();

        // Instancie lettresCHar et lettresLetter
        lettresChar = new ArrayList<>();
        lettresLetter = new LinkedHashSet<>();
        // Instancie le dico
        //lecture du dictionnaire parsing DOM
        //dico = new Dico(PATH_TO_XML + "dico.xml");

        //lecture du dictionnaire parsing SAX
        dico = new Dico();
        // Instancie une autre Room pour les menus
        menuRoom = new Room();

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // instancie le menuText
        menuText = new EnvTextMap(env);

        // Textes affichés à l'écran
        // textes profil choisi
        menuText.addText("1. Commencer une nouvelle partie", "NouvellePartie", 190, 300);
        menuText.addText("2. Charger une partie non terminée", "PartieExistante", 190, 280);
        menuText.addText("3. Détail des niveaux", "DétailNiveaux", 190, 260);
        menuText.addText("4. Retourner au menu principal", "ProfilRetourMenuPrincipal", 190, 240);
        menuText.addText("5. Quitter le jeu", "QuitterJeu2", 190, 220);
        // textes choix niveau
        menuText.addText("1. Niveau 1", "Niveau1", 280, 300);
        menuText.addText("2. Niveau 2", "Niveau2", 280, 280);
        menuText.addText("3. Niveau 3", "Niveau3", 280, 260);
        menuText.addText("4. Niveau 4", "Niveau4", 280, 240);
        menuText.addText("5. Niveau 5", "Niveau5", 280, 220);
        menuText.addText("6. Niveau 6", "Niveau6", 280, 200);

        // textes détail niveaux                                                             d
        menuText.addText("Niveaux 1, 2 et 3 : Les mots sont respectivement de difficulté\n"
                + "facile, moyenne et élevée. Les lettres sont récupérables dans le bon\n"
                + "ordre uniquement. Le mot est affiché en entier pendant 5 secondes avant\n"
                + "le début de la partie.\n\n"
                + "Niveaux 4 et 5 : Les mots sont respectivement de difficulté moyenne et\n"
                + "élevée. Les lettres sont récupérables dans n'importe quel ordre (attention\n"
                + "aux erreurs !). Le mot est affiché en entier pendant 5 secondes avant le\n"
                + "début de la partie.\n\n"
                + "Niveau 6 : Les mots sont de difficulté facile-moyenne. Les lettres sont\n"
                + "récupérables dans n'importe quel ordre (attention aux erreurs !). Seules\n"
                + "la première et la dernière lettre du mot seront affichées avant le début\n"
                + "de la partie, pendant 5 secondes.\n", "DétailNiveau1a6", 60, 430);

        // textes création profil
        menuText.addText("Nom de joueur : ", "CreationNomJoueur", 190, 300);
        menuText.addText("Avatar : ", "CreationAvatarJoueur", 190, 280);
        menuText.addText("Anniversaire (yyyy-mm-dd): ", "CreationAnniversaireJoueur", 190, 260);
        menuText.addText("Nom déjà utilisé", "NomProfilIndisponible", 220, 260);
        menuText.addText("Format incorrect", "FormatDateIncorrect", 220, 220);

        // textes choix profil
        menuText.addText("Saisissez votre nom de joueur : ", "SaisirNomJoueur", 190, 300);
        menuText.addText("Profil introuvable", "ErreurChargementProfil", 220, 300);

        // texte affichage profil
        menuText.addText("", "InfosJoueur", 5, 450);

        // textes menu principal
        menuText.addText("1. Charger un profil de joueur existant", "ChargerProfilExistant", 190, 300);
        menuText.addText("2. Créer un nouveau joueur", "NouveauJoueur", 190, 280);
        menuText.addText("3. Quitter le jeu", "QuitterJeu1", 190, 260);

        // texte choix parties sauvegardées
        menuText.addText("Quelle partie souhaitez vous charger ?", "ChoixPartieCharge", 120, 350);
        menuText.addText("Numéro de partie saisi incorrect", "NuméroPartieIncorrect", 140, 300);
        menuText.addText("Vous n'avez aucune partie sauvegardée", "AucunePartieSauvegardee", 120, 300);

        // textes fin partie 
        menuText.addText("Bravo !", "Bravo", 280, 350);
        menuText.addText("Presque !", "Presque", 270, 350);
        menuText.addText("Temps écoulé !", "TempsEcoulé", 230, 350);
        menuText.addText("Vous avez trouvé le mot ", "TrouvéMot", 230, 300);
        menuText.addText("", "MotEtNiveau", 250, 280);
        menuText.addText("", "TauxMotTrouvé", 220, 280);
        menuText.addText("", "MotEtNiveauErreur", 220, 280);
        menuText.addText("", "NbErreurs", 220, 260);
        menuText.addText("", "TempsTrouveMot", 260, 240);
        menuText.addText("Quitter (Echap)", "Echap", 30, 100);

        menuText.addText("BIENVENUE DANS TUX LETTER GAME !", "Bienvenue", 100, 350);
        menuText.addText("CREATION DU PROFIL", "CreationProfil", 200, 350);
        menuText.addText("CHOIX DU PROFIL", "ChoixProfil", 220, 350);
        menuText.addText("CHOIX DE LA PARTIE", "ChoixPartie", 200, 350);
        menuText.addText("CHOIX DU NIVEAU", "ChoixNiveau", 210, 350);

    }

    public ArrayList<Character> getLettresChar() {
        return lettresChar;
    }

    public LinkedHashSet<Letter> getLettresLetter() {
        return lettresLetter;
    }

    public Room getMainRoom() {
        return mainRoom;
    }

    public EnvTextMap getMenuText() {
        return menuText;
    }

    /**
     * Gère le menu principal
     *
     */
    public void execute() {

        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        this.env.setDisplayStr("Au revoir !", 300, 30);
        env.exit();
    }

    protected Env getEnv() {
        return env;
    }

    protected Tux getTux() {
        return tux;
    }

    protected Profil getProfil() {
        return profil;
    }

    // fourni
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("SaisirNomJoueur").display();
        nomJoueur = menuText.getText("SaisirNomJoueur").lire(true);
        menuText.getText("SaisirNomJoueur").clean();
        return nomJoueur;
    }

    // fourni, à compléter
    private MENU_VAL menuJeu() {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);
            // affiche menu
            menuText.getText("ChoixPartie").display(1.5);
            menuText.getText("NouvellePartie").display();
            menuText.getText("PartieExistante").display();
            menuText.getText("DétailNiveaux").display();
            menuText.getText("ProfilRetourMenuPrincipal").display();
            menuText.getText("QuitterJeu2").display();
            menuText.getText("InfosJoueur").modifyTextAndDisplay("Joueur : " + profil.getNom());

            // vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4 || touche == Keyboard.KEY_5)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("ChoixPartie").clean();
            menuText.getText("NouvellePartie").clean();
            menuText.getText("PartieExistante").clean();
            menuText.getText("DétailNiveaux").clean();
            menuText.getText("ProfilRetourMenuPrincipal").clean();
            menuText.getText("QuitterJeu2").clean();
            menuText.getText("InfosJoueur").clean();
            env.advanceOneFrame();
            // restaure la room du jeu
            env.setRoom(menuRoom);

            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------                
                case Keyboard.KEY_1: // choisi un niveau et charge un mot depuis le dico
                    // .......... dico.******
                    // crée un nouvelle partie
                    int niveau = getNiveauMot();

                    // restaure la room du menu
                    env.setRoom(menuRoom);

                    String mot = dico.getMotDepuisListeNiveau(niveau);
                    partie = new Partie(mot, niveau);

                    preparationPartie(mot, niveau);
                    // joue
                    joue(partie);

                    // enregistre la partie dans le profil --> enregistre le profil
                    // .......... profil.******
                    profil.sauvegarder(profil.getNom());

                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 2 : Charger une partie existante
                // -----------------------------------------                
                case Keyboard.KEY_2: // charge une partie existante

                    //touche vaut en entier le choix plus 1
                    ArrayList<Partie> listePartiesNonTerminees = profil.dernieresPartiesNonTerminees();

                    partie = choixPartieSauvegardee(listePartiesNonTerminees);

                    if (partie != null) {
                        // Préparation partie
                        preparationPartie(partie.getMot(), partie.getNiveau());
                        // Joue
                        joue(partie);
                        // Sauvegarde de la partie dans le profil
                        profil.sauvegarder(profil.getNom());
                    }

                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 3 : Détail des niveaux
                // -----------------------------------------                
                case Keyboard.KEY_3:
                    affichageDetailNiveaux();
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;
                // -----------------------------------------
                // Touche 4 : Sortie de ce jeu
                // -----------------------------------------                
                case Keyboard.KEY_4:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 5 : Quitter le jeu
                // -----------------------------------------                
                case Keyboard.KEY_5:
                    playTheGame = MENU_VAL.MENU_SORTIE;
                    break;
                default:
                    playTheGame = MENU_VAL.MENU_SORTIE;
                    break;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    private MENU_VAL menuPrincipal() {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;

        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Bienvenue").display(1.5);
        menuText.getText("ChargerProfilExistant").display();
        menuText.getText("NouveauJoueur").display();
        menuText.getText("QuitterJeu1").display();

        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Bienvenue").clean();
        menuText.getText("ChargerProfilExistant").clean();
        menuText.getText("NouveauJoueur").clean();
        menuText.getText("QuitterJeu1").clean();

        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_1:
                // demande le nom du joueur existant
                menuText.getText("ChoixProfil").display(1.5);
                nomJoueur = getNomJoueur();
                menuText.getText("ChoixProfil").clean();
                // charge le profil de ce joueur si possible
                if (Profil.trouveProfil(nomJoueur)) {
                    profil = new Profil(nomJoueur);
                    choix = menuJeu();
                } else {
                    affichageErreur("ErreurChargementProfil");
                    choix = MENU_VAL.MENU_CONTINUE;
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur

                // demande l'avatar du nouveau joueur
                // demande l'anniversaire du nouveau joueur
                // crée un profil avec le nom d'un nouveau joueur
                profil = creationProfil();
                profil.sauvegarder(profil.getNom());
                choix = menuJeu();
                break;

            // -------------------------------------
            // Touche 3 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_3:
                choix = MENU_VAL.MENU_SORTIE;
                break;
            default:
                break;
        }
        return choix;
    }

    public void joue(Partie partie) {

        env.setRoom(mainRoom);

        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        // On remplit l'arrayList lettres
        chargeLettresMot(partie);
        // On place toutes les lettres dans la room
        placeLettresRoom();

        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        demarrePartie(partie);

        // Boucle de jeu
        Boolean finished = false;
        while (Boolean.FALSE.equals(finished)) {
            // Contrôles globaux du jeu (sortie, ...)
            if (env.getKeyDown(Keyboard.KEY_K) || partie.getTrouve() == 100 || partie.getTemps() != -1) {
                finished = true;
            }

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.deplace();

            // Ici, on applique les regles
            appliqueRegles(partie);

            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }

        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);

        // on ajoute la partie à la liste des parties du profil
        profil.ajoutePartie(partie);

    }

    /**
     * Demande à l'utilisateur les informations pour la création d'un profil
     *
     * @return Profil
     */
    private Profil creationProfil() {
        menuText.getText("CreationProfil").display(1.5);
        menuText.getText("CreationNomJoueur").display();
        String nom = menuText.getText("CreationNomJoueur").lire(true);
        while (Profil.trouveProfil(nom)) {
            affichageErreur("NomProfilIndisponible");
            menuText.getText("CreationNomJoueur").clean();
            menuText.getText("CreationNomJoueur").display();
            nom = menuText.getText("CreationNomJoueur").lire(true);
        }
        menuText.getText("CreationAvatarJoueur").display();
        String avatar = menuText.getText("CreationAvatarJoueur").lire(true);

        menuText.getText("CreationAnniversaireJoueur").display();
        String anniversaire = menuText.getText("CreationAnniversaireJoueur").lire(true);
        while (!anniversaire.matches("\\d{4}-\\d{2}-\\d{2}")) {
            affichageErreur("FormatDateIncorrect");
            menuText.getText("CreationAnniversaireJoueur").clean();
            menuText.getText("CreationAnniversaireJoueur").display();
            anniversaire = menuText.getText("CreationAnniversaireJoueur").lire(true);
        }

        menuText.getText("CreationNomJoueur").clean();
        menuText.getText("CreationAvatarJoueur").clean();
        menuText.getText("CreationAnniversaireJoueur").clean();
        menuText.getText("CreationProfil").clean();

        return new Profil(nom, anniversaire, avatar);
    }

    /**
     * Récupère le niveau de jeu choisi par le joueur
     *
     * @return int
     */
    private int getNiveauMot() {

        int niveau;

        menuText.getText("ChoixNiveau").display(1.5);
        menuText.getText("Niveau1").display();
        menuText.getText("Niveau2").display();
        menuText.getText("Niveau3").display();
        menuText.getText("Niveau4").display();
        menuText.getText("Niveau5").display();
        menuText.getText("Niveau6").display();

        // vérifie qu'une touche 1, 2, 3, 4, 5 ou 6 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3
                || touche == Keyboard.KEY_4 || touche == Keyboard.KEY_5 || touche == Keyboard.KEY_6)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("ChoixNiveau").clean();
        menuText.getText("Niveau1").clean();
        menuText.getText("Niveau2").clean();
        menuText.getText("Niveau3").clean();
        menuText.getText("Niveau4").clean();
        menuText.getText("Niveau5").clean();
        menuText.getText("Niveau6").clean();

        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            case Keyboard.KEY_1:
                niveau = 1;
                break;
            case Keyboard.KEY_2:
                niveau = 2;
                break;
            case Keyboard.KEY_3:
                niveau = 3;
                break;
            case Keyboard.KEY_4:
                niveau = 4;
                break;
            case Keyboard.KEY_5:
                niveau = 5;
                break;
            case Keyboard.KEY_6:
                niveau = 6;
                break;
            default:
                niveau = 1;
                break;
        }
        env.advanceOneFrame();
        return niveau;
    }

    private void affichageDetailNiveaux() {
        menuText.getText("DétailNiveau1a6").display();
        menuText.getText("Echap").display();
        env.advanceOneFrame();
        
        int t = 0;
        while (t != Keyboard.KEY_ESCAPE) {
            t = env.getKey();
            env.advanceOneFrame();
        }
        menuText.getText("DétailNiveau1a6").clean();
        menuText.getText("Echap").clean();
        env.advanceOneFrame();
    }

    /**
     * Affiche l'écran de préparation de la partie, avec le mot à trouver,
     * pendant 5 secondes
     *
     * @param mot
     * @param niveau
     */
    private void preparationPartie(String mot, int niveau) {
        Chronometre chronoMot = new Chronometre(5);
        menuText.addText("Vous devez retrouver le mot suivant : ", "MontreMot", 120, 360);
        menuText.addText(chronoMot.getRemainingTimeString(), "TempsChronoAffichageMot", MIDDLE_WIDTH_ENV, 230);

        affichageMotPreparationPartie(mot, niveau);

        menuText.getText("MontreMot").display(1.5);
        menuText.getText("TempsChronoAffichageMot").display(3.0);
        env.advanceOneFrame();
        chronoMot.start();
        while (chronoMot.remainsTime()) {
            menuText.getText("TempsChronoAffichageMot").modifyTextAndDisplay(chronoMot.getRemainingTimeString());
            env.advanceOneFrame();
        }
        chronoMot.stop();
        menuText.getText("MontreMot").clean();
        menuText.getText("TempsChronoAffichageMot").clean();
        env.advanceOneFrame();
    }

    /**
     * Affiche à l'écran le mot à retrouver
     *
     * @param mot
     * @param niveau
     */
    private void affichageMotPreparationPartie(String mot, int niveau) {
        int debutAffichageRoom = (int) (menuRoom.getWidth() / 2 - (mot.length() / 2 * (Letter.LETTER_SCALE * 2)) + (mot.length() % 2 == 0 ? Letter.LETTER_SCALE : 0));
        for (int i = 0; i < mot.length(); i++) {
            if (niveau == 6) {
                if (i > 0 && i < mot.length() - 1) {
                    env.addObject(new Letter(' ', debutAffichageRoom + i * ((int) Letter.LETTER_SCALE * 2), 20, 40));
                } else {
                    env.addObject(new Letter(mot.charAt(i), debutAffichageRoom + i * ((int) Letter.LETTER_SCALE * 2), 20, 40));
                }
            } else {
                env.addObject(new Letter(mot.charAt(i), debutAffichageRoom + i * ((int) Letter.LETTER_SCALE * 2), 20, 40));
            }
        }
        env.advanceOneFrame();
    }

    /**
     * Affiche les parties chargeables
     *
     * @param listePartiesNonTerminees
     * @return boolean true si un affichage s'est fait (listePartiesNonTerminees
     * non vide), false sinon
     */
    private boolean affichagePartiesNonTerminees(ArrayList<Partie> listePartiesNonTerminees) {
        int i = 1;
        boolean partieSauveardeeExiste = true;
        if (listePartiesNonTerminees.isEmpty()) {
            affichageErreur("AucunePartieSauvegardee");
            partieSauveardeeExiste = false;
        } else {
            menuText.getText("ChoixPartieCharge").display(1.5);
            for (Partie p : listePartiesNonTerminees) {
                menuText.addText(String.format("%d : mot %s (niveau %d), trouvé à %d%%", i, p.getMot(), p.getNiveau(), p.getTrouve()), "ChoixPartie" + p.hashCode(), 180, 330 - i * 20);
                menuText.getText("ChoixPartie" + p.hashCode()).display();
                i++;
            }
        }
        return partieSauveardeeExiste;
    }

    /**
     * Supprime l'affichage des parties chargeables
     *
     * @param listePartiesNonTerminees la liste des parties chargeables
     */
    private void cleanPartiesNonTerminees(ArrayList<Partie> listePartiesNonTerminees) {
        menuText.getText("ChoixPartieCharge").clean();
        listePartiesNonTerminees.stream().forEach(p -> {
            menuText.getText("ChoixPartie" + p.hashCode()).clean();
            menuText.getText("ChoixPartie" + p.hashCode()).destroy();
        });
        
    }

    /**
     * Renvoi la partie que le joueur souhaite charger et réessayer depuis la
     * liste des parties sauvegardées
     *
     * @param listeParties la liste des parties sauvegardées non terminées
     * @return Partie la partie choisie
     */
    private Partie choixPartieSauvegardee(ArrayList<Partie> listeParties) {
        int choix = -1;
        Partie partie = null;

        if (affichagePartiesNonTerminees(listeParties)) {
            while (choix != Keyboard.KEY_1 && choix != Keyboard.KEY_2 && choix != Keyboard.KEY_3
                    && choix != Keyboard.KEY_4 && choix != Keyboard.KEY_5 && choix != Keyboard.KEY_6
                    && choix != Keyboard.KEY_7 && choix != Keyboard.KEY_8 && choix != Keyboard.KEY_ESCAPE) {
                choix = env.getKey();
                env.advanceOneFrame();
            }

            if (choix != Keyboard.KEY_ESCAPE) {
                choix -= 2;
                if (choix >= 0 && choix < listeParties.size()) {
                    partie = new Partie(listeParties.get(choix).getMot(), listeParties.get(choix).getNiveau());
                } else {
                    affichageErreur("NuméroPartieIncorrect");
                }
            }
        }

        cleanPartiesNonTerminees(listeParties);
        return partie;
    }

    /**
     * Affiche un message d'erreur
     *
     * @param erreur le nom du message à afficher
     */
    @SuppressWarnings("empty-statement")
    private void affichageErreur(String erreur) {
        menuText.getText(erreur).display(1.5);
        Chronometre chrono = new Chronometre(2);
        env.advanceOneFrame();
        chrono.start();
        while (chrono.remainsTime());
        chrono.stop();
        menuText.getText(erreur).clean();
    }

    /**
     * Place toutes les lettres contenues de la liste des lettres dans la room
     */
    private void placeLettresRoom() {
        lettresLetter.stream().forEach(env::addObject);
        LanceurDeJeu.log.log(Level.INFO, "Placement lettres terminé");
    }

    /**
     * Indique si la lettre que l'on souhaite placer aux coordonnées passées en
     * paramètres est possible ou non
     *
     * @param x coordonnée X de la lettre (width)
     * @param z coodronnée Z de la lettre (depth)
     * @return true si on peut placer la lettre à ces coordonnées, false sinon
     */
    private boolean lettrePlacementOk(double x, double z) {
        boolean placementOk = true;

        // Zone dans la quelle on ne peut pas placer une lettre au risque qu'elle en chevauche une autre
        // La base des tuiles de lettre étant un carré, la zone occupée un cercle de rayon l'hypoténuse de la base 
        // Ici on calcule l'hypoténuse, et plus tard on utilisera la méthode de EnvNode distance pour déterminer la zone occupée
        double occupiedArea = Math.sqrt(Math.pow(Letter.LETTER_SCALE * 3.0, 2) * 2.0);
                
        if (((x <= mainRoom.getWidth() / 2.0 + occupiedArea) && (x >= mainRoom.getWidth() / 2.0 - occupiedArea))
                || // La lettre ne doit pas
                ((z <= mainRoom.getDepth() / 2.0 + occupiedArea) && (z >= mainRoom.getDepth() / 2.0 - occupiedArea))
                || // chevaucher Tux
                (x > mainRoom.getWidth() - Letter.LETTER_SCALE * 2)
                || // ne pas déborder coté east
                (x < Letter.LETTER_SCALE * 2)
                || // ne pas déborder coté west
                (z > mainRoom.getDepth() - Letter.LETTER_SCALE * 2)
                || // ne pas déborder coté south
                (z < Letter.LETTER_SCALE * 2)) {   // ne pas déborder coté north

            placementOk = false;
        }

        Iterator<Letter> it = lettresLetter.iterator();
        while (placementOk && it.hasNext()) {
            if (it.next().distance(x, Letter.LETTER_Y, z) <= occupiedArea) { //ne pas chevaucher une autre lettre déjà placée dans la room
                placementOk = false;
            }
        }

        return placementOk;
    }

    /**
     * Place toutes les lettres du mot dans la room
     *
     * @param partie la partie en cours
     */
    private void chargeLettresMot(Partie partie) {
        Random r;
        try {
            r = SecureRandom.getInstanceStrong();
            double randomX;
            double randomZ;
            lettresLetter.clear();
            lettresChar.clear();

            for (char l : partie.getMot().toCharArray()) {
                //on génère aléatoirement l'emplacement de la Lettre
                randomX = mainRoom.getWidth() * r.nextDouble();
                randomZ = mainRoom.getDepth() * r.nextDouble();
                // tant que l'emplacement de la lettre n'est pas correct, on réessaye
                while (!lettrePlacementOk(randomX, randomZ)) {
                    randomX = mainRoom.getWidth() * r.nextDouble();
                    randomZ = mainRoom.getDepth() * r.nextDouble();
                }
                // ajout de la lettre à la liste
                lettresLetter.add(new Letter(l, randomX, randomZ));
                lettresChar.add(l);
                LanceurDeJeu.log.log(Level.INFO, "ajout de " + l + " fait");
            }
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Calcule la distance entre Tux et une lettre
     *
     * @param lettre Letter
     * @return distance entre tux et la lettre
     */
    protected double distance(Letter lettre) {
        return tux.distance(lettre);
    }

    /**
     * Renvoi la lettre avec laquelle Tux est entré en collision
     *
     * @param niveau le niveau du mot, si le niveau est > 4 alors le jeu acccepte la collision entre Tux et n'importe quelle tuile,
     * sinon seulement avec une tuile ayant la bonne lettre dans l'ordre du mot
     * @return Letter la lettre avec laquelle il est entré en collision (null si aucune collision)
     */
    protected Letter collision(int niveau) {
        Letter lettreCollision = null;
        Letter lettre;
        int indiceLettre = 0;
        Iterator<Letter> it = lettresLetter.iterator();

        while (it.hasNext() && lettreCollision == null) {
            lettre = it.next();
            if (distance(lettre) < (tux.getScale() + Letter.LETTER_SCALE) / 1.5) {
                if (niveau > 3) {
                    lettreCollision = lettre;
                    lettresChar.remove(indiceLettre);
                    env.removeObject(lettre);
                    lettresLetter.remove(lettre);
                } else if (lettre.getLettre() == lettresChar.get(0)) {
                    lettreCollision = lettre;
                    lettresChar.remove(0);
                    env.removeObject(lettre);
                    lettresLetter.remove(lettre);
                }
            }
            indiceLettre++;
        }
        return lettreCollision;
    }

    /**
     * Assigne une limite au chronomètre pour la partie en fonction du niveau du
     * mot
     *
     * @param niveau int
     * @return la durée limite du chrono
     */
    protected static int limiteChrono(int niveau) {
        int limite;
        switch (niveau) {
            case 1:
                limite = 20;
                break;
            case 2:
                limite = 23;
                break;
            case 3:
                limite = 25;
                break;
            case 4:
                limite = 30;
                break;
            case 5:
                limite = 45;
                break;
            case 6:
                limite = 60;
                break;
            default:
                limite = 20;
                break;
        }
        return limite;
    }

    protected abstract void demarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);

}
