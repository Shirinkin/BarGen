package model;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



public class Barcode {

    //Где найти потом пдфку
    public static final String DEST = "C:/barcodes/barcodes.pdf";

    public String[] codeLocWith = new String[6];
    public String[] codeLocWithout = new String[6];
    public String ryad;
    public String loc;
    public String[] etage = new String[]{"6","5","4","3","2","1"};
    public String place;

    /**
     * Шрифт для русской локализации
     * @return
     */
    public BaseFont getFont() {
        BaseFont times = null;
        try {
            times = BaseFont.createFont("c:/windows/fonts/times.ttf", "cp1251", BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * Получаем строку таблицы с местом
     * Место - последний символ в code
     */
    public PdfPCell getPlaceCell(String place) {
        PdfPCell cell1 = new PdfPCell();
        cell1.setRotation(90);
        Phrase phrase = new Phrase(place);
        phrase.setFont(new Font(Font.FontFamily.HELVETICA,
                55, Font.BOLD));
        Phrase phrase2 = new Phrase("\n место");
        phrase2.setFont(new Font(getFont(),17));
        cell1.addElement(phrase);
        cell1.addElement(phrase2);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        return cell1;
    }

    /**
     * Получаем строку таблицы с номером ряда и ячейкой
     * @return
     */
    public PdfPCell getRyadCell() {
        PdfPCell cell = new PdfPCell();
        cell.setRotation(90);
        Phrase phrase = new Phrase(ryad);
        phrase.setFont(new Font(Font.FontFamily.HELVETICA,
                35, Font.BOLD));
        Phrase phrase1 = new Phrase(" ряд");
        phrase1.setFont(new Font(getFont(),27));
        Phrase phrase2 = new Phrase(loc);
        phrase2.setFont(new Font(Font.FontFamily.HELVETICA,
                55, Font.BOLD));
        cell.addElement(phrase);
        cell.addElement(phrase1);
        cell.addElement(phrase2);
        return cell;
    }

    /**
     * Парсим строку
     */
    public void getStrings(String code) {
        place = code.substring(code.length() - 1);
        loc = code.substring(4,7);
        ryad = code.substring(0,4);
        for (int index = 0; index != etage.length; index++) {
            codeLocWith[index] = ryad + "-" + loc + "-" + etage[index] + "-" + place;
            codeLocWithout[index] = ryad + loc + etage[index] + place;
        }
    }


    /**
     * Генерирует ШК для одной ячейки
     * @param dest
     * @param code
     * @throws IOException
     * @throws DocumentException
     */
    public void createPdf(String dest, String code) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        getStrings(code);
        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[] {45,10,45});
        table.addCell(getPlaceCell(place)).setBorder(0);
        PdfPCell empty = new PdfPCell();
        table.addCell(empty).setBorder(0);
        table.addCell(empty).setBorder(0);
        for (int index = 0; index != etage.length; index++) {
            PdfPCell cellBar = new PdfPCell();
            cellBar.setPadding(3);
            Phrase numberPhrase = new Phrase(codeLocWith[index]);
            cellBar.addElement(numberPhrase);
            Barcode128 code128 = new Barcode128();
            code128.setFont(null);
            code128.setCode(codeLocWithout[index]);
            code128.setCodeType(Barcode128.CODE128);
            Image code128Image = code128.createImageWithBarcode(cb, null, null);
            code128Image.scaleAbsolute(175,10);
            cellBar.addElement(code128Image);
            table.addCell(cellBar).setBorder(0);
            PdfPCell cellEtage = new PdfPCell();
            cellEtage.setRotation(90);
            cellEtage.setVerticalAlignment(Element.ALIGN_CENTER);
            String numEtage = "  " + etage[index];
            Phrase numEtagePhrase = new Phrase(numEtage);
            numEtagePhrase.setFont(new Font(Font.FontFamily.HELVETICA,
                    25));
            cellEtage.addElement(numEtagePhrase);
            cellEtage.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cellEtage).setBorder(0);
            table.addCell(empty).setBorder(0);
        }
        table.addCell(getRyadCell()).setBorder(0);
        PdfPCell cellLoc = new PdfPCell();
        cellLoc.setRotation(90);
        Phrase phrase3 = new Phrase(" ячейка");
        phrase3.setFont(new Font(getFont(),20, Font.BOLD));
        cellLoc.addElement(phrase3);
        table.addCell(cellLoc).setBorder(0);
        table.addCell(empty).setBorder(0);
        document.add(table);
        document.close();
    }

    /**
     * Генерирует ШК для всех данных
     * @param dest
     * @param list
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void createAll(String dest, ObservableList<String> list) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        for (String code : list) {
            getStrings(code);
            PdfPTable table = new PdfPTable(3);
            table.setWidths(new int[]{45, 10, 45});
            table.addCell(getPlaceCell(place)).setBorder(0);
            PdfPCell empty = new PdfPCell();
            table.addCell(empty).setBorder(0);
            table.addCell(empty).setBorder(0);
            for (int index = 0; index != etage.length; index++) {
                PdfPCell cellBar = new PdfPCell();
                cellBar.setPadding(3);
                Phrase numberPhrase = new Phrase(codeLocWith[index]);
                cellBar.addElement(numberPhrase);
                Barcode128 code128 = new Barcode128();
                code128.setFont(null);
                code128.setCode(codeLocWithout[index]);
                code128.setCodeType(Barcode128.CODE128);
                Image code128Image = code128.createImageWithBarcode(cb, null, null);
                code128Image.scaleAbsolute(175, 10);
                cellBar.addElement(code128Image);
                table.addCell(cellBar).setBorder(0);
                PdfPCell cellEtage = new PdfPCell();
                cellEtage.setRotation(90);
                String numEtage = "  " + etage[index];
                Phrase numEtagePhrase = new Phrase(numEtage);
                numEtagePhrase.setFont(new Font(Font.FontFamily.HELVETICA,
                        25));
                cellEtage.addElement(numEtagePhrase);
                table.addCell(cellEtage).setBorder(0);
                table.addCell(empty).setBorder(0);
            }
            table.addCell(getRyadCell()).setBorder(0);
            PdfPCell cellLoc = new PdfPCell();
            cellLoc.setRotation(90);
            Phrase phrase3 = new Phrase(" ячейка");
            phrase3.setFont(new Font(getFont(), 20, Font.BOLD));
            cellLoc.addElement(phrase3);
            table.addCell(cellLoc).setBorder(0);
            table.addCell(empty).setBorder(0);
            document.add(table);
            document.newPage();
        }
        document.close();
    }
}