package com.example.javafx_ghilani.util;

import com.example.javafx_ghilani.model.Eleve;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {
    private static final String XML_FILE_PATH = "D:\\ENSIT.xml";

    public static void archiveDeletedEleve(Eleve eleve) {
        try {
            File xmlFile = new File(XML_FILE_PATH);
            Document doc;
            Element rootElement;

            if (xmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(xmlFile);
                rootElement = doc.getDocumentElement();
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.newDocument();
                rootElement = doc.createElement("DeletedStudents");
                doc.appendChild(rootElement);
            }

            Element studentElement = doc.createElement("Student");

            Element codeElement = doc.createElement("Code");
            codeElement.appendChild(doc.createTextNode(eleve.getCode()));
            studentElement.appendChild(codeElement);

            Element nomElement = doc.createElement("Nom");
            nomElement.appendChild(doc.createTextNode(eleve.getNom()));
            studentElement.appendChild(nomElement);

            Element prenomElement = doc.createElement("Prenom");
            prenomElement.appendChild(doc.createTextNode(eleve.getPrenom()));
            studentElement.appendChild(prenomElement);

            Element niveauElement = doc.createElement("Niveau");
            niveauElement.appendChild(doc.createTextNode(String.valueOf(eleve.getNiveau())));
            studentElement.appendChild(niveauElement);

            Element filiereElement = doc.createElement("Filiere");
            filiereElement.appendChild(doc.createTextNode(eleve.getCodeFil()));
            studentElement.appendChild(filiereElement);

            Element dateElement = doc.createElement("DeletedDate");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateElement.appendChild(doc.createTextNode(now.format(formatter)));
            studentElement.appendChild(dateElement);

            rootElement.appendChild(studentElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Eleve> getDeletedEleves() {
        List<Eleve> deletedEleves = new ArrayList<>();
        try {
            File xmlFile = new File(XML_FILE_PATH);
            if (!xmlFile.exists()) {
                return deletedEleves;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            NodeList studentList = doc.getElementsByTagName("Student");

            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);
                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;

                    Eleve eleve = new Eleve();
                    eleve.setCode(studentElement.getElementsByTagName("Code").item(0).getTextContent());
                    eleve.setNom(studentElement.getElementsByTagName("Nom").item(0).getTextContent());
                    eleve.setPrenom(studentElement.getElementsByTagName("Prenom").item(0).getTextContent());
                    eleve.setNiveau(Integer.parseInt(studentElement.getElementsByTagName("Niveau").item(0).getTextContent()));
                    eleve.setCodeFil(studentElement.getElementsByTagName("Filiere").item(0).getTextContent());

                    deletedEleves.add(eleve);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deletedEleves;
    }

    public static void removeFromArchive(String codeEleve) {
        try {
            File xmlFile = new File(XML_FILE_PATH);
            if (!xmlFile.exists()) {
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            NodeList studentList = doc.getElementsByTagName("Student");

            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);
                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;
                    String code = studentElement.getElementsByTagName("Code").item(0).getTextContent();

                    if (code.equals(codeEleve)) {
                        studentNode.getParentNode().removeChild(studentNode);
                        break;
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}