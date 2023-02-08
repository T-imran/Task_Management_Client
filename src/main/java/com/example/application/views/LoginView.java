package com.example.application.views;

import com.example.application.api_requests.ApiRequest;
import com.example.application.model.LoginModel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("login")
@Route(value = "")
public class LoginView extends Div {

    LoginView(){
        Div mainDiv = new Div();

        TextField userField = new TextField("Username");
        TextField passField =new TextField("Password");
        Button loginButton = new Button("LOGIN");
        loginButton.addClickListener(e->{
            LoginModel loginRequest = new LoginModel();
            loginRequest.setEmail(userField.getValue());
            loginRequest.setPassword(passField.getValue());
            if(ApiRequest.loginRequest(loginRequest)){
                loginButton.getUI().ifPresent(ui ->
                        ui.navigate("task"));
            }
        });
        VerticalLayout loginFieldLayout = new VerticalLayout();
        loginFieldLayout.add(userField,passField,loginButton);
        mainDiv.add(loginFieldLayout);
        add(mainDiv);
    }
}
