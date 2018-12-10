package controller;

import com.itextpdf.text.DocumentException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Barcode;
import model.Parse;
import sun.security.krb5.internal.crypto.Des;
import validator.Validate;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static model.Barcode.DEST;

public class Controller {



    public String code;
    Validate validate = new Validate();

    @FXML
    Button chooseFile = new Button();

    @FXML
    TextField textFieldFile = new TextField();

    @FXML
    ListView<String> listViewCodes = new ListView<>();

    @FXML
    Button generateOneBarcode = new Button();

    @FXML
    Button generateAll = new Button();

/*
    public void initialize() {
        File file = new File("C:/xls/file.txt");
        textFieldFile.setText(file.toString());
        Parse parse = new Parse();
        try {
            listViewCodes.setItems(parse.parcer(file));
            code = listViewCodes.getItems().get(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Невозможно открыть файл!");
        }
    }*/


    /**
     * Выбрать файл для генерации
     * @throws IOException
     */
    public void setChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(new Stage());
        textFieldFile.setText(file.toString());
        Parse parse = new Parse();
        try {
            listViewCodes.setItems(parse.parcer(file));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Невозможно открыть файл!");
        }

    }

    /**
     * Выбор элемента из списка
     */
    public void selectElem() {
        listViewCodes.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                code = listViewCodes.getSelectionModel().getSelectedItem();
                generateOneBarcode.disableProperty().setValue(false);
                System.out.println(code);//Удалить птом
            }
        });
    }

    /**
     * Генерация штрихкода одного элемента
     * @throws IOException
     * @throws DocumentException
     */
    public void setGenerateOneBarcode() throws IOException, DocumentException {
        if (validate.validateTextField(code)) {
            File file = new File(DEST);
            file.getParentFile().mkdirs();
            Barcode barcode = new Barcode();
            barcode.createPdf(DEST, code);
            JOptionPane.showMessageDialog(null, "N" + code + " Успешно сгенерировано в " + DEST);
            open(file);
        } else {
            JOptionPane.showMessageDialog(null, "ERROR!!");
        }
    }

    /**
     * Генерирует все что есть в коллекции (в файле)
     * @throws IOException
     * @throws DocumentException
     */
    public void setGenerateAll() throws IOException,DocumentException {
        if (validate.validateCollection(listViewCodes.getItems())) {
            File file = new File(DEST);
            file.getParentFile().mkdirs();
            Barcode barcode = new Barcode();
            barcode.createAll(DEST,listViewCodes.getItems());
            JOptionPane.showMessageDialog(null, "Все ШК успешно сгенерированы в " + DEST);
            open(file);
        } else {
            JOptionPane.showMessageDialog(null, "ERROR!!");
        }
    }


    /**
     * Открывает созданную пдфку
     * @param file
     * @throws IOException
     */
    public void open(File file) throws IOException {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file);
        } else {
            throw new UnsupportedOperationException("Open action not supported");

        }
    }

}
