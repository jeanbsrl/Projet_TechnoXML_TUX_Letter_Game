/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author tom
 */
public class Room {
    
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;

    public Room() {
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document xmlDocument = db.parse(Jeu.PATH_TO_XML + "plateau.xml");

            depth = Integer.parseInt(xmlDocument.getElementsByTagName("depth").item(0).getTextContent());
            height = Integer.parseInt(xmlDocument.getElementsByTagName("height").item(0).getTextContent());
            width = Integer.parseInt(xmlDocument.getElementsByTagName("width").item(0).getTextContent());
            
            textureNorth = xmlDocument.getElementsByTagName("textureNorth").item(0).getTextContent();
            textureEast = xmlDocument.getElementsByTagName("textureEast").item(0).getTextContent();
            textureWest = xmlDocument.getElementsByTagName("textureWest").item(0).getTextContent();
            textureBottom = xmlDocument.getElementsByTagName("textureBottom").item(0).getTextContent();
            textureTop = null;
            textureSouth = null;
            
        }catch(SAXException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }
        
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTextureBottom() {
        return textureBottom;
    }


    public String getTextureNorth() {
        return textureNorth;
    }


    public String getTextureEast() {
        return textureEast;
    }

    public String getTextureWest() {
        return textureWest;
    }

    public String getTextureTop() {
        return textureTop;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public String getTextureSouth() {
        return textureSouth;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }
    
    

}


