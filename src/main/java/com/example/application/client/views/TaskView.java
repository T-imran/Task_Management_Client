package com.example.application.client.views;


import com.example.application.client.api_requests.Api_Request;
import com.example.application.client.component_utility.AddComponents;
import com.example.application.model.TaskModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.Json;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CssImport(value = "./themes/taskmanagement/Styles.css", themeFor = "vaadin-grid")
@PageTitle("")
@Route(value = "project", layout = MainLayout.class)
public class TaskView extends Div {

    public static Grid<TaskModel> taskGrid;
    public static final String failed = "Failed";
    public static final String success = "Successfully Loaded";
    private static final String url = "http://localhost:8080/task";
    public static HttpResponse<String> response;
    Json gson = new Json();
    List<TaskModel> nidList;

    TaskView(){
        Div mainDiv = new Div();

        Div subDiv = new Div();
        subDiv.getStyle().set("margin","100px");

        Button createTask = new Button("Add task", VaadinIcon.PLUS.create());
        createTask.addThemeVariants(ButtonVariant.LUMO_SMALL);
        createTask.getStyle().set("color","#2ecc71");
        HorizontalLayout buttonHori = new HorizontalLayout(createTask);
        buttonHori.setAlignItems(FlexComponent.Alignment.END);
        buttonHori.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        createTask.addClickListener(e->{
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Add Task");
            TextField taskTitle = new TextField("Task title");
            TextField taskDate = new TextField();
            TextField startDate = new TextField("Start Time");
            TextField endDate = new TextField("End time");
            HorizontalLayout dialogLayoutHorizontalLayout = new HorizontalLayout(taskTitle,taskDate,startDate,endDate);
            dialog.add(dialogLayoutHorizontalLayout);
            Button saveButton = new Button("Save");
            saveButton.addClickListener(save->{
                TaskModel saveTask = new TaskModel();
                saveTask.setTaskTitle(taskTitle.getValue());
                saveTask.setTaskDate(taskDate.getValue());
                saveTask.setStartTime(startDate.getValue());
                saveTask.setEndTime(endDate.getValue());
                var objectMapper = new ObjectMapper();
                String requestBody = null;
                try {
                    requestBody = objectMapper
                            .writeValueAsString(saveTask);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url+"/save"))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();

                try {
                   response = client.send(request,
                            HttpResponse.BodyHandlers.ofString());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(response.body());
                dialog.close();
                Api_Request.getAllTask();
            });
            Button cancelButton = new Button("Cancel", cancel -> dialog.close());
            dialog.getFooter().add(cancelButton);
            dialog.getFooter().add(saveButton);
            dialog.open();
        });

        //Grid Section

        taskGrid = new Grid<>();
        HorizontalLayout gridHorizontalLayout1 = new HorizontalLayout();
        taskGrid.setClassName("my-grid");
        taskGrid.setHeight("600px");
        taskGrid.addColumn(TaskModel::getTaskTitle).setWidth("120px").setHeader("Task Name").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getTaskDate).setWidth("120px").setHeader("Date").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getStartTime).setWidth("120px").setHeader("Start Date").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getEndTime).setWidth("120px").setHeader("End Date").setSortable(true)
                .setResizable(true);

        taskGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_ROW_STRIPES);
        Api_Request.getAllTask();
        gridHorizontalLayout1.add(taskGrid);





        subDiv.add(buttonHori,gridHorizontalLayout1);
        mainDiv.add(subDiv);
        add(mainDiv);
    }
}
