package com.example.javafx_ghilani.util;

import com.example.javafx_ghilani.model.Eleve;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.IOException;
import java.util.List;

public class PDFUtil {

    public static void generateStudentsReport(List<Eleve> students, String filePath, String title) throws IOException {
        // Création du PdfWriter avec le chemin du fichier
        PdfWriter writer = new PdfWriter(filePath);

        // Création du PdfDocument à partir du writer
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Création du document
        Document document = new Document(pdfDoc);

        // Titre du rapport
        document.add(new Paragraph(title).setBold().setFontSize(18));

        // Création du tableau avec 5 colonnes (largeurs relatives)
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 1, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes
        table.addHeaderCell(new Cell().add(new Paragraph("Code")));
        table.addHeaderCell(new Cell().add(new Paragraph("Nom")));
        table.addHeaderCell(new Cell().add(new Paragraph("Prénom")));
        table.addHeaderCell(new Cell().add(new Paragraph("Niveau")));
        table.addHeaderCell(new Cell().add(new Paragraph("Filière")));

        // Ajout des données des étudiants
        for (Eleve eleve : students) {
            table.addCell(new Cell().add(new Paragraph(eleve.getCode())));
            table.addCell(new Cell().add(new Paragraph(eleve.getNom())));
            table.addCell(new Cell().add(new Paragraph(eleve.getPrenom())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(eleve.getNiveau()))));
            table.addCell(new Cell().add(new Paragraph(eleve.getCodeFil())));
        }

        // Ajout du tableau au document
        document.add(table);

        // Fermeture du document (écrit dans le fichier)
        document.close();
    }

    public static void generateDeletedStudentsReport(List<Eleve> deletedStudents, String filePath) throws IOException {
        generateStudentsReport(deletedStudents, filePath, "Rapport des Étudiants Supprimés");
    }
}
