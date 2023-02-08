package com.example.application.component_utility;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

public class AddComponents extends Div {
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

    public Div dialog() {
        Div mainDiv = new Div();
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("New employee");

        Div dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
        mainDiv.add(dialog);

        dialog.open();

        // Center the button within the example
        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
        return mainDiv;
    }

    public static Div createDialogLayout(){
            Div fromDiv = new Div();
            TextField firstNameField = new TextField("First name", "John", "");
            TextField lastNameField = new TextField("Last name", "Smith", "");
            EmailField emailField = new EmailField("Email address");
            emailField.setValue("john.smith@example.com");
            FormLayout formLayout = new FormLayout(firstNameField, lastNameField,
                    emailField);
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
            formLayout.setColspan(emailField, 2);

            Button createAccount = new Button("Create account");
            createAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button cancel = new Button("Cancel");

            HorizontalLayout buttonLayout = new HorizontalLayout(createAccount,
                    cancel);

            buttonLayout.setPadding(false);
         fromDiv.add(formLayout, buttonLayout);
        return fromDiv;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }
}
