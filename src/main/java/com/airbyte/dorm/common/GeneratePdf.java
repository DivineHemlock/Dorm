package com.airbyte.dorm.common;

import com.airbyte.dorm.model.Characteristic;
import com.airbyte.dorm.model.enums.MaritalStatus;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class GeneratePdf {



    public static ByteArrayInputStream reportCharacteristic(Characteristic characteristic) {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("saadat.jpg").toURI());

            Document document = new Document(PageSize.A5.rotate());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            document.open();
            Image img = Image.getInstance(path.toAbsolutePath().toString());
            img.scaleAbsolute(100, 100);

            Phrase phrase = new Phrase();
            phrase.add(new Chunk(img, -30, -50));
            document.add(new Paragraph(phrase));

            PdfPTable table = new PdfPTable(3);
            addRows(table, characteristic);

            document.add(table);
            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

//    private static void addTableHeader(PdfPTable table) {
//        Stream.of("وزن : 12.5", "عیار : 24", "قیمت نهایی : 12")
//                .forEach(columnTitle -> {
//                    Font font = new Font(font(), 16, Font.BOLD);
//                    LanguageProcessor languageProcessor = new ArabicLigaturizer();
//                    PdfPCell header = new PdfPCell();
//                    header.setBorderWidth(3);
//                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    header.setVerticalAlignment(Element.ALIGN_CENTER);
//                    header.setPhrase(new Phrase(languageProcessor.process(columnTitle), font));
//                    header.setBorderColor(new BaseColor(255, 255, 255));
//                    table.addCell(header);
//                });
//    }

    private static void addRows(PdfPTable table, Characteristic characteristic) {
        table.addCell(generatePdfCell(concater("کد‌ملی: ", characteristic.getNationalCode())));
        table.addCell(generatePdfCell(concater("نام خانوادگی: ", characteristic.getLastName())));
        table.addCell(generatePdfCell(concater("نام: ", characteristic.getFirstName())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("شماره شناسنامه: ", characteristic.getCertificateNumber())));
        table.addCell(generatePdfCell(concater("تاریخ تولد: ", characteristic.getBirthDate())));
        table.addCell(generatePdfCell(concater("محل صدور: ", characteristic.getPlaceOfIssue())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("دین: ", specifyReligion(characteristic.getReligion()))));
        table.addCell(generatePdfCell(concater("نام پدر: ", characteristic.getFatherName())));
        table.addCell(generatePdfCell(concater("ملیت: ", characteristic.getNationality())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("شماره دانشجویی: ", characteristic.getStudentNumber())));
        table.addCell(generatePdfCell(concater("دانشگاه محل تحصیل: ", characteristic.getUniversity())));
        table.addCell(generatePdfCell(concater("مذهب: ", characteristic.getSubReligion())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("نام و نام خانوادگی همسر: ", characteristic.getSpouseFullName())));
        table.addCell(generatePdfCell(concater("وضعیت تاهل: ", specifyMaterialStatus(characteristic.getMaritalStatus()))));
        table.addCell(generatePdfCell(concater("شغل پدر: ", characteristic.getFatherJob())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("توضیحات بیماری: ", characteristic.getHealthyStatus())));
        table.addCell(generatePdfCell(concater("بیماری خاص: ", characteristic.getHealth() ? "بله" : "خیر")));
        table.addCell(generatePdfCell(concater("شغل همسر: ", characteristic.getSpouseJob())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("تاریخ شروع پذیرش: ",
                TimeConverter.convert(characteristic.getTimePeriod().startDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) == null ? "A" : TimeConverter.convert(characteristic.getTimePeriod().startDate(), TimeConverter.UPDATED_PATTERN_FORMAT)
                )
        ));
        table.addCell(generatePdfCell(concater("تلفن منزل: ", characteristic.getHomeNumber())));
        table.addCell(generatePdfCell(concater("همراه اقامتگر: ", characteristic.getPhoneNumber())));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell(concater("آدرس محل سکونت: ", characteristic.getAddress())));
        table.addCell(generatePdfCell("C"));
        table.addCell(generatePdfCell(concater("تاریخ اتمام پذیرش: ", TimeConverter.convert(characteristic.getTimePeriod().endDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) == null ? "A" : TimeConverter.convert(characteristic.getTimePeriod().endDate(), TimeConverter.UPDATED_PATTERN_FORMAT))));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell("A"));
        table.addCell(generatePdfCell("B"));
        table.addCell(generatePdfCell("C"));

        table.addCell(generatePdfCell("A"));
        table.addCell(generateTitlePdfCell("مدیریت خوابگاه سعادت"));
        table.addCell(generatePdfCell("C"));


    }

    private static String specifyMaterialStatus(MaritalStatus maritalStatus) {
        if (maritalStatus == MaritalStatus.married) {
            return "متاهل";
        }
        else if (maritalStatus == MaritalStatus.single) {
            return "مجرد";
        }
        else if (maritalStatus == MaritalStatus.divorced) {
            return "متارکه";
        }
        return null;
    }

    private static String specifyReligion(String religion) {
        if (religion.equals("islam")) {
            return "اسلام";
        }
        else if (religion.equals("christianity")) {
            return "مسیحیت";
        }
        else if (religion.equals("hinduism")) {
            return "هندوئیسم";
        }
        else if (religion.equals("buddhism")) {
            return "آیین بودایی";
        }
        else if (religion.equals("other")) {
            return "سایر";
        }
        return null;
    }

    private static String concater(String title, String value) {
        return title + value;
    }

    private static PdfPCell generateTitlePdfCell(String field) {
        Font font = new Font(font(), 14, Font.BOLD);
        LanguageProcessor languageProcessor = new ArabicLigaturizer();
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidth(1);
        cell.setBorderColor(new BaseColor(255, 255, 255));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(255, 255, 255));
        cell.setPhrase(new Phrase(languageProcessor.process(field), font));
        return cell;
    }

    private static PdfPCell generatePdfCell(String field) {
        Font font = new Font(font(), 13, Font.NORMAL);
        LanguageProcessor languageProcessor = new ArabicLigaturizer();
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidth(1);
        cell.setBorderColor(new BaseColor(255, 255, 255));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(255, 255, 255));
        cell.setPhrase(new Phrase(languageProcessor.process(field), font));
        return cell;
    }

    public static BaseFont font() {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("B Nazanin.TTF").toURI());
            return BaseFont.createFont(
                    path.toAbsolutePath().toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
