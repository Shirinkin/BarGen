package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parse {

    public ObservableList<String> parcer (File fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<String> result = new ArrayList<>();
        while (reader.ready()) {
            result.add(new String(reader.readLine()));
        }
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(result);
        return observableList;
    }
}
