package com.example.application.client.component_utility;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.TextField;

public class components {
    private TextField textField(String label, String height, String weight, String[] margin) {
        TextField textField = new TextField(label);
        textField.getStyle().set("weight", "0px 0px 0px 10px");
        textField.getStyle().set("margin", "0px 0px 0px 10px");
        textField.getStyle().set("background", "#0C1C57").set("padding", "5px");
        return textField;
    }

    private Button button() {
        return null;
    }

    private Dialog dialog() {
        return null;
    }

}
