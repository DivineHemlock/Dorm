package com.airbyte.dorm.common;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.util.concurrent.ExecutionException;

public class PageNumber extends PdfPageEventHelper {
    private PdfTemplate t;
    private Image total;

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (Exception de) {
            de.getMessage();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer);
    }

    private void addHeader(PdfWriter writer) {
        PdfPTable header = new PdfPTable(1);
        try {
            // set defaults
            header.setWidths(new int[]{100});
            header.setTotalWidth(525);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(65);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.WHITE);


            Image logo = Image.getInstance(PageNumber.class.getResource("/saadat.jpg"));
            header.addCell(logo);


        } catch (Exception de) {
            de.getMessage();
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 12)),
                totalWidth, 6, 0);
    }
}
