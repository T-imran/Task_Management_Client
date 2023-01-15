package com.example.application.client.views;


import com.example.application.client.api_requests.ApiRequest;
import com.example.application.model.TaskModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.Json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CssImport(value = "./themes/taskmanagement/Styles.css", themeFor = "vaadin-grid")
@PageTitle("")
@Route(value = "", layout = MainLayout.class)
public class TaskView extends Div {

    public static Grid<TaskModel> taskGrid;
    public static final String failed = "Failed";
    public static final String success = "Successfully Loaded";
    private static final String url = "http://localhost:8080/task";
    public static HttpResponse<String> response;
    Json gson = new Json();
    List<TaskModel> nidList;

    TaskView() {
        Div mainDiv = new Div();

        Div subDiv = new Div();
        subDiv.getStyle().set("margin", "100px");

        Button createTask = new Button("Add task", VaadinIcon.PLUS.create());
        createTask.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        createTask.getStyle().set("margin-bottom", "20px");
        HorizontalLayout buttonHori = new HorizontalLayout(createTask);
        buttonHori.setAlignItems(FlexComponent.Alignment.END);
        buttonHori.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        createTask.addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Add Task");
            TextField taskTitle = new TextField("Task title");
            DatePicker taskDate = new DatePicker("Task date");

            TextField startHour = new TextField("Start Time");
            startHour.setPlaceholder("Hour");
            startHour.setWidth("90px");
            TextField startMin = new TextField();
            startMin.setPlaceholder("Min");
            startMin.setWidth("90px");
            List<String> startPeriodList = new ArrayList<>();
            startPeriodList.add("AM");
            startPeriodList.add("PM");
            ComboBox<String> startPeriod = new ComboBox<String>("Period");
            startPeriod.setWidth("90px");
            startPeriod.setItems(startPeriodList);
            TextField endHour = new TextField("Hour");
            TextField endMin = new TextField("Min");
            List<String> endPeriodList = new ArrayList<>();
            endPeriodList.add("AM");
            endPeriodList.add("PM");
            ComboBox<String> endPeriod = new ComboBox<String>("Period");
            endPeriod.setItems(endPeriodList);

            TextField totalTime = new TextField("Estimated Time");

            List<String> statusList = new ArrayList<>();
            statusList.add("Not Started");
            statusList.add("In Progress");
            statusList.add("Complete");
            ComboBox<String> status = new ComboBox<String>("Status");
            status.setItems(statusList);

            HorizontalLayout horizontalLayoutStartTime = new HorizontalLayout(startHour, startMin, startPeriod);
            HorizontalLayout horizontalLayoutEndTime = new HorizontalLayout(endHour, endMin, endPeriod);
            VerticalLayout dialogLayoutVerticalLayout = new VerticalLayout(taskTitle, taskDate, horizontalLayoutStartTime, horizontalLayoutEndTime, totalTime, status);
            dialog.add(dialogLayoutVerticalLayout);
            Button saveButton = new Button("Save");
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_PRIMARY);
            saveButton.addClickListener(save -> {
                TaskModel saveTask = new TaskModel();
                saveTask.setTaskTitle(taskTitle.getValue());
                saveTask.setTaskDate(taskDate.getValue());
                saveTask.setStartTime(startHour.getValue() + " " + startMin.getValue() + " " + startPeriod.getValue());
                saveTask.setEndTime(endHour.getValue() + " " + endMin.getValue() + " " + endPeriod.getValue());
                saveTask.setTotalTime(totalTime.getValue());
                saveTask.setStatus(status.getValue());
                ApiRequest.saveTask(saveTask);
                dialog.close();
                ApiRequest.getAllTask();
            });
            Button cancelButton = new Button("Cancel");
            cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_CONTRAST);
            cancelButton.addClickListener(cancel -> {
                dialog.close();
            });
            dialog.getFooter().add(cancelButton);
            dialog.getFooter().add(saveButton);
            dialog.open();
        });

        //Grid Section

        taskGrid = new Grid<>();
        HorizontalLayout gridHorizontalLayout1 = new HorizontalLayout();
        taskGrid.setClassName("my-grid");
        taskGrid.setHeight("600px");

        Grid.Column<TaskModel> editColumn = taskGrid.addComponentColumn(person -> {
            Button viewButton = new Button("View");
            viewButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
            Button editButton = new Button("Edit");
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            editButton.addClickListener(doubleClick -> {
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Add Task");
                TextField taskID = new TextField("Task title");
                taskID.setValue(person.getId().toString());

                TextField taskTitle = new TextField("Task title");
                taskTitle.setValue(person.getTaskTitle());

                TextField taskDate = new TextField("Task date");
                taskDate.setValue(person.getTaskDate().toString());

                TextField startDate = new TextField("Start time");
                startDate.setValue(person.getStartTime());

                TextField endDate = new TextField("End time");
                endDate.setValue(person.getEndTime());

                TextField totalTime = new TextField("Estimated Time");
                totalTime.setValue(person.getTotalTime());

                List<String> statusList = new ArrayList<>();
                statusList.add("Not Started");
                statusList.add("In Progress");
                statusList.add("Complete");
                ComboBox<String> status = new ComboBox<String>("Status");
                status.setItems(statusList);
                status.setValue(person.getStatus());


                HorizontalLayout dialogLayoutHorizontalLayout = new HorizontalLayout(taskTitle, taskDate, startDate, endDate, totalTime, status);
                dialog.add(dialogLayoutHorizontalLayout);
                Button saveButton = new Button("Update");
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                saveButton.addClickListener(save -> {
                    TaskModel updateTask = new TaskModel();
                    updateTask.setId(Long.parseLong(taskID.getValue()));
                    updateTask.setTaskTitle(taskTitle.getValue());
                    updateTask.setTaskDate(LocalDate.parse(taskDate.getValue()));
                    updateTask.setStartTime(startDate.getValue());
                    updateTask.setEndTime(endDate.getValue());
                    updateTask.setTotalTime(totalTime.getValue());
                    updateTask.setStatus(status.getValue());

                    ApiRequest.saveTask(updateTask);
                    dialog.close();
                    ApiRequest.getAllTask();
                });
                Button cancelButton = new Button("Cancel");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                        ButtonVariant.LUMO_CONTRAST);
                cancelButton.getStyle().set("margin-left", "auto");
                cancelButton.addClickListener(cancelUpdate -> {
                    dialog.close();
                });
                Button deleteButton = new Button("Delete");
                deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                        ButtonVariant.LUMO_ERROR);
                deleteButton.addClickListener(cancel -> {
                    ApiRequest.deleteTask(person.getId());
                    dialog.close();
                    ApiRequest.getAllTask();
                });
                dialog.getFooter().add(deleteButton);
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);
                dialog.open();
            });
            return new HorizontalLayout(viewButton, editButton);
        }).setHeader("Action").setWidth("180px").setFlexGrow(0);

        taskGrid.addColumn(TaskModel::getId).setWidth("75px").setHeader("ID").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getTaskTitle).setWidth("600px").setHeader("Task Name").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getTaskDate).setWidth("120px").setHeader("Date").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getStartTime).setWidth("120px").setHeader("Start Time").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getEndTime).setWidth("120px").setHeader("End Time").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getTotalTime).setWidth("190px").setHeader("Estimated Time").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getStatus).setWidth("190px").setHeader("Status").setSortable(true)
                .setResizable(true);

        taskGrid.getColumns().get(7).setClassNameGenerator(person -> {
            if (person.getStatus().equals("Not Started")) {
                return "not-started";
            }
            if (person.getStatus().equals("In Progress")) {
                return "in-progress";
            }
            if (person.getStatus().equals("Complete")) {
                return "complete";
            }
            return null;
        });


        taskGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_ROW_STRIPES);
        ApiRequest.getAllTask();
        gridHorizontalLayout1.add(taskGrid);

        //Sub div and Main Div
        subDiv.add(buttonHori, gridHorizontalLayout1);
        mainDiv.add(subDiv);
        add(mainDiv);
    }
}
