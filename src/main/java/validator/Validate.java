package validator;

import javafx.collections.ObservableList;

import java.util.List;

public class Validate {

    public boolean validateTextField(String text) {
        boolean rst = false;
        if (text.length() == 9) {
            rst = true;
        }
        return rst;
    }

    public boolean validateCollection(ObservableList<String> collection) {
        boolean rst = true;
        for (String elem : collection) {
            if (elem.length() != 9) {
                rst = false;
            }
        }
        return rst;
    }
}
