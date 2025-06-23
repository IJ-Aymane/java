package com.example.javafx_ghilani.util;

import com.example.javafx_ghilani.model.Eleve;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class PDFUtil {

    public static void generateDeletedStudentsReport(List<Eleve> deletedEleves, String filePath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Rapport des Étudiants Supprimés", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Current date
            Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Paragraph date = new Paragraph("Date de génération: " + java.time.LocalDate.now(), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);

            if (deletedEleves.isEmpty()) {
                Paragraph noData = new Paragraph("Aucun étudiant supprimé trouvé.", dateFont);
                noData.setAlignment(Element.ALIGN_CENTER);
                document.add(noData);
            } else {
                // Table
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Header
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                PdfPCell headerCell;

                headerCell = new PdfPCell(new Phrase("Code", headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);

                headerCell = new PdfPCell(new Phrase("Nom", headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);

                headerCell = new PdfPCell(new Phrase("Prénom", headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);

                headerCell = new PdfPCell(new Phrase("Niveau", headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);

                headerCell = new PdfPCell(new Phrase("Filière", headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);

                // Data rows
                Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
                for (Eleve eleve : deletedEleves) {
                    table.addCell(new Phrase(eleve.getCode(), cellFont));
                    table.addCell(new Phrase(eleve.getNom(), cellFont));
                    table.addCell(new Phrase(eleve.getPrenom(), cellFont));
                    table.addCell(new Phrase(String.valueOf(eleve.getNiveau()), cellFont));
                    table.addCell(new Phrase(eleve.getCodeFil(), cellFont));
                }

                document.add(table);

                // Summary
                Paragraph summary = new Paragraph("Total des étudiants supprimés: " + deletedEleves.size(), dateFont);
                summary.setSpacingBefore(20);
                document.add(summary);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}