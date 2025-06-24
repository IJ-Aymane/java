package com.example.javafx_ghilani.util;

import com.example.javafx_ghilani.model.Eleve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {

    public static ObservableList<Eleve> loadStudentsFromXML(String filePath) throws ParserConfigurationException, IOException, SAXException {
        List<Eleve> students = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("eleve");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element eleveElement = (Element) nodeList.item(i);
            Eleve eleve = new Eleve(
                    Integer.parseInt(eleveElement.getElementsByTagName("id").item(0).getTextContent()),
                    eleveElement.getElementsByTagName("code").item(0).getTextContent(),
                    eleveElement.getElementsByTagName("nom").item(0).getTextContent(),
                    eleveElement.getElementsByTagName("prenom").item(0).getTextContent(),
                    Integer.parseInt(eleveElement.getElementsByTagName("niveau").item(0).getTextContent()),
                    eleveElement.getElementsByTagName("codeFil").item(0).getTextContent()
            );
            students.add(eleve);
        }
        return FXCollections.observableArrayList(students);
    }

    public static void saveStudentsToXML(ObservableList<Eleve> students, String filePath) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element rootElement = document.createElement("students");
        document.appendChild(rootElement);

        for (Eleve eleve : students) {
            Element eleveElement = document.createElement("eleve");
            eleveElement.appendChild(createElement(document, "id", String.valueOf(eleve.getId())));
            eleveElement.appendChild(createElement(document, "code", eleve.getCode()));
            eleveElement.appendChild(createElement(document, "nom", eleve.getNom()));
            eleveElement.appendChild(createElement(document, "prenom", eleve.getPrenom()));
            eleveElement.appendChild(createElement(document, "niveau", String.valueOf(eleve.getNiveau())));
            eleveElement.appendChild(createElement(document, "codeFil", eleve.getCodeFil()));
            rootElement.appendChild(eleveElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private static Element createElement(Document document, String tagName, String value) {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(value));
        return element;
    }

    public static void exportToXML(ObservableList<Eleve> students) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                saveStudentsToXML(students, file.getAbsolutePath());
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<Eleve> importFromXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                return loadStudentsFromXML(file.getAbsolutePath());
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }
        return FXCollections.observableArrayList();
    }
}
