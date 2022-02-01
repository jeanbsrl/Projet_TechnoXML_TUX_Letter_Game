package game;

// see public class comment below


import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * A utility class that facilitates the manipulation of DOM document, including
 * the evaluation of XPath.
 * <p>
 * Beware: xmlns (default namespace) can not be specified.
 * <p>
 * See inner classes for more information.
 * <p>
 * To simplify usage all methods are static and are declared to throw an
 * indistinct Exception.
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see {@link http://www.gnu.org/licenses/}.
 *
 * @author (C) 2010-2018 Emmanuel Promayon, Universite Joseph Fourier -
 * TIMC-IMAG
 * @coauthor (C) 2017-2018 Nicolas Glade, Universite Grenoble Alpes - TIMC-IMAG
 *
 */
public class XMLUtil {
    /**
     * Inner class to create DOM document with various methods including URL,
     * XSL transformation and files.
     */
    static public class DocumentFactory { //------------------
        /** -----------------
         * build a DOM Document from an XML file.
         *
         * @param fileName name of the input file (have to be XML)
         * @return the corresponding DOM Document
         * @throws java.lang.Exception
         */
        public static Document fromFile(String fileName) throws Exception {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this! // XXXXXX
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document theDocument = builder.parse(new File(fileName));
            return theDocument;
        } //-----------------------
    } // ends DocumentFactory
    
    static public class DocumentTransform {

        /**
         * write a DOM Document in a XML file
         *
         * @param doc the DOM Document to serialize in a file
         * @param outputFileName the file name to write to
         * @throws java.lang.Exception
         */
        public static void writeDoc(Document doc, String outputFileName) throws Exception {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DocumentType dt = doc.getDoctype();
            if (dt != null) {
                String pub = dt.getPublicId();
                if (pub != null) {
                    t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, pub);
                }
                t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dt.getSystemId());
            }
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // NOI18N

            t.setOutputProperty(OutputKeys.INDENT, "yes"); // NOI18N

            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // NOI18N

            Source source = new DOMSource(doc);
            Result result = new StreamResult(new FileOutputStream(outputFileName));
            t.transform(source, result);

        }
    }
}